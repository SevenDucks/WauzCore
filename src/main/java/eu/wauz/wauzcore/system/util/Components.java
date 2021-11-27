package eu.wauz.wauzcore.system.util;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.skills.particles.Colors;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.Title.Times;

/**
 * Util class for interacting with adventure components.
 * 
 * @author Wauzmons
 */
public class Components {
	
	/**
	 * Gets the title of the given inventory.
	 * 
	 * @param inventory The inventory to get the title from.
	 * 
	 * @return The title.
	 */
	public static String inventoryTitle(InventoryView inventory) {
		return fromComponent(inventory.title());
	}

	/**
	 * Creates a custom inventory instance.
	 * 
	 * @param inventory The inventory to instantiate.
	 * @param title The title of the inventory instance.
	 * @param slots The slots of the inventory instance.
	 * 
	 * @return The created inventory.
	 */
	public static Inventory inventory(WauzInventory inventory, String title, int slots) {
		return Bukkit.createInventory(new WauzInventoryHolder(inventory), slots, toComponent(title));
	}
	
	/**
	 * Kicks a player and shows them the given message.
	 * 
	 * @param player The player to kick.
	 * @param message The message to show to the player.
	 */
	public static void kick(Player player, String message) {
		player.kick(toComponent(message));
	}
	
	/**
	 * Sets the kick message of the given async login event.
	 * 
	 * @param event The login event.
	 * @param message The kick message to set.
	 */
	public static void kickMessage(AsyncPlayerPreLoginEvent event, String message) {
		event.kickMessage(toComponent(message));
	}
	
	/**
	 * Sets the screen title of the given player.
	 * 
	 * @param player The player to set the title for.
	 * @param title The title text to set.
	 * @param subtitle The subtitle text to set.
	 */
	public static void title(Player player, String title, String subtitle) {
		Times times = Times.of(Duration.ofMillis(500), Duration.ofMillis(3500), Duration.ofMillis(1000));
		player.showTitle(Title.title(toComponent(title), toComponent(subtitle), times));
	}
	
	/**
	 * Sets the screen title of the given player.
	 * 
	 * @param player The player to set the title for.
	 * @param title The title text to set.
	 * @param subtitle The subtitle text to set.
	 */
	public static void titleShort(Player player, String title, String subtitle) {
		Times times = Times.of(Duration.ofMillis(100), Duration.ofMillis(800), Duration.ofMillis(200));
		player.showTitle(Title.title(toComponent(title), toComponent(subtitle), times));
	}
	
	/**
	 * Sets the action bar of the given player.
	 * 
	 * @param player The player to set the action bar for.
	 * @param actionBar The action bar to set.
	 */
	public static void actionBar(Player player, String actionBar) {
		player.sendActionBar(toComponent(actionBar));
	}
	
	/**
	 * Sets the player list header and footer for the given player.
	 * 
	 * @param player The player to set the player list header and footer for.
	 * @param header The header to set.
	 * @param footer The footer to set.
	 */
	public static void playerListHeaderAndFooter(Player player, String header, String footer) {
		player.sendPlayerListHeaderAndFooter(toComponent(header), toComponent(footer));
	}
	
	/**
	 * Sets the team prefix and color for the given team.
	 * 
	 * @param team The team to set the team prefix and color for.
	 * @param prefix The prefix to set.
	 * @param color The color to set.
	 */
	public static void teamPrefixAndColor(Team team, String prefix, ChatColor color) {
		team.prefix(toComponent(prefix));
		team.color(Colors.getByChatColor(color));
	}
	
	/**
	 * Registers a new objective on the given scoreboard.
	 * 
	 * @param scoreboard The scoreboard to register the objective on.
	 * @param name The name of the objective.
	 * @param criteria The criteria of the objective.
	 * @param displayName The display name of the objective.
	 * 
	 * @return The created objective.
	 */
	public static Objective objective(Scoreboard scoreboard, String name, String criteria, String displayName) {
		return scoreboard.registerNewObjective(name, criteria, toComponent(displayName));
	}
	
	/**
	 * Gets the display name of the given item meta.
	 * 
	 * @param itemMeta The item meta to get the display name from.
	 * 
	 * @return The display name.
	 */
	public static String displayName(ItemMeta itemMeta) {
		return fromComponent(itemMeta.displayName());
	}
	
	/**
	 * Sets the display name of the given item meta.
	 * 
	 * @param itemMeta The item meta to set the display name for.
	 * @param displayName The display name to set.
	 */
	public static void displayName(ItemMeta itemMeta, String displayName) {
		itemMeta.displayName(toComponent(displayName));
	}
	
	/**
	 * Gets the lore of the given item meta.
	 * 
	 * @param itemMeta The item meta to get the lore from.
	 * 
	 * @return The lore.
	 */
	public static List<String> lore(ItemMeta itemMeta) {
		return itemMeta.lore().stream()
				.map(lore -> fromComponent(lore))
				.collect(Collectors.toList());
	}
	
