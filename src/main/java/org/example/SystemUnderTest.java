package org.example;

// This is used in the mocking demo.
public class SystemUnderTest {

  private final Adder adder;
  private final Talker talker;

  public SystemUnderTest(Adder adder, Talker talker) {
    this.adder = adder;
    this.talker = talker;
  }

  public int add(int i, int j) { return adder.add(i, j); }
  public String sayHi() { return talker.sayHi(); }
}
