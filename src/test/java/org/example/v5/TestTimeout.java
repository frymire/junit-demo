package org.example.v5;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;


public class TestTimeout {

  @Test
  public void testLongMethod() { // fails

    System.out.println("Starting testLongMethod()...");

    // Counterintuitively, if it *doesn't* timeout, this will pass.
    // This version completes the code block, even after the timeout is reached.
    assertTimeout(Duration.ofMillis(200), () -> {
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    });
  }

  @Test
  public void testLongMethodPreemptively() { // fails

    System.out.println("Starting testLongMethodPreemptively()...");

    // This version will terminate immediately when the timeout is reached.
    assertTimeoutPreemptively(Duration.ofMillis(200), () -> {
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    });
  }
}
