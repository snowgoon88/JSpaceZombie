import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.vecmath.Color4f;

import shape3D.Arrow;
import shape3D.Basis;

import com.jogamp.opengl.util.FPSAnimator;

/**
 * Using JOGL Tutorial [https://sites.google.com/site/justinscsstuff/jogl-tutorial-2],
 * creation of a simple AWT Windows with JOGL Canva.
 * 
 * To test shape3D stuff.
 */

/**
 * @author snowgoon88ATgmailDOTcom
 *
 */
public class TestSceneAWT implements GLEventListener {
	// Model variables
    static Arrow _arrow_x;
    static Basis _basis;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// JOGL components, GLContext automatically created in GLCanvas
		GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        GLCanvas canvas = new GLCanvas(caps);

        
        // AWT Frame
        Frame frame = new Frame("AWT Window Test");
        frame.setSize(300, 300);
        frame.add(canvas);
        frame.setVisible(true);
        
        // by default, an AWT Frame doesn't do anything when you click
        // the close button; this bit of code will terminate the program when
        // the window is asked to close
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        // Model
        _arrow_x = new Arrow( new Color4f( 1,0,0,0));
        _basis = new Basis();
        
        // Add itself as event listener for drawing
        canvas.addGLEventListener(new TestSceneAWT());
        
        // Animator with consistent FPS
        FPSAnimator animator = new FPSAnimator(canvas, 60);
        animator.add(canvas);
        animator.start();
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		// model update
		update();
		// draw 
	    render(drawable);
		
	}
	/**
	 * Render the model
	 * @param drawable Where to render
	 */
	private void render(GLAutoDrawable drawable) {
		// Need GL2 methods, availability dealt with by GLProfile
		GL2 gl = drawable.getGL().getGL2();
	    
		// Clear color buffet (background)
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		
		// TEMP scale down
		gl.glPushMatrix();
		gl.glScalef(0.7f, 0.7f, 0.7f);
		
	    // draw Model
		//_arrow_x.render( gl );
		_basis.render( gl );
		
		// Restore
		gl.glPopMatrix();
		
	}

	/**
	 * Update the model
	 */
	private void update() {
		// nothing to do here
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		// TODO Auto-generated method stub
		
	}

}
