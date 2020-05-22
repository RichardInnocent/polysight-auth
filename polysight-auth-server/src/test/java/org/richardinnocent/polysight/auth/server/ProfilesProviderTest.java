package org.richardinnocent.polysight.auth.server;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.Test;
import org.springframework.core.env.Environment;

public class ProfilesProviderTest {

  @Test
  public void testActiveProfilesWhenAllProfilesAreRecognised() {
    Profile[] activeProfiles = new Profile[]{Profile.DEVELOPMENT, Profile.QA, Profile.PRODUCTION};
    assertEquals(setOf(activeProfiles),
                 createProviderWithActiveProfiles(activeProfiles).getActiveProfiles());
  }

  @Test
  public void testActiveProfilesIsEmptyIfEnvironmentActiveProfilesIsNull() {
    Set<Profile> activeProfiles =
        createProviderWithActiveProfiles((Profile) null).getActiveProfiles();
    assertNotNull(activeProfiles);
    assertEquals(0, activeProfiles.size());
  }

  @Test
  public void testActiveProfilesAreCaseInsensitive() {
    ProfilesProvider provider =
        createProviderWithActiveProfiles(Profile.DEVELOPMENT.toString().toLowerCase(),
                                         Profile.QA.toString().toLowerCase(),
                                         Profile.PRODUCTION.toString().toLowerCase());
    assertEquals(setOf(Profile.DEVELOPMENT, Profile.QA, Profile.PRODUCTION),
                 provider.getActiveProfiles());
  }

  @Test
  public void testUnrecognisedProfilesAreIgnored() {
    ProfilesProvider provider =
        createProviderWithActiveProfiles(
            arrayOf(Profile.DEVELOPMENT, "unrecognised", Profile.PRODUCTION));
    assertEquals(setOf(Profile.DEVELOPMENT, Profile.PRODUCTION),
                 provider.getActiveProfiles());
  }

  @Test
  public void testProfilesSetIsUnmodifiable() {
    Profile[] activeProfiles = new Profile[]{Profile.DEVELOPMENT, Profile.QA};
    ProfilesProvider provider = createProviderWithActiveProfiles(activeProfiles);
    assertEquals(setOf(activeProfiles),
                 provider.getActiveProfiles());
    provider.getActiveProfiles().add(Profile.PRODUCTION);
    assertEquals(setOf(activeProfiles),
                 provider.getActiveProfiles());
  }

  @Test
  public void testIsProfileActiveReturnsTrueWhenProfileIsActive() {
    ProfilesProvider provider =
        createProviderWithActiveProfiles(Profile.DEVELOPMENT, Profile.QA, Profile.PRODUCTION);
    assertTrue(provider.isProfileActive(Profile.DEVELOPMENT));
    assertTrue(provider.isProfileActive(Profile.QA));
    assertTrue(provider.isProfileActive(Profile.PRODUCTION));
  }

  @Test
  public void testIsProfileActiveReturnsFalseForNull() {
    ProfilesProvider provider =
        createProviderWithActiveProfiles(Profile.DEVELOPMENT, Profile.QA, Profile.PRODUCTION);
    assertFalse(provider.isProfileActive(null));
  }


  @Test
  public void testIsProfileActiveReturnsFalseWhenProfileIsNotActive() {
    ProfilesProvider provider = createProviderWithNoActiveProfiles();
    assertFalse(provider.isProfileActive(Profile.DEVELOPMENT));
    assertFalse(provider.isProfileActive(Profile.QA));
    assertFalse(provider.isProfileActive(Profile.PRODUCTION));
  }

  @Test
  public void testIsAnyProfileIsActiveReturnsFalseWhenProfile1IsNullAndProfilesIsEmpty() {
    assertFalse(createProviderWithNoActiveProfiles().isAnyProfileActive(null));
  }

  @Test
  public void testIsAnyProfileIsActiveReturnsFalseWhenAllSearchedForProfilesAreNull() {
    assertFalse(createProviderWithNoActiveProfiles().isAnyProfileActive(null, null, null, null));
  }

  @Test
  public void testIsAnyProfileActiveReturnsTrueIfProfileIsActiveRegardlessOfEntryOrder() {
    ProfilesProvider provider = createProviderWithActiveProfiles(Profile.DEVELOPMENT);
    assertTrue(
        provider.isAnyProfileActive(Profile.QA, null, Profile.PRODUCTION, Profile.DEVELOPMENT));
    assertTrue(
        provider.isAnyProfileActive(Profile.DEVELOPMENT, null, Profile.PRODUCTION, Profile.QA));
    assertTrue(
        provider.isAnyProfileActive(null, Profile.DEVELOPMENT, Profile.PRODUCTION, Profile.QA));
    assertTrue(provider.isAnyProfileActive(Profile.DEVELOPMENT, (Profile[]) null));
  }

  @Test
  public void testIsAnyProfileActiveReturnsFalseIfProfileIsNotActiveRegardlessOfEntryOrder() {
    ProfilesProvider provider = createProviderWithActiveProfiles(Profile.DEVELOPMENT);
    assertFalse(
        provider.isAnyProfileActive(Profile.QA, null, Profile.PRODUCTION));
    assertFalse(
        provider.isAnyProfileActive(null, Profile.PRODUCTION, Profile.QA));
    assertFalse(provider.isAnyProfileActive(Profile.QA, (Profile[]) null));
  }

  @Test
  public void testNewProfilesAreUpdated() {
    Environment environment = mock(Environment.class);
    when(environment.getActiveProfiles()).thenReturn(new String[0]);
    ProfilesProvider provider = new ProfilesProvider(environment);
    assertFalse(provider.isProfileActive(Profile.QA));

    when(environment.getActiveProfiles()).thenReturn(arrayOf(Profile.QA));
    assertTrue(provider.isProfileActive(Profile.QA));
  }

  private static ProfilesProvider createProviderWithNoActiveProfiles() {
    return createProviderWithActiveProfiles((String[]) null);
  }

  private static ProfilesProvider createProviderWithActiveProfiles(Profile... profiles) {
    return createProviderWithActiveProfiles(arrayOf(profiles));
  }

  private static ProfilesProvider createProviderWithActiveProfiles(String... profiles) {
    Environment environment = mock(Environment.class);
    if (profiles != null) {
      when(environment.getActiveProfiles()).thenReturn(profiles);
    }
    return new ProfilesProvider(environment);
  }

  private static String[] arrayOf(Object... objects) {
    return objects == null ?
        null :
        Stream.of(objects)
              .map(object -> object == null ? null : object.toString())
              .toArray(String[]::new);
  }

  private static Set<Profile> setOf(Profile... profiles) {
    return new HashSet<>(Arrays.asList(profiles));
  }

}