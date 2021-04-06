package eu.wauz.wauzcore.skills;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.system.util.Components;
import eu.wauz.wauzcore.system.util.UnicodeUtils;

/**
 * A skill or similar, that can be casted through a quick slot.
 * 
 * @author Wauzmons
 */
public class Castable {
	
	/**
	 * The icon of the castable.
	 */
	private ItemStack iconItemStack;
	
	/**
	 * The skill to be casted.
	 */
	private WauzPlayerSkill skill;
	
	/**
	 * Constructs a castable skill.
	 * 
	 * @param iconItemStack The icon of the castable.
	 * @param skill The skill to be casted.
	 */
	public Castable(ItemStack iconItemStack, WauzPlayerSkill skill) {
		this.iconItemStack = iconItemStack;
		this.skill = skill;
	}
	
	/**
	 * Lets the given player cast this castable.
	 * 
	 * @param player The player who should cast the castable.
	 */
	public void cast(Player player) {
		if(skill != null) {
			ItemStack itemStack = player.getEquipment().getItemInMainHand();
			WauzPlayerSkillExecutor.tryToUseSkill(player, itemStack, skill);
		}
	}
	
	/**
	 * Gets the item, used to assign this castable to a quick slot.
	 * 
	 * @return The created item stack.
	 * 
	 * @see SkillQuickSlots#getSkillInfo(WauzPlayerSkill)
	 */
	public ItemStack getAssignmentItem() {
		if(skill != null) {
			ItemStack skillItemStack = iconItemStack.clone();
			ItemMeta skillItemMeta = skillItemStack.getItemMeta();
			Components.displayName(skillItemMeta, ChatColor.DARK_AQUA + "Assign Skill");
			skillItemMeta.setLore(SkillQuickSlots.getSkillInfo(skill));
			skillItemStack.setItemMeta(skillItemMeta);
			return skillItemStack;
		}
		return null;
	}
	
	/**
	 * Gets the display information of this castable.
	 * 
	 * @return The display information.
	 */
	public List<String> getCastableInfo() {
		if(skill != null) {
			return SkillQuickSlots.getSkillInfo(skill);
		}
		return new ArrayList<>();
	}
	
	/**
	 * Gets the message to show this castable as quick slot in the casting bar.
	 * 
	 * @param playerData The player data for getting cooldowns etc. for this castable.
	 * 
	 * @return The quick slot message.
	 */
	public String getQuickSlotMessage(WauzPlayerData playerData) {
		if(skill != null) {
			String message = "";
			String skillName = skill.getSkillId();
			ChatColor color = ChatColor.GREEN;
			
			long cooldown = playerData.getSkills().getRemainingSkillCooldown(skillName);
			if(cooldown > 0) {
				cooldown = cooldown / 1000;
				message += ChatColor.YELLOW + "" + (cooldown < 1 ? 1 : cooldown) + "s ";
				color = ChatColor.RED;
			}
			
			int manaCost = skill.getManaCost();
			if(skill.isPhysical()) {
				message += ChatColor.GOLD + "" + manaCost + " " + UnicodeUtils.ICON_SUN + " ";
				if(playerData.getStats().getRage() < manaCost) {
					color = ChatColor.RED;
				}
			}
			else {
				message += ChatColor.LIGHT_PURPLE + "" + manaCost + " " + UnicodeUtils.ICON_STAR + " ";
				if(playerData.getStats().getMana() < manaCost) {
					color = ChatColor.RED;
				}
			}
			
			return message + color + skill.getSkillQuickSlotEffect();
		}
		return ChatColor.GRAY + "Empty";
	}

	/**
	 * @return The icon of the castable.
	 */
	public ItemStack getIconItemStack() {
		return iconItemStack;
	}

}
