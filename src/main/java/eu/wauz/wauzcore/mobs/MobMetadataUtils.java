package eu.wauz.wauzcore.mobs;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import eu.wauz.wauzcore.WauzCore;

/**
 * An util class for reading and writing metadata from mobs.
 * 
 * @author Wauzmons
 */
public class MobMetadataUtils {
	
	/**
	 * Metadata that implies that the damage of the next damage event has a fixed value.
	 */
	private static final String FIXED_DAMAGE = "wzFixedDmg";
	
	/**
	 * Metadata that holds the magic attack multiplier for the next damage event.
	 */
	private static final String MAGIC_DAMAGE_MULTIPLIER = "wzMagic";
	
	/**
	 * Metadata that holds the type of an arrow, used to determine impact effects.
	 */
	private static final String ARROW_TYPE = "wzArrowType";
	
	/**
	 * Metadata that holds the base damage of an arrow, used to determine impact damage.
	 */
	private static final String ARROW_DAMAGE = "wzArrowDmg";
	
	/**
	 * Metadata that holds the bestiary entry name of a mob.
	 */
	private static final String BESTIARY_ENTRY = "wzEntry";
	
	/**
	 * Metadata that implies that a generic menacing modifier exists on a mob.
	 */
	private static final String MENACING_MODIFIER = "wzMod";
	
	/**
	 * Metadata that implies that a mob is a raid boss.
	 */
	private static final String RAID_BOSS = "wzBoss";
	
	/**
	 * Metadata that holds the tier of a strongbox, used to determine loot contents.
	 */
	private static final String STRONGBOX_TIER = "wzStrongboxTier";
	
	/**
	 * Metadata that holds the uuid of the strongbox, that a mob belongs to.
	 */
	private static final String STRONGBOX_MOB_UUID = "wzStrongbox";
	
	/**
	 * Metadata that holds the tier (max level) of an exp drop.
	 */
	private static final String DROP_EXP_TIER = "wzExpTier";
	
	/**
	 * Metadata that holds the amount of an exp drop.
	 */
	private static final String DROP_EXP_AMOUNT = "wzExpAmount";
	
	/**
	 * Metadata that holds the id of an instance key drop.
	 */
	private static final String DROP_KEY_ID = "wzKeyId";
	
	/**
	 * Sets if the damage value should be fixed, the next time the mob takes damage.
	 * 
	 * @param mob The mob that receives the metadata.
	 * @param enabled If the damage value should be fixed.
	 */
	public static void setFixedDamage(Entity mob, boolean enabled) {
		addMetadata(mob, FIXED_DAMAGE, enabled);
	}
	
	/**
	 * Gets if the damage value should be fixed, the next time the mob takes damage.
	 * 
	 * @param mob The mob that gets its metadata checked.
	 * 
	 * @return If the damage value should be fixed.
	 */
	public static boolean hasFixedDamage(Entity mob) {
		return mob.hasMetadata(FIXED_DAMAGE) && getMetadata(mob, FIXED_DAMAGE).asBoolean();
	}
	
	/**
	 * Sets the magic damage multiplier to apply, the next time the mob takes damage.
	 * 
	 * @param mob The mob that receives the metadata.
	 * @param multiplier The multiplier where 1 equals 100 percent, or 0 to disable.
	 */
	public static void setMagicDamageMultiplier(Entity mob, double multiplier) {
		addMetadata(mob, MAGIC_DAMAGE_MULTIPLIER, multiplier);
	}
	
	/**
	 * Gets the magic damage multiplier to apply, the next time the mob takes damage.
	 * 
	 * @param mob The mob that gets its metadata checked.
	 * 
	 * @return The multiplier where 1 equals 100 percent, or 0 if disabled.
	 */
	public static double getMagicDamageMultiplier(Entity mob) {
		return mob.hasMetadata(MAGIC_DAMAGE_MULTIPLIER) ? getMetadata(mob, MAGIC_DAMAGE_MULTIPLIER).asDouble() : 0;
	}
	
	/**
	 * Sets type and base damage of an arrow, to determine its effects and damage on impact.
	 * 
	 * @param arrow The arrow mob that receives the metadata.
	 * @param type The type of the arrow.
	 * @param damage The base damage the arrow deals.
	 */
	public static void setArrowTypeAndDamage(Arrow arrow, String type, int damage) {
		addMetadata(arrow, ARROW_TYPE, type);
		addMetadata(arrow, ARROW_DAMAGE, damage);
	}
	
