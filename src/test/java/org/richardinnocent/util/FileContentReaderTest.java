package org.richardinnocent.util;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.junit.Test;

public class FileContentReaderTest {

  private static final FileContentReader READER = new FileContentReader();

  @Test
  public void testReadingTextFromClasspathFile() throws IOException {
    String filePath = "test.txt";
    String fileContents = "These are the contents\r\nof this test file";
    String detectedContents = READER.getTextContentsFromClassPathFile(filePath);
    assertEquals(fileContents, detectedContents);
  }

  @Test(expected = FileNotFoundException.class)
  public void testReadingTextFromNonExistentClasspathFileThrowsException() throws IOException {
    READER.getTextContentsFromFileAtLocation("Clearly not a file");
  }

  @Test(expected = FileNotFoundException.class)
  public void testReadingTextFromNonExistentFileThrowsException() throws IOException {
    READER.getTextContentsFromFileAtLocation("Clearly not a file");
  }

  @Test
  public void testReadingBytesFromClasspathFile() throws IOException {
    String filePath = "test.txt";
    byte[] detectedContents = READER.getByteContentsFromClassPathFile(filePath);
    byte[] fileContents = "These are the contents\r\nof this test file".getBytes();
    assertArrayEquals(fileContents, detectedContents);
  }

  @Test(expected = FileNotFoundException.class)
  public void testReadingBytesFromNonExistentClasspathFileThrowsException() throws IOException {
    READER.getByteContentsFromClassPathFile("Clearly not a file");
  }

  @Test(expected = FileNotFoundException.class)
  public void testReadingBytesFromNonExistentFileThrowsException() throws IOException {
    READER.getByteContentsFromFileAtLocation("Clearly not a file");
  }

}