package eu.wauz.wauzcore.oneblock;

/**
 * Used for managing one-block plots.
 * 
 * @author Wauzmons
 */
public class OnePlotManager {

	/**
	 * Gets the grid position of the plot with the given index.
	 * A complex formula is used to place the plots on a "quadratic spiral".
	 * 
	 * @param plotIndex The index of the plot.
	 * 
	 * @return The grid postion as array, where 0 = x and 1 = z.
	 */
	public static int[] get(int plotIndex) {
		int k = (int) Math.ceil((Math.sqrt(plotIndex) - 1) / 2);
		int t = 2 * k + 1;
		int m = (int) Math.pow(t, 2);
        t--;
        
        if(plotIndex >= m - t) {
        	return new int[]{k - (m - plotIndex), -k};
        }
        else {
        	m -= t;
        }
        
        if(plotIndex >= m - t) {
        	return  new int[]{-k, -k + (m - plotIndex)};
        }
        else {
        	m -= t;
        }
        
        if(plotIndex >= m - t) {
        	return  new int[]{-k + (m - plotIndex), k};
        }
        else {
        	return  new int[]{k, k - (m - plotIndex - t)};
        }
	}
	
}
