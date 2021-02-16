package eu.wauz.wauzcore.professions;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * The type of a gatherable resource node.
 * 
 * @author Wauzmons
 */
public enum WauzResourceNodeType {
	
	/**
	 * A mineral that can be mined with a pickaxe.
	 */
	MINERAL("Minerals", "Pickaxes", "_PICKAXE", Sound.BLOCK_STONE_HIT, Sound.BLOCK_STONE_BREAK),
	
	/**
	 * An herb that can be harvested with a spade.
	 */
	HERB("Herbs", "Spades", "_SHOVEL", Sound.BLOCK_GRASS_HIT, Sound.BLOCK_GRASS_BREAK);
	
	/**
	 * The message display name of the node type.
	 */
	private String name;
	
	/**
	 * The message display name of the tool type.
	 */
	private String tool;
	
	/**
	 * The string to look for in the tool material.
	 */
	private String neededMaterialString;
	
	/**
	 * The sound to play when the node gets damaged.
	 */
	private Sound damageSound;
	
	/**
	 * The sound to play when the node gets broken.
	 */
	private Sound breakSound;
	
	/**
	 * Creates a new resource node type with given parameters.
	 * 
	 * @param name The message display name of the node type.
	 * @param tool The message display name of the tool type.
	 * @param neededMaterialString The string to look for in the tool material.
	 * @param damageSound The sound to play when the node gets damaged.
	 * @param breakSound The sound to play when the node gets broken.
	 */
	WauzResourceNodeType(String name, String tool, String neededMaterialString, Sound damageSound, Sound breakSound) {
		this.name = name;
		this.tool = tool;
		this.neededMaterialString = neededMaterialString;
		this.damageSound = damageSound;
		this.breakSound = breakSound;
	}
	
	/**
	 * Checks if the resource node of this type can be gathered.
	 * If not, the given player will receive a message with the reason.
	 * 
	 * @param player The player to receive the fail message.
	 * @param toolItemStack The tool item stack used for gathering.
	 * @param nodeTier The tier of the resource node.
	 * 
	 * @return If the node can be gathered.
	 */
	public boolean canGather(Player player, ItemStack toolItemStack, int nodeTier) {
		String materialString = toolItemStack.getType().toString();
		if(!StringUtils.contains(materialString, neededMaterialString)) {
			player.sendMessage(ChatColor.RED + name + " can only be gathered with " + tool + "!");
			return false;
		}
		int toolTier = 0;
		if(toolTier >= nodeTier) {
			player.sendMessage(ChatColor.RED + "These " + name + " can only be gathered with T" + nodeTier + " or better " + tool + "!");
			return false;
		}
		return true;
	}
	
	/**
	 * Returns the message display name of the node type in a title friendly format.
	 * 
	 * @return The message display name of the node type.
	 */
	@Override
	public String toString() {
		return name;
	}

	/**
	 * @return The sound to play when the node gets damaged.
	 */
	public Sound getDamageSound() {
		return damageSound;
	}

	/**
	 * @return The sound to play when the node gets broken.
	 */
	public Sound getBreakSound() {
		return breakSound;
	}

}
