package org.example.v5;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

// Define a test suite by specifying test classes in annotations.
@Suite
@SelectClasses({
    TestAssertions.class,
    TestWithSetupAndTeardown.class
})
public class TestAnnotatedSuite { }
