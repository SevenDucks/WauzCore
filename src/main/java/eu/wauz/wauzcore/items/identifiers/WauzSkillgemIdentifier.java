package eu.wauz.wauzcore.items.identifiers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.items.InventorySerializer;
import eu.wauz.wauzcore.skills.WauzPlayerSkill;
import eu.wauz.wauzcore.skills.WauzPlayerSkillExecutor;
import eu.wauz.wauzcore.system.util.Chance;
import eu.wauz.wauzcore.system.util.Components;

/**
 * Typed identifier, used for identifying skillgem items.
 * 
 * @author Wauzmons
 * 
 * @see WauzIdentifier
 */
public class WauzSkillgemIdentifier {
	
	/**
	 * Identifies the item, based on the given event.
	 * Takes a random skill and adds it to the skillgem name and lore.
	 * Automatically changes the material of the item, to make it a valid skillgem.
	 * Plays an anvil sound to the player, when the identifying has been completed.
	 * 
	 * @param player The player who identifies the item.
	 * @param skillgemItemStack The skillgem item stack, that is getting identified.
	 * @param manual If the identification was triggered manually.
	 */
	public void identifySkillgem(Player player, ItemStack skillgemItemStack, boolean manual) {
		List<String> skills = WauzPlayerSkillExecutor.getAllSkillgemIds();
		String skillgemName = skills.get(Chance.randomInt(skills.size()));
		
		WauzPlayerSkill skill = WauzPlayerSkillExecutor.getSkill(skillgemName);
		createSkillgem(skillgemItemStack, skill);
		
		if(manual) {
			player.getWorld().playEffect(player.getLocation(), Effect.ANVIL_USE, 0);
		}
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
		ItemStack itemStack = new ItemStack(Material.REDSTONE);
		ItemMeta itemMeta = itemStack.getItemMeta();
		Components.displayName(itemMeta, ChatColor.DARK_RED + "Skillgem: " + ChatColor.LIGHT_PURPLE + skill.getSkillId());
		
		List<String> lores = new ArrayList<String>();
		lores.add(ChatColor.GRAY + "Can be inserted into a Weapon,");
		lores.add(ChatColor.GRAY + "which possesses an empty Skill Slot.");
		lores.add("");
		lores.add(ChatColor.WHITE + "Adds Right-Click Skill:");
		lores.add(ChatColor.WHITE + skill.getSkillDescription());
		lores.add(ChatColor.WHITE + skill.getSkillStats());
		
		Components.lore(itemMeta, lores);
		itemStack.setItemMeta(itemMeta);
		ItemStack generatedItemStack = InventorySerializer.serialize(itemStack);
		skillgemItemStack.setType(generatedItemStack.getType());
		skillgemItemStack.setItemMeta(generatedItemStack.getItemMeta());
		return skillgemItemStack;
	}

}
