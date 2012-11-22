/**
 * Colored arrow.
 */
package shape3D;


import java.util.ArrayList;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2GL3;
import javax.vecmath.Color4f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import utils.Matrix;




/**
 * @author snowgoon88ATgmailDOTcom
 *
 */
public class Arrow {
	
	/** Color of the arrow */
	Color4f _color_fg;
	/** Position of arrow */
	Point3f _pos;
	/** Euler angles */
	float _angZ1 = 0f;
	float _angY2 = 0f;
	/** length of vector */
	float _length = 1f;
	
	
	/** length of head */
	float _head_length = 0.1f;
	/** width of head */
	float _head_width = 0.025f;
	ArrayList<float[]> _head_faces;
	
	
	// DEBUG
	public boolean _fg_verb = false;
	
	/**
	 * Create Arrow with color, and initialize _head structure.	
	 * @param _color_fg : Color of Arrow
	 */
	public Arrow(Color4f _color_fg) {
		super();
		this._color_fg = _color_fg;
		this._pos = new Point3f();
		
		_head_faces = new ArrayList<float[]>();
		float face_1[] = {0,0,0, -_head_length,0,_head_width,   -_head_length,-_head_width,0};
		float face_2[] = {0,0,0, -_head_length,-_head_width,0,  -_head_length,0,-_head_width};
		float face_3[] = {0,0,0, -_head_length,0,-_head_width,  -_head_length,_head_width,0};
		float face_4[] = {0,0,0, -_head_length,_head_width,0,   -_head_length,0,_head_width};
		_head_faces.add( face_1 );
		_head_faces.add( face_2 );
		_head_faces.add( face_3 );
		_head_faces.add( face_4 );
	}


	/**
	 * Render the arrow as a line and a _head.
	 * @param gl OpenGL Context
	 */
	public void render( GL2 gl )  {
        // save Light Attributes and remove lighting to get "full" RGB colors
		//gl.glPushAttrib( GL2.GL_LIGHTING_BIT ); 
		gl.glPushAttrib(GL2.GL_ENABLE_BIT);
		gl.glDisable( GL2.GL_LIGHTING );
		
		if( _fg_verb) {
			System.out.println("Arrow pos="+_pos.toString()+"  angZ1="+_angZ1+"  angY2="+_angY2+"  length="+_length);
			_fg_verb = false;
		}
		
		// save transformation matrix, then translate and scale.
		gl.glPushMatrix();
		gl.glTranslatef (_pos.x, _pos.y, _pos.z);
		gl.glRotatef( Matrix.rad2Deg(_angZ1), 0, 0, 1); // rotation around 0z
		gl.glRotatef( Matrix.rad2Deg(_angY2), 0, 1 ,0); // rotation around 0y
		
		// draw line
		gl.glColor4f( _color_fg.x, _color_fg.y, _color_fg.z, _color_fg.w );
		gl.glLineWidth( 1.0f );
		gl.glBegin( GL.GL_LINES ); {
			gl.glVertex3f( 0, 0, 0 );
			gl.glVertex3f( _length, 0, 0 );
		}
		gl.glEnd();
		
		// draw head
		gl.glPolygonMode( GL.GL_FRONT_AND_BACK, GL2GL3.GL_FILL );
		gl.glTranslatef( _length, 0, 0 );
		gl.glBegin( GL.GL_TRIANGLES ); {
			for (float[] face : _head_faces) {
				gl.glVertex3f(face[0], face[1], face[2]);
				gl.glVertex3f(face[3], face[4], face[5]);
				gl.glVertex3f(face[6], face[7], face[8]);
			}
		} 
		gl.glEnd();

		// restore transformation matrix
		gl.glPopMatrix();
		// restore attributes
		gl.glPopAttrib();
	}

	public Point3f getPos() {
		return _pos;
	}
	public void setPos(Point3f pos) {
		this._pos = pos;
	}

	/** Set Arrow to match given vector */	
	public void setup(Vector3f vec) {
		float ang[] = Matrix.angleFromVec(vec);
		_angZ1 = ang[0];
		_angY2 = ang[1];
		_length = vec.length();
	}

}
