package eu.wauz.wauzcore.arcade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

import eu.wauz.wauzcore.skills.SkillUtils;
import eu.wauz.wauzcore.skills.particles.SkillParticle;
import eu.wauz.wauzcore.system.annotations.Minigame;

/**
 * A hunt minigame, where you have to fastly kill vampire bats.
 * 
 * @author Wauzmons
 */
@Minigame
public class MinigameVanHelsing implements ArcadeMinigame {
	
	/**
	 * A map of the player's scores.
	 */
	private Map<Player, Integer> playerScoreMap = new HashMap<>();
	
	/**
	 * A map of the player's boss bars.
	 */
	private Map<Player, BossBar> playerBarMap = new HashMap<>();
	
	/**
	 * The players who have shot enough bats.
	 */
	private List<Player> finishedPlayers = new ArrayList<>();
	
	/**
	 * The remaining bats in the arena.
	 */
	private List<Entity> bats = new ArrayList<>();
	
	/**
	 * The remaining silver bats in the arena.
	 */
	private List<Entity> silverBats = new ArrayList<>();
	
	/**
	 * The particles to show around silver bats.
	 */
	private SkillParticle silverParticle = new SkillParticle(Color.SILVER);
	
	/**
	 * The amount of players who can win the game.
	 */
	private int maxWinningPlayers = 1;
	
	/**
	 * The score needed to win the game.
	 */
	private int neededScore = 7;

	/**
	 * @return The display name of the minigame.
	 */
	@Override
	public String getName() {
		return "Van-Helsing";
	}

	/**
	 * @return The scoreboard description of the minigame.
	 */
	@Override
	public List<String> getDescription() {
		List<String> description = new ArrayList<>();
		description.add(ChatColor.WHITE + "Win by using your Longbow");
		description.add(ChatColor.WHITE + "to shoot " + neededScore + " Vampire Bats");
		description.add(ChatColor.WHITE + "before the other Players!");
		description.add("   ");
		description.add(ChatColor.BLUE + "Qualified Players: " + ChatColor.GOLD + finishedPlayers.size() + " / " + maxWinningPlayers);
		return description;
	}

	/**
	 * Starts a new game.
	 * 
	 * @param players The players who participate.
	 */
	@Override
	public void startGame(List<Player> players) {
		maxWinningPlayers = players.size() / 2;
		Location spawnLocation = new Location(ArcadeLobby.getWorld(), 500.5, 88, 750.5, 0, 0);
		ArcadeUtils.placeTeam(players, spawnLocation, 8, 8);
		int playerCount = players.size();
		for(Player player : players) {
			playerScoreMap.put(player, 0);
			updateProgressBar(player);
		}
		Location batLocation = spawnLocation.clone().add(0, 12, 0);
		for(int bat = 0; bat < playerCount * 5; bat++) {
			bats.add(batLocation.getWorld().spawnEntity(batLocation, EntityType.BAT));
		}
		for(int silverBat = 0; silverBat < playerCount; silverBat++) {
			silverBats.add(batLocation.getWorld().spawnEntity(batLocation, EntityType.BAT));
		}
		ArcadeUtils.runStartTimer(10, 120);
	}
	