	/**
	 * Checks if an arrow has type and base damage values set in its metadata.
	 * 
	 * @param arrow The arrow mob that gets its metadata checked.
	 * 
	 * @return If the mob has type and damage set.
	 */
	public static boolean hasArrowTypeAndDamage(Arrow arrow) {
		return arrow.hasMetadata(ARROW_TYPE) && arrow.hasMetadata(ARROW_DAMAGE);
	}
	
	/**
	 * Gets the type of an arrow from its metadata.
	 * 
	 * @param arrow The arrow mob that gets its metadata checked.
	 * 
	 * @return The type of the arrow.
	 */
	public static String getArrowType(Arrow arrow) {
		return getMetadata(arrow, ARROW_TYPE).asString();
	}
	
	/**
	 * Gets the base damage of an arrow from its metadata.
	 * 
	 * @param arrow The arrow mob that gets its metadata checked.
	 * 
	 * @return The base damage value of the arrow.
	 */
	public static int getArrowDamage(Arrow arrow) {
		return getMetadata(arrow, ARROW_DAMAGE).asInt();
	}
	
	/**
	 * Sets the bestiary entry name of the given mob mob.
	 * 
	 * @param mob The mob that receives the metadata.
	 * @param entry The full name of the bestiary entry.
	 */
	public static void setBestiaryEntry(Entity mob, String entry) {
		addMetadata(mob, BESTIARY_ENTRY, entry);
	}
	
	/**
	 * Checks if the given mob has a bestiary entry.
	 * 
	 * @param mob The mob that gets its metadata checked.
	 * 
	 * @return If the mob has a bestiary entry.
	 */
	public static boolean hasBestiaryEntry(Entity mob) {
		return mob.hasMetadata(BESTIARY_ENTRY);
	}
	
	/**
	 * Gets the bestiary entry name of the given mob mob.
	 * 
	 * @param mob The mob that gets its metadata checked.
	 * 
	 * @return The full name of the bestiary entry.
	 */
	public static String getBestiaryEntry(Entity mob) {
		return getMetadata(mob, BESTIARY_ENTRY).asString();
	}
	
	/**
	 * Sets the given menacing modifier on a mob to enabled.
	 * 
	 * @param mob The mob that receives the metadata.
	 * @param modifier The menacing modifier that the mob receives.
	 */
	public static void setMenacingModifier(Entity mob, MenacingModifier modifier) {
		addMetadata(mob, MENACING_MODIFIER + modifier, true);
	}
	
	/**
	 * Checks if the given menacing modifier on a mob is enabled.
	 * 
	 * @param mob The mob that gets its metadata checked.
	 * @param modifier The menacing modifier to look for.
	 * 
	 * @return If the mob has the given modifier.
	 */
	public static boolean hasMenacingModifier(Entity mob, MenacingModifier modifier) {
		return mob.hasMetadata(MENACING_MODIFIER + modifier);
	}
	
	/**
	 * Sets the raid boss flag on a mob.
	 * 
	 * @param mob The mob that receives the metadata.
	 * @param value If the mob is a raid boss.
	 */
	public static void setRaidBoss(Entity mob, boolean value) {
		if(value) {
			addMetadata(mob, RAID_BOSS, value);
		}
	}
	
	/**
	 * Checks if the raid boss flag on a mob is enabled.
	 * 
	 * @param mob The mob that gets its metadata checked.
	 * 
	 * @return If the mob is a raid boss.
	 */
	public static boolean isRaidBoss(Entity mob) {
		return mob.hasMetadata(RAID_BOSS);
	}
	
	/**
	 * Sets the tier of a strongobx, used to determine loot contents.
	 * 
	 * @param strongbox The strongbox mob that receives the metadata.
	 * @param strongboxTier The tier of the strongbox.
	 */
	public static void setStrongboxTier(ArmorStand strongbox, int strongboxTier) {
		addMetadata(strongbox, STRONGBOX_TIER, strongboxTier);
	}
	
	/**
	 * Gets the tier of a strongobx, used to determine loot contents.
	 * 
	 * @param strongbox The strongbox mob that gets its metadata checked.
	 * 
	 * @return The tier of the strongobx.
	 */
	public static int getStrongboxTier(ArmorStand strongbox) {
		return getMetadata(strongbox, STRONGBOX_TIER).asInt();
	}
	
