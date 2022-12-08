package eu.wauz.wauzcore.system.util;

import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.system.WauzDebugger;
import io.lumine.mythic.api.drops.DropMetadata;
import io.lumine.mythic.api.exceptions.InvalidMobTypeException;
import io.lumine.mythic.api.items.ItemManager;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.api.skills.SkillCaster;
import io.lumine.mythic.bukkit.BukkitAPIHelper;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.bukkit.adapters.BukkitItemStack;
import io.lumine.mythic.core.drops.DropExecutor;
import io.lumine.mythic.core.drops.DropMetadataImpl;
import io.lumine.mythic.core.drops.DropTable;
import io.lumine.mythic.core.drops.LootBag;
import io.lumine.mythic.core.items.MythicItem;

/**
 * An util class to interact with the MythicMobs API.
 * 
 * @author Wauzmons
 */
public class MythicUtils {
	
	/**
	 * Access to the MythicMobs API Helper.
	 */
	private static BukkitAPIHelper mythicMobs = MythicBukkit.inst().getAPIHelper();
	
	private static DropExecutor dropManager = MythicBukkit.inst().getDropManager();
	
	private static ItemManager itemManager = MythicBukkit.inst().getItemManager();
	
	/**
	 * Spawns a mob at the given location.
	 * 
	 * @param type The type of the mob.
	 * @param location The location to spawn the mob at.
	 * @param source What spawned the mob. (Logging)
	 * 
	 * @return The spawned entity. Is null when mob type is invalid.
	 */
	public static Entity spawnMob(String type, Location location, String source) {
		try {
			return mythicMobs.spawnMythicMob(type, location);
		}
		catch (InvalidMobTypeException e) {
			WauzDebugger.log("Invalid MythicMob from \"" + source + "\": " + type);
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Gets a mythic mob.
	 * 
	 * @param type The type of the mob.
	 * 
	 * @return The mythic mob.
	 */
	public static MythicMob getMob(String type) {
		return MythicBukkit.inst().getAPIHelper().getMythicMob(type);
	}
	
	/**
	 * Increases the target's threat, so the mob attacks it.
	 * 
	 * @param mob The mob that is switching targets.
	 * @param target The new target for the mob to attack.
	 */
	public static void taunt(Entity mob, LivingEntity target) {
		mythicMobs.taunt(mob, target);
	}
	
	
	/**
	 * Drops items from a drop table at the given location.
	 * 
	 * @param dropTableName The name of the drop table.
	 * @param location The location to drop the items at.
	 * @param dropper The entity that dropped the items.
	 * @param entity The entity that caused the drop.
	 * @param source What spawned the drops. (Logging)
	 */
	public static void drop(String dropTableName, Location location, SkillCaster dropper, Entity entity, String source) {
		DropMetadata metadata = new DropMetadataImpl(dropper, BukkitAdapter.adapt(entity));
		drop(dropTableName, location, metadata, source);
	}
	
	/**
	 * Drops items from a drop table at the given location.
	 * 
	 * @param dropTableName The name of the drop table.
	 * @param location The location to drop the items at.
	 * @param metadata The metadata of the drop.
	 * @param source What spawned the drops. (Logging)
	 */
	public static void drop(String dropTableName, Location location, DropMetadata metadata, String source) {
		Optional<DropTable> dropTableOptional = dropManager.getDropTable(dropTableName);
		if(dropTableOptional.isPresent()) {
			LootBag lootBag = dropTableOptional.get().generate();
			lootBag.drop(BukkitAdapter.adapt(location));
		}
		else {
			WauzDebugger.log("Invalid MythicMobs DropTable from \"" + source + "\": " + dropTableName);
		}
	}
	
	/**
	 * Gets an instanced item stack.
	 * 
	 * @param itemName The name of the item.
	 * @param source What spawned the item. (Logging)
	 * 
	 * @return The item stack. Is null when item name is invalid.
	 */
	public static ItemStack getItemStack(String itemName, String source) {
		Optional<MythicItem> mythicItemOptional = itemManager.getItem(itemName);
		if(mythicItemOptional.isPresent()) {
			MythicItem mythicItem = mythicItemOptional.get();
			BukkitItemStack bukkitItemStack = (BukkitItemStack) mythicItem.generateItemStack(mythicItem.getAmount());
			return bukkitItemStack.build();
		}
		else {
			WauzDebugger.log("Invalid MythicMobs Item from \"" + source + "\": " + itemName);
			return null;
		}
	}
	
}
