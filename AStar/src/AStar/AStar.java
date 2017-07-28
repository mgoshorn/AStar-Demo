package AStar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;

/**
 * Class utilized to solve a pathfinding problem using a 2D array of Nodes.
 * @author MitchellGoshorn
 *
 */
public class AStar {
	
	/**
	 * DistanceType Setting affects how distances are calculated for traveled and heuristics.
	 */

	public enum DistanceType { MANHATTAN, CHEBYSHEV, EUCLIDEAN }
	public DistanceType calculatedDistanceStyle;
	public DistanceType calculatedHeuristicStyle;
	
	/**
	 * Method calculates distance between two Nodes given their x, y values and the 
	 * distance calculating style designated by the calculatedDistanceStyle value.
	 * @param a First node
	 * @param b Second node
	 * @return  Distance between Nodes
	 */
	public double getDistance(Node a, Node b) {
		switch(this.calculatedDistanceStyle) {
			case MANHATTAN:
				return Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY());
			case CHEBYSHEV:
				return Math.abs(a.getX() - b.getX()) > Math.abs(a.getY() - b.getY()) ? 
						Math.abs(a.getX() - b.getX()) : Math.abs(a.getY() - b.getY());
			case EUCLIDEAN:
			default:
				return Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
		}
	}

	/**
	 * Method calculates distance between two Nodes given their x, y values and the 
	 * distance calculating style designated by the calculatedHeuristicStyle value.
	 * @param a First node
	 * @param b Second node
	 * @return  Distance between Nodes
	 */
	public double getHeuristic(Node a, Node b) {
		switch(this.calculatedHeuristicStyle) {
		case MANHATTAN:
			return Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY());
		case CHEBYSHEV:
			return Math.abs(a.getX() - b.getX()) > Math.abs(a.getY() - b.getY()) ? 
					Math.abs(a.getX() - b.getX()) : Math.abs(a.getY() - b.getY());
		case EUCLIDEAN:
		default:
			return Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
		}
	}
	
	/**
	 * Default constructor, sets distance calculations to Euclidean
	 */
	public AStar() {
		this.calculatedDistanceStyle = DistanceType.EUCLIDEAN;
		this.calculatedHeuristicStyle = DistanceType.EUCLIDEAN;
	}
	
	/**
	 * Constructor assigns DistanceType argument for all distance calculations
	 * @param type
	 */
	public AStar(DistanceType type) {
		this.calculatedDistanceStyle = type;
		this.calculatedHeuristicStyle = type;
	}
	
	/**
	 * Constructor accepts two distance types, one for travelled distance and another for 
	 * heuristic estimates of remaining distance.
	 * @param g Calculation type for traveled distance.
	 * @param h Calculation type for heuristic.
	 */
	public AStar(DistanceType g, DistanceType h) {
		this.calculatedDistanceStyle = g;
		this.calculatedHeuristicStyle = h;
	}
	
	
	/**
	 * Method solves the A* problem
	 * @param map 2D Node array
	 * @param start Starting point (current position)
	 * @param end Ending point (goal location)
	 * @return ArrayList of Nodes representing the path, null if no path found.
	 */
	public ArrayList<Node> solveAStar(Node[][] map, Node start, Node end) {
		//Return null if either start or finish Nodes are undefined.
		if(start == null || end == null) return null;
		
		//Assign initial values to the starting point
		start.setG(0);
		start.setH(this.getHeuristic(start, end));
		start.setVisited(true);
		
		//Initialize heap
		PriorityQueue<Node> heap = new PriorityQueue<>(new NodeComparator());
		
		//add starting point to the heap
		heap.add(start);
		
		//Primary A* loop
		while(!heap.isEmpty()) {
			
			//Get best suited Node
			Node n = heap.poll();
			
			//Check if this Node is the end Node
			if(n.equals(end)) {
				break;
			}
			
			//Find Node's neighbors
			ArrayList<Node> neighbors = n.findNeighbors(map);
			
			//Neighbors loop
			for(Node neighbor : neighbors) {
				
				//Check to make sure neighbor node is valid, and or does not already have an equally efficient path
				if(neighbor != null && !neighbor.isVisited() || neighbor.getG() > n.getG() + this.getDistance(n, neighbor)) {
					
					//Set distance to neighbor Node as parent's distance + travel distance between neighbor and parent		
					neighbor.setG(n.getG() + this.getDistance(n, neighbor));
				
					//Calculate and assign heuristic
					neighbor.setH(this.getHeuristic(neighbor, end));
					
					//Add neighbor to heap
					heap.add(neighbor);
					neighbor.setVisited(true);
					
				}
			}
		}
		
		
		//Create arraylist for calculating the path 
		ArrayList<Node> path = new ArrayList<>();
		
		//Add the end node to the list
		path.add(end);
		
		//Path regression
		while(path.get(0) != start) {
			
			//Get the most recent Node added
			Node n = path.get(0);
			
			//Find Nodes neighbors
			ArrayList<Node> neighbors = n.findNeighbors(map);
			
			//Node used to store the best Node for pathing back
			Node lowest = null;
			
			//Loop checks neighbors for most efficient Node back to the parent
			for(Node neighbor : neighbors) {
				//Check to make sure neighbor has been evaluated, is a better position than the current Node
				//and whether it is more fit than any other fit Node discovered
				if(neighbor.isVisited() && neighbor.getG() < n.getG() && (lowest == null || neighbor.getG() < lowest.getG())) {
					lowest = neighbor;
				}
			}
			
			//Add most fit Node to the path
			if(lowest != null) path.add(0, lowest);
		}
		
		//reverse list so that it now begins at starting point and ends at the end point
		Collections.reverse(path);
		return path;
	}
}
