package org.example.mockito;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TestPartialMock {

  // Build a collaborator for demonstration purposes.
  static class ValueHolder {
    final int value;
    ValueHolder() { value = -1; }
    ValueHolder(int value) { this.value = value; }
    int getValue() { return value; }
    final boolean simpleOperation() { return true; }
  }

  @Test
  public void testPartialMockingWithMockMaker() {
    // Mock the class.
    ValueHolder mock = mock(
        ValueHolder.class,
        withSettings().useConstructor(2).defaultAnswer(CALLS_REAL_METHODS)
    );
    when(mock.getValue()).thenReturn(123);  // configure specific behavior
    assertEquals(123, mock.getValue());  // returns stubbed value
    assertTrue(mock.simpleOperation());  // returns real implementation
  }

  @Test
  public void testPartialMockingWithSpy() {

    // Create real instances.
    ValueHolder c1 = new ValueHolder();
    ValueHolder c2 = new ValueHolder(150);

    // Show that they work normally.
    assertEquals(-1, c1.value);
    assertEquals(150, c2.value);
    assertEquals(-1, c1.getValue());
    assertEquals(150, c2.getValue());
    assertTrue(c1.simpleOperation());
    assertTrue(c2.simpleOperation());

    // Create spies to wrap the real instances and partially mock them.
    ValueHolder spyC1 = spy(c1);
    ValueHolder spyC2 = spy(c2);

    // Mock only the getValue() method
    when(spyC1.getValue()).thenReturn(123);
    when(spyC2.getValue()).thenReturn(123);

    // Original field values and non-mocked methods are unchanged...
    assertEquals(-1, spyC1.value);
    assertEquals(150, spyC2.value);
    assertTrue(spyC1.simpleOperation());
    assertTrue(spyC2.simpleOperation());

    // ...but the mocked value is returned for getValue() calls.
    assertEquals(123, spyC1.getValue());
    assertEquals(123, spyC2.getValue());
  }

}
