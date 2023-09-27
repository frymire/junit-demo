package org.example.v5;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestExceptions {

  @Test
  public void exceptionIsThrown() {
    Exception exception =
        assertThrows(ArithmeticException.class, () -> { System.out.println(1 / 0); });
    assertTrue(exception.getMessage().contains("/ by zero"));
  }

  @Test
  public void throwsDifferentExceptionThanExpected() { // fails
    assertThrows(IllegalArgumentException.class, () -> { System.out.println(1 / 0); });
  }
}
