package eu.wauz.wauzcore.arcade;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import eu.wauz.wauzcore.skills.particles.ParticleSpawner;
import eu.wauz.wauzcore.skills.particles.SkillParticle;
import net.md_5.bungee.api.ChatColor;

/**
 * A group minigame, where you have to "jinx" players of the other team.
 * 
 * @author Wauzmons
 */
public class MinigameJinxed implements ArcadeMinigame {
	
	/**
	 * The members of the green team.
	 */
	private List<Player> teamGreen = new ArrayList<>();
	
	/**
	 * The members of the blue team.
	 */
	private List<Player> teamBlue = new ArrayList<>();
	
	/**
	 * The boss bar, visible for jinxed players.
	 */
	private BossBar jinxedBar;
	
	/**
	 * The particles to show around jinxed players.
	 */
	private SkillParticle jinxedParticle;
	
	/**
	 * Creates a new instance of the minigame.
	 */
	public MinigameJinxed() {
		String jinxedTitle = ChatColor.WHITE + "~~~" + ChatColor.YELLOW + " YOU ARE JINXED " + ChatColor.WHITE + "~~~";
		jinxedBar = Bukkit.createBossBar(jinxedTitle, BarColor.PURPLE, BarStyle.SOLID);
		jinxedBar.setProgress(1);
		jinxedParticle = new SkillParticle(Color.PURPLE);
	}

	/**
	 * @return The display name of the minigame.
	 */
	@Override
	public String getName() {
		return "Jinxed";
	}

	/**
	 * @return The scoreboard description of the minigame.
	 */
	@Override
	public List<String> getDescription() {
		List<String> description = new ArrayList<>();
		description.add(ChatColor.WHITE + "Run away from Jinxed Players!");
		description.add(ChatColor.WHITE + "If you are Jinxed, try to");
		description.add(ChatColor.WHITE + "hit Players of the other Team");
		description.add(ChatColor.WHITE + "to Jinx them and get Unjinxed.");
		description.add("   ");
		description.add(ChatColor.GREEN + "Team Green: " + ChatColor.GOLD + getJinxedCount(teamGreen));
		description.add(ChatColor.AQUA + "Team Blue: " + ChatColor.GOLD + getJinxedCount(teamBlue));
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
		Location greenLocation = new Location(ArcadeLobby.getWorld(), 500.5, 88, 524.5, 180, 0);
		Location blueLocation = new Location(ArcadeLobby.getWorld(), 500.5, 88, 479.5, 0, 0);
		ArcadeUtils.placeTeam(teamGreen, greenLocation, 3, 1);
		ArcadeUtils.placeTeam(teamBlue, blueLocation, 3, 1);
		int playerCount = players.size();
		if(playerCount <= 3) {
			jinxPlayers(ArcadeUtils.selectRandomPlayers(players, 1));
		}
		else if(playerCount <= 6) {
			jinxPlayers(ArcadeUtils.selectRandomPlayers(teamGreen, 1));
			jinxPlayers(ArcadeUtils.selectRandomPlayers(teamBlue, 1));
		}
		else {
			int jinxedCount = (int) Math.ceil((float) playerCount / 4.0f);
			jinxPlayers(ArcadeUtils.selectRandomPlayers(teamGreen, jinxedCount));
			jinxPlayers(ArcadeUtils.selectRandomPlayers(teamBlue, jinxedCount));
		}
		ArcadeUtils.runStartTimer(10, 180);
	}

	/**
	 * Ends the game and decides a winner.
	 * 
	 * @return The players wo won the game.
	 */
	@Override
	public List<Player> endGame() {
		List<Player> winners = new ArrayList<>();
		int jinxedGreen = getJinxedCount(teamGreen);
		int jinxedBlue = getJinxedCount(teamBlue);
		if(jinxedBlue > jinxedGreen) {
			winners.addAll(teamGreen);
		}
		else if(jinxedGreen > jinxedBlue) {
			winners.addAll(teamBlue);
		}
		
		teamGreen.clear();
		teamBlue.clear();
		jinxedBar.removeAll();
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
		jinxedBar.removePlayer(player);
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
		if(!(damageByEntityEvent.getDamager() instanceof Player)) {
			event.setCancelled(true);
			return;
		}
		Player damager = (Player) damageByEntityEvent.getDamager();
		Player damaged = (Player) damageByEntityEvent.getEntity();
		if(jinx(damager, damaged)) {
			event.setCancelled(false);
			event.setDamage(0);
		}
		else {
			event.setCancelled(true);
		}
	}
	
	/**
	 * A method that is called every second of the minigame.
	 */
	@Override
	public void handleTick() {
		for(Player player : jinxedBar.getPlayers()) {
			ParticleSpawner.spawnParticleCircle(player.getLocation(), jinxedParticle, 1, 8);
		}
	}
	
	/**
	 * Checks if a player is jinxed.
	 * 
	 * @param player The player to check.
	 * 
	 * @return If they are jinxed.
	 */
	public boolean isJinxed(Player player) {
		return jinxedBar.getPlayers().contains(player);
	}
	
	/**
	 * Gets the amount of jinxed players in a team.
	 * 
	 * @param team The team to check.
	 * 
	 * @return The amount of jinxed players.
	 */
	public int getJinxedCount(List<Player> team) {
		int jinxedCount = 0;
		for(Player player : team) {
			if(isJinxed(player)) {
				jinxedCount++;
			}
		}
		return jinxedCount;
	}
	
	/**
	 * Tries to jinx a player.
	 * Fails if the players are in the same game or the player is already jinxed.
	 * 
	 * @param jinxer The player trying to jinx.
	 * @param jinxed The player getting jinxed.
	 * 
	 * @return If the jinxing was successful.
	 */
	public boolean jinx(Player jinxer, Player jinxed) {
		if(!isJinxed(jinxer) || isJinxed(jinxed) || sameTeam(jinxer, jinxed)) {
			return false;
		}
		jinxedBar.removePlayer(jinxer);
		jinxedBar.addPlayer(jinxed);
		String jinxedName = getTeamColor(jinxed) + jinxed.getName() + ChatColor.DARK_PURPLE;
		String jinxerName = getTeamColor(jinxer) + jinxer.getName() + ChatColor.DARK_PURPLE;
		for(Player player : ArcadeLobby.getPlayingPlayers()) {
			player.sendMessage(jinxedName + " was jinxed by " + jinxerName + "!");
		}
		return true;
	}
	
	/**
	 * Marks the given players as jinxed.
	 * 
	 * @param players The players to jinx.
	 */
	public void jinxPlayers(List<Player> players) {
		for(Player player : players) {
			jinxedBar.addPlayer(player);
		}
	}
	
	/**
	 * Checks if two players a in the same team.
	 * 
	 * @param player1 The first player.
	 * @param player2 The second player.
	 * 
	 * @return If they are in the same team.
	 */
	public boolean sameTeam(Player player1, Player player2) {
		return (teamGreen.contains(player1) && teamGreen.contains(player2))
				|| (teamBlue.contains(player1) && teamBlue.contains(player2));
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
