package com.google.martian;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
  BodyModifierTest.class,
  ClientTest.class,
  CookieModifierTest.class,
  FifoGroupTest.class,
  HeaderBlacklistTest.class,
  HeaderFilterTest.class,
  HeaderModifierTest.class,
  HeaderVerifierTest.class,
  IntegrationTest.class,
  MethodVerifierTest.class,
  PingbackVerifierTest.class,
  QueryStringFilterTest.class,
  QueryStringModifierTest.class,
  QueryStringVerifierTest.class,
  StatusModifierTest.class,
  StatusVerifierTest.class,
  UrlFilterTest.class,
  UrlModifierTest.class,
  UrlVerifierTest.class,
  VerificationFailureTest.class
})
public class ClientTestSuite {}
