package eu.wauz.wauzcore.players;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.players.PlayerSkillConfigurator;
import eu.wauz.wauzcore.players.classes.Learnable;
import eu.wauz.wauzcore.players.classes.WauzPlayerClassPool;
import eu.wauz.wauzcore.players.classes.WauzPlayerSubclass;
import eu.wauz.wauzcore.skills.Castable;
import eu.wauz.wauzcore.skills.WauzPlayerSkillExecutor;
import eu.wauz.wauzcore.skills.passive.AbstractPassiveSkill;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.WauzPermission;

/**
 * A section of the player data for storing skill data.
 * 
 * @author Wauzmons
 * 
 * @see WauzPlayerData
 */
public class WauzPlayerDataSectionSkills {
	
	/**
	 * The player data the section belongs to.
	 */
	private final WauzPlayerData playerData;
	
	/**
	 * The number of the currently shown action bar.
	 */
	private int actionBar = 0;
	
	/**
	 * The list of selected castables.
	 */
	private List<Castable> selectedCastables = new ArrayList<>();
	
	/**
	 * The list of unlocked castables.
	 */
	private List<Castable> unlockedCastables = new ArrayList<>();
	
	/**
	 * A map of the unlocked castables, indexed by castable key.
	 */
	private Map<String, Castable> castableMap = new HashMap<>();
	
	/**
	 * The skill cooldown times by id.
	 */
	private Map<String, Long> skillCooldownMap = new HashMap<>();
	
	/**
	 * The food cooldown times by id.
	 */
	private Map<String, Long> foodCooldownMap = new HashMap<>();
	
	/**
	 * The action cooldown times by id.
	 */
	private Map<String, Long> actionCooldownMap = new HashMap<>();
	
	/**
	 * The cached passive skills of the player.
	 */
	private Map<String, AbstractPassiveSkill> passiveSkillMap = new HashMap<>();
	
	/**
	 * Constructs a new instance of the section.
	 * 
	 * @param playerData The player data the section belongs to.
	 */
	public WauzPlayerDataSectionSkills(WauzPlayerData playerData) {
		this.playerData = playerData;
	}
	
	/**
	 * @return The player data the section belongs to.
	 */
	public WauzPlayerData getPlayerData() {
		return playerData;
	}
	
	/**
	 * @return The number of the currently shown action bar.
	 */
	public int getActionBar() {
		return actionBar;
	}

	/**
	 * @param actionBar The new number of the currently shown action bar.
	 */
	public void setActionBar(int actionBar) {
		this.actionBar = actionBar;
	}
	
	/**
	 * @return The list of selected castables.
	 */
	public List<Castable> getSelectedCastables() {
		return selectedCastables;
	}

	/**
	 * @return The list of unlocked castables.
	 */
	public List<Castable> getUnlockedCastables() {
		return unlockedCastables;
	}
	
	/**
	 * Gets an unlocked castable of the player.
	 * 
	 * @param castableKey The key of the castable.
	 * 
	 * @return The requested castable or null.
	 */
	public Castable getCastable(String castableKey) {
		return castableMap.get(castableKey);
	}

	/**
	 * Refreshes the list of unlocked castables. Also refreshes selected castables.
	 */
	public void refreshUnlockedCastables() {
		unlockedCastables.clear();
		castableMap.clear();
		Player player = playerData.getPlayer();
		List<WauzPlayerSubclass> subclasses = WauzPlayerClassPool.getClass(player).getSubclasses();
		for(int index = 0; index < subclasses.size(); index++) {
			WauzPlayerSubclass subclass = subclasses.get(index);
			int masteryLevel = PlayerSkillConfigurator.getMasteryStatpoints(player, index + 1);
			for(Learnable learnable : subclass.getLearned(masteryLevel)) {
				Castable castable = new Castable(subclass.getSubclassItemStack(), learnable.getSkill());
				unlockedCastables.add(castable);
				castableMap.put("Skill :: " + learnable.getSkill().getSkillId(), castable);
			}
		}
		refreshSelectedCastables();
	}
	
	/**
	 * Refreshes the list of selected castables.
	 */
	public void refreshSelectedCastables() {
		selectedCastables.clear();
		for(int slot = 1; slot <= 8; slot++) {
			String castableKey = PlayerSkillConfigurator.getQuickSlotSkill(playerData.getPlayer(), slot);
			selectedCastables.add(castableMap.get(castableKey));
		}
	}
	
