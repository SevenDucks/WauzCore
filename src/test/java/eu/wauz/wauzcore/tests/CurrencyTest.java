package eu.wauz.wauzcore.tests;

import static org.junit.Assert.assertEquals;

import org.bukkit.ChatColor;
import org.junit.Test;

import eu.wauz.wauzcore.system.util.Formatters;
import eu.wauz.wauzcore.tests.abstracts.AbstractCoreTest;

/**
 * Tests the currency mechanics from WauzCore.
 * 
 * @author Wauzmons
 */
public class CurrencyTest extends AbstractCoreTest {
	
	/**
	 * Tests the silver, gold and crystal coin formatting.
	 */
	@Test
	public void testCoinFormatting() {
		String format03Digits = ChatColor.GRAY + "100";
		String format06Digits = ChatColor.GOLD + "200" + ChatColor.WHITE + "," + format03Digits;
		String format09Digits = ChatColor.AQUA + "300" + ChatColor.WHITE + "," + format06Digits;
		String format12Digits = ChatColor.AQUA + "400" + ChatColor.WHITE + "," + format09Digits;
		
		assertEquals(format03Digits, Formatters.formatCoins(100L));
		assertEquals(format06Digits, Formatters.formatCoins(200100L));
		assertEquals(format09Digits, Formatters.formatCoins(300200100L));
		assertEquals(format12Digits, Formatters.formatCoins(400300200100L));
	}

}
