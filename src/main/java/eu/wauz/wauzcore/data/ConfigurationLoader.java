package eu.wauz.wauzcore.data;

import eu.wauz.wauzcore.items.WauzEquipment;
import eu.wauz.wauzcore.mobs.bestiary.WauzBestiarySpecies;
import eu.wauz.wauzcore.mobs.citizens.WauzCitizen;
import eu.wauz.wauzcore.mobs.pets.WauzPet;
import eu.wauz.wauzcore.oneblock.OnePhase;
import eu.wauz.wauzcore.players.WauzPlayerGuild;
import eu.wauz.wauzcore.professions.WauzResource;
import eu.wauz.wauzcore.professions.crafting.WauzCraftingRecipes;
import eu.wauz.wauzcore.system.WauzRank;
import eu.wauz.wauzcore.system.WauzRegion;
import eu.wauz.wauzcore.system.WauzTitle;
import eu.wauz.wauzcore.system.WauzWaypoint;
import eu.wauz.wauzcore.system.achievements.WauzAchievement;
import eu.wauz.wauzcore.system.api.StatisticsFetcher;
import eu.wauz.wauzcore.system.economy.WauzCurrency;
import eu.wauz.wauzcore.system.economy.WauzShop;
import eu.wauz.wauzcore.system.quests.WauzQuest;
import eu.wauz.wauzcore.worlds.instances.InstanceManager;
import eu.wauz.wauzcore.worlds.instances.WauzInstance;

/**
 * Used to load all static data from files.
 * 
 * @author Wauzmons
 */
public class ConfigurationLoader {
	
	/**
	 * Calls all other methods for loading and initializing data.
	 * Also recalculates statistics and removes inactive instances.
	 * Only called once per server run.
	 * 
	 * @see InstanceManager#removeInactiveInstances()
	 * @see StatisticsFetcher#calculate()
	 */
	public static void init() {
		WauzRegion.init();
		WauzWaypoint.init();
		WauzEquipment.init();
		WauzCurrency.init();
		WauzAchievement.init();
		WauzShop.init();
		WauzQuest.init();
		WauzResource.init();
		WauzCraftingRecipes.init();
		WauzBestiarySpecies.init();
		WauzCitizen.init();
		WauzPet.init();
		WauzInstance.init();
		WauzRank.init();
		WauzTitle.init();
		WauzPlayerGuild.init();
		OnePhase.init();
		
		InstanceManager.removeInactiveInstances();
		StatisticsFetcher.calculate();
	}
	
}
