package org.example.v5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestWithParameters {

  // Create a stream of test values to be run in sequence.
  @DisplayName("Source for test values")
  public static Stream<Integer> getTestParameters() {
    return Stream.of(1, 2, 3);
  }

  // Define the test for each value. The first two will pass, but the last one will fail.
  @ParameterizedTest
  @MethodSource("getTestParameters")
  public void testParametersFromMethod(Integer testParameter) {
    System.out.println("Test parameter is: " + testParameter);
    assertTrue(testParameter < 3);
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 2, 3})
  public void testParametersFromValue(Integer testParameter) {
    System.out.println("Test parameter is: " + testParameter);
    assertTrue(testParameter < 3);
  }
}
