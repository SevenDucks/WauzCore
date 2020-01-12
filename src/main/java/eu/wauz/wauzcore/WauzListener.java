package eu.wauz.wauzcore;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRecipeDiscoverEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.world.WorldInitEvent;
import org.spigotmc.event.entity.EntityMountEvent;

import com.destroystokyo.paper.event.player.PlayerAdvancementCriterionGrantEvent;

import eu.wauz.wauzcore.events.ArmorEquipEvent;
import eu.wauz.wauzcore.events.ArmorEquipEventListener;
import eu.wauz.wauzcore.items.CustomWeaponBow;
import eu.wauz.wauzcore.items.CustomWeaponGlider;
import eu.wauz.wauzcore.items.CustomWeaponHook;
import eu.wauz.wauzcore.items.DurabilityCalculator;
import eu.wauz.wauzcore.items.Equipment;
import eu.wauz.wauzcore.items.WauzRewards;
import eu.wauz.wauzcore.items.WauzSigns;
import eu.wauz.wauzcore.menu.PetOverviewMenu;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.mobs.MenacingMobsSpawner;
import eu.wauz.wauzcore.mobs.MobEventMapper;
import eu.wauz.wauzcore.players.WauzPlayerRegistrator;
import eu.wauz.wauzcore.players.calc.DamageCalculator;
import eu.wauz.wauzcore.players.calc.FoodCalculator;
import eu.wauz.wauzcore.players.ui.WauzPlayerBossBar;
import eu.wauz.wauzcore.players.ui.WauzPlayerScoreboard;
import eu.wauz.wauzcore.skills.particles.SkillParticle;
import eu.wauz.wauzcore.system.ChatFormatter;
import eu.wauz.wauzcore.system.EventMapper;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.WauzNoteBlockPlayer;
import eu.wauz.wauzcore.system.WauzPermission;
import eu.wauz.wauzcore.system.WauzRegion;
import eu.wauz.wauzcore.system.nms.WauzNmsMinimap;
import eu.wauz.wauzcore.system.util.WauzMode;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobSpawnEvent;
import net.md_5.bungee.api.ChatColor;

/**
 * This is the class, where most Bukkit events are caught
 * and modified to hold WauzCore data instead of normal Minecraft stuff.
 * 
 * @author Wauzmons
 */
public class WauzListener implements Listener {
	
	/**
	 * Storage for player names, to greet them in the MotD.
	 */
	private Map<InetAddress, String> addressNameMap = new HashMap<>();
	
	/**
	 * Gets the player's name, to greet them in the MotD.
	 * 
	 * @param address IP of the player.
	 * @return Name of the player or "Hero" if unknown.
	 */
	private String getNameFromAddress(InetAddress address) {
		String name = addressNameMap.get(address);
		return name == null ? "Hero" : name;
	}

// Player Interaction Listeners
	
	/**
	 * Responds to the players ping with a custom MotD.
	 * 
	 * @param event
	 */
	@EventHandler
	public void onPing(ServerListPingEvent event) {
		String colon = ChatColor.RED + "" + ChatColor.BOLD + ":" + ChatColor.RESET;
		String arrowL = ChatColor.GOLD + ">>>" + ChatColor.YELLOW + "---" + ChatColor.GOLD + "> " + colon;
		String arrowR = colon + ChatColor.GOLD + " <" + ChatColor.YELLOW + "---" + ChatColor.GOLD + "<<<";
		String title = ChatColor.YELLOW + "" + ChatColor.BOLD  + " Wauzland Online ";
		String greet = System.lineSeparator() + ChatColor.GRAY + "Wauzland and Dalyreos need you, ";
		String name = getNameFromAddress(event.getAddress()) + "!";
		String modt = arrowL + title + arrowR + greet + name;
		event.setMotd(modt);
	}

