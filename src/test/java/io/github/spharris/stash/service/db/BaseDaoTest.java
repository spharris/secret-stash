package io.github.spharris.stash.service.db;

import org.junit.After;
import org.junit.Before;

abstract class BaseDaoTest {
  
  @Before
  public void initialize() {
    // create injector
    // open connection
    // create schema
  }
  
  @After
  public void cleanup() {
    // drop tables
    // close connection
  }
}
