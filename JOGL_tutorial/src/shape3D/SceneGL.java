
package shape3D;

import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.nio.IntBuffer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.fixedfunc.GLMatrixFunc;
import javax.media.opengl.glu.GLU;

import model.PhysicEngine;

import utils.Matrix;

import com.jogamp.common.nio.Buffers;


/**
 * A scene is made of OpenGL objects.
 * The view can be rotated using the mouse left button.
 * 
 * 
 * @author snowgoon88ATgmailDOTcom
 *
 */
public class SceneGL implements GLEventListener, MouseListener, MouseMotionListener, KeyListener{

	/** Decimal formating */
	DecimalFormat df4 = new DecimalFormat( "0.####" );
	
	/** for PickingUp */
	private static final int BUFSIZE = 512;
	private IntBuffer selectIntBuffer = Buffers.newDirectIntBuffer(BUFSIZE);
	/** Flag for picking */
	private boolean _fg_picking = false;

	/** The model : 3 arrows */
	private Basis _basis = new Basis();
	
	/** Viewer */
	private ArrayList<IObjectGL> _objects = new ArrayList<IObjectGL>();
	/** Physical Engine */
	private PhysicEngine _physical;
	
	// Running
	private boolean _fg_run = false;
	private long _lastTime = 0L;
	
	
	// Class variables required to implement the virtual sphere.
	/** VirtualSphere drawn around cue point */
	private VirtualSphereGL _virtSphere = new VirtualSphereGL();
	/** Center of the rotation : center of drawable */
	private Point _cueCenter = new Point();
	/** */
	private int _cueRadius;
	/** Rotation matrix */
	private float _rotMatrix[] = Matrix.identity();
	/** The incremental rot-matrix calculated by the virtual sphere */
	private float[] _mouseMtx = new float[16];
	/** The translation of the Scene */
	private Point _sceneTranslate = new Point();
	/** Zoom level */
	private Point _sceneZoom = new Point();
	private float _zoom = 0.6f;

	// State of the mouse left button
	private boolean _mouseLDown = false;
	// State of the mouse right button
	private boolean _mouseRDown = false;
	// State of the mouse middle button
	private boolean _mouseMDown = false;
	// Previous position of the mouse
	private Point _prevMouse;
	
	/**
	 * Create with a null PhysicEngine.
	 */
	public SceneGL() {
		this._physical = null;
	}
	/**
	 * Create with a PhysicEngine
	 */
	public SceneGL(PhysicEngine _physical) {
		super();
		this._physical = _physical;
	}
	
