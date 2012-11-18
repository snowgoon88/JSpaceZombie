import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;
import javax.vecmath.Point3f;

import com.jogamp.opengl.util.FPSAnimator;

import shape3D.AgentGL;
import shape3D.EnvironmentGL;
import shape3D.SceneGL;
import utils.Matrix;

import model.Agent;
import model.Environment;

/**
 * 
 */

/**
 * @author snowgoon88ATgmailDOTcom
 *
 */
public class SimpleCircuit {
	
	Environment _env;
	Agent _rob;
	JFrame _frame;
	
	public static void main(String[] args) {
		SimpleCircuit app = new SimpleCircuit();
	}
	
	public SimpleCircuit() {
		// JOGL components, GLContext automatically created in GLCanvas
		GLProfile glp = GLProfile.getDefault();
		GLCapabilities caps = new GLCapabilities(glp);
		GLCanvas canvas = new GLCanvas(caps);

		// Model
		setupWorld();
		
		// Scene
		SceneGL scene = new SceneGL();
		setupScene(scene);
		
		// AWT Frame
		_frame = new JFrame("Arm - Java2D API");
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
		canvas.addKeyListener(new TopLevelKeyListener());
		canvas.addKeyListener(new AgentKeyListener() );

		// Animator with consistent FPS
		FPSAnimator animator = new FPSAnimator(canvas, 60);
		animator.add(canvas);
		animator.start();
	}

	public void setupWorld() {
		_env = new Environment();
		_rob = new Agent();
		_rob.setPos(new Point3f(new float [] {0.0f, 4.0f, 0.0f}));
		_rob.setAngOz(0.0f);
	}
	public void setupScene(SceneGL scene) {
		EnvironmentGL _envGL = new EnvironmentGL(_env);
		scene.add(_envGL);

		AgentGL _robGL = new AgentGL(_rob);
		scene.add(_robGL);
	}

	class AgentKeyListener implements KeyListener {

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub

		}
		@Override
		public void keyPressed(KeyEvent e) {
			// Arrows up/down : move agent forward/backward
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				Point3f pos = _rob.getPos();
				pos.setX(pos.x + 0.5f * (float)Math.cos(_rob.getAngOz()));
				pos.setY(pos.y + 0.5f * (float)Math.sin(_rob.getAngOz()));
				_rob.setPos(pos);
			}
			else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				Point3f pos = _rob.getPos();
				pos.setX(pos.x - 0.5f * (float)Math.cos(_rob.getAngOz()));
				pos.setY(pos.y - 0.5f * (float)Math.sin(_rob.getAngOz()));
				_rob.setPos(pos);
			}
			// Arrows left/right : rotate agent left/right by PI/4
			else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				_rob.setAngOz(Matrix.clipRAd(_rob.getAngOz()+(float)Math.PI/4));
			}
			else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				_rob.setAngOz(Matrix.clipRAd(_rob.getAngOz()-(float)Math.PI/4));
			}
		}
		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub

		}

	}
	
	/**
	 * Close Window when ESC
	 * 
	 * TODO Does not work as soon as canvas has the focus. 
	 */
	class TopLevelKeyListener implements KeyListener {
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
