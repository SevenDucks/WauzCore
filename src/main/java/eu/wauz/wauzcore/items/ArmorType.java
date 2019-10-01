package eu.wauz.wauzcore.items;

public enum ArmorType {
	
	LIGHT("Light"),
	MEDIUM("Medium"),
	HEAVY("Heavy");
	
	public String name;
	
	ArmorType(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}

}
