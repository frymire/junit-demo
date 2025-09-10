package org.example.mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.example.Adder;
import org.example.Talker;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.MockedConstruction;

class TestVerifications {

  @Test
  void testVerifications() {
    Adder adder = mock(Adder.class);
    adder.add(1, 1);
    verify(adder, times(1)).add(anyInt(), eq(1));
    verify(adder, never()).add(anyDouble(), anyDouble());  // verify it was never invoked with doubles
    verifyNoMoreInteractions(adder);
  }

  @Test
  void testMinAndMaxInvocations() {
    Adder adder = mock(Adder.class);

    adder.add(1, 2);
    adder.add(3, 4);
    adder.add(5, 6);

    verify(adder, atLeast(1)).add(anyInt(), anyInt());
    assertThrows(AssertionError.class,
        // Fails due to 3rd call
        () -> verify(adder, atMost(2)).add(anyInt(), anyInt()));
  }

  @Test
  void testConstructorInvocationCount() {
    try (MockedConstruction<Adder> mocked = mockConstruction(Adder.class)) {
      assertTrue(mocked.constructed().isEmpty());
    }
    try (MockedConstruction<Adder> mocked = mockConstruction(Adder.class)) {
      new Adder();
      new Adder();
      assertEquals(2, mocked.constructed().size());
    }
  }

  @Test
  void testVerifyInOrderWithTwoMocks() {

    Adder adder1 = mock(Adder.class, "adder1");
    Adder adder2 = mock(Adder.class, "adder2");

    adder1.add(1, 2);
    adder2.add(10, 11);
    adder1.add(3, 4);
    adder2.add(12, 13);

    // Ordered checks on adder1 work despite interleaved calls.
    InOrder inOrder = inOrder(adder1);
    inOrder.verify(adder1).add(1, 2);
    inOrder.verify(adder1).add(3, 4);

    // Independent, order-agnostic checks on adder2.
    verify(adder2).add(12, 13);
    verify(adder2).add(10, 11);

    verifyNoMoreInteractions(adder1, adder2);
  }

  @Test
  void testFullVerification() {

    Adder adder = mock(Adder.class);
    adder.add(1, 1);
    adder.add(2, 2);
    adder.add(3, 3);

    verify(adder).add(2, 2);
    verify(adder).add(1, 1);
    // Fails because add(3,3) remains unverified
    assertThrows(AssertionError.class, () -> verifyNoMoreInteractions(adder));
  }

  @Test
  void testFullVerificationInOrder() {

    // Invocations are saved in the mocked class as they are made.
    Adder adder = mock(Adder.class);
    adder.add(1, 1);
    adder.add(2, 2);
    adder.add(3, 3);

    // You can check the order after the fact.
    assertThrows(AssertionError.class, () -> {
      InOrder inOrder = inOrder(adder);
      inOrder.verify(adder).add(1, 1);
      inOrder.verify(adder).add(3, 3);  // fails here
      inOrder.verify(adder).add(2, 2);
      inOrder.verifyNoMoreInteractions();
    });
  }

  @Test
  void testFullVerificationOnDifferentInstances() {

    Adder adder1 = mock(Adder.class);
    Adder adder2 = mock(Adder.class);
    Talker talker = mock(Talker.class);

    adder1.add(1, 1);
    adder1.add(2, 2);
    adder2.add(3, 3);
    talker.sayHi();

    // Scope to adder1 only
    verify(adder1).add(1, 1);
    verify(adder1).add(2, 2);
    verifyNoMoreInteractions(adder1);

    // Scope to talker only
    verify(talker).sayHi();
    verifyNoMoreInteractions(talker);

    // We didn’t verify adder2 further here
  }

  @Test
  void testNoInvocationsOnSpecificMock() {
    Adder adder1 = mock(Adder.class);
    Adder adder2 = mock(Adder.class);
    adder1.add(1, 1);
    verifyNoInteractions(adder2);
  }

  @Test
  void testInvocationsWithSpecificParameters() {

    Adder adder = mock(Adder.class);
    adder.add(2, 3);
    adder.add(2, 3);

    verify(adder, atLeastOnce()).add(intThat(i -> i >= 2), eq(3));

    // For doubles, you could use an ArgumentCaptor and assert with a delta.
    Adder floatingAdder = mock(Adder.class);
    floatingAdder.add(1.95, 3.05);

    ArgumentCaptor<Double> a = ArgumentCaptor.forClass(Double.class);
    ArgumentCaptor<Double> b = ArgumentCaptor.forClass(Double.class);
    verify(floatingAdder).add(a.capture(), b.capture());
    assertEquals(2.0, a.getValue(), 0.1);
    assertEquals(3.0, b.getValue(), 0.1);
  }

  @Test
  void testCapturingArguments() {

    Adder adder = mock(Adder.class);
    adder.add(1, 1);

    ArgumentCaptor<Integer> a = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<Integer> b = ArgumentCaptor.forClass(Integer.class);

    verify(adder).add(a.capture(), b.capture());
    assertEquals(a.getValue(), b.getValue());
  }

  @Test
  void testCapturingArgumentSequences() {

    Adder adder = mock(Adder.class);
    adder.add(0, 1);
    adder.add(1, 1);
    adder.add(2, 1);

    ArgumentCaptor<Integer> a = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<Integer> b = ArgumentCaptor.forClass(Integer.class);
    verify(adder, times(3)).add(a.capture(), b.capture());

    assertEquals(List.of(0, 1, 2), a.getAllValues());
    assertEquals(List.of(1, 1, 1), b.getAllValues());
  }

  @Test
  void testInvocationsOverLoop() {
    Adder adder = mock(Adder.class);
    final int NUM_ITERATIONS = 20;
    for (int i = 0; i < NUM_ITERATIONS; i++) adder.add(i, i);
    verify(adder, times(NUM_ITERATIONS)).add(anyInt(), anyInt());
  }

}
