/**
 * 
 */
package model;

import java.util.Random;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import shape3D.IObjectGL;
import utils.Matrix;

/**
 * @author snowgoon88ATgmailDOTcom
 *
 */
public class Agent implements Physical {
	/** Position of agent */
	private Point3f _pos;
	/** Speed vector of agent */
	private Vector3f _speed;
	/** Orientation around axe Oz */
	private float _angOz;
	/** Steering vector, if possible in "speed" reference. */
	private Vector3f _steering;
	
	private float _max_force = 5.0f;
	private float _max_speed = 10.0f;
	private float _mass = 10.0f;
	
	private int _behavior = SEEK;
	public static final int SEEK = 1;
	public static final int ARRIVE = 2;
	public static final int WANDER = 3;
	public static final int FOLLOW = 4;
	
	private Point3f _target;
	private float _slowingDistance = 1.0f;
	private float _ang_wander = 0.0f;
	private float _span = 1.0f;
	private float _noise = 0.2f;
	private Path _path;
	private float _distSwitch = 1.0f;
	private Random rd = new Random();
	
	
	public Agent() {
		_pos = new Point3f();
		_speed = new Vector3f();
		_steering = new Vector3f();
		_target = new Point3f();
		orientFromSpeed();
	}

	public Agent(Point3f pos, Vector3f speed) {
		super();
		this._pos = pos;
		this._speed = speed;
		_steering = new Vector3f();
		_target = new Point3f();
		orientFromSpeed();
	}
	/**
	 * Go as fast as possible toward a target.
	 * @param target
	 */
	private void seekBehavior( Point3f target, float deltaT) {
		target.get( _steering ); 
		_steering.sub(_pos);
		_steering.normalize();
		_steering.scale(_max_speed);
		//
		applySteeringForce(_steering, deltaT);
	}
	/**
	 * Full speed as long as not within slowingDistance to target.
	 * @param target
	 * @param slowingDistance
	 */
	private void arrivalBehavior( Point3f target, float slowingDistance, float deltaT) {
		target.get( _steering ); 
		_steering.sub(_pos);
		float distance = _steering.length();
		float desiredSpeed = 0f;
		if (distance > slowingDistance) {
			desiredSpeed = _max_speed / distance; 
		} else {
			desiredSpeed = _max_speed  * slowingDistance;
		}
		_steering.scale(desiredSpeed);
		//
		applySteeringForce(_steering, deltaT);
	}
	/**
	 * Wander : as if remembered target point was in a circle in front of the agent.
	 * @param span width of the circle
	 * @param noise amount of noise from one step to another
	 * TODO Regler les détails et les valeurs
	 */
	private void wanderBehavior( float span, float noise, float deltaT) {
		// Center of circle : ahead at half speed
		Vector3f center = new Vector3f( _speed );
		center.normalize();
		center.scale( _max_speed * 0.001f );
		// new angle
		_ang_wander = _ang_wander + (float) rd.nextGaussian() * noise;
		center.add( new Vector3f(0.001f*span*(float)Math.cos(_ang_wander), 0.001f*span*(float)Math.sin(_ang_wander), 0.0f));
		center.add(_pos);
		seekBehavior(new Point3f(center), deltaT);
	}
	/**
	 * Follow a path, as a sequence of Waypoints.
	 * @param path
	 * @param deltaT
	 */
	public void followPathBehavior( Path path, float distSwitch, float deltaT ) {
		// Check if must change Waypoint
		Point3f waypoint = path.getWaypoint();
		Vector3f toGo = new Vector3f( waypoint );
		toGo.sub( _pos );
		if (toGo.length() < distSwitch) {
			path.advanceWaypoint();
			waypoint = path.getWaypoint();
		}
		// seek this waypoint
		seekBehavior( waypoint, deltaT );
	}
	private void applySteeringForce( Vector3f steering, float deltaT) {
		Vector3f speeddelta = new Vector3f(steering);
		speeddelta.sub(_speed);
		// truncate steering force
		if (speeddelta.length() > _max_force) {
			speeddelta.scale(_max_force/speeddelta.length());
		}
		// apply to speed
		Vector3f accel = new Vector3f();
		accel.scale(1.0f/_mass, speeddelta );
		_speed.add(accel);
		// truncate speed
		if (_speed.length() > _max_speed) {
			_speed.scale(_max_speed/_speed.length());
		}
		// update position
		Vector3f _movement = new Vector3f(_speed);
		_movement.scale(deltaT);
		_pos.add(_movement);
		
		orientFromSpeed();
	}
	/**
	 * Update pos and speed according to behavior.
	 */
	public void update( float deltaT )
	{
		switch (_behavior) {
		case SEEK:
			seekBehavior(_target, deltaT);
			break;
		case ARRIVE:
			arrivalBehavior(_target, _slowingDistance, deltaT);
			break;
		case WANDER:
			wanderBehavior(_span, _noise, deltaT);
			break;
		case FOLLOW:
			followPathBehavior(_path, _distSwitch, deltaT);
			break;
		default:
			break;
		}
	}
	public void setupSeekBehavior( Point3f target) {
		_target = target;
	}
	public void setupArriveBehavior( Point3f target, float slowingDistance ) {
		_target = target;
		_slowingDistance = slowingDistance;
	}
	public void setupWanderBehevior( float span, float noise) {
		_span = span;
		_noise = noise;
	}
	public void setupFollowPathBehavior( Path path, float distSwitch ) {
		_path = path;
		_distSwitch = distSwitch;
	}

	/**
	 * Orient agent along speed vector only if [[speed|| > 0.0001.
	 */
	private void orientFromSpeed() {
		if (_speed.length() > 0.0001f) {
			float[] angles = Matrix.angleFromVec(_speed);
			_angOz = angles[0];	
		}
	}

	public Point3f getPos() {
		return _pos;
	}

	public void setPos(Point3f _pos) {
		this._pos = _pos;
	}

	public Vector3f getSpeed() {
		return _speed;
	}

	public void setSpeed(Vector3f _speed) {
		this._speed = _speed;
		orientFromSpeed();
	}

	public float getAngOz() {
		return _angOz;
	}

	public void setAngOz(float angOz) {
		this._angOz = angOz;
	}

	public Vector3f getSteering() {
		return _steering;
	}

	public int getBehavior() {
		return _behavior;
	}

	public void setBehavior(int _behavior) {
		this._behavior = _behavior;
	}

}
