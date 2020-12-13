package eu.wauz.wauzcore.players.classes.warrior;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.menu.heads.CharacterIconHeads;
import eu.wauz.wauzcore.players.classes.BaseSubclass;
import eu.wauz.wauzcore.players.classes.ClassWarrior;
import eu.wauz.wauzcore.players.classes.Learnable;
import eu.wauz.wauzcore.players.classes.WauzPlayerClass;
import eu.wauz.wauzcore.skills.SkillPlaceholder;

/**
 * A subclass / mastery, that belongs to a player class.
 * Specialization: Offensive, Physical Area Attacks
 * 
 * @author Wauzmons
 *
 * @see WauzPlayerClass
 * @see ClassWarrior
 */
public class SubclassBerserker extends BaseSubclass {
	
	/**
	 * The static name of the subclass.
	 */
	public static final String CLASS_NAME = "Berserker";
	
	/**
	 * Constructs a new instance of the subclass and initializes its learnables
	 * 
	 * @see BaseSubclass#registerLearnable(Learnable)
	 */
	public SubclassBerserker() {
		registerLearnable(new Learnable(new SkillPlaceholder(), 1));
	}

	/**
	 * @return The name of the subclass.
	 */
	@Override
	public String getSubclassName() {
		return CLASS_NAME;
	}
	
	/**
	 * @return The description of the subclass.
	 */
	@Override
	public String getSublassDescription() {
		return "Offensive, Physical Area Attacks";
	}
	
	/**
	 * @return The color associated with the subclass.
	 */
	@Override
	public ChatColor getSublassColor() {
		return ChatColor.RED;
	}
	
	/**
	 * @return The item stack representing the subclass.
	 */
	@Override
	public ItemStack getSubclassItemStack() {
		return CharacterIconHeads.getBerserkerItem();
	}

}
