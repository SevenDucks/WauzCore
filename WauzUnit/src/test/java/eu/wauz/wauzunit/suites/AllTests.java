package eu.wauz.wauzunit.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import eu.wauz.wauzunit.CurrencyTest;

/**
 * This suite executes all unit tests.
 * 
 * @author Wauzmons
 */
@RunWith(Suite.class)
@SuiteClasses({CurrencyTest.class})
public class AllTests {

}
