package app.tester.samples;

import app.tester.TestMethod;

public class SimpleSuite {

  public int testCount = 0;
  public int nonTestCount = 0;
  public int nonTestPrivateCount = 0;

  @TestMethod
  public void test() {
    testCount++;
  }

  public void nonTest() {
    nonTestCount++;
  }

  private void nonTestPrivate() {
    nonTestPrivateCount++;
  }
}
