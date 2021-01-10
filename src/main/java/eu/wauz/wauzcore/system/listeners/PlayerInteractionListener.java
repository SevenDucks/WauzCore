package eu.wauz.wauzcore.system.listeners;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.ItemStack;
import org.spigotmc.event.entity.EntityDismountEvent;

import eu.wauz.wauzcore.arcade.ArcadeLobby;
import eu.wauz.wauzcore.data.ServerConfigurator;
import eu.wauz.wauzcore.events.ArmorEquipEvent;
import eu.wauz.wauzcore.items.WauzEquipment;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.menu.MaterialPouch;
import eu.wauz.wauzcore.mobs.pets.WauzActivePet;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.WauzPlayerRegistrator;
import eu.wauz.wauzcore.players.WauzPlayerSit;
import eu.wauz.wauzcore.players.calc.FoodCalculator;
import eu.wauz.wauzcore.players.ui.scoreboard.WauzPlayerScoreboard;
import eu.wauz.wauzcore.skills.particles.SkillParticle;
import eu.wauz.wauzcore.skills.passive.PassiveBreath;
import eu.wauz.wauzcore.system.ChatFormatter;
import eu.wauz.wauzcore.system.EventMapper;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.achievements.AchievementTracker;
import eu.wauz.wauzcore.system.achievements.WauzAchievementType;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * A listener to catch events, related to interactions between the player and the server or other entities.
 * 
 * @author Wauzmons
 */
public class PlayerInteractionListener implements Listener {

	/**
	 * Storage for player names, to greet them in the MotD.
	 */
	private Map<InetAddress, String> addressNameMap = new HashMap<>();
	
	/**
	 * Storage for timestamps, when players started sprinting.
	 */
	private Map<Player, Long> playerSprintMap = new HashMap<>();

	
	/**
	 * Gets the player's name, to greet them in the MotD.
	 * 
	 * @param address IP of the player.
	 * @return Name of the player or "Hero" if unknown.
	 */
	private String getNameFromAddress(InetAddress address) {
		return addressNameMap.get(address);
	}

	/**
	 * Responds to the players ping with a custom MotD.
	 * 
	 * @param event The ping event.
	 */
	@EventHandler
	public void onPing(ServerListPingEvent event) {
		String playerName = getNameFromAddress(event.getAddress());
		event.setMotd(ServerConfigurator.getServerMotd(playerName));
	}

	/**
	 * Logs the player into the game.
	 * Denies access if the player is banned, not whitelisted
	 * or simply when the server is already full.
	 * 
	 * @param event The login event.
	 * 
	 * @see WauzPlayerRegistrator#login(Player)
	 */
	@EventHandler
	public void onLogin(PlayerLoginEvent event) throws Exception {
		Player player = event.getPlayer();
		addressNameMap.put(event.getAddress(), player.getName());
		
		if(event.getResult().equals(Result.KICK_OTHER)) {
			WauzDebugger.log(player.getName() + " shall not pass!");
		}
		else if(Bukkit.getOnlinePlayers().size() >= Bukkit.getMaxPlayers()) {
			event.setResult(Result.KICK_FULL);
		}
		else if(Bukkit.hasWhitelist() && !player.isWhitelisted()) {
			event.setResult(Result.KICK_WHITELIST);
		}
		else if(player.isBanned()) {
			event.setResult(Result.KICK_BANNED);
		}
		else {
			event.setResult(Result.ALLOWED);
			WauzPlayerRegistrator.login(player);
		}
	}

	/**
	 * Logs the player out of the game.
	 * 
	 * @param event The logout event.
	 * 
	 * @see WauzPlayerRegistrator#logout(Player)
	 */
	@EventHandler
	public void onLogout(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		WauzPlayerRegistrator.logout(player);
	}

