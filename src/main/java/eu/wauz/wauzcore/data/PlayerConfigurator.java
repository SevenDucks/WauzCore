package eu.wauz.wauzcore.data;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.api.ConfigurationUtils;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.WauzPlayerGuild;
import eu.wauz.wauzcore.players.calc.DamageCalculator;
import eu.wauz.wauzcore.players.calc.ManaCalculator;
import eu.wauz.wauzcore.system.util.WauzDateUtils;

public class PlayerConfigurator extends ConfigurationUtils {
	
// General Parameters
	
	public static String getRank(OfflinePlayer player) {
		return playerConfigGetString(player, "rank", false);
	}
	
	public static long getSurvivalScore(OfflinePlayer player) {
		return playerConfigGetLong(player, "score.survival", false);
	}
	
	public static void setSurvivalScore(OfflinePlayer player, long score) {
		playerConfigSet(player, "score.survival", score, false);
	}
	
	public static String getLastPlayed(OfflinePlayer player) {
		return WauzDateUtils.formatTimeSince(playerConfigGetLong(player, "lastplayed", false));
	}
	
	public static void setLastPlayed(OfflinePlayer player) {
		playerConfigSet(player, "lastplayed", System.currentTimeMillis(), false);
	}
	
	public static WauzPlayerGuild getGuild(OfflinePlayer player) {
		return WauzPlayerGuild.getGuild(playerConfigGetString(player, "guild", false));
	}
	
	public static void setGuild(OfflinePlayer player, String guildUuidString) {
		playerConfigSet(player, "guild", guildUuidString, false);
	}
	
// Tokens
	
	public static long getTokens(Player player) {
		return playerConfigGetLong(player, "tokens", false);
	}
	
	public static void setTokens(Player player, long tokens) {
		playerConfigSet(player, "tokens", tokens, false);
	}
	
	public static long getTokenLimitDate(Player player, String mode) {
		return playerConfigGetLong(player, "tokenlimit." + mode + ".date", false);
	}
	
	public static void setTokenLimitDate(Player player, String mode, long dateLong) {
		playerConfigSet(player, "tokenlimit." + mode + ".date", dateLong, false);
	}
	
	public static int getTokenLimitAmount(Player player, String mode) {
		return playerConfigGetInt(player, "tokenlimit." + mode + ".amount", false);
	}
	
	public static void setTokenLimitAmount(Player player, String mode, int tokenAmount) {
		playerConfigSet(player, "tokenlimit." + mode + ".amount", tokenAmount, false);
	}
	
// Characters
	
	public static boolean doesCharacterExist(OfflinePlayer player, int slot) {
		return playerConfigGetBoolean(player, "char" + slot + ".exists", false);
	}
	
	public static String getLastPlayed(OfflinePlayer player, int slot) {
		return WauzDateUtils.formatTimeSince(playerConfigGetLong(player, "char" + slot + ".lastplayed", false));
	}
	
	public static String getRaceString(OfflinePlayer player, int slot) {
		return playerConfigGetString(player, "char" + slot + ".race", false);
	}
	
	public static String getWorldString(OfflinePlayer player, int slot) {
		return playerConfigGetString(player, "char" + slot + ".pos.world", false);
	}
	
	public static String getLevelString(OfflinePlayer player, int slot) {
		return playerConfigGetString(player, "char" + slot + ".level", false);
	}
	
// Character Parameters
	
	public static String getCharacterRace(Player player) {
		return playerConfigGetString(player, "race", true);
	}
	
	public static String getCharacterTabard(Player player) {
		return playerConfigGetString(player, "options.tabard", true);
	}
	
	public static void setCharacterTabard(Player player, String tabardName) {
		playerConfigSet(player, "options.tabard", tabardName, true);
	}
	
// Locations
	
	public static String getCharacterWorldString(Player player) {
		return playerConfigGetString(player, "pos.world", true);
	}
	
	public static Location getCharacterSpawn(Player player) {
		return playerConfigGetLocation(player, "pos.spawn", true);
	}
	
	public static Location getCharacterLocation(Player player) {
		return playerConfigGetLocation(player, "pos.location", true);
	}
	
	public static void setCharacterLocation(Player player, Location location) {
		playerConfigSet(player, "pos.location", location.getX() + " " + location.getY() + " " + location.getZ(), true);
	}
	
	public static Location getCharacterHearthstone(Player player) {
		return playerConfigGetLocation(player, "pos.innloc", true);
	}
	
