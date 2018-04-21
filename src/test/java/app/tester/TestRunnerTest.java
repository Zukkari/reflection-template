package app.tester;

import app.tester.samples.DurationSuite;
import app.tester.samples.ExceptionResultsSuite;
import app.tester.samples.SetupSuite;
import app.tester.samples.SimpleResultsSuite;
import app.tester.samples.SimpleSuite;
import app.tester.samples.TeardownSuite;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestRunnerTest {

  @Test
  public void callsOnlyTestMethods() throws Exception {
    SimpleSuite suite = new SimpleSuite();
    new TestRunner().runTests(suite);
    assertEquals("testFirst called", 1, suite.testCount);
    assertEquals("nonTest not called", 0, suite.nonTestCount);
    assertEquals("nonTestPrivate not called", 0, suite.nonTestPrivateCount);
  }

  @Test
  public void reportsCorrectSimpleResults() throws Exception {
    SimpleResultsSuite suite = new SimpleResultsSuite();
    List<TestResult> results = new TestRunner().runTests(suite);
    assertPassed(results, "passingTest");
    assertFailed(results, "failingWithRETest");
    assertFailed(results, "failingWithIOETest");
  }

  @Test
  public void reportsCorrectExceptionResults() throws Exception {
    ExceptionResultsSuite suite = new ExceptionResultsSuite();
    List<TestResult> results = new TestRunner().runTests(suite);
    assertPassed(results, "testThrowExpectedIAE");
    assertFailed(results, "testExpectedIAEButThrowsNPE");
    assertFailed(results, "testExpectedIAEButNoThrow");
  }

  @Test
  public void reportsCorrectDurations() throws Exception {
    DurationSuite suite = new DurationSuite();
    List<TestResult> results = new TestRunner().runTests(suite);
    assertDuration(results, "testPassing", suite.durationPassing);
    assertDuration(results, "testPassingWithException", suite.durationPassingExceptionally);
    assertDuration(results, "testFailing", suite.durationFailing);
  }

  @Test
  public void callsSetupForEachTestMethod() throws Exception {
    SetupSuite suite = new SetupSuite();
    new TestRunner().runTests(suite);
    assertEquals("all test methods called", 2, suite.testCycle);
    assertEquals("@Setup called before each test: before1", suite.testCycle, suite.before1);
    assertEquals("@Setup called before each test: before2", suite.testCycle, suite.before2);
    if (suite.failure != null)
      throw new ExecutionException(suite.failure);
  }

  @Test
  public void callsTeardownForEachTestMethod() throws Exception {
    TeardownSuite suite = new TeardownSuite();
    new TestRunner().runTests(suite);
    assertEquals("all test methods called", 2, suite.testCycle);
    assertEquals("@Teardown called after each test: after1", suite.testCycle, suite.after1);
    assertEquals("@Teardown called after each test: after2", suite.testCycle, suite.after2);
    if (suite.failure != null)
      throw new ExecutionException(suite.failure);
  }

  private static void assertPassed(List<TestResult> results, String testName) {
    assertEquals("results contain " + testName, 1, ofName(results, testName).count());
    assertTrue(testName + " passes", ofName(results, testName).allMatch(TestResult::isPassed));
  }

  private static void assertFailed(List<TestResult> results, String testName) {
    assertEquals("results contain " + testName, 1, ofName(results, testName).count());
    assertFalse(testName + " fails", ofName(results, testName).anyMatch(TestResult::isPassed));
  }

  private void assertDuration(List<TestResult> results, String testName, long duration) {
    assertEquals("results contain " + testName, 1, ofName(results, testName).count());
    assertTrue(
        testName + " duration at least " + duration,
        ofName(results, testName).allMatch(r -> r.getDurationMillis() >= duration));
    assertTrue(
        testName + " duration no longer than 2x the expected " + duration,
        ofName(results, testName).allMatch(r -> r.getDurationMillis() < duration * 2));
  }

  private static Stream<TestResult> ofName(List<TestResult> results, String testName) {
    return results.stream().filter(r -> testName.equals(r.getTestName()));
  }
}
