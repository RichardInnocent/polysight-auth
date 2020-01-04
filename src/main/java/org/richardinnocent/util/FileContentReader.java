package org.richardinnocent.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.stereotype.Component;

@Component
public class FileContentReader {

  public String getFileContentsAtLocation(String filePath) throws IOException {
    return getContentsFromStream(new FileInputStream(filePath));
  }

  public String getClassPathFileContents(String filePath) throws IOException {
    try (InputStream fileStream =
             FileContentReader.class.getClassLoader().getResourceAsStream(filePath)) {
      if (fileStream == null) {
        throw new FileNotFoundException("Resource " + filePath + " not found on classpath");
      }
      return getContentsFromStream(fileStream);
    }
  }

  private String getContentsFromStream(InputStream stream) throws IOException {
    StringBuilder contentBuilder = new StringBuilder();
    int charValue;
    while ((charValue = stream.read()) != -1) {
      contentBuilder.append((char) charValue);
    }
    return contentBuilder.toString();
  }

}
