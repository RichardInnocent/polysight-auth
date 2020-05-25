package org.richardinnocent.polysight.auth.server.persistence.converter;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;

@Converter(autoApply = true)
public class DateTimeAttributeConverter implements AttributeConverter<DateTime, Timestamp> {

  @Override
  public Timestamp convertToDatabaseColumn(DateTime attribute) {
    return attribute == null ? null : new Timestamp(attribute.getMillis());
  }

  @Override
  public DateTime convertToEntityAttribute(Timestamp dbData) {
    return dbData == null ? null : new DateTime(dbData.getTime()).withZone(DateTimeZone.UTC);
  }

}
