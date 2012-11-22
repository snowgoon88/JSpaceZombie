/**
 * 
 */
package model;

import java.util.ArrayList;

import javax.vecmath.Point3f;
import javax.vecmath.Point3f;

/**
 * @author snowgoon88ATgmailDOTcom
 *
 */
public class Path {
	
	/** List of Waypoints */
	private ArrayList<Point3f> _listWP = new ArrayList<Point3f>();
	/** Index of current Waypoint */
	private int _indexWP = 0;
	
	public Path() {
		_indexWP = 0;
	}

	public Point3f getWaypoint() {
		return _listWP.get(_indexWP);
	}
	public void advanceWaypoint() {
		_indexWP = (_indexWP+1)% _listWP.size();
	}
	
	public void addFirst( Point3f pt) {
		_listWP.add(0, pt);
	}
	public void addLast( Point3f pt ) {
		_listWP.add( pt );
	}
	public void concat( Path other) {
		_listWP.addAll(other.getListWP());
	}

	public ArrayList<Point3f> getListWP() {
		return _listWP;
	}

	public void clear() {
		_listWP.clear();
	}
	
	@Override
	public String toString() {
		String str = "PATH\n";
		for (Point3f pt : _listWP) {
			str += "   "+pt.toString()+", ";
		}
		return str;
	}

	/**
	 * Build a simple Path as example.
	 * @return new Path
	 */
	static public Path buildExample() {
		Path path = new Path();
		path.init();
		return path;
	}
	private void init() {
		_listWP.clear();
		_listWP.add( new Point3f(0.0f,5.0f,0.0f));
		_listWP.add( new Point3f(5.0f,0.0f,0.0f));
		_listWP.add( new Point3f(2.0f,-8.0f,0.0f));
		_listWP.add( new Point3f(-3.0f,-3.0f,0.0f));
		_listWP.add( new Point3f(-7.0f,0.0f,0.0f));
		_indexWP = 0;
	}
}