	public static String getCharacterHearthstoneRegion(Player player) {
		return playerConfigGetString(player, "pos.innreg", true);
	}
	
	public static void setCharacterHearthstone(Player player, Location location, String regionName) {
		playerConfigSet(player, "pos.innloc", location.getX() + " " + location.getY() + " " + location.getZ(), true);
		playerConfigSet(player, "pos.innreg", regionName, true);
	}
	
// Experience, Currencies and Reputation
	
	public static int getCharacterLevel(Player player) {
		return playerConfigGetInt(player, "level", true);
	}
	
	public static void levelUpCharacter(Player player) {
		playerConfigSet(player, "stats.points.total", getCharacterTotalStatpoints(player) + 2, true);
		playerConfigSet(player, "level", player.getLevel(), true);
	}
	
	public static double getCharacterExperience(Player player) {
		return playerConfigGetDouble(player, "reput.exp", true);
	}
	
	public static void setCharacterExperience(Player player, double experience) {
		playerConfigSet(player, "reput.exp", experience, true);
		player.setExp((float) (experience / 100));
	}
	
	public static long getCharacterCoins(Player player) {
		return playerConfigGetLong(player, "reput.cow", true);
	}
	
	public static void setCharacterCoins(Player player, long amount) {
		playerConfigSet(player, "reput.cow", amount, true);
	}
	
	public static long getCharacterSoulstones(Player player) {
		return playerConfigGetLong(player, "reput.souls", true);
	}
	
	public static long getCharacterRepRepublicWauzland(Player player) {
		return playerConfigGetLong(player, "reput.wauzland", true);
	}
	
	public static long getCharacterRepEternalEmpire(Player player) {
		return playerConfigGetLong(player, "reput.empire", true);
	}
	
	public static long getCharacterRepDarkLegion(Player player) {
		return playerConfigGetLong(player, "reput.legion", true);
	}
	
	public static long getCharacterCurrency(Player player, String currency) {
		if(currency.equals("tokens"))
			return getTokens(player);
		else
			return playerConfigGetLong(player, currency, true);
	}
	
	public static void setCharacterCurrency(Player player, String currency, long amount) {
		if(currency.equals("tokens"))
			setTokens(player, amount);
		else
			playerConfigSet(player, currency, amount, true);
	}
	
// Cooldowns
	
	public static boolean isCharacterCooldownReady(Player player, String actionId) {
		Long cooldown = playerConfigGetLong(player, "cooldown." + actionId, true);
		return cooldown == null || cooldown <= System.currentTimeMillis();
	}
	
	public static void updateCharacterCooldown(Player player, String actionId, Long cooldown) {
		playerConfigSet(player, "cooldown." + actionId, cooldown + System.currentTimeMillis(), true);
	}
	
	public static short getPvPProtectionTicks(Player player) {
		return (short) (0 + playerConfigGetInt(player, "pvp.resticks", true));
	}
	
	public static void setPvPProtectionTicks(Player player, short ticks) {
		playerConfigSet(player, "pvp.resticks", ticks, true);
	}
	
// Dungeon Items
	
	public static String getCollectedDungeonItemsString(Player player) {
		return playerConfigGetString(player, "ditem.coll", true);
	}
	
	public static String getDungeonItemHookString(Player player) {
		return playerConfigGetString(player, "ditem.hook", true);
	}
	
	public static String getDungeonItemBombString(Player player) {
		return playerConfigGetString(player, "ditem.bomb", true);
	}
	
	public static String getDungeonItemTrodString(Player player) {
		return playerConfigGetString(player, "ditem.trod", true);
	}
	
	public static String getDungeonItemGlidString(Player player) {
		return playerConfigGetString(player, "ditem.glid", true);
	}
	
	public static void setDungeonItemTrackerDestination(Player player, Location location, String name) {
		String locationString = location.getX() + " " + location.getY() + " " + location.getZ();
		playerConfigSet(player, "ditem.tracker.coords", locationString, true);
		playerConfigSet(player, "ditem.tracker.name", name, true);
	}
	
	public static Location getDungeonItemTrackerDestinationLocation(Player player) {
		return playerConfigGetLocation(player, "ditem.tracker.coords", true);
	}
	
	public static String getDungeonItemTrackerDestinationName(Player player) {
		return
				playerConfigGetString(player, "ditem.tracker.name", true) + " " +
				playerConfigGetString(player, "ditem.tracker.coords", true);
	}
	
// Arrows
	
