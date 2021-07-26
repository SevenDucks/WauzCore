package eu.wauz.wauzcore.building.shapes;

/**
 * A block structure blueprint in an abstract shape.
 * 
 * @author Wauzmons
 */
public abstract class WauzShape {
	
	/**
	 * The radius of the shape.
	 */
	protected final int radius;
	
	/**
	 * The height of the shape.
	 */
	protected final int height;
	
	/**
	 * Constructs a block structure blueprint in an abstract shape.
	 * Sets all values to 0.
	 */
	public WauzShape() {
		this.radius = 0;
		this.height = 0;
	}
	
	/**
     * Constructs a block structure blueprint in an abstract shape.
     * 
     * @param radius The radius of the hexagon.
     * @param height The height of the hexagon.
     */
    public WauzShape(int radius, int height) {
        this.radius = radius;
        this.height = height;
    }
    
    /**
     * @return The radius of the circle.
     */
    public int getRadius() {
        return radius;
    }
    
    /**
     * @return The height of the circle.
     */
    public int getHeight() {
    	return height;
    }

}
