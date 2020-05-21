package eu.wauz.wauzunit.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import eu.wauz.wauzunit.CurrencyTest;
import eu.wauz.wauzunit.LevelingTest;
import eu.wauz.wauzunit.OneBlockTests;

/**
 * This suite executes all unit tests.
 * 
 * @author Wauzmons
 */
@RunWith(Suite.class)
@SuiteClasses({CurrencyTest.class, LevelingTest.class, OneBlockTests.class})
public class AllTests {

}