	public static String getSelectedArrows(Player player) {
		return playerConfigGetString(player, "arrows.selected", true);
	}
	
	public static void setSelectedArrowType(Player player, String type) {
		playerConfigSet(player, "arrows.selected", type, true);
	}
	
	public static int getArrowAmount(Player player, String type) {
		return type.equals("normal") ? 999 : playerConfigGetInt(player, "arrows.amount." + type, true);
	}
	
	public static void setArrowAmount(Player player, String type, int amount) {
		playerConfigSet(player, "arrows.amount." + type, amount, true);
	}
	
// Passive Skills
	
	public static int getCharacterTotalStatpoints(Player player) {
		return playerConfigGetInt(player, "stats.points.total", true);
	}
	
	public static int getCharacterSpentStatpoints(Player player) {
		return playerConfigGetInt(player, "stats.points.spent", true);
	}
	
	public static int getCharacterUnusedStatpoints(Player player) {
		return getCharacterTotalStatpoints(player) - getCharacterSpentStatpoints(player);
	}
	
	public static void increaseCharacterSpentSkillpoints(Player player) {
		playerConfigSet(player, "stats.points.spent", getCharacterSpentStatpoints(player) + 1, true);
	}
	
// Passive Skill - Health
	
	public static int getCharacterHealthStatpoints(Player player) {
		return playerConfigGetInt(player, "stats.healthpts", true);
	}
 	
	public static int getCharacterHealth(Player player) {
		return playerConfigGetInt(player, "stats.health", true);
	}
	
	public static void increaseCharacterHealth(Player player) {
		increaseCharacterSpentSkillpoints(player);
		playerConfigSet(player, "stats.healthpts", getCharacterHealthStatpoints(player) + 1, true);
		
		int health = getCharacterHealth(player) + 5;			
		playerConfigSet(player, "stats.health", health, true);
		WauzPlayerDataPool.getPlayer(player).setMaxHealth(health);
		DamageCalculator.setHealth(player, health);
	}
	
// Passive Skill - Trading
	
	public static int getCharacterTradingStatpoints(Player player) {
		return playerConfigGetInt(player, "stats.tradingpts", true);
	}
	
	public static int getCharacterTrading(Player player) {
		return playerConfigGetInt(player, "stats.trading", true);
	}
	
	public static float getCharacterTradingFloat(Player player) {
		return (float) ((float) playerConfigGetInt(player, "stats.trading", true) / 100);
	}
	
	public static void increaseCharacterTrading(Player player) {
		increaseCharacterSpentSkillpoints(player);
		playerConfigSet(player, "stats.tradingpts", getCharacterTradingStatpoints(player) + 1, true);
		
		playerConfigSet(player, "stats.trading", getCharacterTrading(player) + 10, true);
	}
	
// Passive Skill - Luck
	
	public static int getCharacterLuckStatpoints(Player player) {
		return playerConfigGetInt(player, "stats.luckpts", true);
	}
	
	public static int getCharacterLuck(Player player) {
		return playerConfigGetInt(player, "stats.luck", true);
	}
	
	public static void increaseCharacterLuck(Player player) {
		increaseCharacterSpentSkillpoints(player);
		playerConfigSet(player, "stats.luckpts", getCharacterLuckStatpoints(player) + 1, true);
		
		playerConfigSet(player, "stats.luck", getCharacterLuck(player) + 10, true);
	}
	
// Passive Skill - Mana
	
	public static int getCharacterManaStatpoints(Player player) {
		return playerConfigGetInt(player, "stats.manapts", true);
	}
	
	public static int getCharacterMana(Player player) {
		return playerConfigGetInt(player, "stats.mana", true);
	}
	
	public static void increaseCharacterMana(Player player) {
		increaseCharacterSpentSkillpoints(player);
		int statpoints = getCharacterManaStatpoints(player) + 1;
		playerConfigSet(player, "stats.manapts", statpoints, true);
				
		if(statpoints <= 40) {
			int mana = getCharacterMana(player) + 1;			
			playerConfigSet(player, "stats.mana", mana, true);
			WauzPlayerDataPool.getPlayer(player).setMaxMana(mana);
			ManaCalculator.updateManaItem(player);
		}
	}
	
// Passive Skill - Strength
	
	public static int getCharacterStrengthStatpoints(Player player) {
		return playerConfigGetInt(player, "stats.strengthpts", true);
	}
	
	public static int getCharacterStrength(Player player) {
		return playerConfigGetInt(player, "stats.strength", true);
	}
	
