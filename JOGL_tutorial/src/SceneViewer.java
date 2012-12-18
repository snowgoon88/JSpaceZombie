
import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;

import com.jogamp.opengl.util.FPSAnimator;

import shape3D.SceneGL;


/**
 * To test SceneGL.
 * 
 * Using JOGL Tutorial [https://sites.google.com/site/justinscsstuff/jogl-tutorial-2],
 * creation of a simple AWT Windows with JOGL Canva.
 * 
 * @author snowgoon88ATgmailDOTcom
 *
 */
public class SceneViewer {

	static JFrame _frame;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SceneViewer app = new SceneViewer();
		app.buildGUI();
	}
	
	public void buildGUI() {
		// JOGL components, GLContext automatically created in GLCanvas
		GLProfile glp = GLProfile.getDefault();
		GLCapabilities caps = new GLCapabilities(glp);
		GLCanvas canvas = new GLCanvas(caps);

		// Scene
		SceneGL scene = new SceneGL();

		// Swing Frame
		_frame = new JFrame("jArm - Java2D API");
		_frame.setSize(600,600);
		_frame.setLayout(new BorderLayout());
		_frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		_frame.add(canvas, BorderLayout.CENTER);
		
		_frame.addKeyListener( new TopLevelKeyListener());
		_frame.setVisible(true);

		// Add scene as event listener for drawing
        canvas.addGLEventListener(scene);
        canvas.addMouseListener(scene);
        canvas.addMouseMotionListener(scene);
        canvas.addKeyListener(scene);
        
        // Animator with consistent FPS
        FPSAnimator animator = new FPSAnimator(canvas, 60);
        animator.add(canvas);
        animator.start();
		
	}
	
	/**
	 * Close Window when ESC
	 * 
	 * TODO Does not work as soon as canvas has the focus. 
	 */
	static class TopLevelKeyListener implements KeyListener {
		@Override
		public void keyPressed(KeyEvent e) {
			// VK_D : reset to default values
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				_frame.dispatchEvent( new WindowEvent(_frame, WindowEvent.WINDOW_CLOSING));
			}
		}
		@Override
		public void keyTyped(KeyEvent e) {	
		}
		@Override
		public void keyReleased(KeyEvent e) {
		}
	}
}
