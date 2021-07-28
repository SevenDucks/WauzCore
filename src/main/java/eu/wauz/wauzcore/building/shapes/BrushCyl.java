package eu.wauz.wauzcore.building.shapes;

import eu.wauz.wauzcore.system.annotations.Brush;

/**
 * A brush to paint cylinder shaped block structures.
 * 
 * @author Wauzmons
 */
@Brush
public class BrushCyl extends WauzBrush {
	
	/**
	 * Creates a new instance of this brush.
	 */
	public BrushCyl() {
		super();
	}
	
	/**
	 * @return The name of the brush.
	 */
	@Override
	public String getName() {
		return "cyl";
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
		return new BrushCyl().withShape(new ShapeCircle(radius, height, false));
	}

}
