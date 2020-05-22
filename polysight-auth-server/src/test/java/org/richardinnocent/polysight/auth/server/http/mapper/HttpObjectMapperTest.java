package org.richardinnocent.polysight.auth.server.http.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.junit.Test;

import static org.junit.Assert.*;

public class HttpObjectMapperTest {

  private static final HttpObjectMapper MAPPER = new HttpObjectMapper();

  @Test
  public void testSerializationOfJodaDateTimes() throws JsonProcessingException {
    DateTime dateTime = new DateTime(2019, 10, 17, 19, 32, 15, DateTimeZone.forOffsetHours(3));
    assertEquals(Long.toString(dateTime.getMillis()), MAPPER.writeValueAsString(dateTime));
  }

  @Test
  public void testSerializationOfJodaLocalDate() throws JsonProcessingException {
    LocalDate date = new LocalDate(2019, 10, 17);
    assertEquals(String.format("\"%d-%d-%d\"", date.getYear(), date.getMonthOfYear(),
                               date.getDayOfMonth()),
                 MAPPER.writeValueAsString(date));
  }

}