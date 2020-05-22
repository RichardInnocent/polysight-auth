package org.richardinnocent.polysight.auth.v0_0_1_alpha.model.security;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.validation.Validation;
import javax.validation.Validator;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

public class PublicKeyResponseTest {

  private static final Validator VALIDATOR =
      Validation.buildDefaultValidatorFactory().getValidator();

  @Test
  public void testEqualsAndHashCode() {
    EqualsVerifier.forClass(PublicKeyResponse.class)
                  .suppress(Warning.STRICT_INHERITANCE)
                  .suppress(Warning.NONFINAL_FIELDS)
                  .verify();
  }

  @Test
  public void testValidIfAllFieldsPopulated() {
    PublicKeyResponse response = new PublicKeyResponse();
    response.setAlgorithm("Algorithm");
    response.setFormat("Format");
    response.setKey("Key".getBytes());
    assertTrue(isValid(response));
  }

  @Test
  public void testInvalidIfAlgorithmIsNull() {
    PublicKeyResponse response = new PublicKeyResponse();
    response.setFormat("Format");
    response.setKey("Key".getBytes());
    assertFalse(isValid(response));
  }

  @Test
  public void testInvalidIfAlgorithmIsEmpty() {
    PublicKeyResponse response = new PublicKeyResponse();
    response.setAlgorithm("");
    response.setFormat("Format");
    response.setKey("Key".getBytes());
    assertFalse(isValid(response));
  }

  @Test
  public void testInvalidIfFormatIsNull() {
    PublicKeyResponse response = new PublicKeyResponse();
    response.setAlgorithm("Algorithm");
    response.setKey("Key".getBytes());
    assertFalse(isValid(response));
  }

  @Test
  public void testInvalidIfFormatIsEmpty() {
    PublicKeyResponse response = new PublicKeyResponse();
    response.setAlgorithm("Algorithm");
    response.setFormat("");
    response.setKey("Key".getBytes());
    assertFalse(isValid(response));
  }

  @Test
  public void testInvalidIfKeyIsNull() {
    PublicKeyResponse response = new PublicKeyResponse();
    response.setAlgorithm("Algorithm");
    response.setFormat("Format");
    assertFalse(isValid(response));
  }

  @Test
  public void testInvalidIfKeyIsEmpty() {
    PublicKeyResponse response = new PublicKeyResponse();
    response.setAlgorithm("Algorithm");
    response.setFormat("Format");
    response.setKey(new byte[0]);
    assertFalse(isValid(response));
  }

  private static boolean isValid(PublicKeyResponse response) {
    return VALIDATOR.validate(response).isEmpty();
  }

}