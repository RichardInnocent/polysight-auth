package org.richardinnocent.polysight.auth.server.http.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class MissingParametersExceptionTest {

  private static final String MESSAGE = "The request is missing mandatory parameters";

  @Test
  public void testInitialisingWithNullCollection() {
    MissingParametersException exception =
        new MissingParametersException((Collection<String>) null);
    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(Collections.emptyList(), exception.getMissingParameters());
  }

  @Test
  public void testInitialisingWithEmptyCollection() {
    MissingParametersException exception = new MissingParametersException(Collections.emptyList());
    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(Collections.emptyList(), exception.getMissingParameters());
  }

  @Test
  public void testInitialisingWithSingletonCollection() {
    String missingParameter = "missingParam";
    Collection<String> missingParams = Collections.singletonList(missingParameter);
    MissingParametersException exception = new MissingParametersException(missingParams);
    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(missingParams, exception.getMissingParameters());
  }

  @Test
  public void testInitialisingWithMultiElementCollection() {
    String missingParam1 = "missingParam1";
    String missingParam2 = "missingParam2";
    String missingParam3 = "missingParam3";
    Collection<String> missingParams = Arrays.asList(missingParam1, missingParam2, missingParam3);
    MissingParametersException exception = new MissingParametersException(missingParams);
    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(missingParams, exception.getMissingParameters());
  }

  @Test
  public void testInitialisingWithNullVarargs() {
    MissingParametersException exception =
        new MissingParametersException((String[]) null);
    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(Collections.emptyList(), exception.getMissingParameters());
  }

  @Test
  public void testInitialisingWithEmptyVarargs() {
    MissingParametersException exception = new MissingParametersException();
    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(Collections.emptyList(), exception.getMissingParameters());
  }

  @Test
  public void testInitialisingWithSingletonVarargs() {
    String missingParameter = "missingParam";
    MissingParametersException exception = new MissingParametersException(missingParameter);
    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(Collections.singletonList(missingParameter), exception.getMissingParameters());
  }

  @Test
  public void testInitialisingWithMultiElementVarargs() {
    String missingParam1 = "missingParam1";
    String missingParam2 = "missingParam2";
    String missingParam3 = "missingParam3";
    MissingParametersException exception =
        new MissingParametersException(missingParam1, missingParam2, missingParam3);
    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(Arrays.asList(missingParam1, missingParam2, missingParam3),
                 exception.getMissingParameters());
  }

  @Test
  public void testResponseStatusAnnotation() {
    ResponseStatus responseStatus =
        MissingParametersException.class.getAnnotation(ResponseStatus.class);
    assertNotNull(responseStatus);
    assertEquals(HttpStatus.BAD_REQUEST, responseStatus.value());
  }

  @Test
  public void testBuilderDoesNotThrowWhenNoElements() {
    MissingParametersException.creator().throwIfAnyMissing();
  }

  @Test
  public void testBuilderThrowsWhenThereIsAnElement() {
    String missingParam = "missingParam";
    try {
      MissingParametersException.creator().addMissingParameter(missingParam).throwIfAnyMissing();
      fail("Exception was not thrown");
    } catch (MissingParametersException e) {
      assertEquals(MESSAGE, e.getMessage());
      assertEquals(Collections.singletonList(missingParam), e.getMissingParameters());
    }
  }

  @Test
  public void testBuilderThrowsWhenThereAreMultipleElements() {
    String missingParam1 = "missingParam1";
    String missingParam2 = "missingParam2";
    String missingParam3 = "missingParam3";
    try {
      MissingParametersException.creator()
                                .addMissingParameter(missingParam1)
                                .addMissingParameter(missingParam2)
                                .addMissingParameter(missingParam3)
                                .throwIfAnyMissing();
      fail("Exception was not thrown");
    } catch (MissingParametersException e) {
      assertEquals(MESSAGE, e.getMessage());
      assertEquals(Arrays.asList(missingParam1, missingParam2, missingParam3),
                   e.getMissingParameters());
    }
  }

  @Test
  public void testBuilderDoesNotAddElementWhenParameterFunctionDoesNotReturnNull() {
    MissingParametersException.Creator creator = MissingParametersException.creator();
    creator.getOrLogMissing("element", param -> "Not null");
    creator.throwIfAnyMissing();
  }

  @Test
  public void testBuilderThrowsWhenParameterFunctionReturnsNull() {
    String paramName = "param";
    MissingParametersException.Creator creator = MissingParametersException.creator();
    creator.getOrLogMissing(paramName, param -> null);
    try {
      creator.throwIfAnyMissing();
      fail("Exception was not thrown");
    } catch (MissingParametersException e) {
      assertEquals(MESSAGE, e.getMessage());
      assertEquals(Collections.singletonList(paramName), e.getMissingParameters());
    }
  }

}