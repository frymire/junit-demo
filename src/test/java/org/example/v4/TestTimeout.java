package org.example.v4;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class TestTimeout {

  // This results in an unexpected exception in v4, not an assert failure.
  @Test(timeout=500)
  public void testLongMethod() throws InterruptedException { 
    System.out.println("Starting testLongMethod()...");
    Thread.sleep(600);
    assertTrue(true);
  }
}
