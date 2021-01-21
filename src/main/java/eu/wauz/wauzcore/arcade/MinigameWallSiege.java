package eu.wauz.wauzcore.arcade;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Panda;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import eu.wauz.wauzcore.skills.SkillUtils;
import eu.wauz.wauzcore.system.annotations.Minigame;
import eu.wauz.wauzcore.system.util.Chance;

/**
 * A race minigame, where you have to climb walls with pushable boxes.
 * 
 * @author Wauzmons
 */
@Minigame
public class MinigameWallSiege implements ArcadeMinigame {
	
	/**
	 * The players who have crossed the finish line.
	 */
	private List<Player> finishedPlayers = new ArrayList<>();
	
	/**
	 * The remaining boxes in the arena.
	 */
	private List<Entity> boxes = new ArrayList<>();
	
	/**
	 * The amount of players who can win the game.
	 */
	private int maxWinningPlayers = 1;

	/**
	 * @return The display name of the minigame.
	 */
	@Override
	public String getName() {
		return "Wall-Siege";
	}

	/**
	 * @return The scoreboard description of the minigame.
	 */
	@Override
	public List<String> getDescription() {
		List<String> description = new ArrayList<>();
		description.add(ChatColor.WHITE + "Push the Pandas to");
		description.add(ChatColor.WHITE + "create a Way over the Walls");
		description.add(ChatColor.WHITE + "to reach the Finish Line!");
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
		World world = ArcadeLobby.getWorld();
		Location spawnLocation = new Location(world, 1000.5, 88, 750.5, 180, 0);
		ArcadeUtils.placeTeam(players, spawnLocation, 10, 1);
		
		Location location1 = spawnLocation.subtract(0, 0, 10);
		spawnBoxes(location1, 1, 10);
		Location location2 = location1.subtract(0, 0, 15);
		spawnBoxes(location2, 1, 5);
		spawnBoxes(location2, 2, 5);
		Location location3 = location2.subtract(0, 0, 15);
		spawnBoxes(location3, 1, 4);
		spawnBoxes(location3, 2, 3);
		spawnBoxes(location3, 3, 3);
		Location location4 = location3.subtract(0, 0, 15);
		spawnBoxes(location4, 1, 3);
		spawnBoxes(location4, 2, 3);
		spawnBoxes(location4, 3, 2);
		spawnBoxes(location4, 4, 2);
		
		ArcadeUtils.runStartTimer(10, 180);
	}

	/**
	 * Ends the game and decides a winner.
	 * 
	 * @return The players wo won the game.
	 */
	@Override
	public List<Player> endGame() {
		List<Player> winners = new ArrayList<>(finishedPlayers);
		finishedPlayers.clear();
		for(Entity box : boxes) {
			box.remove();
		}
		boxes.clear();
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
		if(event.getEntity() instanceof Panda) {
			event.setCancelled(true);
			Panda panda = (Panda) event.getEntity();
			if(panda.getVehicle() == null && event instanceof EntityDamageByEntityEvent) {
				Entity damager = ((EntityDamageByEntityEvent) event).getDamager();
				SkillUtils.throwBackEntity(panda, damager.getLocation(), 0.5);
			}
		}
	}
	
	/**
	 * Handles the given move event, that occured in the minigame.
	 * 
	 * @param event The move event.
	 */
	@Override
	public void handleMoveEvent(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Location location = player.getLocation();
		if(location.getZ() <= 682 && location.getY() >= 92) {
			finishedPlayers.add(player);
			player.teleport(new Location(ArcadeLobby.getWorld(), 1000.5, 101, 710.5, 180, 0));
			player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1, 1);
			for(Player playing : ArcadeLobby.getPlayingPlayers()) {
				playing.sendMessage(ChatColor.GOLD + player.getName() + ChatColor.BLUE + " has been qualified!");
			}
			if(finishedPlayers.size() >= maxWinningPlayers) {
				ArcadeLobby.endGame();
			}
		}
		else if(location.getY() <= 72) {
			player.teleport(player.getBedSpawnLocation());
		}
	}
	
	/**
	 * Spawns pushable shulker boxes around the given location.
	 * 
	 * @param location The location where to spawn the boxes.
	 * @param height The height of the boxes.
	 * @param amount The amount of boxes to spawn.
	 */
	public void spawnBoxes(Location location, int height, int amount) {
		DyeColor color;
		switch (height) {
		case 1:
			color = DyeColor.LIME;
			break;
		case 2:
			color = DyeColor.YELLOW;
			break;
		case 3:
			color = DyeColor.ORANGE;
			break;
		default:
			color = DyeColor.RED;
			break;
		}
		for(int number = 0; number < amount; number++) {
			float x = Chance.negativePositive(10);
			float z = Chance.negativePositive(2);
			Location shulkerLocation = location.clone().add(x, 0, z);
			Shulker shulker = (Shulker) shulkerLocation.getWorld().spawnEntity(shulkerLocation, EntityType.SHULKER);
			boxes.add(shulker);
			shulker.setAI(false);
			shulker.setSilent(true);
			shulker.setInvulnerable(true);
			shulker.setColor(color);
			Entity nextPassenger = shulker;
			for(int mount = 0; mount < height; mount++) {
				Panda panda = (Panda) shulkerLocation.getWorld().spawnEntity(shulkerLocation, EntityType.PANDA);
				boxes.add(panda);
				panda.setSilent(true);
				panda.setAware(false);
				panda.setCollidable(false);
				panda.setAdult();
				panda.addPassenger(nextPassenger);
				nextPassenger = panda;
			}
		}
	}
	
}
