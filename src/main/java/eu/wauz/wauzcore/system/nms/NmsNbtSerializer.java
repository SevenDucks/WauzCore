package eu.wauz.wauzcore.system.nms;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.craftbukkit.v1_16_R2.inventory.CraftItemStack;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_16_R2.MojangsonParser;
import net.minecraft.server.v1_16_R2.NBTTagCompound;

/**
 * Methods for serializing data to  NBT, using net.minecraft.server specific methods.
 * 
 * @author Wauzmons
 */
public class NmsNbtSerializer {
	
	/**
	 * Saves the given Inventory into a string list.
	 * 
	 * @param inventory The inventory to save.
	 * 
	 * @return The created string list.
	 */
	public static List<String> saveInventory(Inventory inventory) {
		List<String> dataStrings = new ArrayList<>();
		try {
			for(int index = 0; index < inventory.getContents().length; index++) {
				ItemStack itemStack = inventory.getItem(index);
				if(itemStack == null) {
					dataStrings.add(null);
				}
				else {
					dataStrings.add(CraftItemStack.asNMSCopy(itemStack).save(new NBTTagCompound()).asString());
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return dataStrings;
		
	}
	
	/**
	 * Loads the given Inventory from a string list.
	 * 
	 * @param inventory The inventory to load.
	 * @param dataStrings The data to insert.
	 * 
	 * @return The filled inventory.
	 */
	public static Inventory loadInventory(Inventory inventory, List<?> dataStrings) {
		try {
			for(int index = 0; index < inventory.getContents().length && index < dataStrings.size(); index++) {
				String dataString = (String) dataStrings.get(index);
				if(dataString == null) {
					inventory.setItem(index, null);
				}
				else {
					NBTTagCompound compound = MojangsonParser.parse(dataString);
					ItemStack itemStack = CraftItemStack.asBukkitCopy(net.minecraft.server.v1_16_R2.ItemStack.a(compound));
					inventory.setItem(index, itemStack);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return inventory;
	}

}
