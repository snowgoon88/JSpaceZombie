/**
 * 
 */
package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import javax.vecmath.Point3f;

/**
 * @author snowgoon88ATgmailDOTcom
 *
 */
public class GraphExplorer {
	
	private Graph<Point3f> _graph;
	
	/**
	 * Create for a given graph.
	 * @param graph
	 */
	public GraphExplorer(Graph<Point3f> graph) {
		super();
		this._graph = graph;
	}
		
	/**
	 * Look for the shortest path to target.
	 * @param target
	 * @throws NoSuchElementException if target does not identify a valid Node.
	 */
	public Path findShortestPath( Point3f target) {
		// Set value of all Nodes to MAXFLOAT
		// and find targetNode (if any).
		System.out.println("Shortest path from "+_graph.getElem().toString()+" to "+target.toString());
		Graph<Point3f> targetNode = null;
		GraphIterator<Point3f> it = new GraphIterator<Point3f>(_graph);
		while (it.hasNext()) {
			Graph<Point3f> node = (Graph<Point3f>) it.getNext();
			node.setVal( Float.MAX_VALUE );
			if (node.getElem().equals(target)) {
				targetNode = node;
			}
		}
		System.out.println("Setting to MAX_VALUE\n"+toString()+"\n-----\n");
		if (targetNode == null)
			throw new NoSuchElementException();
		
		// Compute shortest node to all points.
		ArrayList<Graph<Point3f>> openNode = new ArrayList<Graph<Point3f>>();
		openNode.add(_graph);
		_graph.setVal(0.0f); // Start from here
		while (openNode.size() > 0) {
			Graph<Point3f> node = openNode.remove(0);
			System.out.println("Dealing with node="+node.getElem().toString());
			for (GraphLink<Point3f> link : node.getLink()) {
				float dist = node.getVal() + link.getVal();
				if (dist < link.getNode().getVal()) {
					link.getNode().setVal(dist);
					openNode.add(link.getNode());
					System.out.println("Adding node="+link.getNode().getElem().toString());
				}
			}
			System.out.println("STEP------\n"+toString()+"\n-----\n");
			String str = "OPEN=";
			for (Graph<Point3f> n : openNode) {
				str += n.getElem().toString() + " ";
			}
			System.out.println(str+"\n--------open");
//			//
//			// Wait for user
//			System.out.println("Any key to go on...");
//			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//			try {
//				br.readLine();
//			} catch (IOException ioe) {
//				System.out.println("IO error trying to read your name!");
//				System.exit(1);
//			}

		}
		System.out.println("Shortest Distance\n"+toString()+"\n-----\n");
		
		// Reverse shortest path from target node
		Path path = new Path();
		path.clear();
		path.addFirst(targetNode.getElem());
		Graph<Point3f> currentNode = targetNode;
		while (currentNode != _graph) {
			float mindist = Float.MAX_VALUE;
			Graph<Point3f> newNode = null;
			for (GraphLink<Point3f> parent : currentNode.getParents()) {
				if (parent.getNode().getVal() < mindist) {
					mindist = parent.getNode().getVal();
					newNode = parent.getNode();
				}
			}
			if (newNode != null ) {
				System.out.println("Best parent at "+newNode.getElem().toString()+", dist="+mindist);
				path.addFirst(newNode.getElem());
				currentNode = newNode;
			}
			else {
				System.out.println("No best parent, return null");
				return null;
			}
			String str = "CurrentPath = ";
			for (Point3f pt : path.getListWP()) {
				str += pt.toString()+" -> ";
			}
			System.out.println("Path="+str+"\n");
		}
		System.out.println("Shortest Path*****");
		String str = "";
		for (Point3f pt : path.getListWP()) {
			str += pt.toString()+" -> ";
		}
		System.out.println(str+"\n");
		
		return path;
	}
	
	@Override
	public String toString() {
		String str = "";
		GraphIterator<Point3f> it = new GraphIterator<Point3f>(_graph);
		while (it.hasNext()) {
			Graph<Point3f> node = (Graph<Point3f>) it.getNext();
			str += node.toString();
		}
		return str;
	}
		

}
