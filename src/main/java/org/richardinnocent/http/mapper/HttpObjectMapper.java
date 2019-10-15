package org.richardinnocent.http.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class HttpObjectMapper extends ObjectMapper {

  public HttpObjectMapper() {
    registerModule(new JodaModule());
  }

}
