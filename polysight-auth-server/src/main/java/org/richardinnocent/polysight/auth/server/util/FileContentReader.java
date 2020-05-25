package org.richardinnocent.polysight.auth.server.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.stereotype.Component;

/**
 * Utility class to perform common file reading-related tasks.
 */
@Component
public class FileContentReader {

  /**
   * Gets the full contents of the file at the specified location.
   * @param filePath The path to the file.
   * @return The full contents of the file.
   * @throws IOException Thrown if there is a problem reading the file.
   */
  public String getTextContentsFromFileAtLocation(String filePath) throws IOException {
    return getTextContentsFromStream(getStreamForFileAtLocation(filePath));
  }

  /**
   * Gets the full contents from the file at the specified location on the classpath.
   * @param filePath The path to the file on the classpath.
   * @return The full contents of the file.
   * @throws IOException Thrown if there is a problem reading the file.
   */
  public String getTextContentsFromClassPathFile(String filePath) throws IOException {
    try (InputStream fileStream = getStreamForClassPathFile(filePath)) {
      return getTextContentsFromStream(fileStream);
    }
  }

  private String getTextContentsFromStream(InputStream stream) throws IOException {
    StringBuilder contentBuilder = new StringBuilder();
    int charValue;
    while ((charValue = stream.read()) != -1) {
      contentBuilder.append((char) charValue);
    }
    return contentBuilder.toString();
  }

  /**
   * Gets a stream for the file at the specified location.
   * @param filePath The path to the file.
   * @return A new file stream.
   * @throws IOException Thrown if there is a problem opening a stream for this file.
   */
  private InputStream getStreamForFileAtLocation(String filePath) throws IOException {
    return new FileInputStream(filePath);
  }

  /**
   * Gets a stream for the file at the specified location on the classpath.
   * @param filePath The path to the file.
   * @return A new file stream.
   * @throws FileNotFoundException Thrown if there is a problem opening a stream for this file.
   */
  private InputStream getStreamForClassPathFile(String filePath) throws FileNotFoundException {
    InputStream stream = FileContentReader.class.getClassLoader().getResourceAsStream(filePath);
    if (stream == null) {
      throw new FileNotFoundException("Resource " + filePath + " not found on classpath");
    }
    return stream;
  }

  /**
   * Gets the full byte contents of the file at the specified location.
   * @param filePath The path to the file.
   * @return The byte contents of the file.
   * @throws IOException Thrown if there is a problem reading the file.
   */
  public byte[] getByteContentsFromFileAtLocation(String filePath) throws IOException {
    try (InputStream fileStream = getStreamForFileAtLocation(filePath)) {
      return fileStream.readAllBytes();
    }
  }

  /**
   * Gets the full byte contents of the file at the specified location on the classpath.
   * @param filePath The path to the file.
   * @return The byte contents of the file.
   * @throws IOException Thrown if there is a problem reading the file.
   */
  public byte[] getByteContentsFromClassPathFile(String filePath) throws IOException {
    try (InputStream fileStream = getStreamForClassPathFile(filePath)) {
      return fileStream.readAllBytes();
    }
  }


}
