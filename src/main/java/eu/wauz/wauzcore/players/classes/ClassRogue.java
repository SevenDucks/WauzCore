package eu.wauz.wauzcore.players.classes;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.items.enums.ArmorCategory;
import eu.wauz.wauzcore.items.identifiers.WauzEquipmentHelper;
import eu.wauz.wauzcore.menu.heads.CharacterIconHeads;
import eu.wauz.wauzcore.players.classes.rogue.SubclassBard;
import eu.wauz.wauzcore.players.classes.rogue.SubclassJuggernaut;
import eu.wauz.wauzcore.players.classes.rogue.SubclassMarksman;
import eu.wauz.wauzcore.players.classes.rogue.SubclassUmbralist;
import eu.wauz.wauzcore.skills.SkillTheHierophant;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkill;
import eu.wauz.wauzcore.system.annotations.CharacterClass;

/**
 * A class, that can be chosen by a player.
 * Master of agility and sword art.
 * 
 * @author Wauzmons
 *
 * @see WauzPlayerClassPool
 */
@CharacterClass
public class ClassRogue extends BaseClass {
	
	/**
	 * Constructs a new instance of the class and initializes its subclasses.
	 * 
	 * @see BaseClass#registerSubclass(WauzPlayerSubclass)
	 */
	public ClassRogue() {
		registerSubclass(new SubclassUmbralist());
		registerSubclass(new SubclassMarksman());
		registerSubclass(new SubclassJuggernaut());
		registerSubclass(new SubclassBard());
	}

	/**
	 * @return The name of the class.
	 */
	@Override
	public String getClassName() {
		return "Rogue";
	}
	
	/**
	 * @return The description of the class.
	 */
	@Override
	public String getClassDescription() {
		return "For rogues, the only code is the contract, and their honor is purchased in gold. Free from the constraints of a conscience, these mercenaries rely on brutal and efficient tactics.";
	}

	/**
	 * @return The color associated with the class.
	 */
	@Override
	public ChatColor getClassColor() {
		return ChatColor.GREEN;
	}

	/**
	 * @return The item stack representing the class.
	 */
	@Override
	public ItemStack getClassItemStack() {
		return CharacterIconHeads.getRogueItem();
	}

	/**
	 * @return The highest weight armor category the class can wear.
	 */
	@Override
	public ArmorCategory getArmorCategory() {
		return ArmorCategory.MEDIUM;
	}
	
	/**
	 * @return The starting stats and passive skills of the class.
	 */
	@Override
	public WauzPlayerClassStats getStartingStats() {
		WauzPlayerClassStats stats = new WauzPlayerClassStats();
		stats.setSwordSkill(135000);
		stats.setSwordSkillMax(250000);
		stats.setStaffSkill(80000);
		return stats;
	}

	@Override
	public ItemStack getStartingWeapon() {
		WauzPlayerSkill skill = new SkillTheHierophant();
		return WauzEquipmentHelper.getSkillgemWeapon(skill, Material.DIAMOND_SWORD, false);
	}

}
