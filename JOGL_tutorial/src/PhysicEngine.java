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
	
	public PhysicEngine() {
		_ownTime = 0L;
		_lastTime = System.nanoTime();
		_fg_run = false;
	}
	
	public void update() {
		if (_fg_run) {
			long delta = System.nanoTime() - _lastTime;
			_ownTime += delta;
		}
	}

	public void start() {
		_lastTime = System.nanoTime();
		_fg_run = true;
	}
	public void stop() {
		_fg_run = false;
	}
	
	
	public long getOwnTime() {
		return _ownTime;
	}

}
