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

import eu.wauz.wauzcore.skills.particles.SkillParticle;
import net.md_5.bungee.api.ChatColor;

public class MinigameJinxed implements ArcadeMinigame {
	
	private List<Player> teamGreen = new ArrayList<>();
	
	private List<Player> teamBlue = new ArrayList<>();
	
	private BossBar jinxedBar;
	
	private SkillParticle jinxedParticle;
	
	public MinigameJinxed() {
		jinxedBar = Bukkit.createBossBar("You are Jinxed!", BarColor.PURPLE, BarStyle.SOLID);
		jinxedBar.setProgress(1);
		jinxedParticle = new SkillParticle(Color.PURPLE);
//		ParticleSpawner.spawnParticleCircle(damageable.getLocation(), jinxedParticle, 1, 8);
	}

	@Override
	public String getName() {
		return "Jinxed";
	}

	@Override
	public String getDescription() {
		List<String> rowStrings = new ArrayList<>();
		return null;
	}

	@Override
	public void startGame(List<Player> players) {
		List<List<Player>> teams = ArcadeUtils.splitIntoTeams(players, 2);
		teamGreen.addAll(teams.get(0));
		teamBlue.addAll(teams.get(1));
		ArcadeUtils.equipTeamColor(teamGreen, Color.LIME);
		ArcadeUtils.equipTeamColor(teamBlue, Color.TEAL);
		Location greenLocation = new Location(ArcadeLobby.getWorld(), 500.5, 88, 524.5);
		Location blueLocation = new Location(ArcadeLobby.getWorld(), 500.5, 88, 479.5);
		ArcadeUtils.placeTeam(teamGreen, greenLocation);
		ArcadeUtils.placeTeam(teamBlue, blueLocation);
		ArcadeUtils.runStartTimer(15, 180);
	}

	@Override
	public void endGame() {
		teamGreen.clear();
		teamBlue.clear();
		jinxedBar.getPlayers().clear();
	}

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
			event.setDamage(0);
		}
		else {
			event.setCancelled(true);
		}
	}
	
	public boolean isJinxed(Player player) {
		return jinxedBar.getPlayers().contains(player);
	}
	
	public boolean jinx(Player jinxer, Player jinxed) {
		if(!isJinxed(jinxer) || sameTeam(jinxer, jinxed)) {
			return false;
		}
		jinxedBar.getPlayers().remove(jinxer);
		jinxedBar.getPlayers().add(jinxed);
		for(Player player : ArcadeLobby.getPlayingPlayers()) {
			player.sendMessage(ChatColor.DARK_PURPLE + jinxed.getName() + " was jinxed by " + jinxer.getName() + "!");
		}
		return true;
	}
	
	public boolean sameTeam(Player player1, Player player2) {
		return (teamGreen.contains(player1) && teamGreen.contains(player2))
				|| (teamBlue.contains(player1) && teamBlue.contains(player2));
	}

}
