package eu.wauz.wauzcore.building.shapes;

import eu.wauz.wauzcore.system.annotations.Brush;

/**
 * A brush to paint hollow cylinder shaped block structures.
 * 
 * @author Wauzmons
 */
@Brush
public class BrushHCyl implements WauzBrush {
	
	/**
	 * @return The name of the brush.
	 */
	@Override
	public String getName() {
		return "hcyl";
	}

}
