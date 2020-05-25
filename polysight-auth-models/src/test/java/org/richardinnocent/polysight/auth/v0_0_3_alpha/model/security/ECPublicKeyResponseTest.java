package org.richardinnocent.polysight.auth.v0_0_3_alpha.model.security;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import javax.validation.Validation;
import javax.validation.Validator;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

public class ECPublicKeyResponseTest {

  private static final Validator VALIDATOR =
      Validation.buildDefaultValidatorFactory().getValidator();

  @Test
  public void testEqualsAndHashCode() {
    EqualsVerifier.forClass(ECPublicKeyResponse.class)
                  .suppress(Warning.STRICT_INHERITANCE)
                  .suppress(Warning.NONFINAL_FIELDS)
                  .verify();
  }

  @Test
  public void testValidIfAllFieldsPopulated() {
    ECPublicKeyResponse response = new ECPublicKeyResponse();
    response.setAlgorithm("Algorithm");
    response.setFormat("Format");
    response.setKey("Key".getBytes());
    response.setParams(mock(ECParameterSpec.class));
    response.setW(mock(ECPoint.class));
    assertTrue(isValid(response));
  }

  @Test
  public void testInvalidIfAlgorithmIsNull() {
    ECPublicKeyResponse response = new ECPublicKeyResponse();
    response.setFormat("Format");
    response.setKey("Key".getBytes());
    response.setParams(mock(ECParameterSpec.class));
    response.setW(mock(ECPoint.class));
    assertFalse(isValid(response));
  }

  @Test
  public void testInvalidIfAlgorithmIsEmpty() {
    ECPublicKeyResponse response = new ECPublicKeyResponse();
    response.setAlgorithm("");
    response.setFormat("Format");
    response.setKey("Key".getBytes());
    response.setParams(mock(ECParameterSpec.class));
    response.setW(mock(ECPoint.class));
    assertFalse(isValid(response));
  }

  @Test
  public void testInvalidIfFormatIsNull() {
    ECPublicKeyResponse response = new ECPublicKeyResponse();
    response.setAlgorithm("Algorithm");
    response.setKey("Key".getBytes());
    response.setParams(mock(ECParameterSpec.class));
    response.setW(mock(ECPoint.class));
    assertFalse(isValid(response));
  }

  @Test
  public void testInvalidIfFormatIsEmpty() {
    ECPublicKeyResponse response = new ECPublicKeyResponse();
    response.setAlgorithm("Algorithm");
    response.setFormat("");
    response.setKey("Key".getBytes());
    response.setParams(mock(ECParameterSpec.class));
    response.setW(mock(ECPoint.class));
    assertFalse(isValid(response));
  }

  @Test
  public void testInvalidIfKeyIsNull() {
    ECPublicKeyResponse response = new ECPublicKeyResponse();
    response.setAlgorithm("Algorithm");
    response.setFormat("Format");
    response.setParams(mock(ECParameterSpec.class));
    response.setW(mock(ECPoint.class));
    assertFalse(isValid(response));
  }

  @Test
  public void testInvalidIfKeyIsEmpty() {
    ECPublicKeyResponse response = new ECPublicKeyResponse();
    response.setAlgorithm("Algorithm");
    response.setFormat("Format");
    response.setKey(new byte[0]);
    response.setParams(mock(ECParameterSpec.class));
    response.setW(mock(ECPoint.class));
    assertFalse(isValid(response));
  }

  @Test
  public void testInvalidIfParamsAreNull() {
    ECPublicKeyResponse response = new ECPublicKeyResponse();
    response.setAlgorithm("Algorithm");
    response.setFormat("Format");
    response.setKey("Key".getBytes());
    response.setW(mock(ECPoint.class));
    assertFalse(isValid(response));
  }

  @Test
  public void testInvalidIfWIsEmpty() {
    ECPublicKeyResponse response = new ECPublicKeyResponse();
    response.setAlgorithm("Algorithm");
    response.setFormat("Format");
    response.setKey("Key".getBytes());
    response.setParams(mock(ECParameterSpec.class));
    assertFalse(isValid(response));
  }

  private static boolean isValid(ECPublicKeyResponse response) {
    return VALIDATOR.validate(response).isEmpty();
  }

}