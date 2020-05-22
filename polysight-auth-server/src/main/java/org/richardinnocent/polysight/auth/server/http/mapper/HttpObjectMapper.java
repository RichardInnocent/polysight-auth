package org.richardinnocent.polysight.auth.server.http.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.richardinnocent.polysight.auth.server.http.mapper.joda.LocalDateModule;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * An {@link ObjectMapper} used to serialize/deserialize HTTP requests/responses.
 */
@Component
@Primary
public class HttpObjectMapper extends ObjectMapper {

  /**
   * Creates a new object mapper, used in the serialization and deserialization of entities in HTTP
   * requests/responses, registering the appropriate modules.
   */
  public HttpObjectMapper() {
    registerModule(new JodaModule());
    registerModule(new LocalDateModule());
  }

}
