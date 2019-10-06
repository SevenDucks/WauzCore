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
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
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
import org.bukkit.inventory.Inventory;
import org.spigotmc.event.entity.EntityMountEvent;

import eu.wauz.wauzcore.events.ArmorEquipEvent;
import eu.wauz.wauzcore.events.ArmorEquipEventListener;
import eu.wauz.wauzcore.items.CustomWeaponBow;
import eu.wauz.wauzcore.items.DurabilityCalculator;
import eu.wauz.wauzcore.items.Equipment;
import eu.wauz.wauzcore.items.WauzRewards;
import eu.wauz.wauzcore.items.WauzSigns;
import eu.wauz.wauzcore.items.dungeon.DungeonItemChickenGlider;
import eu.wauz.wauzcore.items.dungeon.DungeonItemGrapplingHook;
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
import eu.wauz.wauzcore.system.WauzNoteBlockPlayer;
import eu.wauz.wauzcore.system.WauzRegion;
import eu.wauz.wauzcore.system.commands.WauzDebugger;
import eu.wauz.wauzcore.system.nms.WauzNmsMinimap;
import eu.wauz.wauzcore.system.util.WauzMode;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobSpawnEvent;
import net.md_5.bungee.api.ChatColor;

/**
 * This is the Class, where most Bukkit Events are caught
 * and modified to hold WauzCore Data instead of normal Minecraft Stuff.
 * 
 * @author Wauzmons
 */
public class WauzListener implements Listener {
	
	/**
	 * Storage for Player Names, to greet them in the MotD.
	 */
	private Map<InetAddress, String> addressNameMap = new HashMap<>();
	
	/**
	 * Gets the Player's Name, to greet them in the MotD.
	 * 
	 * @param address IP of the Player.
	 * @return Name of the Player or "Hero" if unknown.
	 */
	private String getNameFromAddress(InetAddress address) {
		String name = addressNameMap.get(address);
		return name == null ? "Hero" : name;
	}

// Player Interaction Listeners
	
	/**
	 * Responds to the Players Ping with a Custom MotD.
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
	 * Logs the Player into the Game.
	 * Denies Access if the Player is banned, not whitelisted
	 * or if the Server is full.
	 * TODO Test how online Players are counted.
	 * 
	 * @param event
	 * 
	 * @see WauzPlayerRegistrator#login(Player)
	 */
	@EventHandler
	public void onLogin(PlayerLoginEvent event) throws Exception {
		Player player = event.getPlayer();
		addressNameMap.put(event.getAddress(), player.getName());
		if(Bukkit.getOnlinePlayers().size() >= Bukkit.getMaxPlayers()) {
			event.setResult(Result.KICK_FULL);
		}
		if(Bukkit.hasWhitelist() && !player.isWhitelisted()) {
			event.setResult(Result.KICK_WHITELIST);
		}
		if(player.isBanned()) {
			event.setResult(Result.KICK_BANNED);
		}
		else {
			event.setResult(Result.ALLOWED);
			WauzPlayerRegistrator.login(player);
		}
	}

	/**
	 * Logs the Player out of the Game.
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
	 * Lets the Player automatically respawn on Death.
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
	 * Formats the Chat Message of the Player.
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
	 * Lets the Mapper decide how to handle the Interaction with an Entity.
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
	 * Lets the Mapper decide how to handle the Interaction with an Object.
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
	 * Updates the Scoreboard of an MMORPG Player,
	 * in case they picked up an Item that is relevant to a Quest.
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
	 * Reads Food Stats from the consumed Item,
	 * to apply all relevant Effects to the Player.
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
	 * Changes the Armor Set of a Player,
	 * but only if they meet the Equip Requirements.
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
	 * Prevents MMORPG Players to modify Armor Stands,
	 * because they are used as Displays for Damage Numbers and more.
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
	 * Starts a Double Jump if a Player tries to fly in the Hub.
	 * This is used for faster Travelling and Exploration.
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
	 * Prevents Players to ride on Mounts that belong to someone else.
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
	 * Prevents that the Spawn is loaded if a new World is initialized.
	 * This prevents lag on entering Instances.
	 * 
	 * @param event
	 */
	@EventHandler
	public void onWorldInit(WorldInitEvent event) {
		event.getWorld().setKeepSpawnInMemory(false);
	}

