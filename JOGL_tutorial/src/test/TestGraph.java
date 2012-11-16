/**
 * 
 */
package test;

import java.util.ArrayList;
import java.util.Random;

import javax.vecmath.Point3f;

import model.Graph;
import model.GraphExplorer;
import model.GraphIterator;

/**
 * @author snowgoon88ATgmailDOTcom
 *
 */
public class TestGraph {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Graph<Integer> g = buildGraph1();
		GraphIterator<Integer> it;
		
		System.out.println("Graph1");
		g = buildGraph1();
		it = new GraphIterator<Integer>(g);
		while (it.hasNext()) {
			Integer elem = it.getNext().getElem();
			System.out.println("next="+elem);
		}
		
		System.out.println("Graph2");
		g = buildGraph2();
		it = new GraphIterator<Integer>(g);
		while (it.hasNext()) {
			Integer elem = it.getNext().getElem();
			System.out.println("next="+elem);
		}

		System.out.println("Graph3");
		g = buildGraph3();
		it = new GraphIterator<Integer>(g);
		while (it.hasNext()) {
			Integer elem = it.getNext().getElem();
			System.out.println("next="+elem);
		}

		System.out.println("Graph4");
		g = buildGraph4();
		it = new GraphIterator<Integer>(g);
		while (it.hasNext()) {
			Integer elem = it.getNext().getElem();
			System.out.println("next="+elem);
		}

		System.out.println("Graph5");
		g = buildGraph5();
		it = new GraphIterator<Integer>(g);
		while (it.hasNext()) {
			Integer elem = it.getNext().getElem();
			System.out.println("next="+elem);
		}

		System.out.println("Graph6");
		g = buildGraph6();
		it = new GraphIterator<Integer>(g);
		while (it.hasNext()) {
			Integer elem = it.getNext().getElem();
			System.out.println("next="+elem);
		}
		
		System.out.println("WPGraph************************");
		Graph<Point3f> gwp = buildWPGraph();
		System.out.println("---------SHORTEST--------------");
		GraphExplorer explo = new GraphExplorer(gwp);
		explo.findShortestPath( new Point3f(-1f,-1f,0f));

	}
	
	static Graph<Integer> buildGraph1() {
		Graph<Integer> n0 = new Graph<Integer>(0);
		return n0;
	}
	static Graph<Integer> buildGraph2() {
		Graph<Integer> n0 = new Graph<Integer>(0);
		Graph<Integer> n1 = new Graph<Integer>(1);
		n0.addSubgraph(n1, 0);
		Graph<Integer> n2 = new Graph<Integer>(2);
		n1.addSubgraph(n2, 1);
		return n0;
	}
	static Graph<Integer> buildGraph3() {
		Graph<Integer> n0 = new Graph<Integer>(0);
		Graph<Integer> n1 = new Graph<Integer>(1);
		n0.addSubgraph(n1, 0);
		Graph<Integer> n2 = new Graph<Integer>(2);
		n0.addSubgraph(n2, 0);
		return n0;
	}
	static Graph<Integer> buildGraph4() {
		Graph<Integer> n0 = new Graph<Integer>(0);
		Graph<Integer> n1 = new Graph<Integer>(1);
		n0.addSubgraph(n1, 0);
		Graph<Integer> n2 = new Graph<Integer>(2);
		n0.addSubgraph(n2, 0);
		n2.addSubgraph(n0, 2);
		return n0;
	}
	static Graph<Integer> buildGraph5() {
		Graph<Integer> n0 = new Graph<Integer>(0);
		Graph<Integer> n1 = new Graph<Integer>(1);
		n0.addSubgraph(n1, 0);
		n1.addSubgraph(n0, 1);
		Graph<Integer> n2 = new Graph<Integer>(2);
		n0.addSubgraph(n2, 0);
		n2.addSubgraph(n1, 2);
		n2.addSubgraph(n0, 2);
		return n0;
	}
	static Graph<Integer> buildGraph6() {
		Graph<Integer> n0 = new Graph<Integer>(0);
		n0.addSubgraph(n0, 0);
		return n0;
	}
	
	static public Graph<Point3f> buildWPGraph() {
		Point3f p0 = new Point3f(0f,0f,0f);
		Point3f p1 = new Point3f(1f,1f,0f);
		Point3f p2 = new Point3f(2f,-1f,0f);
		Point3f p3 = new Point3f(-1f,-1f,0f);
		
		Graph<Point3f> n0 = new Graph<Point3f>(p0);
		Graph<Point3f> n1 = new Graph<Point3f>(p1);
		Graph<Point3f> n2 = new Graph<Point3f>(p2);
		Graph<Point3f> n3 = new Graph<Point3f>(p3);
		n0.addSubgraph(n1, p0.distance(p1));
		n1.addSubgraph(n2, p1.distance(p2));
		n2.addSubgraph(n3, p2.distance(p3));
		n2.addSubgraph(n0, p2.distance(p0));
		n3.addSubgraph(n1, p3.distance(p1));
		n3.addSubgraph(n2, p3.distance(p2));
		return n0;
	}
	
//	static public Graph<Point3f> buildRandomWP( int nbPoint, int maxLink, float min, float max) {
//		Random rd = new Random();
//		// Random points
//		ArrayList<Point3f> lpt = new ArrayList<Point3f>();
//		for( int i=0; i<nbPoint; ++i) {
//			lpt.add( new Point3f(rd.nextFloat()*(max-min)+min,rd.nextFloat()*(max-min)+min,rd.nextFloat()*(max-min)+min));
//		}
//		// Random links
//		ArrayList<Point3f> next = new ArrayList<Point3f>();
//		next.clear();
//		while (next.size() < maxLink) {
//			
//			
//		}
//		
//	}
}
