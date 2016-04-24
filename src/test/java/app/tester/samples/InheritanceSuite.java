package app.tester.samples;

import app.tester.TestMethod;

public class InheritanceSuite extends InheritanceSuper {

  @TestMethod
  public void testThird() {
    testThirdCount++;
  }

  public void nonTest() {
    nonTestCount++;
  }
}
