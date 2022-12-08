package eu.wauz.wauzcore.building;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.tuple.Pair;

import eu.wauz.wauzcore.system.util.Chance;

/**
 * A two dimensional path generator.
 * It saves a random path from top to bottom in an array of integers.
 * Can be used for map generation and other things.
 * 
 * @author Wauzmons
 */
public class PathGenerator {
	
	/**
	 * The width of the path matrix.
	 */
	private int width;
	
	/**
	 * The height of the path matrix.
	 */
	private int height;
	
	/**
	 * The 2D array, to save path states.
	 */
	private int[][] pathMatrix;
	
	/**
	 * Creates a new path generator with given sizes.
	 * 
	 * @param width The width of the path matrix.
	 * @param height The height of the path matrix.
	 */
	public PathGenerator(int width, int height) {
		this.width = width;
		this.height = height;
		pathMatrix = new int[width][height];
	}
	
	/**
	 * Lets the generator run, to generate the room matrix.
	 */
	public void run() {
		run(Chance.randomInt(width));
	}
	
	/**
	 * Lets the generator run, to generate the room matrix.
	 * 
	 * @param pathStart The horizontal cell where the path should start.
	 */
	public void run(int pathStart) {
		int x = pathStart;
		int y = 0;
		
		pathMatrix[x][y] = 1;
		while(y + 1 < height) {
			List<Pair<Integer, Integer>> possibilities = new ArrayList<>();
			if(isValidCell(x - 1, y, true)) {
				possibilities.add(Pair.of(x - 1, y));
				possibilities.add(Pair.of(x - 1, y));
			}
			if(isValidCell(x + 1, y, true)) {
				possibilities.add(Pair.of(x + 1, y));
				possibilities.add(Pair.of(x + 1, y));
			}
			if(isValidCell(x, y + 1, true)) {
				possibilities.add(Pair.of(x, y + 1));
			}
			if(possibilities.isEmpty()) {
				break;
			}
			Pair<Integer, Integer> coords = possibilities.get(Chance.randomInt(possibilities.size()));
			x = coords.getLeft();
			y = coords.getRight();
			pathMatrix[x][y] = 1;
		}
	}
	
	/**
	 * Checks if a cell can be made into a path.
	 * 
	 * @param x The x coordinate of the cell.
	 * @param y The y coordinate of the cell.
	 * @param checkNeighbours If it should be checked, that the cell doesn't collide with neighbours.
	 * 
	 * @return If the cell is valid.
	 */
	private boolean isValidCell(int x, int y, boolean checkNeighbours) {
		if(x < 0 || y < 0 || x >= width || y >= height || pathMatrix[x][y] == 1) {
			return false;
		}
		if(checkNeighbours && !isValidCell(x, y - 1, false) && (!coll(x + 1, y) || !coll(x - 1, y))) {
			return false;
		}
		return true;
	}
	
	private boolean coll(int x, int y) {
		if(x < 0 || y < 0 || x >= width || y >= height) {
			return true;
		}
		return pathMatrix[x][y] != 1;
	}

	/**
	 * @return The 2D array, to save path states.
	 */
	public int[][] getPathMatrix() {
		return pathMatrix;
	}

}
