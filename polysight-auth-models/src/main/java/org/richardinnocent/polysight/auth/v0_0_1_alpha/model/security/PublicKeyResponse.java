package org.richardinnocent.polysight.auth.v0_0_1_alpha.model.security;

import java.util.Arrays;
import java.util.Objects;
import javax.validation.constraints.NotEmpty;

/**
 * The public key response information from the Polysight Auth app. This information allows other
 * services to decrypt the JWT used for authentication.
 */
@SuppressWarnings("unused")
public class PublicKeyResponse {

  @NotEmpty
  private String algorithm;

  @NotEmpty
  private String format;

  @NotEmpty
  private byte[] key;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PublicKeyResponse)) {
      return false;
    }
    PublicKeyResponse that = (PublicKeyResponse) o;
    return Objects.equals(algorithm, that.algorithm) &&
        Objects.equals(format, that.format) &&
        Arrays.equals(key, that.key);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(algorithm, format);
    result = 31 * result + Arrays.hashCode(key);
    return result;
  }
}
