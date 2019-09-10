package eu.wauz.wauzcore.items.enhancements;

import eu.wauz.wauzcore.items.EquipmentType;

public interface WauzEnhancement {
	
	public String getEnhancementId();
	
	public EquipmentType getEquipmentType();
	
	public WauzEnhancementParameters enhanceEquipment(WauzEnhancementParameters parameters);

}
