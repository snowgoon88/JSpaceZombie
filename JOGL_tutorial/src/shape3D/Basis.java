/**
 * Basis is made of 3 arrows (X:red; Y:green, Z:blue).
 */
package shape3D;


import java.util.ArrayList;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2GL3;




/**
 * @author snowgoon88ATgmailDOTcom
 *
 */
public class Basis {
	
	/** length of head */
	float _head_length = 0.1f;
	/** width of head */
	float _head_width = 0.025f;
	ArrayList<float[]> _head_face_x;
	ArrayList<float[]> _head_face_y;
	ArrayList<float[]> _head_face_z;
	
	/** With/without grid */
	public boolean _fg_grid = true;


	/**
	 * Create Basis and initialize various _head structures.	
	 */
	public Basis() {
		super();
		
		_head_face_x = new ArrayList<float[]>();
		float face_1_x[] = {0,0,0, -_head_length,0,_head_width,   -_head_length,-_head_width,0};
		float face_2_x[] = {0,0,0, -_head_length,-_head_width,0,  -_head_length,0,-_head_width};
		float face_3_x[] = {0,0,0, -_head_length,0,-_head_width,  -_head_length,_head_width,0};
		float face_4_x[] = {0,0,0, -_head_length,_head_width,0,   -_head_length,0,_head_width};
		_head_face_x.add( face_1_x );
		_head_face_x.add( face_2_x );
		_head_face_x.add( face_3_x );
		_head_face_x.add( face_4_x );
		_head_face_y = new ArrayList<float[]>();
		float face_1_y[] = {0,0,0, 0,-_head_length,_head_width,   -_head_width,-_head_length,0};
		float face_2_y[] = {0,0,0, -_head_width,-_head_length,0,  0,-_head_length,-_head_width};
		float face_3_y[] = {0,0,0, 0,-_head_length,-_head_width,  _head_width,-_head_length,0};
		float face_4_y[] = {0,0,0, _head_width,-_head_length,0,   0,-_head_length,_head_width};
		_head_face_y.add( face_1_y );
		_head_face_y.add( face_2_y );
		_head_face_y.add( face_3_y );
		_head_face_y.add( face_4_y );
		_head_face_z = new ArrayList<float[]>();
		float face_1_z[] = {0,0,0, _head_width,0,-_head_length,   0,-_head_width,-_head_length};
		float face_2_z[] = {0,0,0, 0,-_head_width,-_head_length,  -_head_width,0,-_head_length};
		float face_3_z[] = {0,0,0, -_head_width,0,-_head_length,  0,_head_width,-_head_length};
		float face_4_z[] = {0,0,0, 0,_head_width,-_head_length,   _head_width,0,-_head_length};
		_head_face_z.add( face_1_z );
		_head_face_z.add( face_2_z );
		_head_face_z.add( face_3_z );
		_head_face_z.add( face_4_z );

	}


