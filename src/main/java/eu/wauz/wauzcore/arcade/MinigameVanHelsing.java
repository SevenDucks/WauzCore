package eu.wauz.wauzcore.arcade;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
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
import org.bukkit.projectiles.ProjectileSource;

import eu.wauz.wauzcore.system.annotations.Minigame;

/**
 * A group minigame, where you have to kill the most vampire bats.
 * 
 * @author Wauzmons
 */
@Minigame
public class MinigameVanHelsing implements ArcadeMinigame {
	
	/**
	 * The members of the green team.
	 */
	private List<Player> teamGreen = new ArrayList<>();
	
	/**
	 * The members of the blue team.
	 */
	private List<Player> teamBlue = new ArrayList<>();
	
	/**
	 * The remaining bats in the arena.
	 */
	private List<Entity> bats = new ArrayList<>();
	
	/**
	 * The score of the green team.
	 */
	private int greenScore = 0;
	
	/**
	 * The score of the blue team.
	 */
	private int blueScore = 0;

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
		description.add(ChatColor.WHITE + "to shoot more Vampire Bats");
		description.add(ChatColor.WHITE + "than the other Team!");
		description.add("   ");
		description.add(ChatColor.GREEN + "Team Green: " + ChatColor.GOLD + greenScore);
		description.add(ChatColor.AQUA + "Team Blue: " + ChatColor.GOLD + blueScore);
		return description;
	}

	/**
	 * Starts a new game.
	 * 
	 * @param players The players who participate.
	 */
	@Override
	public void startGame(List<Player> players) {
		List<List<Player>> teams = ArcadeUtils.splitIntoTeams(players, 2);
		teamGreen.addAll(teams.get(0));
		teamBlue.addAll(teams.get(1));
		ArcadeUtils.equipTeamColor(teamGreen, Color.LIME, ChatColor.GREEN + "Team Green");
		ArcadeUtils.equipTeamColor(teamBlue, Color.AQUA, ChatColor.AQUA + "Team Blue");
		Location spawnLocation = new Location(ArcadeLobby.getWorld(), 500.5, 88, 750.5, 0, 0);
		ArcadeUtils.placeTeam(teamGreen, spawnLocation, 8, 8);
		ArcadeUtils.placeTeam(teamBlue, spawnLocation, 8, 8);
		int playerCount = players.size();
		if(teamGreen.size() < teamBlue.size()) {
			greenScore = 3;
		}
		else if(teamBlue.size() < teamGreen.size()) {
			blueScore = 3;
		}
		Location batLocation = spawnLocation.clone().add(0, 12, 0);
		for(int bat = 0; bat <= playerCount * 4; bat++) {
			bats.add(batLocation.getWorld().spawnEntity(batLocation, EntityType.BAT));
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
		List<Player> winners = new ArrayList<>();
		if(greenScore >= blueScore) {
			winners.addAll(teamGreen);
		}
		if(blueScore >= greenScore) {
			winners.addAll(teamBlue);
		}
		for(Entity bat : bats) {
			bat.remove();
		}
		teamGreen.clear();
		teamBlue.clear();
		bats.clear();
		greenScore = 0;
		blueScore = 0;
		return winners;
	}
	
	/**
	 * Handles the given quit event, that occured in the minigame.
	 * 
	 * @param player The player who quit.
	 */
	@Override
	public void handleQuitEvent(Player player) {
		teamGreen.remove(player);
		teamBlue.remove(player);
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
		ChatColor teamColor = getTeamColor(damager);
		if(teamColor.equals(ChatColor.GREEN)) {
			greenScore++;
		}
		else if(teamColor.equals(ChatColor.AQUA)) {
			blueScore++;
		}
		damager.playSound(damager.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1, 1);
		bats.remove(damaged);
		damaged.remove();
		for(Player player : ArcadeLobby.getPlayingPlayers()) {
			player.sendMessage(teamColor + damager.getName() + ChatColor.LIGHT_PURPLE + " shot a bat!");
		}
		if(bats.size() == 0) {
			ArcadeLobby.endGame();
		}
	}
	
	/**
	 * Gets the team color of the given player.
	 * 
	 * @param player The player to check.
	 * 
	 * @return Their team color.
	 */
	public ChatColor getTeamColor(Player player) {
		if(teamGreen.contains(player)) {
			return ChatColor.GREEN;
		}
		else if(teamBlue.contains(player)) {
			return ChatColor.AQUA;
		}
		else {
			return ChatColor.WHITE;
		}
	}

}
