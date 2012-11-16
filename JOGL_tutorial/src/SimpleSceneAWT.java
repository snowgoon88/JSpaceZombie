import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;

import utils.Matrix;

import com.jogamp.common.util.RunnableExecutor.CurrentThreadExecutor;
import com.jogamp.graph.curve.OutlineShape.VerticesState;
import com.jogamp.opengl.util.FPSAnimator;

/**
 * Using JOGL Tutorial [https://sites.google.com/site/justinscsstuff/jogl-tutorial-2],
 * creation of a simple AWT Windows with JOGL Canva.
 * Added triangle drawing.
 */

/**
 * @author snowgoon88ATgmailDOTcom
 *
 */
public class SimpleSceneAWT implements GLEventListener {
	// Model variables
    private double theta = 0;
    private double s = 0;
    private double c = 0;
    
    static long oldTime, currentTime, lastReportTime;
    static int sizeTimeArray = 10;
    static long updateArray[], renderArray[], sleepArray[];
    static int nbFrame = 0;
    static int frameReport = 0;
    DecimalFormat df = new DecimalFormat( "0.##" );
    DecimalFormat timedf = new DecimalFormat( "0.####" );
    
    // Component
    static Label _infoLabel;
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Matrix.clipDeg(0);
		
		updateArray = new long[sizeTimeArray];
		renderArray = new long[sizeTimeArray];
		sleepArray = new long[sizeTimeArray];
		
		// JOGL components, GLContext automatically created in GLCanvas
		GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        GLCanvas canvas = new GLCanvas(caps);

        
        // AWT Frame
        Frame frame = new Frame("AWT Window Test");
        frame.setSize(300, 300);
        frame.setLayout( new BorderLayout());
        frame.add(canvas, BorderLayout.CENTER);
        _infoLabel = new Label();
        frame.add( _infoLabel, BorderLayout.PAGE_END);
        frame.setVisible(true);
        
        // by default, an AWT Frame doesn't do anything when you click
        // the close button; this bit of code will terminate the program when
        // the window is asked to close
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        
        // Add itself as event listener for drawing
        canvas.addGLEventListener(new SimpleSceneAWT());
        
        // Animator with consistent FPS
        FPSAnimator animator = new FPSAnimator(canvas, 60);
        animator.add(canvas);
        oldTime = System.nanoTime();
        lastReportTime = oldTime;
        animator.start();
	} 

	// From GLEventListener
	@Override
	public void display(GLAutoDrawable drawable) {
		currentTime = System.nanoTime();
		sleepArray[ nbFrame % sizeTimeArray ] = currentTime - oldTime;
		oldTime=currentTime;
		
		// model update
		update();
		currentTime = System.nanoTime();
		updateArray[ nbFrame % sizeTimeArray ] = currentTime - oldTime;
		oldTime=currentTime;
		
		// draw 
	    render(drawable);
	    currentTime = System.nanoTime();
		renderArray[ nbFrame % sizeTimeArray ] = currentTime - oldTime;
		oldTime=currentTime;
		
	
		if (currentTime - lastReportTime > 2000000000L) {
			statReport();
			lastReportTime = System.nanoTime();
			frameReport = nbFrame;
		}
		nbFrame++;
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
		
	    // draw a triangle filling the window
	    gl.glBegin(GL.GL_TRIANGLES);
	    gl.glColor3f(1, 0, 0);
	    gl.glVertex2d(-c, -c);
	    gl.glColor3f(0, 1, 0);
	    gl.glVertex2d(0, c);
	    gl.glColor3f(0, 0, 1);
	    gl.glVertex2d(s, -s);
	    gl.glEnd();
		
	}

	/**
	 * Update the model
	 */
	private void update() {
		// nothing to do here
		theta += 0.01;
		s = Math.sin(theta);
		c = Math.cos(theta);
	}

	// From GLEventListener	
	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}
	// From GLEventListener
	@Override
	public void init(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}
	// From GLEventListener
	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		// TODO Auto-generated method stub
		
	}
	
	void statReport() {
		double averageFPS = (double) (nbFrame - frameReport) / (double) (currentTime - lastReportTime) * 1000000000L;
		
		long meanRender = 0, meanUpdate=0, meanSleep=0;
		for (int i = 0; i < sizeTimeArray; i++) {
			meanUpdate += updateArray[i];
			meanRender += renderArray[i];
			meanSleep += sleepArray[i];
		}
		
		String info = timedf.format( (double) (currentTime - lastReportTime) / 1000000000 ) + " " +
				df.format( averageFPS ) + " afps " +
				timedf.format( (double) meanUpdate / 1000000 / (double) sizeTimeArray) + " up " +
				timedf.format( (double) meanRender / 1000000 / (double) sizeTimeArray) + " re " +
				timedf.format( (double) meanSleep / 1000000 / (double) sizeTimeArray) + " sl ";
		_infoLabel.setText( info );
		System.out.println( info );
		
	}

}