	/**
	 * Lets the player automatically respawn on death.
	 * 
	 * @param event The death event.
	 * 
	 * @see WauzPlayerRegistrator#respawn(Player)
	 */
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		WauzPlayerRegistrator.respawn(player);
		if(WauzMode.isMMORPG(player)) {
			WauzActivePet.tryToUnsummon(player, false);
		}
		else if(WauzMode.isArcade(player)) {
			ArcadeLobby.handleDeathEvent(event);
		}
	}

	/**
	 * Formats the chat messages of the player.
	 * 
	 * @param event The chat event.
	 * 
	 * @see ChatFormatter#global(AsyncPlayerChatEvent)
	 */
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		event.setFormat(ChatFormatter.global(event));
	}
	
	/**
	 * Disables certain commands for all players.
	 * 
	 * @param event The command event.
	 */
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event) {
		if(StringUtils.startsWithAny(event.getMessage().toLowerCase(), "/op ", "/deop ")) {
			event.getPlayer().sendMessage(ChatColor.RED + "This command is disabled!");
			event.setCancelled(true);
		}
	}

	/**
	 * Handles possible interactions with pets.
	 * 
	 * @param event The interact event.
	 * 
	 * @see WauzActivePet#handlePetInteraction(PlayerInteractEntityEvent)
	 */
	@EventHandler
	public void onEntityInteraction(PlayerInteractEntityEvent event) {
		if(WauzMode.isMMORPG(event.getPlayer())) {
			WauzActivePet.handlePetInteraction(event);
		}
	}
	
	/**
	 * Lets the mapper decide how to handle the interaction with an object.
	 * 
	 * @param event The interact event.
	 * 
	 * @see EventMapper
	 */
	@EventHandler
	public void onInteraction(PlayerInteractEvent event) {
		if(WauzMode.isMMORPG(event.getPlayer())) {
			EventMapper.handleItemInteraction(event);
		}
		else if(WauzMode.isSurvival(event.getPlayer())) {
			EventMapper.handleSurvivalItemInteraction(event);
		}
		else if(WauzMode.isArcade(event.getPlayer())) {
			ArcadeLobby.handleInteractEvent(event);
		}
	}
	
	/**
	 * Catches the player animation for the Arcade mode.
	 * 
	 * @param event The animation event.
	 */
	@EventHandler
	public void onAnimate(PlayerAnimationEvent event) {
		if(WauzMode.isArcade(event.getPlayer())) {
			ArcadeLobby.handleAnimationEvent(event);
		}
	}

	/**
	 * Stores the item in the fitting invntory for MMORPG players
	 * and updates there scoreboard, if it was relevant for a quest.
	 * 
	 * @param event The pickup event.
	 * 
	 * @see MaterialPouch#addItem(Player, ItemStack, String)
	 * @see WauzPlayerScoreboard#scheduleScoreboardRefresh(Player)
	 */
	@EventHandler
	public void onPickup(EntityPickupItemEvent event) {
		if (event.getEntity() instanceof Player && WauzMode.isMMORPG(event.getEntity())) {
			Player player = (Player) event.getEntity();
			ItemStack itemStack = event.getItem().getItemStack();
			if(ItemUtils.isMaterialItem(itemStack)) {
				event.setCancelled(true);
				event.getItem().remove();
				MaterialPouch.addItem(player, itemStack, "materials");
			}
			else if(ItemUtils.isQuestItem(itemStack)) {
				event.setCancelled(true);
				event.getItem().remove();
				MaterialPouch.addItem(player, itemStack, "questitems");
				WauzPlayerScoreboard.scheduleScoreboardRefresh(player);
			}
			
			if(ItemUtils.hasDisplayName(itemStack)) {
				String displayName = itemStack.getItemMeta().getDisplayName();
				AchievementTracker.checkForAchievement(player, WauzAchievementType.COLLECT_ARTIFACTS, displayName);
			}
		}
	}

	/**
	 * Reads food stats from the consumed item,
	 * to apply all relevant effects to the player.
	 * 
	 * @param event The consume event.
	 * 
	 * @see FoodCalculator#applyItemEffects(PlayerItemConsumeEvent)
	 */
	@EventHandler
	public void onConsume(PlayerItemConsumeEvent event) {
		if(WauzMode.isMMORPG(event.getPlayer())) {
			FoodCalculator.applyItemEffects(event);
		}
	}
	
	/**
	 * Changes the armor set of a player,
	 * but only if they meet the equip requirements.
	 * 
	 * @param event The equip event.
	 * 
	 * @see WauzEquipment#equipArmor(ArmorEquipEvent)
	 * @see ArmorEquipEventListener Custom Event Listener
	 */
	@EventHandler
	public void onEquip(ArmorEquipEvent event) {
		if(WauzMode.isMMORPG(event.getPlayer())) {
			WauzEquipment.equipArmor(event);
		}
	}
	
	/**
	 * Prevents MMORPG players to modify armor stands,
	 * because they are used as displays for damage numbers and more.
	 * 
	 * @param event The equip event.
	 */
	@EventHandler
	public void onArmorStandEquip(PlayerArmorStandManipulateEvent event) {
		if(WauzMode.isMMORPG(event.getPlayer()) && !event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
			event.setCancelled(true);
		}
	}
	
	/**
	 * Starts a double jump if a player tries to fly in the hub.
	 * This is used for faster travelling and exploration.
	 * 
	 * @param event The flight toggle event.
	 */
	@EventHandler
	public void onDoubleJump(PlayerToggleFlightEvent event) {
		Player player = event.getPlayer();
		if(WauzMode.inHub(player) && !player.getGameMode().equals(GameMode.CREATIVE)) {
			event.setCancelled(true);
			player.setFlying(false);
			player.setAllowFlight(false);
			Location location = player.getLocation();
			player.setVelocity(location.getDirection().multiply(1.2).setY(1.2));
			location.getWorld().playSound(location, Sound.ENTITY_BLAZE_SHOOT, 1, 0.5f);
			new SkillParticle(Particle.CLOUD).spawn(location, 20);
		}
	}
	
	/**
	 * Measures the time a player sprinted.
	 * Used to increase the breath skill in MMORPG mode.
	 * 
	 * @param event The sprint toggle event.
	 */
	@EventHandler
	public void onSprint(PlayerToggleSprintEvent event) {
		Player player = event.getPlayer();
		if(event.isSprinting()) {
			playerSprintMap.put(player, System.currentTimeMillis());
		}
		else if(WauzMode.isMMORPG(player) && !WauzMode.inHub(player)) {
			long sprintedMillis = System.currentTimeMillis() - playerSprintMap.get(player);
			WauzPlayerDataPool.getPlayer(player).getCachedPassive(PassiveBreath.PASSIVE_NAME).grantExperience(player, sprintedMillis);
		}
	}
	
	/**
	 * Makes a player stand up, if they were sitting and despawns their mount.
	 *
	 * @param event The dismount event.
	 */
	@EventHandler
	public void onDismount(EntityDismountEvent event) {
		if(!(event.getEntity() instanceof Player)) {
			return;
		}
		Player player = (Player) event.getEntity();
		if(event.getDismounted() instanceof Arrow) {
			WauzDebugger.log((Player) event.getEntity(), "Stood Up");
			WauzPlayerSit.standUp(event);
			return;
		}
		Entity owner = WauzActivePet.getOwner(event.getDismounted());
		if(owner != null && owner.getUniqueId().equals(player.getUniqueId())) {
			WauzActivePet.tryToUnsummon(player, false);
		}
	}
	
	/**
	 * Prevents a player's spawn from changing when leaving a bed.
	 * 
	 * @param event The bed leave event.
	 */
	@EventHandler
	public void onBedLeave(PlayerBedLeaveEvent event) {
		event.setSpawnLocation(false);
	}
	
}
