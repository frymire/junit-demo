//package org.example.jmockit;
//
//import mockit.Delegate;
//import mockit.Expectations;
//import mockit.Injectable;
//import mockit.Mocked;
//import mockit.StrictExpectations;
//import org.example.Adder;
//import org.junit.Test;
//
//import static org.junit.Assert.assertEquals;
//
//public class JMockitExpectations {
//
//  // @Injectable mocks one particular instance, so you can mix with real instances.
//  Talker realTalker = new Talker();
//  @Injectable Talker fakeTalker;
//  //  @Mocked Talker fakeTalker; // Doesn't work.
//
//  @Test public void testWithInjectedMock() {
//
//    // Mock some behavior if someone calls sayHi() on the injectedTalker instance.
//    new Expectations() {{
//      fakeTalker.sayHi();
//      result = "The mocked talker says hi.";
//    }};
//
//    assertEquals("The mocked talker says hi.", fakeTalker.sayHi());
//
//    // If you used @Mocked instead of @Injected for the talker test double above,
//    // JMockit would have also mocked the realTalker instance, even though it was
//    // declared without any annotation. As a result, this test would have failed.
//    assertEquals("The real talker says hi.", realTalker.sayHi());
//
//  }
//
//  @Test public void testFutureInstanceExpectationsWithOneRecorder(@Mocked final Adder anyAdder) {
//
//    // To set expectations on all future instances of a mocked field, instantiate a
//    // new instance in the Expectations block to record behavior.
//    new Expectations() {{
//      Adder recorderAdder = new Adder();
//      recorderAdder.add(2, 2);
//      result = 5;
//      recorderAdder.add(1, 1);
//      result = 3;
//    }};
//
//    // All new instances expect the recorded behavior.
//
//    Adder testAdder1 = new Adder();
//    assertEquals(3, testAdder1.add(1, 1));
//    assertEquals(5, testAdder1.add(2, 2));
//
//    Adder testAdder2 = new Adder();
//    assertEquals(3, testAdder2.add(1, 1));
//    assertEquals(5, testAdder2.add(2, 2));
//
//  }
//
//  @Test public void testFutureInstanceExpectationsWithMultipeRecorders(
//      @Mocked final Adder recorderAdderA,
//      @Mocked final Adder recorderAdderB
//  ) {
//
//    // To set expectations on two different subsets of future instances of a mocked
//    // field, map separate recorder mock fields to new instances in the expectations
//    // block, and record their behavior separately.
//    new Expectations() {{
//
//      new Adder("Type A");
//      result = recorderAdderA;
//      recorderAdderA.add(2, 2);
//      result = 5;
//
//      new Adder("Type B");
//      result = recorderAdderB;
//      recorderAdderB.add(2, 2);
//      result = 6;
//
//    }};
//
//    // Each new type of new instances expects the corresponding recorded behavior.
//
//    Adder testAdderA = new Adder("Type A");
//    assertEquals(5, testAdderA.add(2, 2));
//
//    Adder testAdderB = new Adder("Type B");
//    assertEquals(6, testAdderB.add(2, 2));
//
//  }
//
//}
