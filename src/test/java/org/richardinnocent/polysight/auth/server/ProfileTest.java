package org.richardinnocent.polysight.auth.server;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import org.junit.jupiter.api.Test;

public class ProfileTest {

  @Test
  public void testCorrectNumberOfProfiles() {
    assertEquals(4, Profile.values().length);
  }

  @Test
  public void testFromNameWhenCaseFound() {
    Profile profile = Profile.DEVELOPMENT;
    String name = profile.toString();
    Optional<Profile> detectedProfile = Profile.fromName(name);
    assertTrue(detectedProfile.isPresent());
    assertEquals(profile, detectedProfile.get());
  }

  @Test
  public void testFromNameIsCaseInsensitive() {
    Profile profile = Profile.DEVELOPMENT;
    String name = profile.toString().toLowerCase();
    Optional<Profile> detectedProfile = Profile.fromName(name);
    assertTrue(detectedProfile.isPresent());
    assertEquals(profile, detectedProfile.get());
  }

  @Test
  public void testFromNameReturnsEmptyOptionalWhenNameIsNull() {
    assertTrue(Profile.fromName(null).isEmpty());
  }

  @Test
  public void testFromNameReturnsEmptyOptionalWhenNameNotRecognised() {
    assertTrue(Profile.fromName("not a profile").isEmpty());
  }

}