	/**
	 * Render each arrow as a line and a _head.
	 * @param gl OpenGL Context
	 */
	public void render( GL2 gl )  {
        // save Light Attributes and remove lighting to get "full" RGB colors
		gl.glPushAttrib( GL2.GL_LIGHTING_BIT );
		gl.glDisable( GL2.GL_LIGHTING );
		
		// save transformation matrix, then translate and scale.
		gl.glPushMatrix();
//		glTranslatef (get_position().x, get_position().y, get_position().z);
//		glRotatef( to_deg(_ang_z1), 0, 0, 1); // rotation around 0z
//		glRotatef( to_deg(_ang_y2), 0, 1 ,0); // rotation around 0y
		
		if (_fg_grid) {
			drawGridXY(gl, 10.0f, 1.0f);
		}
		
		// X arrow in red
		// draw line
		gl.glColor3f( 1.0f, 0.0f, 0.0f );
		gl.glLineWidth( 2.0f );
		gl.glBegin( GL.GL_LINES ); {
			gl.glVertex3f( 0, 0, 0 );
			gl.glVertex3f( 1, 0, 0 );
		}
		gl.glEnd();
		// draw head
		gl.glPolygonMode( GL.GL_FRONT_AND_BACK, GL2GL3.GL_FILL );
		gl.glTranslatef( 1, 0, 0 );
		gl.glBegin( GL.GL_TRIANGLES ); {
			for (float[] face : _head_face_x) {
				gl.glVertex3f(face[0], face[1], face[2]);
				gl.glVertex3f(face[3], face[4], face[5]);
				gl.glVertex3f(face[6], face[7], face[8]);
			}
		} 
		gl.glEnd();
		gl.glTranslatef(-1.0f, 0.0f, 0.0f);

		// Y arrow in green
		// draw line
		gl.glColor3f( 0.0f, 1.0f, 0.0f );
		gl.glLineWidth( 2.0f );
		gl.glBegin( GL.GL_LINES ); {
			gl.glVertex3f( 0, 0, 0 );
			gl.glVertex3f( 0, 1, 0 );
		}
		gl.glEnd();
		// draw head
		gl.glPolygonMode( GL.GL_FRONT_AND_BACK, GL2GL3.GL_FILL );
		gl.glTranslatef( 0, 1, 0 );
		gl.glBegin( GL.GL_TRIANGLES ); {
			for (float[] face : _head_face_y) {
				gl.glVertex3f(face[0], face[1], face[2]);
				gl.glVertex3f(face[3], face[4], face[5]);
				gl.glVertex3f(face[6], face[7], face[8]);
			}
		} 
		gl.glEnd();
		gl.glTranslatef( 0, -1, 0 );
		
		// Z arrow in blue
		// draw line
		gl.glColor3f( 0.0f, 0.0f, 1.0f );
		gl.glLineWidth( 2.0f );
		gl.glBegin( GL.GL_LINES ); {
			gl.glVertex3f( 0, 0, 0 );
			gl.glVertex3f( 0, 0, 1 );
		}
		gl.glEnd();
		// draw head
		gl.glPolygonMode( GL.GL_FRONT_AND_BACK, GL2GL3.GL_FILL );
		gl.glTranslatef( 0, 0, 1 );
		gl.glBegin( GL.GL_TRIANGLES ); {
			for (float[] face : _head_face_z) {
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

	private void drawGridXY( GL2 gl, float size, float step ) {
		// disable lighting
	    //gl.glDisable(GL2.GL_LIGHTING);

	    gl.glBegin(GL2.GL_LINES);
	    gl.glEnable( GL2.GL_LINE_SMOOTH );
	    gl.glLineWidth( 0.1f );
	    gl.glColor3f(0.3f, 0.3f, 0.3f);
	    for(float i=step; i <= size; i+= step)
	    {
	        gl.glVertex3f(-size, i, 0);   // lines parallel to X-axis
	        gl.glVertex3f( size, i, 0);
	        gl.glVertex3f(-size, -i, 0);   // lines parallel to X-axis
	        gl.glVertex3f( size, -i, 0);

	        gl.glVertex3f( i, -size, 0);   // lines parallel to Z-axis
	        gl.glVertex3f( i,  size, 0);
	        gl.glVertex3f(-i, -size, 0);   // lines parallel to Z-axis
	        gl.glVertex3f(-i,  size, 0);
	    }

	    // x-axis
	    gl.glColor3f(0.5f, 0, 0);
	    gl.glVertex3f(-size, 0, 0);
	    gl.glVertex3f( size, 0, 0);

	    // y-axis
	    gl.glColor3f(0,0.5f,0);
	    gl.glVertex3f(0, -size, 0);
	    gl.glVertex3f(0, size, 0);

	    gl.glEnd();

	    gl.glDisable( GL2.GL_LINE_SMOOTH );
	    // enable lighting back
	    //gl.glEnable(GL2.GL_LIGHTING);
	}
}
