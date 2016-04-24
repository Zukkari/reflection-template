package app.tester.samples;

import app.tester.TestMethod;

public class InheritanceSuper extends InheritanceSuperSuper {

  @TestMethod
  public void testSecond() {
    testSecondCount++;
  }

  public void nonTestSuper() {
    nonTestSuperCount++;
  }
}
