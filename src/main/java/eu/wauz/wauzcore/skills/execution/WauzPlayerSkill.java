package eu.wauz.wauzcore.skills.execution;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public interface WauzPlayerSkill { 
	
	public String getSkillId();
	
	default public String getSkillDescription() {
		return
			"[" + ChatColor.RED + getSkillDescriptionType() + ChatColor.WHITE + "] " +
			"[" + ChatColor.GRAY + getSkillDescriptionEffect() + ChatColor.WHITE + "]";
	}
	
	public String getSkillDescriptionType();
	
	public String getSkillDescriptionEffect();
	
	default public String getSkillStats() {
		return
			"[" + ChatColor.GRAY + "CD: " + ChatColor.YELLOW + getCooldownSeconds() + ChatColor.GRAY + "s" + ChatColor.WHITE + "] " +
			"[" + ChatColor.GRAY + "MP: " + ChatColor.BLUE + getManaCost() + ChatColor.WHITE + "]";
	}
	
	public int getCooldownSeconds();
	
	public int getManaCost();
	
	public boolean executeSkill(Player player);

}
