package eu.wauz.wauzcore.events;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.data.players.PlayerCollectionConfigurator;
import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.data.players.PlayerSkillConfigurator;
import eu.wauz.wauzcore.data.players.PlayerQuestConfigurator;
import eu.wauz.wauzcore.data.players.PlayerRelationConfigurator;
import eu.wauz.wauzcore.items.WauzRewards;
import eu.wauz.wauzcore.menu.LootContainer;
import eu.wauz.wauzcore.mobs.citizens.RelationLevel;
import eu.wauz.wauzcore.mobs.citizens.RelationTracker;
import eu.wauz.wauzcore.players.calc.ExperienceCalculator;
import eu.wauz.wauzcore.system.achievements.AchievementTracker;
import eu.wauz.wauzcore.system.achievements.WauzAchievementType;
import eu.wauz.wauzcore.system.quests.QuestType;
import eu.wauz.wauzcore.system.quests.WauzQuest;

/**
 * An event that lets a player complete a quest and receive the rewards.
 * The quest slot and the phase are cleared, the cooldown for daily quests gets reset,
 * the quest completions increase, and finally an effect and the completion message is shown.
 * After that the quest rewards are handed out.
 * 
 * @author Wauzmons
 */
public class WauzPlayerEventQuestComplete implements WauzPlayerEvent {
	
	/**
	 * The quest to complete.
	 */
	private WauzQuest quest;
	
	/**
	 * The slot this quest is saved in.
	 */
	private String questSlot;
	
	/**
	 * The loot item stacks handed out to the player.
	 */
	private List<ItemStack> questLoot;
	
	/**
	 * Creates an event to complete the given quest.
	 * 
	 * @param quest The quest to complete.
	 * @param questSlot The slot this quest is saved in.
	 * @param questLoot The loot item stacks handed out to the player.
	 */
	public WauzPlayerEventQuestComplete(WauzQuest quest, String questSlot) {
		this.quest = quest;
		this.questSlot = questSlot;
	}

	/**
	 * Executes the event for the given player.
	 * 
	 * @param player The player for the execution.
	 * 
	 * @return If the event was executed successfully.
	 * 
	 * @see PlayerQuestConfigurator#setQuestPhase(Player, String, int)
	 * @see PlayerConfigurator#setCharacterQuestSlot(Player, String, String)
	 * @see PlayerQuestConfigurator#setQuestCooldown(Player, String)
	 * @see PlayerQuestConfigurator#addQuestCompletions(Player, String)
	 * @see RelationLevel#getRewardMultiplier()
	 * @see ExperienceCalculator#grantExperience(Player, int, double, org.bukkit.Location)
	 * @see WauzRewards#earnMmoRpgToken(Player)
	 */
	@Override
	public boolean execute(Player player) {
		try {
			String questName = quest.getQuestName();
			String questGiver = quest.getQuestGiver();
			
			PlayerQuestConfigurator.setQuestPhase(player, questName, 0);
			PlayerConfigurator.setCharacterQuestSlot(player, questSlot, "none");
			if(quest.getType().equals(QuestType.DAILY)) {
				PlayerQuestConfigurator.setQuestCooldown(player, questName);
			}
			PlayerQuestConfigurator.addQuestCompletions(player, questName);
			player.getWorld().playEffect(player.getLocation(), Effect.DRAGON_BREATH, 0);
			player.sendMessage(ChatColor.GREEN + "You completed [" + quest.getDisplayName() + "]");
			
			int relationProgress = PlayerRelationConfigurator.getRelationProgress(player, questGiver);
			double rewardMultiplier = RelationLevel.getRelationLevel(relationProgress).getRewardMultiplier();
			int rewardCoins = (int) (quest.getRewardCoins() * PlayerSkillConfigurator.getTradingFloat(player) * rewardMultiplier);
			double rewardExp = quest.getRewardExp() * rewardMultiplier;
			
			RelationTracker.addProgress(player, questGiver, quest.getRewardRelationExp());
			PlayerCollectionConfigurator.setCharacterCoins(player, PlayerCollectionConfigurator.getCharacterCoins(player) + rewardCoins);
			AchievementTracker.addProgress(player, WauzAchievementType.EARN_COINS, rewardCoins);
			ExperienceCalculator.grantExperience(player, quest.getLevel(), rewardExp, player.getLocation());
			WauzRewards.earnMmoRpgToken(player);
			
			if(questLoot != null && questLoot.size() > 0) {
				LootContainer.open(player, questLoot);
			}
			
			new WauzPlayerEventCitizenTalk(questGiver, quest.getCompletedDialog()).execute(player);
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			player.sendMessage(ChatColor.RED + "An Error occurred while completing the quest!");
			player.closeInventory();
			return false;
		}
	}

	/**
	 * @param questLoot The loot item stacks handed out to the player.
	 */
	public void setQuestLoot(List<ItemStack> questLoot) {
		this.questLoot = questLoot;
	}

}
