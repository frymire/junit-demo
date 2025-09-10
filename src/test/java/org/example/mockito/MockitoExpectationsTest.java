package org.example.mockito;

import org.example.Adder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Answer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MockitoExpectationsTest {

  // Mock fields declared at the class level can be reused in any test.
  @Mock Adder adder1;
  @Mock Adder adder2;

  @Test
  public void testMultipleInvocations() {

    // Mock the (silly) behavior that add() always returns 10.
    when(adder1.add(anyInt(), anyInt())).thenReturn(10);

    // Verify the (wrong and silly) mocked behavior to show that the
    // mock is being called, rather than real class instances. You can
    // call the same mocked invocation multiple times, even though it
    // was only defined once. These pass.
    assertEquals(10, adder1.add(1, 1));
    assertEquals(10, adder1.add(1, 1));
  }

  @Test
  void testOrderIndependence() {

    when(adder2.add(2, 3)).thenReturn(5);
    when(adder2.add(4, 5)).thenReturn(9);

    // You can call the mocked methods in any order. These pass.
    assertEquals(9, adder2.add(4, 5));
    assertEquals(5, adder2.add(2, 3));

    // Verify without order constraint.
    verify(adder2).add(4, 5);
    verify(adder2).add(2, 3);
    verifyNoMoreInteractions(adder2);
  }

  @Test
  @MockitoSettings(strictness = Strictness.STRICT_STUBS)
  public void testUncalledExpectations() {
    when(adder2.add(1, 1)).thenReturn(2);
    // No invocation of defined expectations here, JMockit will throw an UnnecessaryStubbingException.
  }

  @Test
  public void testThrownException() {

    // Say that we expect an error to be thrown for a specific invocation.
    when(adder1.add(1, -1)).thenThrow(new IllegalArgumentException());

    // This will pass, because the mock throws the exception.
    assertThrows(IllegalArgumentException.class, () -> adder1.add(1, -1));
  }

  // You can write mocked instances directly in test parameters to avoid interactions among tests.
  @Test
  public void testInvocationsCountWithMockParameter(@Mock final Adder localAdder) {

    when(localAdder.add(1, 1)).thenReturn(2);

    // Require that add() is called exactly twice.
    assertEquals(2, localAdder.add(1, 1));
    assertEquals(2, localAdder.add(1, 1)); // Fail without this.
//    assertEquals(2, localAdder.add(1, 1)); // Fail with this.

    verify(localAdder, times(2)).add(1, 1);
  }

  @Test
  public void testMockedConstruction() {

    // MockedConstruction intercepts constructor calls to set expectations on all future instances of a mocked field
    // within the try-with-resources scope.
    try (@SuppressWarnings("unused") MockedConstruction<Adder> mocked =
             mockConstruction(Adder.class, MockitoExpectationsTest::initializeAdderMock)) {

      Adder adder1 = new Adder();
      assertEquals(5, adder1.add(2, 2));
      assertEquals(3, adder1.add(1, 1));

      Adder adder2 = new Adder();
      assertEquals(3, adder2.add(1, 1));
      assertEquals(5, adder2.add(2, 2));
    }
  }

  private static void initializeAdderMock(final Adder mock, final MockedConstruction.Context context) {
    if (!context.arguments().isEmpty()) { throw new IllegalArgumentException("Expected exactly zero ctor args."); }
    when(mock.add(2, 2)).thenReturn(5);
    when(mock.add(1, 1)).thenReturn(3);
  }

  @Test
  public void testMockedConstructionWithArguments() {
    // Use MockedConstruction to set instance-specific expectations.
    try (@SuppressWarnings("unused") MockedConstruction<Adder> mocked =
             mockConstruction(Adder.class, MockitoExpectationsTest::initializeAdderMockWithArguments)) {
      assertEquals(5, new Adder("Type A").add(2, 2));
      assertEquals(6, new Adder("Type B").add(2, 2));
      assertEquals(5, new Adder("Type A").add(2, 2));
    }
  }

  private static void initializeAdderMockWithArguments(final Adder mock, final MockedConstruction.Context context) {
    if (context.arguments().size() != 1) { throw new IllegalArgumentException("Expected exactly one ctor arg"); }
    final Object argument = context.arguments().get(0);
    if (!(argument instanceof String type)) {
      throw new IllegalArgumentException("Expected String ctor arg, got: " + argument);
    }
    switch (type) {
      case "Type A" -> when(mock.add(eq(2), eq(2))).thenReturn(5);
      case "Type B" -> when(mock.add(eq(2), eq(2))).thenReturn(6);
      default -> throw new IllegalArgumentException("Unexpected Adder type: " + type);
    }
  }

  @Test
  public void testMockReturnSequence(@Mock final Adder localAdder) {

    // Mock behavior that returns different values for subsequent invocations.
    when(localAdder.add(anyInt(), anyInt())).thenReturn(0, 1, 2, 3);

    assertEquals(0, localAdder.add(1, 2));
    assertEquals(1, localAdder.add(3, 4));
    assertEquals(2, localAdder.add(5, 6));
    assertEquals(3, localAdder.add(7, 8));
    assertEquals(3, localAdder.add(9, 0));  // continue to return the last value
  }

  @Test
  public void testCustomDelegate(@Mock final Adder adder) {

    // Rather than scripting exact results for specific mocked method calls, you can
    // define an alternative method in a delegate.
    final class CustomDelegate implements Answer<Integer> {
      @Override
      public Integer answer(InvocationOnMock invocation) {
        int i = invocation.getArgument(0, Integer.class);
        int j = invocation.getArgument(1, Integer.class);
        return i - j;
      }
    }

    when(adder.add(anyInt(), anyInt())).thenAnswer(new CustomDelegate());
    assertEquals(2, adder.add(5, 3));
  }

}
