/**
 * 
 */
package shape3D;

import javax.media.opengl.GL2;

/**
 * Interface that must implement any ObjetGL in order to be rendered
 * in a SceneGL.
 * 
 * @author snowgoon88ATgmailDOTcom
 */
public interface IObjectGL {

	public void render(GL2 gl);
	public void renderSelect(GL2 gl);
}
