
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;

import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;

/**
 * Using JOGL Tutorial [https://sites.google.com/site/justinscsstuff/jogl-tutorial-2],
 * creation of a simple NEWT Windows with JOGL Canva.
 */

/**
 * @author snowgoon88ATgmailDOTcom
 *
 */
public class SimpleSceneNEWT {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// JOGL components, GLContext automatically created in GLWindow
		GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);

        GLWindow window = GLWindow.create(caps);
        window.setSize(300, 300);
        window.setVisible(true);
        window.setTitle("NEWT Window Test");

        window.addWindowListener(new WindowAdapter() {
            public void windowDestroyNotify(WindowEvent arg0) {
                System.exit(0);
            };
        });

	}
}
