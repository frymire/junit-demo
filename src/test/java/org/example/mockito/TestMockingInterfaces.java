package org.example.mockito;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedConstruction;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TestMockingInterfaces {

  // Define an interface for which we'll mock all extending classes and instances.
  public interface ReturnsValue { int getValue(); }

  // Define a class that implements the interface
  final class ValueOf1 implements ReturnsValue {
    public int getValue() { return 1; }
  }

  public final class ValueAdder {
    private final ReturnsValue value1 = new ValueOf1();
    private final ReturnsValue value2 = new ReturnsValue() {
      public int getValue() { return 2; }
    };
    public int getValueSum() { return value1.getValue() + value2.getValue(); }
  }

  public static class ValueAdderWithDependencyInjection {
    private final ReturnsValue value1;
    private final ReturnsValue value2;
    public ValueAdderWithDependencyInjection(ReturnsValue value1, ReturnsValue value2) {
      this.value1 = value1;
      this.value2 = value2;
    }
    public int getValueSum() { return value1.getValue() + value2.getValue(); }
  }

  @Test
  public void testUsingMockedConstruction() {
    // Mock construction of ValueOf1 class
    try (MockedConstruction<ValueOf1> mockedValueOf1 = mockConstruction(ValueOf1.class,
        (mock, context) -> { when(mock.getValue()).thenReturn(3); })) {
      assertEquals(5, new ValueAdder().getValueSum()); // 3 + 2
      assertEquals(1, mockedValueOf1.constructed().size());  // verify that the construction was mocked
    }
  }

  @Test
  public void testUsingDependencyInjection() {
    ReturnsValue mock1 = mock(ReturnsValue.class);
    ReturnsValue mock2 = mock(ReturnsValue.class);
    when(mock1.getValue()).thenReturn(3);
    when(mock2.getValue()).thenReturn(4);
    assertEquals(7, new ValueAdderWithDependencyInjection(mock1, mock2).getValueSum());
  }
}
