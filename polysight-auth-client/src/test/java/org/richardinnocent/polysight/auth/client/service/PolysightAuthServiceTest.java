package org.richardinnocent.polysight.auth.client.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.richardinnocent.polysight.core.client.service.PolysightServiceConfiguration;
import org.richardinnocent.polysight.core.client.service.ServiceVersion;

public class PolysightAuthServiceTest {

  @Test
  public void testParametersAreSetFromConstructor() {
    String baseUri = "base URI";
    ServiceVersion version = mock(ServiceVersion.class);
    PolysightServiceConfiguration config = new PolysightServiceConfiguration(baseUri, version);
    PolysightAuthService service = new PolysightAuthService(config);
    assertEquals(baseUri, service.getBaseUri());
    assertEquals(version, service.getVersion());
  }

}