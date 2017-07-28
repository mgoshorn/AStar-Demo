package AStar;

import java.util.ArrayList;

/**
 * 
 * @author Mitchell Goshorn
 *
 */
public class Node {
	private int x, y;
	private double g, h;
	private boolean visited;
	
	/**
	 * @param x - Node x-position in grid
	 * @param y - Node y-position in grid
	 */
	public Node(int x, int y) {
		this.x = x;
		this.y = y;
		this.g = -1;
		this.h = -1;
	}

	/**
	 * Gets heuristic distance from the source Node to the goal Node by summing the distance from the source Node to
	 * the current Node and the heuristic distance from the current Node to the goal Node
	 * @return known distance
	 */
	public double getF() {
		return this.h + this.g;
	}

	/**
	 * G - Known cost to travel from the source Node to the current Node
	 * @return g
	 */
	public double getG() {
		return g;
	}

	/**
	 * G - Known cost to travel from the source Node to the current Node
	 * @param g value to be assigned to g
	 */
	public void setG(double g) {
		this.g = g;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	/**
	 * H - Heuristic cost to go from the current Node to the goal Node
	 * @return h
	 */
	public double getH() {
		return this.h;
	}
	
	/**
	 * H - Heuristic cost to go from the current Node to the goal Node
	 * @param h
	 */
	public void setH(double h) {
		this.h = h;
	}
	
	public ArrayList<Node> findNeighbors(Node[][] map) {
		ArrayList<Node> neighbors = new ArrayList<>();
		for(int x = this.getX() - 1; x < this.getX() + 2; x++) {
			for(int y = this.getY() - 1; y < this.getY() + 2; y++) {
				if(x < 0 || x >= map.length || y < 0 || y >= map[x].length || this.equals(map[x][y]) || map[x][y] == null) continue;
				neighbors.add(map[x][y]);
			}
		}
		return neighbors;
	}
	
	@Override
	public String toString() {
		return "(" + this.x + ", " + this.y + ") - G: " + this.g + ", H: " + this.h + ", F: "+ this.getF(); 
	}
	
	
}