	/**
	 * Logs the player into the game.
	 * Denies access if the player is banned, not whitelisted
	 * or simply when the server is already full.
	 * 
	 * @param event
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
	 * @param event
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
	 * @param event
	 * 
	 * @see WauzPlayerRegistrator#respawn(Player)
	 */
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		WauzPlayerRegistrator.respawn(player);
	}

	/**
	 * Formats the chat messages of the player.
	 * 
	 * @param event
	 * 
	 * @see ChatFormatter#global(AsyncPlayerChatEvent)
	 */
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		event.setFormat(ChatFormatter.global(event));
	}

	/**
	 * Lets the mapper decide how to handle the interaction with an entity.
	 * 
	 * @param event
	 * 
	 * @see EventMapper
	 */
	@EventHandler
	public void onEntityInteraction(PlayerInteractEntityEvent event) {
		if(WauzMode.isMMORPG(event.getPlayer()) && event.getRightClicked().getCustomName() != null) {
			EventMapper.handleEntityInteraction(event);
		}
	}

	/**
	 * Lets the mapper decide how to handle the interaction with an object.
	 * 
	 * @param event
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
	}

	/**
	 * Updates the scoreboard of an MMORPG player,
	 * in case they picked up an item that is relevant to a quest.
	 * 
	 * @param event
	 * 
	 * @see WauzPlayerScoreboard#scheduleScoreboard(Player)
	 */
	@EventHandler
	public void onPickup(EntityPickupItemEvent event) {
		if (event.getEntity() instanceof Player && WauzMode.isMMORPG(event.getEntity())) {
			WauzPlayerScoreboard.scheduleScoreboard((Player) event.getEntity());
		}
	}

	/**
	 * Reads food stats from the consumed item,
	 * to apply all relevant effects to the player.
	 * 
	 * @param event
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
	 * @param event
	 * 
	 * @see Equipment#equipArmor(ArmorEquipEvent)
	 * @see ArmorEquipEventListener Custom Event Listener
	 */
	@EventHandler
	public void onEquip(ArmorEquipEvent event) {
		if(WauzMode.isMMORPG(event.getPlayer())) {
			Equipment.equipArmor(event);
		}
	}
	
	/**
	 * Prevents MMORPG players to modify armor stands,
	 * because they are used as displays for damage numbers and more.
	 * 
	 * @param event
	 */
	@EventHandler
	public void onArmorStandEquip(PlayerArmorStandManipulateEvent event) {
		if(WauzMode.isMMORPG(event.getPlayer())) {
			event.setCancelled(true);
		}
	}
	
	/**
	 * Starts a double jump if a player tries to fly in the hub.
	 * This is used for faster travelling and exploration.
	 * 
	 * @param event
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
	 * Prevents players to ride on mounts, that belong to someone else.
	 * 
	 * @param event
	 */
	@EventHandler
	public void onMount(EntityMountEvent event) {
		Entity owner = PetOverviewMenu.getOwner(event.getMount());
		if(owner != null && !owner.getUniqueId().equals(event.getEntity().getUniqueId())) {
			event.getEntity().sendMessage(ChatColor.RED + "This is not your mount!");
			event.setCancelled(true);
		}
	}

// Player Ambient Listeners
	
	/**
	 * Prevents that the spawn is loaded if a new world is initialized.
	 * This prevents lag on entering instances.
	 * 
	 * @param event
	 */
	@EventHandler
	public void onWorldInit(WorldInitEvent event) {
		event.getWorld().setKeepSpawnInMemory(false);
	}

	/**
	 * Reloads most custom UI if the player changes their current world.
	 * This assures that the correct scoreboard, minimap etc. are shown.
	 * Also checks for a new music track.
	 * 
	 * @param event
	 */
	@EventHandler
	public void onWorldEnter(PlayerChangedWorldEvent event) {
		WauzNoteBlockPlayer.play(event.getPlayer());
		WauzPlayerBossBar.clearBar(event.getPlayer());
		WauzPlayerScoreboard.scheduleScoreboard(event.getPlayer());
		WauzNmsMinimap.init(event.getPlayer());
	}
	
	/**
	 * Prevents players to break blocks in certain regions.
	 * 
	 * @param event
	 * 
	 * @see WauzRegion#disallowBuild(Block)
	 */
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if(!player.hasPermission(WauzPermission.DEBUG_BUILDING.toString()) && WauzRegion.disallowBuild(event.getBlock())) {
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED + "You can't build here! Find another spot!");
		}
	}
	
	/**
	 * Prevents players to place blocks in certain regions.
	 * 
	 * @param event
	 * 
	 * @see WauzRegion#disallowBuild(Block)
	 */
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if(!player.hasPermission(WauzPermission.DEBUG_BUILDING.toString()) && WauzRegion.disallowBuild(event.getBlock())) {
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED + "You can't build here! Find another spot!");
		}
	}

	/**
	 * Checks if an OP player just created a sign that is bound to an event.
	 * 
	 * @param event
	 * 
	 * @see WauzSigns#create(SignChangeEvent)
	 */
	@EventHandler
	public void onSignChange(SignChangeEvent event) {
		if(event.getPlayer().isOp() && WauzMode.isMMORPG(event.getPlayer())) {
			WauzSigns.create(event);
		}
	}
	
	/**
	 * Rewards tokens if a Survival player exceeded the maximum level.
	 * 
	 * @param event
	 * 
	 * @see WauzRewards#survivalToken(Player)
	 */
	@EventHandler
	public void onLevelUp(PlayerLevelChangeEvent event) {
		if(WauzMode.isSurvival(event.getPlayer()) && event.getNewLevel() > WauzCore.MAX_PLAYER_LEVEL_SURVIVAL) {
			WauzRewards.survivalToken(event.getPlayer());
		}
	}
	
	/**
	 * Prevents MMORPG players from learning normal Minecraft recipes.
	 * 
	 * @param event
	 */
	@EventHandler
	public void onRecipeDiscover(PlayerRecipeDiscoverEvent event) {
		if(WauzMode.isMMORPG(event.getPlayer())) {
			event.setCancelled(true);
		}
	}
	
	/**
	 * Prevents MMORPG players from receiving normal Minecraft advancements.
	 * 
	 * @param event
	 */
	public void onAdvancement(PlayerAdvancementCriterionGrantEvent event) {
		if(WauzMode.isMMORPG(event.getPlayer())) {
			event.setCancelled(true);
		}
	}
	
	/**
	 * Prevents players from loosing saturation in the hub.
	 * 
	 * @param event
	 */
	@EventHandler
	public void onHunger(FoodLevelChangeEvent event) {
		if(WauzMode.inHub(event.getEntity())) {
			event.setCancelled(true);
		}
	}

