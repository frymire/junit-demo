package org.example.v5;

import org.example.v4.TestWithSetup;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

public class ConstructedSuite {

  public static void main(String[] a) {

    // Create a test launcher, and register a listener to gather summary results.
    Launcher launcher = LauncherFactory.create();
    SummaryGeneratingListener listener = new SummaryGeneratingListener();
    launcher.registerTestExecutionListeners(listener);

    // Create a request to discover tests for specified classes.
    LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder
        .request()
        .selectors(
            selectClass(TestAssertions.class),
            selectClass(TestWithSetup.class)
        )
        .build();

    // Run the tests and print the results.
    launcher.execute(request);
    TestExecutionSummary summary = listener.getSummary();
    System.out.println("Number of test cases found = " + summary.getTestsFoundCount());
    System.out.println("Number of test cases succeeded = " + summary.getTestsSucceededCount());
  }
}
