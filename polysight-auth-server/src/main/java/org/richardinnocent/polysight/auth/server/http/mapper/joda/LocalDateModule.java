package org.richardinnocent.polysight.auth.server.http.mapper.joda;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.joda.time.LocalDate;

import java.io.IOException;

/**
 * Formats jodatime {@link LocalDate} entities as yyyy-mm-dd.
 */
public class LocalDateModule extends SimpleModule {

  private static final LocalDateSerializer LOCAL_DATE_SERIALIZER = new LocalDateSerializer();

  public LocalDateModule() {
    addSerializer(LOCAL_DATE_SERIALIZER);
  }

  private static class LocalDateSerializer extends StdSerializer<LocalDate> {

    private LocalDateSerializer() {
      super(LocalDate.class);
    }

    @Override
    public void serialize(LocalDate localDate,
                          JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
      jsonGenerator.writeString(
          Integer.toString(localDate.getYear()) + '-'
          + localDate.getMonthOfYear() + '-'
          + localDate.getDayOfMonth()
      );
    }

  }

}
