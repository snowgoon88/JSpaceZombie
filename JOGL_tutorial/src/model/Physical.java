/**
 * 
 */
package model;

/**
 * @author snowgoon88ATgmailDOTcom
 *
 */
public interface Physical {
	
	/**
	 * Update Model when deltaT seconds have passed.
	 * @param deltaT in seconds.
	 */
	public void update( float deltaT);
	/**
	 * Scan its environment.
	 * @param env
	 */
	public void scan( Environment env);

}
