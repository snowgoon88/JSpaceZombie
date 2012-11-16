/**
 * 
 */
package model;

/**
 * A GraphLink points to a Graph<E>, valued with a float.
 * @author snowgoon88ATgmailDOTcom
 *
 */
public class GraphLink<E> {
	
	private Graph<E> _node;
	private float _val;
	
	public GraphLink(Graph<E> node, float val) {
		super();
		this._node = node;
		this._val = val;
	}

	public Graph<E> getNode() {
		return _node;
	}
	public void setNode(Graph<E> node) {
		this._node = node;
	}

	public float getVal() {
		return _val;
	}
	public void setVal(float val) {
		this._val = val;
	}
}
