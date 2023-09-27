package org.example.v4;

import junit.framework.TestSuite;
import junit.framework.TestResult;

// A test suite is a composite of tests.
public class ConstructedSuite {

  public static void main(String[] a) {

    // Add tests to the suite.
    TestSuite suite = new TestSuite(TestAssertions.class, TestWithSetup.class);
    System.out.println("Number of test classes = " + suite.countTestCases());

    // Run the suite.
    TestResult result = new TestResult();    
    suite.run(result);
    System.out.println("Number of test classes run = " + result.runCount());
  }
}
