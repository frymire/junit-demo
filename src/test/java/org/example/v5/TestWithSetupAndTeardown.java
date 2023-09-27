package org.example.v5;

import org.junit.jupiter.api.*;

public class TestWithSetupAndTeardown {

  @BeforeAll
  public static void setupBeforeAll() { System.out.println("Set up before all tests..."); }

  @BeforeEach
  public void setupBeforeEach() { System.out.println("\nSet up before each test..."); }

  @Test
  public void testWithDefaultDisplayName(TestInfo testInfo) {
    System.out.println("Test Case Name = " + testInfo.getDisplayName());
    // JUnit 5 does not have a direct method to get the count of test cases,
    // and changing the name of a test at runtime isn't directly supported.
  }

  @Test
  @DisplayName("should print a custom display name")
  public void testWithCustomDisplayName(TestInfo testInfo) {
    System.out.println("Test Case Name = " + testInfo.getDisplayName());
  }

  @AfterEach
  public void tearDownAfterEach() { System.out.println("Tear down after each test..."); }

  @AfterAll
  public static void tearDownAfterAll() { System.out.println("\nTear down after all tests..."); }
}
