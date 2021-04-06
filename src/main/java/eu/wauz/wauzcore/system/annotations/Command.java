package eu.wauz.wauzcore.system.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.bukkit.Bukkit;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.commands.completion.TabCompleterEnhancements;
import eu.wauz.wauzcore.commands.completion.TabCompleterEquip;
import eu.wauz.wauzcore.commands.completion.TabCompleterGuilds;
import eu.wauz.wauzcore.commands.completion.TabCompleterInstances;
import eu.wauz.wauzcore.commands.completion.TabCompleterMenus;
import eu.wauz.wauzcore.commands.completion.TabCompleterMinigames;
import eu.wauz.wauzcore.commands.completion.TabCompleterPetAbilities;
import eu.wauz.wauzcore.commands.completion.TabCompleterPets;
import eu.wauz.wauzcore.commands.completion.TabCompleterRanks;
import eu.wauz.wauzcore.commands.completion.TabCompleterRunes;
import eu.wauz.wauzcore.commands.completion.TabCompleterSkills;
import eu.wauz.wauzcore.commands.completion.TabCompleterWaypoints;
import eu.wauz.wauzcore.commands.completion.TabCompleterWorlds;
import eu.wauz.wauzcore.commands.execution.WauzCommand;
import eu.wauz.wauzcore.commands.execution.WauzCommandExecutor;

/**
 * An annotation to mark classes as commands, to automatically register them on startup.
 * 
 * @author Wauzmons
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Command {
	
	/**
	 * An utility to help registering commands.
	 * 
	 * @author Wauzmons
	 */
	public static class CommandAnnotationHelper {
		
		/**
		 * Registers instances of all classes, annotated with this annotation.
		 * 
		 * @param loader The annotation loader to use.
		 * 
		 * @throws Exception Failed to load a class.
		 */
		public static void init(AnnotationLoader loader) throws Exception {
			int count = 0;
			for(Class<?> clazz : loader.getAnnotatedClasses(Command.class)) {
				Object object = clazz.getDeclaredConstructor().newInstance();
				WauzCommandExecutor.registerCommand((WauzCommand) object);
				count++;
			}
			addCompleters();
			WauzCore.getInstance().getLogger().info("Loaded " + count + " Commands!");
		}
		
		/**
		 * Initializes all predefined command completers.
		 */
		private static void addCompleters() {
			Bukkit.getPluginCommand("menu").setTabCompleter(new TabCompleterMenus());
			Bukkit.getPluginCommand("apply").setTabCompleter(new TabCompleterGuilds());
			Bukkit.getPluginCommand("wzEnter").setTabCompleter(new TabCompleterInstances());
			Bukkit.getPluginCommand("wzEnter.dev").setTabCompleter(new TabCompleterWorlds());
			Bukkit.getPluginCommand("wzGetEquip").setTabCompleter(new TabCompleterEquip());
			Bukkit.getPluginCommand("wzGetEquip.enhanced").setTabCompleter(new TabCompleterEnhancements());
			Bukkit.getPluginCommand("wzGetPet").setTabCompleter(new TabCompleterPets());
			Bukkit.getPluginCommand("wzGetPet.ability").setTabCompleter(new TabCompleterPetAbilities());
			Bukkit.getPluginCommand("wzGetRune").setTabCompleter(new TabCompleterRunes());
			Bukkit.getPluginCommand("wzRank").setTabCompleter(new TabCompleterRanks());
			Bukkit.getPluginCommand("wzSkill").setTabCompleter(new TabCompleterSkills());
			Bukkit.getPluginCommand("wzSkill.weapon").setTabCompleter(new TabCompleterSkills());
			Bukkit.getPluginCommand("wzStart").setTabCompleter(new TabCompleterMinigames());
			Bukkit.getPluginCommand("wzTravel").setTabCompleter(new TabCompleterWaypoints());
		}
		
	}
	
}
