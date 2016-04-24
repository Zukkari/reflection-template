package app.tester;

import java.util.List;

public class TestRunner {

  /**
   * Runs all valid {@link TestMethod} methods in the given test suite.
   * The test methods, setup methods and teardown methods are searched from the provided class,
   * including all its superclasses. All tests are run, even if one of them fails. If a Setup or
   * a Teardown method fails, then the testing is aborted with an exception.
   *
   * @return a list of {@link TestResult} that contains a result for each test method.
   */
  public List<TestResult> runTests(Object testSuite) throws Exception {
    return null; // TODO implement
  }
}
