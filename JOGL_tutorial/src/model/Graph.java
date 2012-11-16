/**
 * 
 */
package model;

import java.util.ArrayList;
import java.util.Random;

import javax.vecmath.Point3f;


/**
 * Un Graphe est composé de:
 *  - E: element
 *  - float: val (valuer ce noeud du graphe )
 *  - GraphLink: link une liste de liens valués vers les sous-graphes.
 *  - GraphLink: parents une liste de liens pour remonter le graphe.
 * TODO Ajouter une static ArrayList of Graph pour rendre la recherche de max
 * et la mise à jour des valeurs plus simple. 
 * 
 * @author snowgoon88ATgmailDOTcom
 *
 */
public class Graph<E> {
	
	private E _elem;
	private float _val;
	private ArrayList<GraphLink<E>> _link;
	private ArrayList<GraphLink<E>> _parents;
	
	/**
	 * Default _val=0f.
	 * @param elem new node.
	 */
	public Graph( E elem ) {
		this._elem = elem;
		this._val = 0f;
		_link = new ArrayList<GraphLink<E>>();
		_parents = new ArrayList<GraphLink<E>>();
	}
	/**
	 * Add a link to another subgraph.
	 * @param sub
	 * @param linkVal
	 */
	public void addSubgraph( Graph<E> sub, float linkVal ) {
		_link.add( new GraphLink<E>(sub, linkVal));
		sub._parents.add(new GraphLink<E>(this, linkVal));
	}
		
	@Override
	public String toString() {
		String str = "";
		str += "Graph [_elem=" + _elem.toString() + ", _val=" + _val + "]\n";
		for (GraphLink<E> link : _link) {
			str += "   => "+link.getNode().getElem().toString()+" _val="+link.getVal()+"\n";
		}
		return str;
	}
	
	public E getElem() {
		return _elem;
	}

	public void setElem(E elem) {
		this._elem = elem;
	}
	
	public float getVal() {
		return _val;
	}
	public void setVal(float val) {
		this._val = val;
	}
	
	public ArrayList<GraphLink<E>> getLink() {
		return _link;
	}
	public ArrayList<GraphLink<E>> getParents() {
		return _parents;
	}
	
	/**
	 * Build a random graph
	 * @param nbPoint Number of Point3f in the graph
	 * @param maxLink Each elem of the graph is linked to at most maxLink other elements
	 * @param minLink Each elem of the graph is linked to at least minLink others
	 * @param min minimum of all coordinates of Point3f
	 * @param max maximum for all coordinates of Point3f
	 * @return a Graph<Point3f>
	 */
	static public Graph<Point3f> buildRandomWP( int nbPoint, int maxLink, int minLink,
			float min, float max) {
		Random rd = new Random();
		// Random points
		ArrayList<Graph<Point3f>> lgraph = new ArrayList<Graph<Point3f>>();
		for( int i=0; i<nbPoint; ++i) {
			lgraph.add( new Graph<Point3f>(new Point3f(rd.nextFloat()*(max-min)+min,
					rd.nextFloat()*(max-min)+min,
					0f)));
		}
		// Connect to other points
		for (int gIndex = 0; gIndex < lgraph.size(); gIndex++) {
			Graph<Point3f> g = lgraph.get(gIndex);
			// maxLink different Random links
			ArrayList<Integer> next = new ArrayList<Integer>();
			next.clear();
			int nbLink = minLink + rd.nextInt( maxLink-minLink );
			while (next.size() < nbLink) {
				int newIndex = rd.nextInt(lgraph.size());
				if (next.contains(newIndex) == false && newIndex != gIndex) {
					next.add(newIndex);
				}
			}
			// create links
			for (Integer index : next) {
				g.addSubgraph(lgraph.get(index),
						g.getElem().distance(lgraph.get(index).getElem()));
			}
		}
		return lgraph.get(0);
	}
}
