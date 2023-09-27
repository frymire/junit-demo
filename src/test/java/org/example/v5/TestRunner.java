package org.example.v5;

import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

public class TestRunner {

  // Run some tests and print the results.
  public static void main(String[] args) {

    report(LauncherDiscoveryRequestBuilder.request()
        .selectors(selectClass(TestAssertions.class))
        .build()); // run one class

    report(LauncherDiscoveryRequestBuilder.request()
        .selectors(selectClass(TestAnnotatedSuite.class))
        .build()); // run a test suite
  }

  // Define a method to print the result of a test.
  private static void report(LauncherDiscoveryRequest request) {
    Launcher launcher = LauncherFactory.create();
    MyTestExecutionListener listener = new MyTestExecutionListener();
    launcher.registerTestExecutionListeners(listener);
    launcher.execute(request);
    listener.getResults().forEach(System.out::println);
    System.out.println("Test successful? -> " + listener.allSuccessful());
  }

  static class MyTestExecutionListener implements TestExecutionListener {
    private boolean allSuccessful = true;
    private final List<Result> results = new ArrayList<>();

    @Override
    public void executionFinished(TestIdentifier testID, TestExecutionResult result) {
      results.add(new Result(testID, result));
      if (result.getStatus() != TestExecutionResult.Status.SUCCESSFUL) { allSuccessful = false; }
    }

    public List<Result> getResults() { return results; }

    public boolean allSuccessful() { return allSuccessful; }

    static class Result {
      private final TestIdentifier testID;
      private final TestExecutionResult result;

      Result(TestIdentifier testID, TestExecutionResult result) {
        this.testID = testID;
        this.result = result;
      }

      @Override
      public String toString() {
        final String status = result.getStatus().toString();
        String thrownMessage = status.equals("FAILED") ? " : " + result.getThrowable() : "";
        return testID.getDisplayName() + ": " + status + thrownMessage; }
    }
  }

}
