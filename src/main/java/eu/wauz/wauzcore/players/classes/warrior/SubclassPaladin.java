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
 * Specialization: Support, Healing through Fighting
 * 
 * @author Wauzmons
 *
 * @see WauzPlayerClass
 * @see ClassWarrior
 */
public class SubclassPaladin extends BaseSubclass {
	
	/**
	 * The static name of the subclass.
	 */
	public static final String CLASS_NAME = "Paladin";
	
	/**
	 * Constructs a new instance of the subclass and initializes its learnables
	 * 
	 * @see BaseSubclass#registerLearnable(Learnable)
	 */
	public SubclassPaladin() {
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
		return "Support, Healing through Fighting";
	}
	
	/**
	 * @return The color associated with the subclass.
	 */
	@Override
	public ChatColor getSublassColor() {
		return ChatColor.GREEN;
	}
	
	/**
	 * @return The item stack representing the subclass.
	 */
	@Override
	public ItemStack getSubclassItemStack() {
		return CharacterIconHeads.getPaladinItem();
	}

}