	public static float getCharacterStrengthFloat(Player player) {
		return (float) ((float) playerConfigGetInt(player, "stats.strength", true) / 100);
	}
	
	public static void increaseCharacterStrength(Player player) {
		increaseCharacterSpentSkillpoints(player);
		int statpoints = getCharacterStrengthStatpoints(player) + 1;
		playerConfigSet(player, "stats.strengthpts", statpoints, true);
		
		if(statpoints <= 40) {
			playerConfigSet(player, "stats.strength", getCharacterStrength(player) + 5, true);
		}
	}

// Passive Skill - Agility
	
	public static int getCharacterAgilityStatpoints(Player player) {
		return playerConfigGetInt(player, "stats.agilitypts", true);
	}
	
	public static int getCharacterAgility(Player player) {
		return playerConfigGetInt(player, "stats.agility", true);
	}
	
	public static void increaseCharacterAgility(Player player) {
		increaseCharacterSpentSkillpoints(player);
		int statpoints = getCharacterAgilityStatpoints(player) + 1;
		playerConfigSet(player, "stats.agilitypts", statpoints, true);
		
		if(statpoints <= 40) {
			playerConfigSet(player, "stats.agility", getCharacterAgility(player) + 1, true);
		}
	}
	
// Crafting Skill
	
	public static Integer getCharacterCraftingSkill(Player player) {
		return playerConfigGetInt(player, "skills.crafting", true);
	}
	
