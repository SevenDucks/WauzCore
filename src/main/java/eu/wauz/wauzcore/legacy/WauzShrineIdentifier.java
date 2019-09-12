//package eu.wauz.wauzcore.legacy;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Random;
//
//import org.bukkit.Effect;
//import org.bukkit.Material;
//import org.bukkit.entity.Player;
//import org.bukkit.event.inventory.InventoryClickEvent;
//import org.bukkit.inventory.ItemStack;
//import org.bukkit.inventory.meta.ItemMeta;
//
//import net.md_5.bungee.api.ChatColor;
//
//public class WauzShrineIdentifier {
//	
//	private static List<String> shrineNames = new ArrayList<>(Arrays.asList(
//			"Amon", "Atar", "Baka", "Bael", "Cata", "Cesa", "Daku", "Deus", "Elek", "Esto", "Furo", "Fitu", "Garo",
//			"Gyro", "Hino", "Hane", "Ivel", "Inuk", "Jago", "Jojo", "Kaka", "Kars", "Leto", "Lupo", "Maro", "Magi",
//			"Naga", "Notu", "Omek", "Oraa", "Popo", "Pino", "Qire", "Quez", "Raka", "Reem", "Sora", "Sado", "Tera",
//			"Toem", "Utga", "Uros", "Vari", "Vago", "Wesa", "Wamu", "Xera", "Xulu", "Yaga", "Yare", "Zaga", "Zeli"));
//	
//	public void identifyShrine(InventoryClickEvent event) {
//		Player player = (Player) event.getWhoClicked();
//		ItemStack shrineItemStack = event.getCurrentItem();	
//		
//		Random random = new Random();
//		String shrineName = ChatColor.RESET 
//				+ shrineNames.get(random.nextInt(shrineNames.size())) + "-" 
//				+ shrineNames.get(random.nextInt(shrineNames.size())) + " Shrine";
//		
//		ItemMeta itemMeta = shrineItemStack.getItemMeta();
//		itemMeta.setDisplayName(shrineName);
//		
//		List<String> lores = new ArrayList<String>();
//		lores.add(ChatColor.RED + "Shrine Map");
//		lores.add("");
//		lores.add(ChatColor.YELLOW + "" + (random.nextInt(9) + 5) + " Rooms");
//		lores.add("");
//		lores.add(ChatColor.GRAY + "Right Click to enter Shrine.");
//		lores.add(ChatColor.GRAY + "Map resets upon Death.");
//		
//		itemMeta.setLore(lores);	
//		shrineItemStack.setItemMeta(itemMeta);
//		shrineItemStack.setType(Material.PAPER);
//		
//		player.getWorld().playEffect(player.getLocation(), Effect.ANVIL_USE, 0);
//	}
//
//}
