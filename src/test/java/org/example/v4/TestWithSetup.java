package org.example.v4;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

public class TestWithSetup extends TestCase  {
  
   protected double fValue1;
   protected double fValue2;
   
   @Before 
   public void setUp() {
      fValue1 = 2.0;
      fValue2 = 3.0;
   }
  
   @Test
   public void testAdd() {
     
      // TestCase.countTestCases() reports the number of test cases.
      // TestCase.getName() gets the name of this test.
      System.out.println("Test Case #" + countTestCases());
      System.out.println("Test Case Name = " + getName());

      // Use the setName() method of TestCase to change the name of this test.
      setName("testNewAdd");
      String newName = getName();
      System.out.println("Updated Test Case Name = " + newName);
   }
   
   // TearDown is used to close the connection or clean up activities.
   public void tearDown() { }
}
