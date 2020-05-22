package org.richardinnocent.polysight.auth.server.http.mapper.joda;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.LocalDate;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class LocalDateModuleTest {

  private static final ObjectMapper MAPPER = new ObjectMapper();

  @BeforeClass
  public static void setUpMapper() {
    MAPPER.registerModule(new LocalDateModule());
  }

  @Test
  public void testModuleAllowsAppropriateSerializationOfDate() throws JsonProcessingException {
    int year = 2019;
    int month = 10;
    int day = 17;
    LocalDate date = new LocalDate(year, month, day);
    assertEquals(String.format("\"%d-%d-%d\"", year, month, day), MAPPER.writeValueAsString(date));
  }

}