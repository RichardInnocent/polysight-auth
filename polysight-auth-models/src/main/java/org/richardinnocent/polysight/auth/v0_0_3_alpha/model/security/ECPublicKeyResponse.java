package org.richardinnocent.polysight.auth.v0_0_3_alpha.model.security;

import java.security.interfaces.ECPublicKey;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.util.Arrays;
import java.util.Objects;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * The public key response information from the Polysight Auth app. This information allows other
 * services to decrypt the JWT used for authentication.
 */
@SuppressWarnings("unused")
public class ECPublicKeyResponse {

  @NotEmpty
  private String algorithm;

  @NotEmpty
  private String format;

  @NotEmpty
  private byte[] key;

  @NotNull
  public ECPoint w;

  @NotNull
  public ECParameterSpec params;

  /**
   * Gets the algorithm used to generate the key.
   * @return The algorithm used to generate the key.
   */
  public String getAlgorithm() {
    return algorithm;
  }

  /**
   * Sets the algorithm used to generate the key.
   * @param algorithm The algorithm used to generate the key.
   */
  public void setAlgorithm(String algorithm) {
    this.algorithm = algorithm;
  }

  /**
   * Gets the format of the key.
   * @return The format of the key.
   */
  public String getFormat() {
    return format;
  }

  /**
   * Sets the format of the key.
   * @param format The format of the key.
   */
  public void setFormat(String format) {
    this.format = format;
  }

  /**
   * Gets the key.
   * @return The key.
   */
  public byte[] getKey() {
    return key;
  }

  /**
   * Sets the key.
   * @param key The key.
   */
  public void setKey(byte[] key) {
    this.key = key;
  }

  /**
   * Returns the public point, W.
   * @return The public point, W.
   * @see ECPublicKey#getW()
   */
  public ECPoint getW() {
    return w;
  }

  /**
   * Sets the public point, W.
   * @param w The public point, W.
   * @see ECPublicKey#getW()
   */
  public void setW(ECPoint w) {
    this.w = w;
  }

  /**
   * Gets the domain parameters for this key.
   * @return The domain parameters for this key.
   * @see ECPublicKey#getParams()
   */
  public ECParameterSpec getParams() {
    return params;
  }

  /**
   * Sets the domain parameters for this key.
   * @param params The domain parameters for this key.
   * @see ECPublicKey#getParams()
   */
  public void setParams(ECParameterSpec params) {
    this.params = params;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ECPublicKeyResponse)) {
      return false;
    }
    ECPublicKeyResponse that = (ECPublicKeyResponse) o;
    return Objects.equals(algorithm, that.algorithm) &&
        Objects.equals(format, that.format) &&
        Arrays.equals(key, that.key) &&
        Objects.equals(w, that.w) &&
        Objects.equals(params, that.params);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(algorithm, format, w, params);
    result = 31 * result + Arrays.hashCode(key);
    return result;
  }
}
