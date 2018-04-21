package app.tester.samples;

import app.tester.TestMethod;

import java.io.IOException;

public class SimpleResultsSuite {

  @TestMethod
  public void passingTest() throws Exception {
  }

  @TestMethod
  public void failingWithRETest() throws Exception {
    throw new RuntimeException("test failed");
  }

  @TestMethod
  public void failingWithIOETest() throws Exception {
    throw new IOException("test failed");
  }
}