	/**
	 * Reloads most Custom UI if the Player changes their current World.
	 * This assures that the correct Scoreboard, Minimap etc. are shown.
	 * Also checks for a new Music Track.
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
	 * Prevents Players to break blocks in certain Regions.
	 * 
	 * @param event
	 * 
	 * @see WauzRegion#disallowBuild(Block)
	 */
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if(!player.hasPermission("wauz.debug.building") && WauzRegion.disallowBuild(event.getBlock())) {
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED + "You can't build here! Find another spot!");
		}
	}
	
	/**
	 * Prevents Players to place blocks in certain Regions.
	 * 
	 * @param event
	 * 
	 * @see WauzRegion#disallowBuild(Block)
	 */
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if(!player.hasPermission("wauz.debug.building") && WauzRegion.disallowBuild(event.getBlock())) {
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED + "You can't build here! Find another spot!");
		}
	}

	/**
	 * Checks if the Player just created a Sign that is bound to an Event.
	 * 
	 * @param event
	 * 
	 * @see WauzSigns#create(SignChangeEvent)
	 */
	@EventHandler
	public void onSignChange(SignChangeEvent event) {
		if(WauzMode.isMMORPG(event.getPlayer())) {
			WauzSigns.create(event);
		}
	}
	
	/**
	 * Rewards Tokens if a Survival Player exceeded the Maximum Level.
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
	 * Prevents MMORPG Players from learning normal Minecraft Recipes.
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
	 * Prevents Players from loosing Saturation in the Hub.
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
	 * TODO Write Javadoc
	 * 
	 * @param event
	 */
	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if(WauzMode.inHub(event.getEntity()) || PetOverviewMenu.getOwner(event.getEntity()) != null) {
			event.setCancelled(true);
			return;
		}
		if(event.getCause().equals(DamageCause.BLOCK_EXPLOSION)) {
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
			if(!WauzMode.isMMORPG(event.getEntity())) {
				return;
			}
			if(entityEvent.getDamager() instanceof Player) {
				DamageCalculator.attack(entityEvent);
				if(playerBossBar != null)
					playerBossBar.addPlayer((Player) entityEvent.getDamager(), entityEvent.getDamage());
			}
			if(event.getEntity() instanceof Player) {
				DamageCalculator.reflect(entityEvent);
			}
		}
		if(event.getEntity() instanceof Player && WauzMode.isMMORPG(event.getEntity())) {
			event.setCancelled(true);
			DungeonItemChickenGlider.cancelFallDamage(event);
			DamageCalculator.defend(event);
		}
		DamageCalculator.removeDamageModifiers(event);
		if(playerBossBar != null) {
			playerBossBar.updateBossBar(event.getDamage());
		}
	}

	/**
	 * Handles Healing Calculation in MMORPG Mode.
	 * Also Updates the Boss Bar if one exists.
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
	 * Handles the Killing of Entities in MMORPG Mode.
	 * This Handler focuses on Effects like Life Leech caused by the Player,
	 * not on the actual Death of the Entity.
	 * 
	 * @param event
	 * 
	 * @see DamageCalculator#kill(EntityDeathEvent)
	 * @see WauzListener#onMythicDeath(MythicMobDeathEvent) Real Death Event Handler
	 */
	@EventHandler
	public void onKill(EntityDeathEvent event) {
		if(event.getEntity().getKiller() != null && WauzMode.isMMORPG(event.getEntity().getKiller())) {
			DamageCalculator.kill(event);
		}
	}
	
	/**
	 * Prevents Item Damage in MMORPG Mode.
	 * The Equipment there has its own Durability System.
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
	 * Handles the Spawn of a (Mythic) Mob.
	 * This includes the Initialization of Modifiers, Loot and Boss Bars.
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
	  * Lets the Mapper decide how to handle the Death of a (Mythic) Mob.
	  * This includes Exp Rewards, Pet Deaths and Modifier Effects.
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
	 * Logs what Inventory the Player opened.
	 * 
	 * The original Idea was to construct/dispose Custom Items in the Players normal Inventory.
	 * This cannot work because the Crafting Inventory is only opened on Client Side.
	 * 
	 * @param event
	 */
	@EventHandler
	public void onInventoryOpen(InventoryOpenEvent event) {
		Player player = (Player) event.getPlayer();
		Inventory inventory = event.getInventory();
		WauzDebugger.log(player, "Opened Inventory  " + inventory.getType().toString());
		if(WauzMode.isMMORPG(player) && inventory.getType().equals(InventoryType.CRAFTING)) {
			MenuUtils.constructPlayerInventory(event);
		}
	}
	
	/**
	 * Logs what Inventory the Player closed.
	 * 
	 * The original Idea was to construct/dispose Custom Items in the Players normal Inventory.
	 * This cannot work because the Crafting Inventory is only opened on Client Side.
	 * 
	 * @param event
	 */
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		Inventory inventory = event.getInventory();
		WauzDebugger.log(player, "Closed Inventory " + inventory.getType().toString());
		if(WauzMode.isMMORPG(player) && inventory.getType().equals(InventoryType.CRAFTING)) {
			MenuUtils.disposePlayerInventory(event);
		}
	}

	/**
	 * Lets the Mapper decide how to handle the Interaction with an Inventory / Menu.
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
	 * Updates the Scoreboard of an MMORPG Player,
	 * in case they dropped an Item that is relevant to a Quest.
	 * Also prevents Dropping of static Items (e.g. Mana Points).
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
	 * Prevents Swapping of static Items (e.g. Mana Points) in MMORPG Mode.
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
	 * Handles the Mechanics of Hooks in MMORPG Mode.
	 * 
	 * @param event
	 * 
	 * @see DungeonItemGrapplingHook#use(ProjectileLaunchEvent)
	 */
	@EventHandler
	public void onHookLaunch(ProjectileLaunchEvent event) {
		if(WauzMode.isMMORPG(event.getEntity())) {
			if(event.getEntityType().equals(EntityType.FISHING_HOOK)) {
				DungeonItemGrapplingHook.use(event);
			}
		}
	}

	/**
	 * Handles the instant Despawn of Arrows in MMORPG Mode.
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
	 * Handles Glider Mechanics in MMORPG Mode, aswell as flying Permissions.
	 * 
	 * @param event
	 * 
	 * @see DungeonItemChickenGlider#glide(PlayerMoveEvent)
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
			boolean creative = player.getGameMode().equals(GameMode.CREATIVE);
			boolean spectate = player.getGameMode().equals(GameMode.SPECTATOR);
			event.getPlayer().setAllowFlight(creative || spectate);
		}
		
		if(WauzMode.isMMORPG(player)) {
			if (player.getEquipment().getItemInMainHand().getType().equals(Material.FEATHER)) {
				DungeonItemChickenGlider.glide(event);
			}
			else {
				DungeonItemChickenGlider.dechick(event);
			}
		}
	}
	
// Block Protection Listeners
	
	/**
	 * Prevents Changes in protected Regions.
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
	 * Prevents Changes in protected Regions.
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
	 * Prevents Changes in protected Regions.
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
	 * Prevents Changes in protected Regions.
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
	 * Prevents Changes in protected Regions.
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
	 * Prevents Changes in protected Regions.
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
	 * Prevents Changes in protected Regions.
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
	 * Prevents Changes in protected Regions.
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
