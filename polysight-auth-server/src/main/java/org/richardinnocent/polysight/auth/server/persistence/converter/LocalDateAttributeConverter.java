package org.richardinnocent.polysight.auth.server.persistence.converter;

import org.joda.time.LocalDate;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Date;

@Converter(autoApply = true)
public class LocalDateAttributeConverter implements AttributeConverter<LocalDate, Date> {

  @Override
  public Date convertToDatabaseColumn(LocalDate localDate) {
    return localDate == null ? null : Date.valueOf(constructDateString(localDate));
  }

  private String constructDateString(LocalDate localDate) {
    return localDate.getYear() + "-" + localDate.getMonthOfYear() + '-' + localDate.getDayOfMonth();
  }

  @Override
  public LocalDate convertToEntityAttribute(Date sqlDate) {
    return sqlDate == null ? null : new LocalDate(sqlDate.toString());
  }

}
