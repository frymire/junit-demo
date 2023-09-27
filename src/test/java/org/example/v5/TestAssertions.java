package org.example.v5;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestAssertions {

  String expected = "abc";
  String actual = "abc";
  String str3 = null;
  String str4 = "abc";
  String str5 = "abc";
  String[] array1 = {"one", "two", "three"};
  String[] array2 = {"one", "two", "three"};

  // Check that two objects are equal. Put the expected on the left and the actual on the right.
  @Test public void testEquals() { assertEquals(expected, actual); }

  // Check that a condition is true.
  @Test public void testTrue() { assertTrue(0 < 1); }

  // Check that a condition is false.
  @Test public void testFalse() { assertFalse(0 > 1); }

  // Check that a reference isn't null.
  @Test public void testNotNull() { assertNotNull(expected); }

  // Check that two references point to the same object.
  @Test public void testSame() { assertSame(str4, str5); }

  // Check that two references do not point to the same object.
  @Test public void testNotSame() { assertNotSame(expected, str3); }

  // Check that two arrays have the same contents.
  @Test public void testArrayEquals() { assertArrayEquals(array1, array2); }

  @Disabled
  @Test public void testDisable() { assertTrue(false); }

  // Force a failure
  @Test public void testFailure() { fail("If at first you don't succeed, you fail."); }

  @Test
  public void testDoubleWithTolerance() { // fails
    double easyDelta = 0.0001;
    double hardDelta = 0.000000000000000001;
    assertEquals(0.3, 0.1 + 0.2, easyDelta, "Actual value is beyond tolerance.");
    assertEquals(0.3, 0.1 + 0.2, hardDelta, "Actual value is beyond tolerance."); // fails
  }
}
