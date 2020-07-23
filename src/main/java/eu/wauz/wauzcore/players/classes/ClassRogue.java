package eu.wauz.wauzcore.players.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

/**
 * A class, that can be chosen by a player.
 * Master of agility and sword art.
 * 
 * @author Wauzmons
 *
 * @see WauzPlayerClassPool
 */
public class ClassRogue implements WauzPlayerClass {
	
	/**
	 * A map of the classes' subclasses / masteries, indexed by name.
	 */
	private Map<String, WauzPlayerSubclass> subclassMap = new HashMap<>();
	
	/**
	 * Constructs a new instance of the class and initializes its subclasses.
	 */
	public ClassRogue() {
		subclassMap.put(SubclassUmbralist.CLASS_NAME, new SubclassUmbralist());
		subclassMap.put(SubclassMarksman.CLASS_NAME, new SubclassMarksman());
		subclassMap.put(SubclassJuggernaut.CLASS_NAME, new SubclassJuggernaut());
		subclassMap.put(SubclassBard.CLASS_NAME, new SubclassBard());
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
	 * @return All subclasses of the class.
	 */
	@Override
	public List<WauzPlayerSubclass> getSubclasses() {
		return new ArrayList<>(subclassMap.values());
	}

	/**
	 * @param subclass The name of a subclass.
	 * 
	 * @return The subclass with that name or null.
	 */
	@Override
	public WauzPlayerSubclass getSubclass(String subclass) {
		return subclassMap.get(subclass);
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
