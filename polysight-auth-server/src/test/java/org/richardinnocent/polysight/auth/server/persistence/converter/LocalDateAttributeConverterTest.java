package org.richardinnocent.polysight.auth.server.persistence.converter;

import org.joda.time.LocalDate;
import org.junit.Test;

import java.sql.Date;

import static org.junit.Assert.*;

public class LocalDateAttributeConverterTest {

  private final LocalDateAttributeConverter converter = new LocalDateAttributeConverter();

  @Test
  public void testConvertToDatabaseColumnWhenNotNull() {
    int year = 2019;
    int month = 10;
    int day = 15;
    LocalDate localDate = new LocalDate(getIsoDate(year, month, day));
    Date date = converter.convertToDatabaseColumn(localDate);
    java.time.LocalDate result = date.toLocalDate();
    assertEquals(year, result.getYear());
    assertEquals(month, result.getMonthValue());
    assertEquals(day, result.getDayOfMonth());
  }

  @Test
  public void testConvertToDatabaseColumnWhenNull() {
    assertNull(converter.convertToDatabaseColumn(null));
  }

  @Test
  public void testConvertToEntityAttributeWhenNotNull() {
    int year = 2019;
    int month = 10;
    int day = 15;
    Date date = Date.valueOf(getIsoDate(year, month, day));
    LocalDate result = converter.convertToEntityAttribute(date);
    assertEquals(year, result.getYear());
    assertEquals(month, result.getMonthOfYear());
    assertEquals(day, result.getDayOfMonth());
  }

  @Test
  public void testConvertToEntityAttributeWhenNull() {
    assertNull(converter.convertToEntityAttribute(null));
  }

  private String getIsoDate(int year, int month, int day) {
    return year + "-" + month + "-" + day;
  }

}