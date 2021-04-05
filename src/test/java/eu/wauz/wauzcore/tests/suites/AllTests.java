package eu.wauz.wauzcore.tests.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import eu.wauz.wauzcore.tests.CurrencyTest;
import eu.wauz.wauzcore.tests.EquipmentTest;
import eu.wauz.wauzcore.tests.LevelingTest;
import eu.wauz.wauzcore.tests.OneBlockTest;

/**
 * This suite executes all unit tests.
 * 
 * @author Wauzmons
 */
@RunWith(Suite.class)
@SuiteClasses({CurrencyTest.class, EquipmentTest.class, LevelingTest.class, OneBlockTest.class})
public class AllTests {

}
