package model;
import java.util.ArrayList;
import java.util.Iterator;


import shape3D.IObjectGL;

/**
 * 
 */

/**
 * @author snowgoon88ATgmailDOTcom
 *
 */
public class PhysicEngine {
	
	/** Last time Model was updated. */
	private long _lastTime;
	/** Time of running. */
	private long _ownTime;
	/** flag run */
	private boolean _fg_run;
	
	/** Environment to consider */
	Environment _env = null;
	/** Physical Objects to manage */
	private ArrayList<Physical> _objects = new ArrayList<Physical>();
	
	public PhysicEngine(Environment env ) {
		_env =env;
		
		_ownTime = 0L;
		_lastTime = System.nanoTime();
		_fg_run = false;
	}
	
	public void update() {
		if (_fg_run) {
			long timeNow = System.nanoTime();
			float deltaT = (float)(timeNow - _lastTime) / 1000000000.0f;
			
			
			// Update position
			for (Physical obj : _objects) {
				obj.update(deltaT);
			}
			// Scan
			for (Physical obj : _objects) {
				obj.scan(_env);
			}

			_ownTime += timeNow - _lastTime;
			_lastTime = timeNow;
		}
	}

	public void setRunning(boolean run) {
		_lastTime = System.nanoTime();
		_fg_run = run;
	}
	public boolean isRunning() {
		return _fg_run;
	}
	
	
	public long getOwnTime() {
		return _ownTime;
	}

	public int size() {
		return _objects.size();
	}
	public boolean isEmpty() {
		return _objects.isEmpty();
	}
	
	public boolean contains(Physical obj) {
		return _objects.contains( obj );
	}
	
	public Iterator<Physical> iterator() {
		return _objects.iterator();
	}

	public boolean add(Physical obj) {
		return _objects.add( obj );
	}
	public boolean remove(Physical obj) {
		return _objects.remove( obj );
	}
	
	public void clear() {
		_objects.clear();
	}
	
}
