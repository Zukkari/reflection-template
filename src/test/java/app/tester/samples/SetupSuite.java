package app.tester.samples;

import app.tester.Setup;
import app.tester.TestMethod;

public class SetupSuite extends SetupSuper {

  @Setup
  public void before2() {
    before2++;
    if (before2 <= testCycle)
      failure = new IllegalStateException("@Setup must run before the test");
  }

  @TestMethod
  public void testFirst() {
    testCycle++;
    if (before1 != testCycle || before2 != testCycle)
      failure = new IllegalStateException("all @Setup methods have not run");
  }

  @TestMethod
  public void testSecond() {
    testCycle++;
    if (before1 != testCycle || before2 != testCycle)
      failure = new IllegalStateException("all @Setup methods have not run");
    throw new NullPointerException("intentional");
  }
}
