package org.richardinnocent;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Provides the profiles that are currently active on the environment, and a simple method of
 * detecting whether these profiles are active.
 */
@Component
public class ProfilesProvider {

  public final Environment environment;

  /**
   * Creates a new provider that picks up only the recognised profiles from the environment at
   * start-up. Any changes to the environment's profile during execution will be detected.
   * @param environment The Spring environment.
   */
  public ProfilesProvider(@Autowired Environment environment) {
    this.environment = environment;
  }

  /**
   * Gets all profiles that are currently active.
   * @return All active profiles.
   */
  public Set<Profile> getActiveProfiles() {
    String[] detectedProfiles = environment.getActiveProfiles();
    if (detectedProfiles == null) {
      return Collections.emptySet();
    }

    return Arrays.stream(detectedProfiles)
                 .map(Profile::fromName)
                 .filter(Optional::isPresent)
                 .map(Optional::get)
                 .collect(Collectors.toSet());
  }



  /**
   * Checks whether a profile is currently active.
   * @param profile The profile to check.
   * @return {@code true} if the profile is active.
   */
  public boolean isProfileActive(Profile profile) {
    return getActiveProfiles().contains(profile);
  }

  /**
   * Checks whether any of the given profiles are active.
   * @param profile1 The first profile to check for.
   * @param profiles All other profiles that should be checked for.
   * @return {@code true} is at least one of the profiles is active.
   */
  public boolean isAnyProfileActive(Profile profile1, Profile... profiles) {
    if (isProfileActive(profile1)) {
      return true;
    } else {
      return isAnyProfileActive(profiles);
    }
  }

  private boolean isAnyProfileActive(Profile... profiles) {
    if (profiles == null || profiles.length == 0) {
      return false;
    } else {
      return Arrays.stream(profiles).anyMatch(this::isProfileActive);
    }
  }

}
