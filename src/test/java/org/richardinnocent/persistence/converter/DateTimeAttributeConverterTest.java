package org.richardinnocent.persistence.converter;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import java.sql.Timestamp;

import static org.junit.Assert.*;

public class DateTimeAttributeConverterTest {

  private final DateTimeAttributeConverter converter = new DateTimeAttributeConverter();

  @Test
  public void testConvertToDatabaseColumnWhenNotNull() {
    DateTime dateTime = new DateTime("2019-10-15T20:13:03.000Z").withZone(DateTimeZone.UTC);
    Timestamp result = converter.convertToDatabaseColumn(dateTime);
    assertEquals(dateTime.getMillis(), result.getTime());
  }

  @Test
  public void testConvertToDatabaseColumnWhenNull() {
    assertNull(converter.convertToDatabaseColumn(null));
  }

  @Test
  public void testConvertToEntityAttributeWhenNotNull() {
    DateTime dateTime = new DateTime("2019-10-15T20:16:05.000").withZone(DateTimeZone.UTC);
    Timestamp timestamp = new Timestamp(dateTime.getMillis());
    assertEquals(dateTime, converter.convertToEntityAttribute(timestamp));
  }

  @Test
  public void testConvertToEntityAttributeWhenNull() {
    assertNull(converter.convertToEntityAttribute(null));
  }

}