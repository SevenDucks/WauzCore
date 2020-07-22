package eu.wauz.wauzcore.items.identifiers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.skills.execution.WauzPlayerSkill;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkillExecutor;

/**
 * Typed identifier, used for identifying skillgem items.
 * 
 * @author Wauzmons
 * 
 * @see WauzIdentifier
 */
public class WauzSkillgemIdentifier {
	
	/**
	 * A random instance for determining random skills.
	 */
	private static Random random = new Random();
	
	/**
	 * Identifies the item, based on the given event.
	 * Takes a random skill and adds it to the skillgem name and lore.
	 * Automatically changes the material of the item, to make it a valid skillgem.
	 * Plays an anvil sound to the player, when the identifying has been completed.
	 * 
	 * @param player The player who identifies the item.
	 * @param skillgemItemStack The skillgem item stack, that is getting identified.
	 */
	public void identifySkillgem(Player player, ItemStack skillgemItemStack) {
		List<String> skills = WauzPlayerSkillExecutor.getAllSkillIds();
		String skillgemName = skills.get(random.nextInt(skills.size()));
		
		WauzPlayerSkill skill = WauzPlayerSkillExecutor.getSkill(skillgemName);
		createSkillgem(skillgemItemStack, skill);
		
		player.getWorld().playEffect(player.getLocation(), Effect.ANVIL_USE, 0);
	}
	
	/**
	 * Creates a skillgem.
	 * 
	 * @param skillgemItemStack The item stack, that is made into a skillgem.
	 * @param skill The skill, that should be contained in the skillgem.
	 * 
	 * @return The created skillgem item stack.
	 */
	public static ItemStack createSkillgem(ItemStack skillgemItemStack, WauzPlayerSkill skill) {
		ItemMeta itemMeta = skillgemItemStack.getItemMeta();
		itemMeta.setDisplayName(ChatColor.DARK_RED + "Skillgem: " + ChatColor.LIGHT_PURPLE + skill.getSkillId());
		
		List<String> lores = new ArrayList<String>();
		lores.add(ChatColor.GRAY + "Can be inserted into a Weapon,");
		lores.add(ChatColor.GRAY + "which possesses an empty Skill Slot.");
		lores.add("");
		lores.add(ChatColor.WHITE + "Adds Right-Click Skill:");
		lores.add(ChatColor.WHITE + skill.getSkillDescription());
		lores.add(ChatColor.WHITE + skill.getSkillStats());
		
		itemMeta.setLore(lores);
		skillgemItemStack.setItemMeta(itemMeta);
		skillgemItemStack.setType(Material.REDSTONE);
		return skillgemItemStack;
	}

}
