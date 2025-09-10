package org.example.mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.example.Adder;
import org.example.Talker;
import org.example.SystemUnderTest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TestInjection {

  @Mock Adder adder;
  @Mock Talker talker;

  // Mockito will construct this and inject mocked Adder and Talker (but not primitives, so limited utility).
  @InjectMocks SystemUnderTest testInstance;

  @BeforeEach
  void stubBehavior() {
    when(adder.add(anyInt(), anyInt())).thenReturn(1);
    lenient().when(talker.sayHi()).thenReturn("Whassup?");  // lenient means it doesn't have to be invoked
  }

  @AfterEach
  void verifyInteractions() {
    verify(adder).add(eq(2), anyInt()); // verify adder.add(2, ?) was called
    verify(adder).add(anyInt(), eq(3)); // verify adder.add(?, 3) was called
    verify(talker, never()).sayHi(); // verify talker.sayHi() was never called
    verifyNoMoreInteractions(adder, talker);
  }

  @Test
  void testInjectMocks() {
    // For classes with no literals in the constructor signature, @InjectMocks could
    // instantiate the system under test for you. Otherwise, just instantiate it yourself.
    assertEquals(1, testInstance.add(2, 3));
  }

  @Test
  void testInjectionWithDirectInstantiation() {
    testInstance = new SystemUnderTest(adder, talker);
    assertEquals(1, testInstance.add(2, 3));
  }
}
