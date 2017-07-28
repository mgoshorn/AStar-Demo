package Display;
import AStar.AStar;
import AStar.Node;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Program extends JPanel {
	private static final long serialVersionUID = 8941841312582348223L;
	private static final boolean RANDOM_OBSTACLE_MODE = true;
	private static final int RANDOM_OBSTACLE_COUNT = 1200;
	public static final Color NODE_UNVISITED_COLOR = Color.GRAY;
	public static final Color LOWEST_H_COLOR = Color.GREEN;
	public static final Color HIGHEST_H_COLOR = Color.RED;
	public static final Color LOWEST_F_COLOR = Color.GREEN;
	public static final Color HIGHEST_F_COLOR = Color.RED;
	public static final Color START_NODE_COLOR = Color.YELLOW;
	public static final Color END_NODE_COLOR = Color.CYAN;
	public static final Color NULL_NODE_COLOR = new Color(0,0,0,0);
	public static final BasicStroke PATH_STROKE = new BasicStroke(5.0f,
            BasicStroke.CAP_ROUND,
            BasicStroke.JOIN_ROUND);
	public static double lowest_h = Double.MAX_VALUE;
	public static double highest_h = Double.MIN_VALUE;
	public static double lowest_f = Double.MAX_VALUE;
	public static double highest_f = Double.MIN_VALUE;
	public static double lowest_g = Double.MAX_VALUE;
	public static double highest_g = Double.MIN_VALUE;
	public Node[][] nodemap;
	public Node start;
	public Node end;
	public static int nodeSize = 20;
	public static int nodePadding = 1;
	public static int displayPadding = 20;
	public static int nodesWide, nodesHigh;
	public JFrame display;
	private enum Colorization { HEURISTIC, DISTANCE, EFFICIENCY };
	private Colorization colorization = Colorization.DISTANCE;
	private ArrayList<Node> solution = new ArrayList<>();
	
	public Program() {
		
		JFrame mainFrame = new JFrame();
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBackground(Color.BLACK);
		mainFrame.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(1200, 900));
		
		mainFrame.setLocationRelativeTo(null);
		mainFrame.add(this, BorderLayout.CENTER);
		mainFrame.pack();
		mainFrame.setVisible(true);
		this.display = mainFrame;
		nodesWide = ((this.getWidth() - displayPadding * 2) / (nodeSize+nodePadding));
		nodesHigh = ((this.getHeight() - displayPadding * 2) / (nodeSize+nodePadding));
	}
	
	private Color getNodeColor(Node n) {
		if(n.equals(this.start)) return START_NODE_COLOR;
		if(n.equals(this.end)) return END_NODE_COLOR;
		Color nodeColor;
		double index;
		switch(this.colorization) {
			case HEURISTIC:
				if(!n.isVisited() || lowest_h == Double.MAX_VALUE || highest_h == Double.MIN_VALUE) {
					return NODE_UNVISITED_COLOR;
				}
				index = (n.getH() - lowest_h) / (highest_h - lowest_h);
				nodeColor = new Color(
						(int)(HIGHEST_H_COLOR.getRed() * index + 	LOWEST_H_COLOR.getRed() * (1 - index)),
						(int)(HIGHEST_H_COLOR.getGreen() * index + 	LOWEST_H_COLOR.getGreen() * (1 - index)),
						(int)(HIGHEST_H_COLOR.getBlue() * index + 	LOWEST_H_COLOR.getBlue() * (1 - index)),
						(int)(HIGHEST_H_COLOR.getAlpha() * index + 	LOWEST_H_COLOR.getAlpha() * (1 - index)));
				return nodeColor;
			
			case DISTANCE:
				if(!n.isVisited() || lowest_g == Double.MAX_VALUE || highest_g == Double.MIN_VALUE) {
					return NODE_UNVISITED_COLOR;
				}
				index = (n.getG() - lowest_g) / (highest_g - lowest_g);
				nodeColor = new Color(
						(int)(HIGHEST_F_COLOR.getRed() * index + 	LOWEST_F_COLOR.getRed() * (1 - index)),
						(int)(HIGHEST_F_COLOR.getGreen() * index + 	LOWEST_F_COLOR.getGreen() * (1 - index)),
						(int)(HIGHEST_F_COLOR.getBlue() * index + 	LOWEST_F_COLOR.getBlue() * (1 - index)),
						(int)(HIGHEST_F_COLOR.getAlpha() * index + 	LOWEST_F_COLOR.getAlpha() * (1 - index)));
				return nodeColor;
				
			case EFFICIENCY:
			default:
				if(!n.isVisited() || lowest_f == Double.MAX_VALUE || highest_f == Double.MIN_VALUE) {
					return NODE_UNVISITED_COLOR;
				}
				index = (n.getF() - lowest_f) / (highest_f - lowest_f);
				nodeColor = new Color(
						(int)(HIGHEST_F_COLOR.getRed() * index + 	LOWEST_F_COLOR.getRed() * (1 - index)),
						(int)(HIGHEST_F_COLOR.getGreen() * index + 	LOWEST_F_COLOR.getGreen() * (1 - index)),
						(int)(HIGHEST_F_COLOR.getBlue() * index + 	LOWEST_F_COLOR.getBlue() * (1 - index)),
						(int)(HIGHEST_F_COLOR.getAlpha() * index + 	LOWEST_F_COLOR.getAlpha() * (1 - index)));
				return nodeColor;
			
		}
		
		
		
	}
	
	private Rectangle2D getDrawRectangle(Node n) {
		return new Rectangle2D.Double(
				displayPadding + (n.getX() * nodeSize) + (nodePadding * (n.getX())),
				displayPadding + (n.getY() * nodeSize) + (nodePadding * (n.getY())),
				nodeSize, nodeSize
		);
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		for(int x = 0; x < nodemap.length; x++) {
			for(int y = 0; y < nodemap[x].length; y++) {
				Node n = nodemap[x][y];
				if(n == null) continue;
				Rectangle2D rectangle = getDrawRectangle(n);
				Color color = getNodeColor(n);
				g2d.setColor(color);
				g2d.fill(rectangle);
			}
		}
		//Draw path
		g2d.setColor(Color.WHITE);
		g2d.setStroke(PATH_STROKE);
		if(solution == null) return;
		for(int i = 0; i < solution.size() - 1; i++) {
			Rectangle2D a = getDrawRectangle(solution.get(i));
			Rectangle2D b = getDrawRectangle(solution.get(i+1));
			g2d.drawLine((int)a.getCenterX(), (int)a.getCenterY(), (int)b.getCenterX(), (int)b.getCenterY());
		}
	}
	
	public void removeBlock(int x1, int y1, int x2, int y2, Node[][] map) {
		if(x1 > x2) {
			x1 += x2;
			x2 = x1 - x2;
			x1 = x1 - x2;
		}
		if(y1 > y2) {
			y1 += y2;
			y2 = y1 - y2;
			y1 = y1 - y2;
		}
		for(int x = x1; x < x2; x++) {
			for(int y = y1; y < y2; y++) {
				map[x][y] = null;
			}
		}
	}
	
	public static void main(String[] args) {
		Node[][] nodemap;
		Program program = new Program();
		nodemap = new Node[nodesWide][nodesHigh];
		program.nodemap = nodemap;
		
		program.colorization = Colorization.DISTANCE;
		for(int x = 0; x < nodemap.length; x++) {
			for(int y = 0; y < nodemap[x].length; y++) {
				nodemap[x][y] = new Node(x, y);
			}
		}
		
		AStar astar = new AStar();
		program.start = nodemap[1][1];
		program.end = nodemap[35][35];
		
		if(RANDOM_OBSTACLE_MODE) {
			for(int i = 0; i < RANDOM_OBSTACLE_COUNT; i++) {
				int x = (int)(Math.random()*nodemap.length);
				int y = (int)(Math.random()*nodemap[x].length);
				if(nodemap[x][y] != program.start && nodemap[x][y] != program.end) {
					nodemap[x][y] = null;
				}
			}
		} else {
			//program.removeBlock(0, 5, 15, 8, nodemap);
			/*
			program.removeBlock(5, 0, 7, 20, nodemap);
			program.removeBlock(10, 15, 12, 35, nodemap);
			program.removeBlock(29, 16, 31, 25, nodemap);
			program.removeBlock(31, 16, 39, 18, nodemap);
			program.removeBlock(31,  23, 39, 25, nodemap);
			*/
			
			program.removeBlock(3, 1, 20, 2, nodemap);
			program.removeBlock(20, 1, 21, 20, nodemap);
			program.removeBlock(6, 20, 21, 21, nodemap);
			program.repaint();
		}
		
		program.solution = astar.solveAStar(nodemap, program.start, program.end);
		if(program.solution == null) System.out.println("No solution found.");
		for(Node[] x : nodemap) {
			for(Node y : x) {
				if(y == null) continue;
				if(y.getH() < lowest_h) {
					lowest_h = y.getH();
				}
				if(y.getH() > highest_h) {
					highest_h = y.getH();
				}
				if(y.isVisited() && y.getF() < lowest_f) {
					lowest_f = y.getF();
				}
				if(y.isVisited() && y.getF() > highest_f) {
					highest_f = y.getF();
				}
				if(y.isVisited() && y.getG() < lowest_g) {
					lowest_g = y.getG();
				}
				if(y.isVisited() && y.getG() > highest_g) {
					highest_g = y.getG();
				}
			}
		}
		program.repaint();
	}
}
