package org.richardinnocent.polysight.auth.server.http.binder.joda;

import java.beans.PropertyEditorSupport;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

/**
 * Converts a date value passed in as form data or request params to a {@link LocalDate}.
 */
public class LocalDateEditor extends PropertyEditorSupport {

  @Override
  public void setAsText(String text) throws IllegalArgumentException {
    setValue(new LocalDate(text, DateTimeZone.UTC));
  }

}