	/**
	 * Handles the start event, that gets fired when the start countdown ends.
	 */
	@Override
	public void handleStartEvent() {
		ItemStack arrowItemStack = new ItemStack(Material.ARROW);
		ItemStack bowItemStack = new ItemStack(Material.BOW);
		ItemMeta bowItemMeta = bowItemStack.getItemMeta();
		bowItemMeta.setDisplayName(ChatColor.RED + "Longbow");
		bowItemMeta.setUnbreakable(true);
		bowItemMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, false);
		bowItemStack.setItemMeta(bowItemMeta);
		for(Player player : ArcadeLobby.getPlayingPlayers()) {
			player.getInventory().addItem(bowItemStack, arrowItemStack);
		}
	}

	/**
	 * Ends the game and decides a winner.
	 * 
	 * @return The players wo won the game.
	 */
	@Override
	public List<Player> endGame() {
		List<Player> winners = new ArrayList<>(finishedPlayers);
		playerScoreMap.clear();
		for(BossBar bar : playerBarMap.values()) {
			bar.removeAll();
		}
		playerBarMap.clear();
		finishedPlayers.clear();
		for(Entity bat : bats) {
			bat.remove();
		}
		bats.clear();
		for(Entity bat : silverBats) {
			bat.remove();
		}
		silverBats.clear();
		maxWinningPlayers = 1;
		return winners;
	}
	
	/**
	 * Handles the given damage event, that occured in the minigame.
	 * 
	 * @param event The damage event.
	 */
	@Override
	public void handleDamageEvent(EntityDamageEvent event) {
		if(!(event instanceof EntityDamageByEntityEvent)) {
			event.setCancelled(true);
			return;
		}
		EntityDamageByEntityEvent damageByEntityEvent = (EntityDamageByEntityEvent) event;
		if(!(damageByEntityEvent.getDamager() instanceof Arrow) || !(damageByEntityEvent.getEntity() instanceof Bat)) {
			event.setCancelled(true);
			return;
		}
		ProjectileSource source = ((Arrow) damageByEntityEvent.getDamager()).getShooter();
		if(!(source instanceof Player)) {
			event.setCancelled(true);
			return;
		}
		Player damager = (Player) source;
		Entity damaged = damageByEntityEvent.getEntity();
		if(bats.contains(damaged)) {
			bats.remove(damaged);
			addProgress(damager, 1);
		}
		else if(silverBats.contains(damaged)) {
			silverBats.remove(damaged);
			addProgress(damager, 3);
		}
		damaged.remove();
		damager.playSound(damager.getLocation(), Sound.ENTITY_BAT_HURT, 1, 1);
	}
	
	/**
	 * A method that is called every second of the minigame.
	 */
	@Override
	public void handleTick() {
		for(Entity silverBat : silverBats) {
			silverParticle.spawn(silverBat.getLocation(), 1);
			SkillUtils.addPotionEffect(silverBat, PotionEffectType.SPEED, 1, 2);
		}
	}
	
	/**
	 * Adds progress to the given player's goal.
	 * 
	 * @param player The player to add progress for.
	 * @param progress The amount of progress to add.
	 */
	private void addProgress(Player player, int progress) {
		int score = playerScoreMap.get(player) + progress;
		if(score > neededScore) {
			score = neededScore;
		}
		playerScoreMap.put(player, score);
		updateProgressBar(player);
		if(score >= neededScore) {
			finishedPlayers.add(player);
			player.teleport(new Location(ArcadeLobby.getWorld(), 500.5, 105, 750.5, 0, 0));
			player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1, 1);
			for(Player playing : ArcadeLobby.getPlayingPlayers()) {
				playing.sendMessage(ChatColor.GOLD + player.getName() + ChatColor.BLUE + " has been qualified!");
			}
			if(finishedPlayers.size() >= maxWinningPlayers) {
				ArcadeLobby.endGame();
			}
		}
	}
	
	/**
	 * Updates the progress bar for the given player.
	 * 
	 * @param player The player to update the bar for.
	 */
	private void updateProgressBar(Player player) {
		int currentScore = playerScoreMap.get(player);
		double progress = (double) currentScore / (double) neededScore;
		String scoreString = ChatColor.GOLD + " " + currentScore + " / " + neededScore;
		String barTitle = ChatColor.WHITE + "~~~" + scoreString + ChatColor.YELLOW + " POINTS EARNED " + ChatColor.WHITE + "~~~";
		BossBar bar = playerBarMap.get(player);
		if(bar == null) {
			bar = Bukkit.createBossBar(barTitle, BarColor.YELLOW, BarStyle.SOLID);
			bar.setProgress(progress);
			bar.addPlayer(player);
			playerBarMap.put(player, bar);
		}
		else {
			bar.setTitle(barTitle);
			bar.setProgress(progress);
		}
	}

}
