/**
 * 
 */
package model;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

/**
 * @author snowgoon88ATgmailDOTcom
 *
 */
public class Wall {
	
	/** Origin of Wall */
	Point3f _ori;
	/** End point of Wall */
	Point3f _end;
	/** Direction to other end point */
	Vector3f _u;
	/** Length of wall */
	float _length;
	
	final double _epsilon = 0.00001;
	
	/**
	 * Create a Wall with a starting point and a direction.
	 * @param origin
	 * @param direction
	 * @param length
	 */
	public Wall(Point3f origin, Vector3f direction, float length) {
		this._ori = origin;
		this._u = direction;
		this._length = length;
		this._end = new Point3f(direction);
		this._end.scale(length);
		this._end.add(origin);
	}
	/**
	 * Creation from 2 points.
	 * @param pt1
	 * @param pt2
	 */
	public Wall(Point3f pt1, Point3f pt2) {
		this._ori = pt1;
		this._end = pt2;
		_u = new Vector3f(pt2);
		_u.sub(pt1);
		_length = _u.length();
		_u.normalize();
	}
	
	/**
	 * Compute if line starting at 'from' in direction 'dir' intersect the Wall
	 * @param from
	 * @param dir
	 * @return null is no intersection, (intersect_point - from) otherwise
	 */
	public Vector3f intersect( Point3f from, Vector3f dir ) {
		// _ori + t._u = from + l.dir
		// (_ori + t._u) x dir = (from + l.dir) x dir
		// t.(_u x dir) = (from-_ori)xdir + l.(dir x dir) = (from-_ori)xdir + 0
		// Si (_u x dir) = 0 : parralele
		Vector3f cross = new Vector3f();
		cross.cross(_u, dir);
		if (Math.abs(cross.length()) > _epsilon ) {
			Vector3f res = new Vector3f(from);
			res.sub(_ori);
			res.cross(res, dir);
			float t = res.length() / cross.length();
			if (t >= 0.0 && t <= _length ) {
				res = new Vector3f(_u);
				res.scale(t);
				res.add(_ori);
				res.sub(from);
				return res;
			}
			else {
				return null;
			}
		}
		return null;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Wall [from="+_ori.toString()+" dir="+_u.toString()+" l="+Double.toString(_length)+"]";
	}
	/**
	 * @return the Point3f of origin
	 */
	public Point3f getOrigin() {
		return _ori;
	}
	/**
	 * @return the end Point3f of the Wall
	 */
	public Point3f getEnd() {
		return _end;
	}
	/**
	 * @return The unit vector along wall
	 */
	public Vector3f getU() {
		return _u;
	}
	/**
	 * @return The length of the Wall
	 */
	public double getLength() {
		return _length;
	}

	
}