	private void render(GL2 gl) {
		// Scene render
		_basis.render( gl );
		
		for (IObjectGL obj : _objects) {
			obj.render(gl);
		}
	}
	private void renderSelect(GL2 gl) {
	}
	private void startPickup(GL2 gl) {
		GLU glu = new GLU();
		// initialize the selection buffer
		gl.glSelectBuffer(BUFSIZE, selectIntBuffer);
		
		// switch to selection mode, using an empty name stack
		gl.glRenderMode(GL2.GL_SELECT);
		gl.glInitNames();
		
		// save the original projection matrix
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glPushMatrix();
		gl.glLoadIdentity();
		
		// get the current viewport = (x,y,width,height)
		int viewport[] = new int[4];
		gl.glGetIntegerv(GL.GL_VIEWPORT, viewport, 0);
		
		// create a 5x5 pixel picking volume near the cursor location
		glu.gluPickMatrix((double) _prevMouse.x, (double) (viewport[3] - _prevMouse.y),
				5.0, 5.0, viewport, 0);
		
		/* set projection (perspective or orthogonal) exactly as it is in
		normal rendering (i.e. duplicate the gluPerspective() call
		in resizeView()) */
		//glu.gluPerspective(45.0, (float)panelWidth/(float)panelHeight, 1, 100);
		gl.glFrustum(-1.0f, 1.0f, -(float)viewport[3]/(float)viewport[2],
				(float)viewport[3]/(float)viewport[2], 5.0f, 60.0f);
		// Restore model view
		gl.glMatrixMode(GL2.GL_MODELVIEW);
	}
	private void stopPickup(GL2 gl) {
	// stop Picking process
		// restore original projection matrix
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glPopMatrix();
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glFlush();
		// return to normal rendering mode, and process hits
		int numHits = gl.glRenderMode(GL2.GL_RENDER);
		
		/* Process hits returned */
		System.out.println("*** PICKING ***");
		if (numHits == 0) {
			System.out.println("=> no hits");
		}
		// Loop through hits
		int offset = 0;
		for (int i=0; i < numHits; i++) {
			System.out.println("Hit: " + (i + 1));
			int numNames = selectIntBuffer.get(offset);
			offset++;
			// minZ and maxZ are taken from the Z buffer
			long depth = (long) selectIntBuffer.get(offset); // large -ve number
			float minZ = (1.0f + ((float) depth / 0x7fffffff));
			offset++;
			//
			depth = (long) selectIntBuffer.get(offset); // large -ve number
			float maxZ = (1.0f + ((float) depth / 0x7fffffff));
			offset++;
			System.out.println(" minZ: " + df4.format(minZ) +
					"; maxZ: " + df4.format(maxZ));
			// print name IDs stored on the name stack
			System.out.print(" Name(s): ");
			int nameID;
			for (int j=0; j < numNames; j++) {
				nameID = selectIntBuffer.get(offset);
				System.out.println( "  "+nameID );
				offset++;
			}
		}
		// Look for closest object
		float minObjectDist = Float.MAX_VALUE;
		int nearestObjInd = -1;
		offset = 0;
		for (int i=0; i < numHits; i++) {
			// nb Hits
			int numNames = selectIntBuffer.get(offset);
			offset++;
			// minZ and maxZ are taken from the Z buffer
			long depth = (long) selectIntBuffer.get(offset); // large -ve number
			float minZ = (1.0f + ((float) depth / 0x7fffffff));
			offset++;
			// maxDepth
			offset++;
			// name IDs stored on the name stack
			int nameID = -1;
			if (numNames > 0) {
				nameID = selectIntBuffer.get(offset);
			}
			// offset for other names
			for (int j=0; j < numNames; j++) {
				offset++;
			}
			// BETTER ??
			if (minZ < minObjectDist) {
				minObjectDist = minZ;
				nearestObjInd = nameID;
			}
		}
		
		_fg_picking = false;
		// End of Picking
	}
	
	
	@Override
	public void mouseDragged(MouseEvent e) {
		Point newMouse = e.getPoint();

		if (_mouseLDown == true ) {
			if (newMouse.x != _prevMouse.x || newMouse.y != _prevMouse.y) {

				// Use virtual sphere to determine an incremental rotation matrix.
				_virtSphere.makeRotationMtx(_prevMouse, newMouse, _cueCenter, _cueRadius, _mouseMtx);

				// Multiply overall rotation matrix by incremental rot-matrix to get new rotation.
				_rotMatrix = Matrix.multiply(_rotMatrix, _mouseMtx);
				fixRotationMatrix();

				_prevMouse = newMouse;
			}
		}
		else if (_mouseRDown == true) {
			if (newMouse.x != _prevMouse.x || newMouse.y != _prevMouse.y) {
				_sceneTranslate.x += (newMouse.x - _prevMouse.x);
				_sceneTranslate.y += (newMouse.y - _prevMouse.y);
				
				_prevMouse = newMouse;
			}
		}
		else if (_mouseMDown == true) {
			if (newMouse.x != _prevMouse.x || newMouse.y != _prevMouse.y) {
				_sceneZoom.y = (newMouse.y - _prevMouse.y);
				
				_prevMouse = newMouse;
			}
		}
	}
	private void fixRotationMatrix() {
		// Fix any problems with the rotation matrix.
		_rotMatrix[3] =
		_rotMatrix[7] =
		_rotMatrix[11] =
		_rotMatrix[12] =
		_rotMatrix[13] =
		_rotMatrix[14] = 0.0f;
		_rotMatrix[15] = 1.0f;
		float fac;
		if ((fac = (float)Math.sqrt
			((_rotMatrix[0]*_rotMatrix[0]) +
			 (_rotMatrix[4]*_rotMatrix[4]) +
			 (_rotMatrix[8]*_rotMatrix[8]))) != 1.0f)
		{
			if (fac != 0.0f)
			{
				fac = 1.0f/fac;
				_rotMatrix[0] *= fac;
				_rotMatrix[4] *= fac;
				_rotMatrix[8] *= fac;
			}
		}
		if ((fac = (float)Math.sqrt
			((_rotMatrix[1]*_rotMatrix[1]) +
			 (_rotMatrix[5]*_rotMatrix[5]) +
			 (_rotMatrix[9]*_rotMatrix[9]))) != 1.0f)
		{
			if (fac != 0.0f)
			{
				fac = 1.0f/fac;
				_rotMatrix[1] *= fac;
				_rotMatrix[5] *= fac;
				_rotMatrix[9] *= fac;
			}
		}
		if ((fac = (float)Math.sqrt
			((_rotMatrix[2]*_rotMatrix[2]) +
			 (_rotMatrix[6]*_rotMatrix[6]) +
			 (_rotMatrix[10]*_rotMatrix[10]))) != 1.0f)
		{
			if (fac != 0.0f)
			{
				fac = 1.0f/fac;
				_rotMatrix[2] *= fac;
				_rotMatrix[6] *= fac;
				_rotMatrix[10] *= fac;
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		//_mouseDown = true;
		_prevMouse = e.getPoint();
		if ((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0) {
			if (e.isControlDown()) {
				_fg_picking = true;
			}
			else {
				_mouseLDown = true;
			}
		}
		else if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {
			_mouseRDown = true;
		}
		else if ((e.getModifiers() & InputEvent.BUTTON2_MASK) != 0) {
			_mouseMDown = true;
		}
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		//_mouseDown = false;
		if ((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0) {
			_mouseLDown = false;
		}
		else if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {
			_mouseRDown = false;
		}
		else if ((e.getModifiers() & InputEvent.BUTTON2_MASK) != 0) {
			_mouseMDown = false;
			_sceneZoom.y = 0;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void display(GLAutoDrawable drawable) {
		
		// Update the model
		if (_physical != null) {
			_physical.update();	
		}
		
		// Display : set up Scene
		GL2 gl = drawable.getGL().getGL2();
		
		// Between Picking and Drawing
		if (_fg_picking) {
			startPickup(gl);
		}
		else {
			gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
			gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
			
			gl.glEnable( GL2.GL_DEPTH_TEST );
			gl.glDepthMask( true );
			gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
			gl.glEnable( GL.GL_BLEND );
		}
		gl.glPushMatrix();

		// translation : need to transform mouseDeplacement to correct 'viewDeplacement'
		// because of frustrum, view_width/width_at_reference = frustrum_near/z_of_reference
		float widthRef = 2.0f * 15.0f / 5.0f;
		float xRatio = widthRef / drawable.getWidth();
		gl.glTranslatef(_sceneTranslate.x * xRatio, -_sceneTranslate.y *xRatio, 0.0f);
		gl.glMultMatrixf(_rotMatrix,0);
		// zoom : same than translate
		_zoom = _zoom * (1.0f + (_sceneZoom.y * xRatio));
		if (_zoom < 0.1f) {
			_zoom = 0.1f;
		}
		else if (_zoom > 10.0f) {
			_zoom = 10.0f;
		}
		gl.glScalef(_zoom, _zoom, _zoom );
		
		if (_fg_picking) {
			// Render selectable objects
			renderSelect(gl);
		} else {
			// Render all GL objects
			render(gl);
		}
		gl.glPopMatrix();

		if (_fg_picking) {
			stopPickup(gl);
		} else {
			// If the mouse is down, draw the virtual sphere on-screen representation.
			if (_mouseLDown)	renderVirtSphere(drawable);
		} // end else
	}
	
	@Override
	public void dispose(GLAutoDrawable drawable) {

	}

	@Override
	public void init(GLAutoDrawable drawable) {
		// Initialize the VirtualSphereGL.
		setupVirtSphere(drawable.getWidth(), drawable.getHeight());
		// Set the initial view angles.
		_rotMatrix = Matrix.identity();
		Matrix.rotateZ(Matrix.deg2Rad(0), _rotMatrix);
		Matrix.rotateX(Matrix.deg2Rad(0), _rotMatrix);
		// Translate
		_sceneTranslate.setLocation(0, 0);
		// zoom
		_zoom = 0.5f;
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		float h = (float)height / (float)width;

		GL2 gl = drawable.getGL().getGL2();

		gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);	

		gl.glLoadIdentity();

		// project to (-1,1)(-h,h) from depth 5 to 60
		// and set back origin by -15 along Oz
		gl.glFrustum(-1.0f, 1.0f, -h, h, 5.0f, 60.0f);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glTranslatef(0.0f, 0.0f, -15.0f);
		
		// Resize the virtual sphere too!
		setupVirtSphere(width, height);

	}

	/**
	 *  Set up the virtual sphere cue center and radius.
	 **/
	private void setupVirtSphere(int w, int h) {
		_cueCenter.x = w/2;
		_cueCenter.y = h/2;
		_cueRadius = Math.min(w-20, h-20)/2;
	}
	/**
	*  Method that sets the drawing characteristics and then
	*  renders the virtual sphere feedback graphics.
	**/
	private void renderVirtSphere(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		
		//	First, save off some state variables that we are going to change.
		boolean lighting = gl.glIsEnabled(GL2.GL_LIGHTING);
		boolean texture2D = gl.glIsEnabled(GL2.GL_TEXTURE_2D);
		boolean fog = gl.glIsEnabled(GL2.GL_FOG);
		
		//	Make sure the GL settings are correct.
		gl.glDisable(GL2.GL_LIGHTING);
		gl.glDisable(GL2.GL_FOG);

		//	Set the line color.
		gl.glColor3f(0.5F, 0.5F, 0.5F);
		
		//	Render the virtual sphere.
		GLU glu = new GLU();
		_virtSphere.draw(gl, glu, drawable.getWidth(), drawable.getHeight(), _prevMouse, _cueCenter, _cueRadius);

		//	Reset state machine settings changed above.
		if (lighting)	gl.glEnable(GL2.GL_LIGHTING);
		if (texture2D)	gl.glEnable(GL2.GL_TEXTURE_2D);
		if (fog)		gl.glEnable(GL2.GL_FOG);
		
	}
	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		// VK_D : reset to default values
		if (e.getKeyCode() == KeyEvent.VK_D) {
			// Set the initial view angles.
			_rotMatrix = Matrix.identity();
			Matrix.rotateZ(Matrix.deg2Rad(-45), _rotMatrix);
			Matrix.rotateX(Matrix.deg2Rad(60), _rotMatrix);
			// Translate
			_sceneTranslate.setLocation(0, 0);
			// zoom
			_zoom = 0.5f;
		}
		// VK_G : Grid on/off
		else if (e.getKeyCode() == KeyEvent.VK_G) {
			_basis._fg_grid = ! _basis._fg_grid;
		}	
		// VK_P : planar to Oxy
		else if (e.getKeyCode() == KeyEvent.VK_P) {
			// Set the initial view angles.
			_rotMatrix = Matrix.identity();
			Matrix.rotateY(Matrix.deg2Rad(0), _rotMatrix);
			Matrix.rotateX(Matrix.deg2Rad(0), _rotMatrix);
			_zoom = 0.5f;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}
	
	public int size() {
		return _objects.size();
	}
	public boolean isEmpty() {
		return _objects.isEmpty();
	}
	
	public boolean contains(IObjectGL obj) {
		return _objects.contains( obj );
	}
	
	public Iterator<IObjectGL> iterator() {
		return _objects.iterator();
	}

	public boolean add(IObjectGL obj) {
		return _objects.add( obj );
	}
	public boolean remove(IObjectGL obj) {
		return _objects.remove( obj );
	}
	
	public void clear() {
		_objects.clear();
	}

}