	/**
	 * Sets the uuid of the strongbox, the given mob belongs to.
	 * 
	 * @param mob The mob that receives the metadata.
	 * @param strongbox The strongbox mob to which the mob belongs.
	 */
	public static void setStrongboxMob(Entity mob, ArmorStand strongbox) {
		addMetadata(mob, STRONGBOX_MOB_UUID, strongbox.getUniqueId().toString());
	}
	
	/**
	 * Checks if the given mob belongs to a strongbox.
	 * 
	 * @param mob The mob that gets its metadata checked.
	 * 
	 * @return If the mob belongs to a strongbox.
	 */
	public static boolean hasStrongboxMob(Entity mob) {
		return mob.hasMetadata(STRONGBOX_MOB_UUID);
	}
	
	/**
	 * Gets the uuid of the strongbox, the given mob belongs to.
	 * 
	 * @param mob The mob that gets its metadata checked.
	 * 
	 * @return The uuid of the strongbox, the mob belongs to.
	 */
	public static String getStronboxMob(Entity mob) {
		return getMetadata(mob, STRONGBOX_MOB_UUID).asString();
	}
	
	/**
	 * Sets the exp to drop, after the mob gets killed.
	 * 
	 * @param mob The mob that receives the metadata.
	 * @param expTier The tier (max level) of the exp to drop.
	 * @param expAmount The amount of exp to drop.
	 */
	public static void setExpDrop(Entity mob, int expTier, double expAmount) {
		addMetadata(mob, DROP_EXP_TIER, expTier);
		addMetadata(mob, DROP_EXP_AMOUNT, expAmount);
	}
	
	/**
	 * Checks for an exp drop, after the mob gets killed.
	 * 
	 * @param mob The mob that gets its metadata checked.
	 * 
	 * @return If the mob drops exp.
	 */
	public static boolean hasExpDrop(Entity mob) {
		return mob.hasMetadata(DROP_EXP_TIER) && mob.hasMetadata(DROP_EXP_AMOUNT);
	}
	
	/**
	 * Gets the tier (max level) of the exp to drop, after the mob gets killed.
	 * 
	 * @param mob The mob that gets its metadata checked.
	 * 
	 * @return The tier of the exp dropped.
	 */
	public static int getExpDropTier(Entity mob) {
		return getMetadata(mob, DROP_EXP_TIER).asInt();
	}
	
	/**
	 * Gets the exp to drop, after the mob gets killed.
	 * 
	 * @param mob The mob that gets its metadata checked.
	 * 
	 * @return The amount of exp dropped.
	 */
	public static double getExpDropAmount(Entity mob) {
		return getMetadata(mob, DROP_EXP_AMOUNT).asDouble();
	}
	
	/**
	 * Sets the key to drop, after the mob gets killed.
	 * 
	 * @param mob The mob that receives the metadata.
	 * @param keyId The id of the instance key to drop.
	 */
	public static void setKeyDrop(Entity mob, String keyId) {
		addMetadata(mob, DROP_KEY_ID, keyId);
	}
	
	/**
	 * Checks for a key drop, after the mob gets killed.
	 * 
	 * @param mob The mob that gets its metadata checked.
	 * 
	 * @return If the mob drops a key.
	 */
	public static boolean hasKeyDrop(Entity mob) {
		return mob.hasMetadata(DROP_KEY_ID);
	}
	
	/**
	 * Gets the key drop, after the mob gets killed.
	 * 
	 * @param mob The mob that gets its metadata checked.
	 * 
	 * @return The id of the dropped key.
	 */
	public static String getKeyDrop(Entity mob) {
		return getMetadata(mob, DROP_KEY_ID).asString();
	}
	
	/**
	 * Sets a metadata value in the given mob's metadata store.
	 * 
	 * @param mob The mob that receives the metadata.
	 * @param key The key under which the metadata is stored.
	 * @param value The value of the metadata.
	 */
	private static void addMetadata(Entity mob, String key, Object value) {
		mob.setMetadata(key, new FixedMetadataValue(WauzCore.getInstance(), value));
	}
	
	/**
	 * Gets a metadata value from the given mob's metadata store.
	 * 
	 * @param mob The mob that gets its metadata checked.
	 * @param key The key under which the metadata is stored.
	 * 
	 * @return The value of the metadata.
	 */
	private static MetadataValue getMetadata(Entity mob, String key) {
		return mob.getMetadata(key).get(0);
	}
	
}