	/**
	 * Sets the lore of the given item meta.
	 * 
	 * @param itemMeta The item meta to set the lore for.
	 * @param loreLines The lore to set.
	 */
	public static void lore(ItemMeta itemMeta, List<String> loreLines) {
		itemMeta.lore(loreLines.stream()
				.map(lore -> toComponent(lore))
				.collect(Collectors.toList()));
	}
	
	/**
	 * Gets the line of the given sign.
	 * 
	 * @param sign The sign to get the line from.
	 * @param index The index of the line.
	 * 
	 * @return The line.
	 */
	public static String line(Sign sign, int index) {
		return fromComponent(sign.line(index));
	}
	
	/**
	 * Sets the line of the given sign.
	 * 
	 * @param sign The sign to set the line for.
	 * @param index The index of the line.
	 * @param line The line to set.
	 */
	public static void line(Sign sign, int index, String line) {
		sign.line(index, toComponent(line));
	}
	
	/**
	 * Gets the lines of the given sign event.
	 * 
	 * @param event The sign event to get the lines from.
	 * 
	 * @return The lines.
	 */
	public static List<String> lines(SignChangeEvent event) {
		return event.lines().stream()
				.map(line -> fromComponent(line))
				.collect(Collectors.toList());
	}
	
	/**
	 * Sets the lines of the given sign event.
	 * 
	 * @param event The sign event to set the lines for.
	 * @param lines The lines to set.
	 */
	public static void lines(SignChangeEvent event, List<String> lines) {
		for(int index = 0; index < lines.size(); index++) {
			event.line(index, toComponent(lines.get(index)));
		}
	}
	
	/**
	 * Sets the modt of the given ping event.
	 * 
	 * @param event The ping event to set the motd for.
	 * @param motd The motd to set.
	 */
	public static void motd(ServerListPingEvent event, String motd) {
		event.motd(toComponent(motd));
	}
	
	/**
	 * Gets the message of the given chat event.
	 * 
	 * @param event The chat event to get the message from.
	 * 
	 * @return The message.
	 */
	public static String message(AsyncChatEvent event) {
		return fromComponent(event.message());
	}
	
	/**
	 * Creates an item component from the given values.
	 * 
	 * @param message The message that should be shown.
	 * @param itemStack The item stack that should be shown.
	 * 
	 * @return The created component.
	 */
	public static TextComponent itemComponent(String message, ItemStack itemStack) {
		return toComponent(message)
				.append(toComponent(ChatColor.WHITE + " " + ItemUtils.getDisplayName(itemStack))
				.hoverEvent(itemStack));
	}
	
	/**
	 * Creates a command component from the given values.
	 * 
	 * @param message The message that should be shown.
	 * @param command The command that should be executed on click.
	 * 
	 * @return The created component.
	 */
	public static TextComponent commandComponent(String message, String command) {
		return toComponent(message)
				.append(toComponent(ChatColor.AQUA + " Click Here")
				.hoverEvent(HoverEvent.showText(toComponent("Run Command")))
				.clickEvent(ClickEvent.runCommand("/" + command)));
	}
	
	/**
	 * Creates a hyperlink component from the given values.
	 * 
	 * @param message The message that should be shown.
	 * @param url The url that should be opened on click.
	 * 
	 * @return The created component.
	 */
	public static TextComponent hyperlinkComponent(String message, String url) {
		return toComponent(message)
				.append(toComponent(ChatColor.AQUA + " Click Here")
				.hoverEvent(HoverEvent.showText(toComponent("Open URL")))
				.clickEvent(ClickEvent.openUrl(url)));
	}
	
	/**
	 * Sends a message to everyone on the server.
	 * 
	 * @param message The message to send.
	 */
	public static void broadcastLocally(String message) {
		broadcastLocally(toComponent(message));
	}
	
	/**
	 * Sends a component to everyone on the server.
	 * 
	 * @param component The component to send.
	 */
	public static void broadcastLocally(Component component) {
		Bukkit.getServer().broadcast(component);
	}
	
	/**
	 * Sends a message to everyone on the network.
	 * 
	 * @param player The player to send the message.
	 * @param message The message to send.
	 */
	public static void broadcastGlobally(Player player, String message) {
		broadcastGlobally(player, toComponent(message));
	}
	
	/**
	 * Sends a component to everyone on the network.
	 * 
	 * @param player The player to send the message.
	 * @param component The component to send.
	 */
	public static void broadcastGlobally(Player player, Component component) {
		BungeeUtils.broadcast(player, component);
	}
	
	/**
	 * Converts the given text to a component.
	 * 
	 * @param text The text to convert.
	 * 
	 * @return The created component.
	 */
	private static TextComponent toComponent(String text) {
		return Component.text(text);
	}
	
	/**
	 * Converts the given component to text.
	 * 
	 * @param component The component to convert.
	 * 
	 * @return The created text.
	 */
	private static String fromComponent(Component component) {
		return LegacyComponentSerializer.legacySection().serializeOrNull(component);
	}
	
}
