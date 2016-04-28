package app.tester;

import app.tester.samples.ExceptionSuite;
import app.tester.samples.InheritanceSuite;
import app.tester.samples.SetupSuite;
import app.tester.samples.TeardownSuite;
import org.junit.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestRunnerTest {

  @Test
  public void testAllAndOnlyTestMethodsAreCalledOnce() throws Exception {
    InheritanceSuite suite = new InheritanceSuite();
    new TestRunner().runTests(suite);
    assertEquals("testFirst called", 1, suite.testFirstCount);
    assertEquals("testSecond called", 1, suite.testSecondCount);
    assertEquals("testThird called", 1, suite.testThirdCount);
    assertEquals("nonTest not called", 0, suite.nonTestCount);
    assertEquals("nonTestSuper not called", 0, suite.nonTestSuperCount);
    assertEquals("nonTestSuperSuper not called", 0, suite.nonTestSuperSuperCount);
  }

  @Test
  public void testCorrectResultReported() throws Exception {
    ExceptionSuite suite = new ExceptionSuite();
    List<TestResult> results = new TestRunner().runTests(suite);
    assertEquals("test results count", 5, results.size());
    assertPassed(results, "testNoThrow");
    assertPassed(results, "testThrowExpectedIAE");
    assertFailed(results, "testExpectedButThrowsDifferent");
    assertFailed(results, "testExpectedButNoThrow");
    assertFailed(results, "testThrowUnexpectedIAE");
  }

  @Test
  public void testCorrectDurationsReported() throws Exception {
    ExceptionSuite suite = new ExceptionSuite();
    List<TestResult> results = new TestRunner().runTests(suite);
    assertEquals("test results count", 5, results.size());
    assertDuration(results, "testNoThrow", ExceptionSuite.SLEEP_NOTHROW);
    assertDuration(results, "testThrowExpectedIAE", ExceptionSuite.SLEEP_THROW_EXPECTED);
    assertDuration(results, "testExpectedButThrowsDifferent", ExceptionSuite.SLEEP_THROW_UNEXPECTED);
    assertDuration(results, "testExpectedButNoThrow", ExceptionSuite.SLEEP_NOTHROW);
    assertDuration(results, "testThrowUnexpectedIAE", ExceptionSuite.SLEEP_THROW_UNEXPECTED);
  }

  @Test
  public void testSetupWithInheritance() throws Exception {
    SetupSuite suite = new SetupSuite();
    new TestRunner().runTests(suite);
    assertEquals("all test methods called", 2, suite.testCycle);
    assertEquals("@Setup called before each test: before1", suite.testCycle, suite.before1);
    assertEquals("@Setup called before each test: before2", suite.testCycle, suite.before2);
    if (suite.failure != null)
      throw suite.failure;
  }

  @Test
  public void testTeardownWithInheritance() throws Exception {
    TeardownSuite suite = new TeardownSuite();
    new TestRunner().runTests(suite);
    assertEquals("all test methods called", 2, suite.testCycle);
    assertEquals("@Teardown called after each test: after1", suite.testCycle, suite.after1);
    assertEquals("@Teardown called after each test: after2", suite.testCycle, suite.after2);
    if (suite.failure != null)
      throw suite.failure;
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
