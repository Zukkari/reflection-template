package app.tester.samples;

import app.tester.TestMethod;

public class InheritanceSuperSuper {

  public int testFirstCount = 0;
  public int testSecondCount = 0;
  public int testThirdCount = 0;
  public int nonTestCount = 0;
  public int nonTestSuperCount = 0;
  public int nonTestSuperSuperCount = 0;

  @TestMethod
  public void testFirst() {
    testFirstCount++;
  }

  public void nonTestSuperSuper() {
    nonTestSuperSuperCount++;
  }
}
