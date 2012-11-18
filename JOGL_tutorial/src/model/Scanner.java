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
public class Scanner {
	
	/** Relative orientation */
	float _rang;
	/** Maximum range */
	float _maxRange;
	/** Relative Vector3f to obstacle */
	Vector3f _obs;
	
	/** Attached to an Agent */
	Agent _agent;
	
	/**
	 * Create attached to an Agent.
	 */
	public Scanner( Agent agent, float relativeAngle, float maxRange ) {
		_agent = agent;
		_rang = relativeAngle;
		_maxRange = maxRange;
		float aang = _agent.getAngOz()+_rang;
		// set as if obstacle at maximum range
		_obs = new Vector3f(_maxRange*(float)Math.cos(aang), _maxRange*(float)Math.sin(aang), 0f);
	}
	
	/**
	 * Look among all Wall of the Environment which one has the closest intersection.
	 * @param env
	 */
	public void scan( Environment env ) {
		// Current position
		Point3f pos = getPos();
		//System.out.println("Scan from="+pos.toString()+", _obs="+_obs.toString());
		// Distance to nearest impact
		float scanDist = Float.MAX_VALUE;
		for (Wall w : env.getWalls()) {
			//System.out.println("  scanning "+w.toString()+"with obs="+_obs.toString()); 
			Vector3f intersect = w.intersect(pos, _obs);
			if (intersect != null ) {
				//System.out.println("  IMPACT at "+intersect.toString());
				if (intersect.length() < scanDist && intersect.length() < _maxRange) { // New hit
					//System.out.println("  => new better hit");
					scanDist = intersect.length();
					_obs = intersect;
				}
			}
		}
	}
	/**
	 * When agent's angle has changed, update _obs.
	 */
	public void update() {	
		float aang = _agent.getAngOz()+_rang;
		// set as if obstacle at maximum range
		_obs = new Vector3f(_maxRange*(float)Math.cos(aang), _maxRange*(float)Math.sin(aang), 0f);	
	}
	
	public Point3f getPos() {
		return _agent.getPos();
	}
	public Vector3f getObstacle() {
		return _obs;
	}
}
