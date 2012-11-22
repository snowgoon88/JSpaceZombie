import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;
import javax.vecmath.Color4f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import com.jogamp.opengl.util.FPSAnimator;

import shape3D.AgentGL;
import shape3D.EnvironmentGL;
import shape3D.PathGL;
import shape3D.ScannerGL;
import shape3D.SceneGL;
import utils.Matrix;

import model.Agent;
import model.Environment;
import model.Path;
import model.PhysicEngine;
import model.Scanner;

/**
 * 
 */

/**
 * @author snowgoon88ATgmailDOTcom
 *
 */
public class SimpleCircuit {
	
	PhysicEngine _engine = null;
	Environment _env;
	Agent _rob;
	Scanner [] _scan;
	Path _path;
	
	AgentGL _robGL;
	
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
		SceneGL scene = new SceneGL(_engine);
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
		// Environment
		_env = new Environment();
		// PhysicEngine
		_engine = new PhysicEngine(_env);
		_engine.setRunning(false);

		// Agent
		_rob = new Agent();
		_rob.setPos(new Point3f(new float [] {0.0f, 4.0f, 0.0f}));
		_rob.setAngOz(0.0f);
		_engine.add(_rob);
		// With some scanner
		_scan = new Scanner[5];
		_scan[0] = new Scanner(_rob, (float)Math.PI/2f, 2.0f);
		_scan[1] = new Scanner(_rob, (float)Math.PI/6f, 2.0f);
		_scan[2] = new Scanner(_rob, 0.0f, 2.0f);
		_scan[3] = new Scanner(_rob, -(float)Math.PI/6f, 2.0f);
		_scan[4] = new Scanner(_rob, -(float)Math.PI/2f, 2.0f);
		for (int i = 0; i < _scan.length; i++) {
			_scan[i].update(0.0f);
			_scan[i].scan(_env);
			_engine.add(_scan[i]);
		}
		// And a Path to follow
		_path = new Path();
		_path.addLast( new Point3f(4f,3f,0f));
		_path.addLast( new Point3f(3f,-2.5f,0f));
		_path.addLast( new Point3f(-4f,-1f,0f));
		_path.addLast( new Point3f(-3f,4f,0f));
		_rob.setupFollowPathBehavior( _path, 0.5f);
		_rob.setBehavior( Agent.FOLLOW);
	}
	public void setupScene(SceneGL scene) {
		EnvironmentGL _envGL = new EnvironmentGL(_env);
		scene.add(_envGL);

		_robGL = new AgentGL(_rob);
		_robGL._fg_debug = true;
		scene.add(_robGL);
		
		for (int i = 0; i < _scan.length; i++) {
			ScannerGL _scanGL = new ScannerGL(_scan[i]);
			scene.add(_scanGL);
		}
		
		PathGL _pathGL = new PathGL(_path);
		_pathGL.setColor_fg( new Color4f( 0.0f, 1.0f, 0.0f, 0.5f));
		_pathGL.setCycle(true);
		scene.add(_pathGL);
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
				for (int i = 0; i < _scan.length; i++) {
					_scan[i].update(0.0f);
					_scan[i].scan(_env);
				}
			}
			else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				Point3f pos = _rob.getPos();
				pos.setX(pos.x - 0.5f * (float)Math.cos(_rob.getAngOz()));
				pos.setY(pos.y - 0.5f * (float)Math.sin(_rob.getAngOz()));
				_rob.setPos(pos);
				for (int i = 0; i < _scan.length; i++) {
					_scan[i].update(0.0f);
					_scan[i].scan(_env);
				}
			}
			// Arrows left/right : rotate agent left/right by PI/4
			else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				_rob.setAngOz(Matrix.clipRAd(_rob.getAngOz()+(float)Math.PI/4));
				for (int i = 0; i < _scan.length; i++) {
					_scan[i].update(0.0f);
					_scan[i].scan(_env);
				}
			}
			else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				_rob.setAngOz(Matrix.clipRAd(_rob.getAngOz()-(float)Math.PI/4));
				for (int i = 0; i < _scan.length; i++) {
					_scan[i].update(0.0f);
					_scan[i].scan(_env);
				}
			}
			// VK_A : Robot speed to 0.
			else if (e.getKeyCode() == KeyEvent.VK_A) {
				_rob.setSpeed(new Vector3f());
			}
			// VK_Z : Show speed/steering vectors for Agent
			else if (e.getKeyCode() == KeyEvent.VK_Z) {
				_robGL._fg_debug = ! _robGL._fg_debug;
			}
			// VK_E : Print info on Agent
			else if (e.getKeyCode() == KeyEvent.VK_E) {
				System.out.println("Agent _pos="+_rob.getPos().toString()+" _speed="+_rob.getSpeed().toString());
			}
			
			// VK_R : run Physical Engine
			else if (e.getKeyCode() == KeyEvent.VK_R) {
				//if( _engine.isRunning()) System.out.println("Engine STOP");
				//else System.out.println("Engine START");
				_engine.setRunning( !_engine.isRunning() );
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
