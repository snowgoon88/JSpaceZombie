/**
 * 
 */
package shape3D;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2GL3;
import javax.vecmath.Color4f;
import javax.vecmath.Point3f;

import utils.Matrix;

/**
 * TODO Blend
 * 
 * @author snowgoon88ATgmailDOTcom
 *
 */
public class CubeGL {
	/** Color of the arrow */
	Color4f _color_fg;
	/** Position of arrow */
	Point3f _pos;
	/** Euler angles */
	float _angZ1 = 0f;
	float _angY2 = 0f;
	/** Size of Cube */
	float _size = 1f;
	
	/** selection mode */
	private boolean _fg_select_mode = false;
	/** is selected ? */
	public boolean _fg_selected = false;
	
	/** name */
	String _name = "";
	/** Unique id */
	private int _id;
	/** Count identifier */
	static private int _class_id;
	
	
	/**
	 * Create cube aligned to axes, at given position.
	 */
	public CubeGL(Point3f position, float size) {
		super();
		this._pos = position;
		this._size = size;
		
		_color_fg = new Color4f( 0f, 0f, 1f, 1f);
		
		_id = _class_id;
		_name = "CubeGL_"+_id;
		_class_id++;
	}
	
	public void render( GL2 gl )  {
		// If selected, draw a cube around.
//		if (_fg_selected) {
//			Color4f color_fg_ori = new Color4f(_color_fg);
//			//setColor_fg(_color_fg.x, _color_fg.y, _color_fg.z, _color_fg.w/4f);
//			setColor_fg(1.0f, 0f, 0f, 0.3f);
//			_size = _size * 1.4f;
//			drawCube(gl);
//			setColor_fg(color_fg_ori);
//			_size = _size / 1.4f;
//		}
		drawCube(gl);
		if (_fg_selected) {
			Color4f color_fg_ori = new Color4f(_color_fg);
			//setColor_fg(_color_fg.x, _color_fg.y, _color_fg.z, _color_fg.w/4f);
			setColor_fg(1.0f, 0f, 0f, 0.3f);
			_size = _size * 1.2f;
			drawCube(gl);
			setColor_fg(color_fg_ori);
			_size = _size / 1.2f;
		}
	}
	public void renderSelect( GL2 gl ) {
		_fg_select_mode = true;
		drawCube(gl);
		_fg_select_mode = false;
	}
	
	public void drawCube(GL2 gl) {
		
		// save transformation matrix, then translate and scale.
		gl.glPushMatrix();
		gl.glTranslatef (_pos.x, _pos.y, _pos.z);
		gl.glRotatef( Matrix.rad2Deg(_angZ1), 0, 0, 1); // rotation around 0z
		gl.glRotatef( Matrix.rad2Deg(_angY2), 0, 1 ,0); // rotation around 0y
		gl.glScalef(_size, _size, _size);
	
		if (_fg_select_mode) {
			gl.glPushName(_id);
		}
		
		// Draw faces
		gl.glColor4f( _color_fg.x, _color_fg.y, _color_fg.z, _color_fg.w );
		gl.glPolygonMode( GL.GL_FRONT_AND_BACK, GL2GL3.GL_FILL );
		gl.glBegin( GL2.GL_QUADS); {
			// right 
			gl.glVertex3f(  0.5f, -0.5f, -0.5f);
			gl.glVertex3f(  0.5f,  0.5f, -0.5f);
			gl.glVertex3f(  0.5f,  0.5f,  0.5f);
			gl.glVertex3f(  0.5f, -0.5f,  0.5f);
			// back
			gl.glVertex3f(  0.5f,  0.5f, -0.5f);
			gl.glVertex3f( -0.5f,  0.5f, -0.5f);
			gl.glVertex3f( -0.5f,  0.5f,  0.5f);
			gl.glVertex3f(  0.5f,  0.5f,  0.5f);
			// left
			gl.glVertex3f( -0.5f,  0.5f, -0.5f);
			gl.glVertex3f( -0.5f, -0.5f, -0.5f);
			gl.glVertex3f( -0.5f, -0.5f,  0.5f);
			gl.glVertex3f( -0.5f,  0.5f,  0.5f);
			// front
			gl.glVertex3f( -0.5f, -0.5f, -0.5f);
			gl.glVertex3f(  0.5f, -0.5f, -0.5f);
			gl.glVertex3f(  0.5f, -0.5f,  0.5f);
			gl.glVertex3f( -0.5f, -0.5f,  0.5f);
			// up
			gl.glVertex3f( -0.5f, -0.5f,  0.5f);
			gl.glVertex3f(  0.5f, -0.5f,  0.5f);
			gl.glVertex3f(  0.5f,  0.5f,  0.5f);
			gl.glVertex3f( -0.5f,  0.5f,  0.5f);
			// down
			gl.glVertex3f( -0.5f, -0.5f, -0.5f);
			gl.glVertex3f( -0.5f,  0.5f, -0.5f);
			gl.glVertex3f(  0.5f,  0.5f, -0.5f);
			gl.glVertex3f(  0.5f, -0.5f, -0.5f);
		}
		gl.glEnd();

		if (_fg_select_mode) {
			gl.glPopName();
		}
		
		// restore transformation matrix
		gl.glPopMatrix();
		// restore attributes
		//gl.glPopAttrib();
	}
	
	@Override
	public String toString() {
		return "CubeGL [_pos=" + _pos + ", _angZ1=" + _angZ1 + ", _angY2="
				+ _angY2 + ", _size=" + _size + ", _name=" + _name + "]";
	}

	public int getId() {
		return _id;
	}

	public Color4f getColor_fg() {
		return _color_fg;
	}
	public void setColor_fg(Color4f _color_fg) {
		this._color_fg = _color_fg;
	}
	public void setColor_fg(float r, float g, float b, float a) {
		this._color_fg = new Color4f(r,g,b,a);
	}
	
	
}
