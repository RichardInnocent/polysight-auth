package org.richardinnocent.polysight.auth.server.util;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class FileContentReaderTest {

  private static final FileContentReader READER = new FileContentReader();

  @Test
  public void testReadingTextFromClasspathFile() throws IOException {
    String filePath = "test.txt";
    String fileContents = "These are the contents\nof this test file";
    String detectedContents = READER.getTextContentsFromClassPathFile(filePath);
    assertEquals(fileContents, detectedContents);
  }

  @Test
  public void testReadingTextFromNonExistentClasspathFileThrowsException() {
    assertThrows(
        FileNotFoundException.class,
        () -> READER.getTextContentsFromFileAtLocation("Clearly not a file")
    );
  }

  @Test
  public void testReadingTextFromNonExistentFileThrowsException() {
    assertThrows(
        FileNotFoundException.class,
        () -> READER.getTextContentsFromFileAtLocation("Clearly not a file")
    );
  }

  @Test
  public void testReadingBytesFromClasspathFile() throws IOException {
    String filePath = "test.txt";
    byte[] detectedContents = READER.getByteContentsFromClassPathFile(filePath);
    byte[] fileContents = "These are the contents\nof this test file".getBytes();
    assertArrayEquals(fileContents, detectedContents);
  }

  @Test
  public void testReadingBytesFromNonExistentClasspathFileThrowsException() {
    assertThrows(
        FileNotFoundException.class,
        () -> READER.getByteContentsFromClassPathFile("Clearly not a file")
    );
  }

  @Test
  public void testReadingBytesFromNonExistentFileThrowsException() {
    assertThrows(
        FileNotFoundException.class,
        () -> READER.getByteContentsFromFileAtLocation("Clearly not a file")
    );
  }

}