package eu.wauz.wauzcore.arcade;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffectType;

import eu.wauz.wauzcore.skills.SkillUtils;
import eu.wauz.wauzcore.skills.particles.ParticleSpawner;
import eu.wauz.wauzcore.skills.particles.SkillParticle;
import eu.wauz.wauzcore.system.annotations.Minigame;

/**
 * A hunt minigame, where you have to "jinx" other players.
 * 
 * @author Wauzmons
 */
@Minigame
public class MinigameJinxed implements ArcadeMinigame {
	
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
		description.add(ChatColor.WHITE + "hit Players to Jinx them.");
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
		Location greenLocation = new Location(ArcadeLobby.getWorld(), 500.5, 88, 524.5, 180, 0);
		Location blueLocation = new Location(ArcadeLobby.getWorld(), 500.5, 88, 479.5, 0, 0);
		ArcadeUtils.placeTeam(teams.get(0), greenLocation, 3, 1);
		ArcadeUtils.placeTeam(teams.get(1), blueLocation, 3, 1);
		jinxPlayers(ArcadeUtils.selectRandomPlayers(players, players.size() - (players.size() / 2)));
		ArcadeUtils.runStartTimer(10, 120);
	}

	/**
	 * Ends the game and decides a winner.
	 * 
	 * @return The players wo won the game.
	 */
	@Override
	public List<Player> endGame() {
		List<Player> winners = new ArrayList<>();
		for(Player player : ArcadeLobby.getPlayingPlayers()) {
			if(!isJinxed(player)) {
				winners.add(player);
			}
		}
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
		if(!(damageByEntityEvent.getDamager() instanceof Player) || !(damageByEntityEvent.getEntity() instanceof Player)) {
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
			SkillUtils.addPotionEffect(player, PotionEffectType.SPEED, 1, 0);
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
		if(!isJinxed(jinxer) || isJinxed(jinxed)) {
			return false;
		}
		jinxedBar.removePlayer(jinxer);
		jinxedBar.addPlayer(jinxed);
		SkillUtils.addPotionEffect(jinxed, PotionEffectType.BLINDNESS, 2, 50);
		SkillUtils.addPotionEffect(jinxed, PotionEffectType.SLOW, 2, 50);
		String jinxedName = ChatColor.GOLD + jinxed.getName() + ChatColor.LIGHT_PURPLE;
		String jinxerName = ChatColor.GOLD + jinxer.getName() + ChatColor.LIGHT_PURPLE;
		jinxed.playSound(jinxed.getLocation(), Sound.ENTITY_BLAZE_DEATH, 1, 1);
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
	
}