	public static void increaseCharacterCraftingSkill(Player player) {
		try {
			Integer characterCraftingSkill = getCharacterCraftingSkill(player);
			playerConfigSet(player, "skills.crafting", characterCraftingSkill + 1, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
// Weapon Skill - Sword
	
	public static Integer getCharacterSwordSkill(Player player) {
		return playerConfigGetInt(player, "skills.sword", true);
	}
	
	public static Integer getCharacterSwordSkillMax(Player player) {
		return playerConfigGetInt(player, "skills.swordmax", true);
	}
	
	public static void increaseCharacterSwordSkill(Player player) {
		try {
			Integer characterSwordSkill = getCharacterSwordSkill(player);
			Integer characterSwordSkillMax = getCharacterSwordSkillMax(player);
			if(characterSwordSkill < characterSwordSkillMax) {
				playerConfigSet(player, "skills.sword", characterSwordSkill + 1, true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
// Weapon Skill - Axe
	
	public static Integer getCharacterAxeSkill(Player player) {
		return playerConfigGetInt(player, "skills.axe", true);
	}
	
	public static Integer getCharacterAxeSkillMax(Player player) {
		return playerConfigGetInt(player, "skills.axemax", true);
	}
	
	public static void increaseCharacterAxeSkill(Player player) {
		try {
			Integer characterAxeSkill = getCharacterAxeSkill(player);
			Integer characterAxeSkillMax = getCharacterAxeSkillMax(player);
			if(characterAxeSkill < characterAxeSkillMax) {
				playerConfigSet(player, "skills.axe", characterAxeSkill + 1, true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
// Weapon Skill Staff
	
	public static Integer getCharacterStaffSkill(Player player) {
		return playerConfigGetInt(player, "skills.staff", true);
	}
	
	public static Integer getCharacterStaffSkillMax(Player player) {
		return playerConfigGetInt(player, "skills.staffmax", true);
	}
	
	public static void increaseCharacterStaffSkill(Player player) {
		try {
			Integer characterStaffSkill = getCharacterStaffSkill(player);
			Integer characterStaffSkillMax = getCharacterStaffSkillMax(player);
			if(characterStaffSkill < characterStaffSkillMax) {
				playerConfigSet(player, "skills.staff", characterStaffSkill + 1, true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
// Quests - Options
	
	public static boolean getHideSpecialQuestsForCharacter(Player player) {
		return playerConfigGetBoolean(player, "options.hideSpecialQuests", true);
	}
	
	public static void setHideSpecialQuestsForCharacter(Player player, boolean value) {
		playerConfigSet(player, "options.hideSpecialQuests", value, true);
	}
	
	public static boolean getHideCompletedQuestsForCharacter(Player player) {
		return playerConfigGetBoolean(player, "options.hideCompletedQuests", true);
	}
	
	public static void setHideCompletedQuestsForCharacter(Player player, boolean value) {
		playerConfigSet(player, "options.hideCompletedQuests", value, true);
	}
	
// Quests - Quest Slots
	
	public static void setCharacterQuestSlot(Player player, String questSlot, String questName) {
		playerConfigSet(player, questSlot, questName, true);
	}
	
	public static String getCharacterRunningMainQuest(Player player) {
		return playerConfigGetString(player, "quest.running.main", true);
	}
	
	public static String getCharacterRunningCampaignQuest1(Player player) {
		return playerConfigGetString(player, "quest.running.campaign1", true);
	}
	
	public static String getCharacterRunningCampaignQuest2(Player player) {
		return playerConfigGetString(player, "quest.running.campaign2", true);
	}
	
	public static String getCharacterRunningDailyQuest1(Player player) {
		return playerConfigGetString(player, "quest.running.daily1", true);
	}
	
	public static String getCharacterRunningDailyQuest2(Player player) {
		return playerConfigGetString(player, "quest.running.daily2", true);
	}
	
	public static String getCharacterRunningDailyQuest3(Player player) {
		return playerConfigGetString(player, "quest.running.daily3", true);
	}
	
// Quests - Progress
	
	public static long getCharacterCompletedQuests(Player player) {
		return playerConfigGetLong(player, "quest.completed", true);
	}
	
	public static void addCharacterCompletedQuests(Player player) {
		playerConfigSet(player, "quest.completed", getCharacterCompletedQuests(player) + 1, true);
	}
	
	public static int getCharacterQuestPhase(Player player, String questName) {
		return playerConfigGetInt(player, "quest." + questName, true);
	}
	
	public static String getCharacterQuestPhaseString(Player player, String questName) {
		return playerConfigGetString(player, "quest." + questName, true);
	}
	
	public static void setCharacterQuestPhase(Player player, String questName, int phase) {
		playerConfigSet(player, "quest." + questName, phase, true);
	}
	
	public static void setCharacterQuestCooldown(Player player, String questName) {
		playerConfigSet(player, "quest." + questName, "c " + System.currentTimeMillis(), true);
	}
	
	public static void setCharacterQuestDone(Player player, String questName) {
		playerConfigSet(player, "quest." + questName, "done", true);
	}
	
// Pets
	
	public static int getCharacterActivePetSlot(Player player) {
		return playerConfigGetInt(player, "pets.active.slot", true);
	}
	
	public static void setCharacterActivePetSlot(Player player, int petSlot) {
		playerConfigSet(player, "pets.active.slot", petSlot, true);
	}
	
	public static String getCharacterActivePetId(Player player) {
		return playerConfigGetString(player, "pets.active.id", true);
	}
	
	public static void setCharacterActivePetId(Player player, String petId) {
		playerConfigSet(player, "pets.active.id", petId, true);
	}
	
	public static String getCharacterPetType(Player player, int petSlot) {
		return playerConfigGetString(player, "pets.slot" + petSlot + ".type", true);
	}
	
	public static void setCharacterPetType(Player player, int petSlot, String petType) {
		if(petType.equals("none"))
			playerConfigSet(player, "pets.slot" + petSlot, "", true);
		playerConfigSet(player, "pets.slot" + petSlot + ".type", petType, true);
	}
	
	public static int getCharacterUsedPetSlots(Player player) {
		int usedPetSlots = 0;
		for(int petSlot = 0; petSlot < 5; petSlot++) {
			String petType = getCharacterPetType(player, petSlot);
			if(!petType.equals("none"))
				usedPetSlots++;
		}
		return usedPetSlots;
	}
	
// Pets - Intelligence
	
	public static int getCharacterPetIntelligence(Player player, int petSlot) {
		return playerConfigGetInt(player, "pets.slot" + petSlot + ".stats.int", true);
	}
	
	public static void setCharacterPetIntelligence(Player player, int petSlot, int intelligence) {
		playerConfigSet(player, "pets.slot" + petSlot + ".stats.int", intelligence, true);
	}
	
	public static int getCharacterPetIntelligenceMax(Player player, int petSlot) {
		return playerConfigGetInt(player, "pets.slot" + petSlot + ".stats.intmax", true);
	}
	
	public static void setCharacterPetIntelligenceMax(Player player, int petSlot, int intelligenceMax) {
		playerConfigSet(player, "pets.slot" + petSlot + ".stats.intmax", intelligenceMax, true);
	}
	
	public static boolean isCharacterPetIntelligenceMaxed(Player player, int petSlot) {
		return getCharacterPetIntelligence(player, petSlot) >= getCharacterPetIntelligenceMax(player, petSlot);
	}
	
	public static int getCharacterPetIntelligenceExpNeeded(Player player, int petSlot) {
		return playerConfigGetInt(player, "pets.slot" + petSlot + ".stats.intexp", true);
	}
	
	public static void setCharacterPetIntelligenceExpNeeded(Player player, int petSlot, int neededExp) {
		playerConfigSet(player, "pets.slot" + petSlot + ".stats.intexp", neededExp, true);
	}
	
// Pets - Dexterity
	
	public static int getCharacterPetDexterity(Player player, int petSlot) {
		return playerConfigGetInt(player, "pets.slot" + petSlot + ".stats.dex", true);
	}
	
	public static void setCharacterPetDexterity(Player player, int petSlot, int dexterity) {
		playerConfigSet(player, "pets.slot" + petSlot + ".stats.dex", dexterity, true);
	}
	
	public static int getCharacterPetDexterityMax(Player player, int petSlot) {
		return playerConfigGetInt(player, "pets.slot" + petSlot + ".stats.dexmax", true);
	}
	
	public static void setCharacterPetDexterityMax(Player player, int petSlot, int dexterityMax) {
		playerConfigSet(player, "pets.slot" + petSlot + ".stats.dexmax", dexterityMax, true);
	}
	
	public static boolean isCharacterPetDexterityMaxed(Player player, int petSlot) {
		return getCharacterPetDexterity(player, petSlot) >= getCharacterPetDexterityMax(player, petSlot);
	}
	
	public static int getCharacterPetDexterityExpNeeded(Player player, int petSlot) {
		return playerConfigGetInt(player, "pets.slot" + petSlot + ".stats.dexexp", true);
	}
	
	public static void setCharacterPetDexterityExpNeeded(Player player, int petSlot, int neededExp) {
		playerConfigSet(player, "pets.slot" + petSlot + ".stats.dexexp", neededExp, true);
	}
	
// Pets - Absorbtion
	
	public static int getCharacterPetAbsorption(Player player, int petSlot) {
		return playerConfigGetInt(player, "pets.slot" + petSlot + ".stats.abs", true);
	}
	
	public static void setCharacterPetAbsorption(Player player, int petSlot, int absorption) {
		playerConfigSet(player, "pets.slot" + petSlot + ".stats.abs", absorption, true);
	}
	
	public static int getCharacterPetAbsorptionMax(Player player, int petSlot) {
		return playerConfigGetInt(player, "pets.slot" + petSlot + ".stats.absmax", true);
	}
	
	public static void setCharacterPetAbsorptionMax(Player player, int petSlot, int absorptionMax) {
		playerConfigSet(player, "pets.slot" + petSlot + ".stats.absmax", absorptionMax, true);
	}
	
	public static boolean isCharacterPetAbsorptionMaxed(Player player, int petSlot) {
		return getCharacterPetAbsorption(player, petSlot) >= getCharacterPetAbsorptionMax(player, petSlot);
	}
	
	public static int getCharacterPetAbsorptionExpNeeded(Player player, int petSlot) {
		return playerConfigGetInt(player, "pets.slot" + petSlot + ".stats.absexp", true);
	}
	
	public static void setCharacterPetAbsorptionExpNeeded(Player player, int petSlot, int neededExp) {
		playerConfigSet(player, "pets.slot" + petSlot + ".stats.absexp", neededExp, true);
	}
	
// Pet - Breeding
	
	public static int getCharacterPetBreedingFreeSlot(Player player) {
		if(getCharacterPetType(player, 6).equals("none"))
			return 6;
		if(getCharacterPetType(player, 8).equals("none"))
			return 8;
		
		return -1;
	}
	
	public static String getCharcterPetBreedingDisallowString(Player player, int petSlot) {
		if(getCharacterPetBreedingFreeSlot(player) == -1)
			return "Breeding Station is in use!";
		
		if(!isCharacterPetIntelligenceMaxed(player, petSlot)
				&& !isCharacterPetDexterityMaxed(player, petSlot)
				&& !isCharacterPetAbsorptionMaxed(player, petSlot))
			return "Pet needs at least 1 maxed Stat!";
		
		return null;
	}
	
	public static long getCharacterPetBreedingHatchTime(Player player) {
		return playerConfigGetLong(player, "pets.egg.time", true);
	}
	
	public static void setCharacterPetBreedingHatchTime(Player player, long hatchTime) {
		playerConfigSet(player, "pets.egg.time", hatchTime, true);
	}
	
}
