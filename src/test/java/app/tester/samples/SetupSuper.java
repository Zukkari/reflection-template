package app.tester.samples;

import app.tester.Setup;

public class SetupSuper {

  public int testCycle = 0;
  public int before1 = 0;
  public int before2 = 0;
  public Exception failure;

  @Setup
  public void before1() {
    before1++;
    if (before1 <= testCycle)
      failure = new IllegalStateException("@Setup must run before the test");
  }
}
