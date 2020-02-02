package org.richardinnocent.http.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.function.Function;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Creates an exception to indicate that a request was received that does not contain all mandatory
 * parameters.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MissingParametersException extends RuntimeException {

  private final Collection<String> missingParameters;

  /**
   * Creates a new exception to indicate that the specified mandatory parameters were not provided.
   * @param missingParameters The missing mandatory parameters.
   */
  public MissingParametersException(Collection<String> missingParameters) {
    super("The request is missing mandatory parameters");
    this.missingParameters = missingParameters == null ?
        Collections.emptyList() : new ArrayList<>(missingParameters);
  }

  /**
   * Creates a new exception to indicate that the specified mandatory parameters were not provided.
   * @param missingParameters The missing mandatory parameters.
   */
  public MissingParametersException(String... missingParameters) {
    super("The request is missing mandatory parameters");
    this.missingParameters = missingParameters == null ?
        Collections.emptyList() : Arrays.asList(missingParameters);
  }

  /**
   * Gets all of the missing mandatory parameters that caused this exception.
   * @return All missing mandatory parameters.
   */
  public Collection<String> getMissingParameters() {
    return missingParameters;
  }

  /**
   * Builds a new creator that can help to mandate that some parameters exist and throw a new
   * exception if not.
   * @return A new creator.
   */
  public static Creator creator() {
    return new Creator();
  }

  /**
   * Creator class that helps to sequentially create the a list of all missing mandatory parameters
   * without overly obfuscating logic.
   */
  public static class Creator {
    private final Collection<String> missingParams = new LinkedList<>();

    private Creator() {}

    /**
     * Adds a missing mandatory parameter.
     * @param paramName The name of the mandatory parameter.
     * @return This instance.
     */
    public Creator addMissingParameter(String paramName) {
      if (paramName != null) {
        missingParams.add(paramName);
      }
      return this;
    }

    /**
     * <p>Allows parameters to be retrieved via the provided {@code supplier} method. If {@code
     * supplier.apply(paramName) != null}, the result will be returned and no further action will
     * occur. However, if {@code supplier.apply(paramName) == null}, the {@code paramName} will be
     * recorded as a missing mandatory property and {@code null} will be returned.</p>
     * <p>This minimises null checking, as this can be used as follows:</p>
     * {@code String mandatoryParam =
     * creator.getOrLogMissing("username", paramName -> request::getParameter);}
     * @param paramName The name of the parameter.
     * @param supplier The method of retrieving the parameter.
     * @param <T> The type of object to be retrieved.
     * @return The result of {@code supplier.apply(paramName)}.
     */
    public <T> T getOrLogMissing(String paramName, Function<String, ? extends T> supplier) {
      T value = supplier.apply(paramName);
      if (value == null) {
        missingParams.add(paramName);
      }
      return value;
    }

    /**
     * If any parameters were logged as missing, this method will throw a {@link
     * MissingParametersException}. Otherwise, this method will have no effect.
     * @throws MissingParametersException Thrown if any parameters are logged as missing.
     */
    public void throwIfAnyMissing() throws MissingParametersException {
      if (missingParams.size() > 0) {
        throw new MissingParametersException(missingParams);
      }
    }

  }

}
