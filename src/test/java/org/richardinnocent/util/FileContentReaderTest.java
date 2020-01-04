package org.richardinnocent.util;

import static org.junit.Assert.*;

import java.io.IOException;
import org.junit.Test;

public class FileContentReaderTest {

  private static final FileContentReader READER = new FileContentReader();

  @Test
  public void testReadingClasspathFile() throws IOException {
    String filePath = "test.txt";
    String fileContents = "These are the contents\r\nof this test file";
    String detectedContents = READER.getClassPathFileContents(filePath);
    assertEquals(fileContents, detectedContents);
  }

}