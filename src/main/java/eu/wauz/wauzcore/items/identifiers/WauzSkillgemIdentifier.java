package eu.wauz.wauzcore.items.identifiers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.skills.execution.WauzPlayerSkill;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkillExecutor;
import net.md_5.bungee.api.ChatColor;

public class WauzSkillgemIdentifier {
	
	public void identifySkillgem(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		ItemStack skillgemItemStack = event.getCurrentItem();
		
		Random random = new Random();
		List<String> skills = WauzPlayerSkillExecutor.getAllSkillIds();
		String skillgemName = skills.get(random.nextInt(skills.size()));
		
		ItemMeta itemMeta = skillgemItemStack.getItemMeta();
		itemMeta.setDisplayName(ChatColor.DARK_RED + "Skillgem: " + ChatColor.LIGHT_PURPLE + skillgemName);
		
		WauzPlayerSkill skill = WauzPlayerSkillExecutor.getSkill(skillgemName);
		
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
		
		player.getWorld().playEffect(player.getLocation(), Effect.ANVIL_USE, 0);
	}

}