// Player Combat Listeners

	/**
	 * Handles damage calculation in MMORPG mode.
	 * In attack debug mode, the damage output is multiplied by 100.
	 * Cancels out damage in hub or defense debug mode, damage to pets or damage from cosmetic explosions.
	 * Prevents damaging players in non PvP areas and fall damage when gliding.
	 * 
	 * Also updates the boss bar if one exists.
	 * 
	 * @param event
	 * 
	 * @see DamageCalculator#attack(EntityDamageByEntityEvent)
	 * @see DamageCalculator#reflect(EntityDamageByEntityEvent)
	 * @see DamageCalculator#defend(EntityDamageEvent)
	 * @see DamageCalculator#removeDamageModifiers(EntityDamageEvent)
	 * @see WauzPlayerBossBar#updateBossBar(double)
	 * @see WauzDebugger#toggleAttackDebugMode(Player)
	 * @see WauzDebugger#toggleDefenseDebugMode(Player)
	 */
	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if(WauzMode.inHub(event.getEntity())
				|| PetOverviewMenu.getOwner(event.getEntity()) != null
				|| event.getEntity().hasPermission(WauzPermission.DEBUG_DEFENSE.toString())
				|| event.getCause().equals(DamageCause.BLOCK_EXPLOSION)) {
			event.setCancelled(true);
			return;
		}
		WauzPlayerBossBar playerBossBar = WauzPlayerBossBar.getBossBar(event.getEntity());
		if(event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent entityEvent = (EntityDamageByEntityEvent) event;
			
			if(WauzRegion.disallowPvP(entityEvent.getDamager(), event.getEntity())) {
				event.setCancelled(true);
				return;
			}
			if(entityEvent.getDamager() instanceof Arrow && CustomWeaponBow.cancelArrowImpact(entityEvent)) {
				event.setCancelled(true);
				return;
			}
			if(entityEvent.getDamager().hasPermission(WauzPermission.DEBUG_ATTACK.toString())) {
				event.setDamage(event.getDamage() * 100);
			}
			if(!WauzMode.isMMORPG(event.getEntity())) {
				return;
			}
			if(entityEvent.getDamager() instanceof Player) {
				DamageCalculator.attack(entityEvent);
				if(playerBossBar != null) {
					playerBossBar.addPlayer((Player) entityEvent.getDamager(), entityEvent.getDamage());
				}
			}
			if(event.getEntity() instanceof Player) {
				DamageCalculator.reflect(entityEvent);
			}
		}
		if(event.getEntity() instanceof Player && WauzMode.isMMORPG(event.getEntity())) {
			event.setCancelled(true);
			CustomWeaponGlider.cancelFallDamage(event);
			DamageCalculator.defend(event);
		}
		DamageCalculator.removeDamageModifiers(event);
		if(playerBossBar != null) {
			playerBossBar.updateBossBar(event.getDamage());
		}
	}

	/**
	 * Handles healing calculation in MMORPG mode.
	 * Also updates the boss bar if one exists.
	 * 
	 * @param event
	 * 
	 * @see DamageCalculator#heal(EntityRegainHealthEvent)
	 * @see WauzPlayerBossBar#updateBossBar(double)
	 */
	@EventHandler
	public void onHeal(EntityRegainHealthEvent event) {
		if(event.getEntity() instanceof Player && WauzMode.isMMORPG(event.getEntity())) {
			DamageCalculator.heal(event);
		}
		WauzPlayerBossBar playerBossBar = WauzPlayerBossBar.getBossBar(event.getEntity());
		if(playerBossBar != null) {
			playerBossBar.updateBossBar(- event.getAmount());
		}
	}

	/**
	 * Handles the killing of entities in MMORPG mode.
	 * This handler focuses on effects like life leech caused by the player,
	 * not on the actual death or loot drops of the entity.
	 * 
	 * @param event
	 * 
	 * @see DamageCalculator#kill(EntityDeathEvent)
	 * @see WauzListener#onMythicDeath(MythicMobDeathEvent) "Real" death event handler
	 */
	@EventHandler
	public void onKill(EntityDeathEvent event) {
		if(event.getEntity().getKiller() != null && WauzMode.isMMORPG(event.getEntity().getKiller())) {
			DamageCalculator.kill(event);
		}
	}
	
	/**
	 * Prevents item damage in MMORPG mode.
	 * The equipment there has its own durability system.
	 * 
	 * @param event
	 * 
	 * @see DurabilityCalculator
	 */
	@EventHandler
	public void onItemDamage(PlayerItemDamageEvent event) {
		if(WauzMode.isMMORPG(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

// MythicMobs Listeners

	/**
	 * Handles the spawn of a (mythic) mob.
	 * This includes the initialization of modifiers, loot and boss bars.
	 * 
	 * @param event
	 * 
	 * @see MenacingMobsSpawner
	 */
	 @EventHandler
	 public void onMythicSpawn(MythicMobSpawnEvent event) {
		 MenacingMobsSpawner.addMenacingMob(event.getEntity(), event.getMobType());
	 }

	 /**
	  * Lets the mapper decide how to handle the death of a (mythic) mob.
	  * This includes exp rewards, pet deaths and modifier effects.
	  * 
	  * @param event
	  * 
	  * @see MobEventMapper#death(MythicMobDeathEvent)
	  */
	@EventHandler
	public void onMythicDeath(MythicMobDeathEvent event) {
		if(StringUtils.isNotBlank(event.getEntity().getCustomName())) {
			MobEventMapper.death(event);
		}
	}

// Inventory Listeners

	/**
	 * Lets the mapper decide how to handle the interaction with an inventory / menu.
	 * 
	 * @param event
	 * 
	 * @see EventMapper#handleMenuInteraction(InventoryClickEvent)
	 */
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		EventMapper.handleMenuInteraction(event);
	}

	/**
	 * Updates the scoreboard of an MMORPG player,
	 * in case they dropped an item that is relevant to a quest.
	 * Also prevents dropping of static items (e.g. mana points).
	 * 
	 * @param event
	 * 
	 * @see WauzPlayerScoreboard#scheduleScoreboard(Player)
	 * @see MenuUtils#checkForStaticItemDrop(PlayerDropItemEvent)
	 */
	@EventHandler
	public void onItemDrop(PlayerDropItemEvent event) {
		if(WauzMode.isMMORPG(event.getPlayer())) {
			MenuUtils.checkForStaticItemDrop(event);
			WauzPlayerScoreboard.scheduleScoreboard(event.getPlayer());
		}
	}

	/**
	 * Prevents swapping of static items (e.g. mana points) in MMORPG mode.
	 * 
	 * @param event
	 * 
	 * @see MenuUtils#checkForStaticItemSwap(PlayerSwapHandItemsEvent)
	 */
	@EventHandler
	public void onSwapItem(PlayerSwapHandItemsEvent event) {
		if(WauzMode.isMMORPG(event.getPlayer())) {
			MenuUtils.checkForStaticItemSwap(event);
		}
	}

// Projectile Movement Listeners

	/**
	 * Handles the mechanics of hooks in MMORPG mode.
	 * 
	 * @param event
	 * 
	 * @see CustomWeaponHook#use(ProjectileLaunchEvent)
	 */
	@EventHandler
	public void onHookLaunch(ProjectileLaunchEvent event) {
		if(WauzMode.isMMORPG(event.getEntity())) {
			if(event.getEntityType().equals(EntityType.FISHING_HOOK)) {
				CustomWeaponHook.use(event);
			}
		}
	}

	/**
	 * Handles the instant despawn of arrows in MMORPG mode.
	 * 
	 * @param event
	 * 
	 * @see CustomWeaponBow
	 */
	@EventHandler
	public void onArrowHit(ProjectileHitEvent event) {
		if(WauzMode.isMMORPG(event.getEntity())) {
			if(event.getEntityType().equals(EntityType.ARROW)) {
				event.getEntity().remove();
			}
		}
	}

	/**
	 * Handles glider mechanics in MMORPG mode, aswell as flying-permissions.
	 * Flying in every region, besides the hub, will be allowed in flying debug mode.
	 * 
	 * @param event
	 * 
	 * @see CustomWeaponGlider#glide(PlayerMoveEvent)
	 * @see WauzDebugger#toggleFlyingDebugMode(Player)
	 */
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if(WauzMode.inHub(player)) {
			if(player.isOnGround()) {
				event.getPlayer().setAllowFlight(true);
			}
		}
		else {
			event.getPlayer().setAllowFlight(player.hasPermission(WauzPermission.DEBUG_FLYING.toString())
					|| player.getGameMode().equals(GameMode.CREATIVE)
					|| player.getGameMode().equals(GameMode.SPECTATOR));
		}
		
		if(WauzMode.isMMORPG(player)) {
			if (player.getEquipment().getItemInMainHand().getType().equals(Material.FEATHER)) {
				CustomWeaponGlider.glide(event);
			}
			else {
				CustomWeaponGlider.dechick(event);
			}
		}
	}
	
// Block Protection Listeners
	
	/**
	 * Prevents changes to protected regions.
	 * 
	 * @param event
	 * 
	 * @see WauzRegion#disallowBlockChange(Block)
	 */
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event) {
		for(Block block : new ArrayList<>(event.blockList())) {
			if(WauzRegion.disallowBlockChange(block)) {
				event.blockList().remove(block);
			}
		}
	}
	
	/**
	 * Prevents changes to protected regions.
	 * 
	 * @param event
	 * 
	 * @see WauzRegion#disallowBlockChange(Block)
	 */
	@EventHandler
	public void onBlockChangeExplode(BlockExplodeEvent event) {
		if(WauzRegion.disallowBlockChange(event.getBlock())) {
			event.setCancelled(true);
		}
	}
	
	/**
	 * Prevents changes to protected regions.
	 * 
	 * @param event
	 * 
	 * @see WauzRegion#disallowBlockChange(Block)
	 */
	@EventHandler
	public void onBlockChangeFade(BlockFadeEvent event) {
		if(WauzRegion.disallowBlockChange(event.getBlock())) {
			event.setCancelled(true);
		}
	}
	
	/**
	 * Prevents changes to protected regions.
	 * 
	 * @param event
	 * 
	 * @see WauzRegion#disallowBlockChange(Block)
	 */
	@EventHandler
	public void onBlockChangeFertilize(BlockFertilizeEvent event) {
		if(WauzRegion.disallowBlockChange(event.getBlock())) {
			event.setCancelled(true);
		}
	}
	
	/**
	 * Prevents changes to protected regions.
	 * 
	 * @param event
	 * 
	 * @see WauzRegion#disallowBlockChange(Block)
	 */
	@EventHandler
	public void onBlockChangeGrow(BlockGrowEvent event) {
		if(WauzRegion.disallowBlockChange(event.getBlock())) {
			event.setCancelled(true);
		}
	}
	
	/**
	 * Prevents changes to protected regions.
	 * 
	 * @param event
	 * 
	 * @see WauzRegion#disallowBlockChange(Block)
	 */
	@EventHandler
	public void onBlockChangeIgnite(BlockIgniteEvent event) {
		if(WauzRegion.disallowBlockChange(event.getBlock())) {
			event.setCancelled(true);
		}
	}
	
	/**
	 * Prevents changes to protected regions.
	 * 
	 * @param event
	 * 
	 * @see WauzRegion#disallowBlockChange(Block)
	 */
	@EventHandler
	public void onBlockChangePistonExtend(BlockPistonExtendEvent event) {
		if(WauzRegion.disallowBlockChange(event.getBlock())) {
			event.setCancelled(true);
		}
	}
	
	/**
	 * Prevents changes to protected regions.
	 * 
	 * @param event
	 * 
	 * @see WauzRegion#disallowBlockChange(Block)
	 */
	@EventHandler
	public void onBlockChangePistonRetract(BlockPistonRetractEvent event) {
		if(WauzRegion.disallowBlockChange(event.getBlock())) {
			event.setCancelled(true);
		}
	}

}
