package model;

import java.util.ArrayList;
import java.util.NoSuchElementException;


/**
 * @author snowgoon88ATgmailDOTcom
 *
 */
public class GraphIterator<E> {
	
	private Graph<E> _graph;
	
	private ArrayList<Graph<E>> _openNodes;
	private ArrayList<Graph<E>> _closedNodes;
	
	public GraphIterator( Graph<E> graph ) {
		_graph = graph;
		_openNodes = new ArrayList<Graph<E>>();
		_openNodes.add( _graph );
		_closedNodes = new ArrayList<Graph<E>>();
	}
	
	public boolean hasNext() {
		return !_openNodes.isEmpty();
	}
	
	public Graph<E> getNext() {
		if (_openNodes.isEmpty()) 
			throw new NoSuchElementException();	
		Graph<E> _node = _openNodes.remove(0);
		_closedNodes.add( _node );
		for (GraphLink<E> link : _node.getLink()) {
			if (!_closedNodes.contains(link.getNode())) {
				_openNodes.add( link.getNode() );
			}
		}
		return _node;
	}
	

}
