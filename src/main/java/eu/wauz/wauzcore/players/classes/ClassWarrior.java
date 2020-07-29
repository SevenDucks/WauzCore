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
import eu.wauz.wauzcore.players.classes.warrior.SubclassBerserker;
import eu.wauz.wauzcore.players.classes.warrior.SubclassBulwark;
import eu.wauz.wauzcore.players.classes.warrior.SubclassPaladin;
import eu.wauz.wauzcore.players.classes.warrior.SubclassTemplar;
import eu.wauz.wauzcore.skills.SkillTheChariot;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkill;

/**
 * A class, that can be chosen by a player.
 * Master of defense and axe combat.
 * 
 * @author Wauzmons
 *
 * @see WauzPlayerClassPool
 */
public class ClassWarrior implements WauzPlayerClass {
	
	/**
	 * A map of the classes' subclasses / masteries, indexed by name.
	 */
	private Map<String, WauzPlayerSubclass> subclassMap = new HashMap<>();
	
	/**
	 * An ordered list of all the classes' subclasses.
	 */
	private List<WauzPlayerSubclass> subclasses = new ArrayList<>();
	
	/**
	 * Constructs a new instance of the class and initializes its subclasses.
	 * 
	 * @see ClassWarrior#registerSubclass(WauzPlayerSubclass)
	 */
	public ClassWarrior() {
		registerSubclass(new SubclassBerserker());
		registerSubclass(new SubclassBulwark());
		registerSubclass(new SubclassTemplar());
		registerSubclass(new SubclassPaladin());
	}
	
	/**
	 * Registers a subclass of the class.
	 * 
	 * @param subclass The subclass to register.
	 */
	private void registerSubclass(WauzPlayerSubclass subclass) {
		subclasses.add(subclass);
		subclassMap.put(subclass.getSubclassName(), subclass);
	}

	/**
	 * @return The name of the class.
	 */
	@Override
	public String getClassName() {
		return "Warrior";
	}
	
	/**
	 * @return The description of the class.
	 */
	@Override
	public String getClassDescription() {
		return "For as long as war has raged, heroes from every race have aimed to master the art of battle. Warriors combine strength, leadership, and a vast knowledge of weapons to wreak havoc in glorious combat.";
	}

	/**
	 * @return The color associated with the class.
	 */
	@Override
	public ChatColor getClassColor() {
		return ChatColor.YELLOW;
	}

	/**
	 * @return The item stack representing the class.
	 */
	@Override
	public ItemStack getClassItemStack() {
		return CharacterIconHeads.getWarriorItem();
	}
	
	/**
	 * @return All subclasses of the class.
	 */
	@Override
	public List<WauzPlayerSubclass> getSubclasses() {
		return new ArrayList<>(subclasses);
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
		return ArmorCategory.HEAVY;
	}
	
	/**
	 * @return The starting stats and passive skills of the class.
	 */
	@Override
	public WauzPlayerClassStats getStartingStats() {
		WauzPlayerClassStats stats = new WauzPlayerClassStats();
		stats.setAxeSkill(135000);
		stats.setAxeSkillMax(250000);
		stats.setStaffSkill(80000);
		return stats;
	}

	@Override
	public ItemStack getStartingWeapon() {
		WauzPlayerSkill skill = new SkillTheChariot();
		return WauzEquipmentHelper.getSkillgemWeapon(skill, Material.DIAMOND_AXE, false);
	}

}
