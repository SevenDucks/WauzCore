package eu.wauz.wauzcore.items.enhancements;

import java.util.List;

import org.bukkit.inventory.meta.ItemMeta;

public class WauzEnhancementParameters {
	
	private int enhancementLevel;
	
	private String enhancementLore;
	
	private ItemMeta itemMeta;
	
	private List<String> lores;
	
	private String mainStatString;
	
	private int attackStat;
	
	private int defenseStat;
	
	private int durabilityStat;
	
	public WauzEnhancementParameters(int enhancementLevel) {
		this.enhancementLevel = enhancementLevel;
	}

	public int getEnhancementLevel() {
		return enhancementLevel;
	}

	public String getEnhancementLore() {
		return enhancementLore;
	}

	public void setEnhancementLore(String enhancementLore) {
		this.enhancementLore = enhancementLore;
	}

	public ItemMeta getItemMeta() {
		return itemMeta;
	}

	public void setItemMeta(ItemMeta itemMeta) {
		this.itemMeta = itemMeta;
	}

	public List<String> getLores() {
		return lores;
	}

	public void setLores(List<String> lores) {
		this.lores = lores;
	}

	public String getMainStatString() {
		return mainStatString;
	}

	public void setMainStatString(String mainStatString) {
		this.mainStatString = mainStatString;
	}

	public int getAttackStat() {
		return attackStat;
	}

	public void setAttackStat(int attackStat) {
		this.attackStat = attackStat;
	}

	public int getDefenseStat() {
		return defenseStat;
	}

	public void setDefenseStat(int defenseStat) {
		this.defenseStat = defenseStat;
	}

	public int getDurabilityStat() {
		return durabilityStat;
	}

	public void setDurabilityStat(int durabilityStat) {
		this.durabilityStat = durabilityStat;
	}

}
