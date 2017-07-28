package AStar;

import java.util.Comparator;

public class NodeComparator implements Comparator<Node> {
	/**
	 * Fast mode can be set to true to use a Heuristic that should find the solution faster
	 * but is less likely to be the best path
	 */
	boolean fastMode;
	
	@Override
	public int compare(Node a, Node b) {
		if(fastMode) return fastComparison(a, b);
		return standardComparison(a, b);
		
	}
	
	public int standardComparison(Node a, Node b) {
		if(a.getF() == b.getF()) {
			if(a.getH() < b.getH()) {
				return -1;
			}
			if(a.getH() == b.getH()) {
				return 0;
			}
			return 1;
		}
		if(a.getF() < b.getF()) return -1;
		return 1;
	}
	
	public int fastComparison(Node a, Node b) {
		if(a.getH() == b.getH()) {
			if(a.getG() < b.getG()) {
				return -1;
			}
			if(a.getG() == b.getG()) {
				return 0;
			}
			return 1;
		}
		if(a.getH() < b.getH()) return -1;
		return 1;
	}

	
	
}
