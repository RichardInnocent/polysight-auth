package org.richardinnocent.polysight.auth.server.http.binder.joda;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.beans.PropertyEditorSupport;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class LocalDateEditorTest {

  private final PropertyEditorSupport editor = mock(LocalDateEditor.class);

  @Test
  public void testSetAsTextForValidString() {
    String dateText = "2020-01-10";
    LocalDate expected = new LocalDate(dateText, DateTimeZone.UTC);
    doCallRealMethod().when(editor).setAsText(dateText);

    editor.setAsText(dateText);

    ArgumentCaptor<Object> setValueCaptor = ArgumentCaptor.forClass(Object.class);
    verify(editor).setValue(setValueCaptor.capture());

    assertEquals(expected, setValueCaptor.getValue());
  }

}