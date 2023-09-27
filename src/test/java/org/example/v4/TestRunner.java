package org.example.v4;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

// Create an executable main that runs tests using the JUnitCore facade.
public class TestRunner {

  // Define a method to print the result of a test.
  private static void report(Result result) {
    System.out.println();    
    for (Failure f: result.getFailures()) { System.out.println(f); }
    System.out.println("Test successful? -> " + result.wasSuccessful());
  }
  

  // Run some tests and print the results.
  public static void main(String[] args) {
    report(JUnitCore.runClasses(TestAssertions.class)); // run one class
    report(JUnitCore.runClasses(TestAnnotatedSuite.class)); // run a test suite
  }
}
