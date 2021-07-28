package eu.wauz.wauzcore.building.shapes;

import eu.wauz.wauzcore.system.annotations.Brush;

/**
 * A brush to paint hollow cylinder shaped block structures.
 * 
 * @author Wauzmons
 */
@Brush
public class BrushHCyl extends WauzBrush {
	
	/**
	 * Creates a new instance of this brush.
	 */
	public BrushHCyl() {
		super();
	}
	
	/**
	 * @return The name of the brush.
	 */
	@Override
	public String getName() {
		return "hcyl";
	}
	
	/**
	 * Gets a new instance of this brush.
     * 
     * @param radius The radius of the brush.
     * @param height The height of the brush.
     * 
     * @return The created instance.
	 */
	@Override
	public WauzBrush getInstance(int radius, int height) {
		return new BrushHCyl().withShape(new ShapeCircle(radius, height, true));
	}
	
}