	/**
	 * Gets the remaining cooldown milliseconds for the given skill.
	 * 
	 * @param skillId The id of the skill.
	 * 
	 * @return The remaining millis.
	 */
	public long getRemainingSkillCooldown(String skillId) {
		Long cooldown = skillCooldownMap.get(skillId);
		return cooldown == null ? 0 : cooldown - System.currentTimeMillis();
	}
	
	/**
	 * Checks if the cooldown timestamp for the given skill is smaller than the current time.
	 * 
	 * @param skillId The id of the skill.
	 * 
	 * @return If the cooldown is ready.
	 */
	public boolean isSkillReady(String skillId) {
		Long cooldown = skillCooldownMap.get(skillId);
		if(cooldown == null || cooldown <= System.currentTimeMillis()) {
			return true;
		}
		long remaining = (cooldown - System.currentTimeMillis()) / 1000 + 1;
		playerData.getPlayer().sendMessage(ChatColor.RED + "Skill not ready! " + remaining + " Seconds remain!");
		return false;
	}
	
	/**
	 * Resets the cooldown for the given skill.
	 * Only one second if the player has magic debug permissions.
	 * 
	 * @param skillId The id of the skill.
	 * 
	 * @see WauzDebugger#toggleMagicDebugMode(Player)
	 */
	public void updateSkillCooldown(String skillId) {
		boolean hasDebugPermission = playerData.getPlayer().hasPermission(WauzPermission.DEBUG_MAGIC.toString());
		Long cooldown = (long) (hasDebugPermission ? 1 : WauzPlayerSkillExecutor.getSkill(skillId).getCooldownSeconds());
		skillCooldownMap.put(skillId, cooldown * 1000 + System.currentTimeMillis());
	}
	
	/**
	 * Checks if the cooldown timestamp for the given food is smaller than the current time.
	 * 
	 * @param foodId The id of the food.
	 * 
	 * @return If the cooldown is ready.
	 */
	public boolean isFoodReady(String foodId) {
		Long cooldown = foodCooldownMap.get(foodId);
		if(cooldown == null || cooldown <= System.currentTimeMillis()) {
			return true;
		}
		long remaining = (cooldown - System.currentTimeMillis()) / 1000 + 1;
		playerData.getPlayer().sendMessage(ChatColor.RED + "Food not ready! " + remaining + " Seconds remain!");
		return false;
	}
	
	/**
	 * Resets the cooldown for the given food.
	 * 
	 * @param foodId The id of the food.
	 * @param cooldown The food cooldown in seconds.
	 */
	public void updateFoodCooldown(String foodId, int cooldown) {
		foodCooldownMap.put(foodId, cooldown * 1000 + System.currentTimeMillis());
	}
	
	/**
	 * Checks if the cooldown timestamp for the given id is smaller than the current time.
	 * 
	 * @param actionId The id of the action.
	 * 
	 * @return If the cooldown is ready.
	 */
	public boolean isActionReady(String actionId) {
		Long cooldown = actionCooldownMap.get(actionId);
		return cooldown == null || cooldown <= System.currentTimeMillis();
	}
	
	/**
	 * Sets a cooldown in milliseconds from now, for the given id.
	 * 
	 * @param actionId The id of the action.
	 * @param cooldown The milliseconds of the cooldown.
	 */
	public void updateActionCooldown(String actionId, Long cooldown) {
		actionCooldownMap.put(actionId, cooldown + System.currentTimeMillis());
	}

	/**
	 * Gets all of the cached passive skills.
	 * 
	 * @return The cached passives.
	 */
	public List<AbstractPassiveSkill> getAllCachedPassives() {
		return new ArrayList<>(passiveSkillMap.values());
	}
	
	/**
	 * Gets the cached passive skill with the given name.
	 * 
	 * @param skillName The name of the passive.
	 * 
	 * @return The cached passive.
	 */
	public AbstractPassiveSkill getCachedPassive(String skillName) {
		return passiveSkillMap.get(skillName);
	}
	
	/**
	 * Caches the given passive skill instance.
	 * 
	 * @param passiveSkill The passive to cache.
	 */
	public void cachePassive(AbstractPassiveSkill passiveSkill) {
		passiveSkillMap.put(passiveSkill.getPassiveName(), passiveSkill);
	}

}
