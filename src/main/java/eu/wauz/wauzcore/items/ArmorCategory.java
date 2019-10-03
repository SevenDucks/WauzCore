package eu.wauz.wauzcore.items;

public enum ArmorCategory {
	
	LIGHT("Light"),
	MEDIUM("Medium"),
	HEAVY("Heavy"),
	UNKNOWN("???");
	
	public String name;
	
	ArmorCategory(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public static ArmorCategory fromRaceAndClass(String raceAndClass) {
		if(raceAndClass.contains("Crusader")) {
			return HEAVY;
		}
		else if(raceAndClass.contains("Nephilim")) {
			return MEDIUM;
		}
		else if(raceAndClass.contains("Assassin")) {
			return LIGHT;
		}
		else {
			return UNKNOWN;
		}
	}

}
