/**
 * 
 */
package shape3D;

import java.util.ArrayList;

import javax.media.opengl.GL2;
import javax.vecmath.Color4f;

import utils.Matrix;

import model.Agent;

/**
 * @author @author snowgoon88ATgmailDOTcom
 *
 */
public class AgentGL implements IObjectGL {
	/** model linked */
	private Agent _agent;
	/** Color of the agent */
	Color4f _color_fg = new Color4f( 0.8f, 0.0f, 0.8f, 1.0f);
	
	/** internal : list of faces */
	ArrayList<float[]> _faces;
	
	/** flag Debug mode */
	public boolean _fg_debug = false;
	
	public AgentGL( Agent agent ) {
		this._agent = agent;
		
		// Setup faces of the agent.
		float h = 0.1f;
		_faces = new ArrayList<float[]>();
		float face_1[] = { 0.0f,0.0f,h,     -0.2f,-0.1f, h,      0.3f,0.0f,h,    -0.2f,0.1f,h}; 
		float face_2[] = { 0.0f,0.0f,0.0f,  -0.2f,0.1f,0.0f,     0.3f,0.0f,0.0f, -0.2f,-0.1f,0.0f};
		float face_3[] = { 0.0f,0.0f,0.0f,  -0.2f,-0.1f,0.0f,   -0.2f,-0.1f,h,    0.0f,0.0f,h};
		float face_4[] = {-0.2f,-0.1f,0.0f,  0.3f,0.0f,0.0f,     0.3f,0.0f,h,    -0.2f,-0.1f,h};
		float face_5[] = { 0.3f,0.0f,0.0f,  -0.2f,0.1f,0.0f,    -0.2f,0.1f,h,     0.3f,0.0f,h};
		float face_6[] = {-0.2f,0.1f,0.0f,   0.0f,0.0f,0.0f,     0.0f,0.0f,h,    -0.2f,0.1f,h};
		_faces.add( face_1 );
		_faces.add( face_2 );
		_faces.add( face_3 );
		_faces.add( face_4 );
		_faces.add( face_5 );
		_faces.add( face_6 );
	}
	
	public void render( GL2 gl )  {
		drawAgent(gl);
	}
	
	private void drawAgent( GL2 gl ) {
		gl.glPushAttrib( GL2.GL_ENABLE_BIT );
		gl.glDisable( GL2.GL_TEXTURE_2D );
		gl.glDisable( GL2.GL_LIGHTING );

		// set pose
		gl.glPushMatrix();
		gl.glTranslated( _agent.getPos().x, _agent.getPos().y, _agent.getPos().z );
		gl.glPushMatrix();
		gl.glRotated( Math.toDegrees(_agent.getAngOz()), 0.0f, 0.0f, 1.0f);

		// draw player
		gl.glPolygonMode (GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);  
		// Color of front face
		gl.glColor4f( _color_fg.x, _color_fg.y, _color_fg.z, _color_fg.w);
		gl.glBegin( GL2.GL_QUADS ); {
			for (float[] face : _faces) {
				int i=0;
				gl.glVertex3f(face[i++], face[i++], face[i++]);
				gl.glVertex3f(face[i++], face[i++], face[i++]);
				gl.glVertex3f(face[i++], face[i++], face[i++]);
				gl.glVertex3f(face[i++], face[i++], face[i++]);
			}
		}
		gl.glEnd();
		gl.glPopMatrix();
		
		// Debug
		if (_fg_debug) {
			// Draw speed as blue arrow
			Arrow speedArrow = new Arrow( new Color4f( 0.0f,0.0f,1.0f,1.0f));
			speedArrow.setup( _agent.getSpeed() );
			//speedArrow.setPos( _agent.getPos() );
			speedArrow.render(gl);
			// Draw steering as red arrow
			Arrow steerArrow = new Arrow( new Color4f( 1.0f,0.0f,0.0f,1.0f));
			steerArrow.setup( _agent.getSteering() );
			//steerArrow.setPos( _agent.getPos() );
			steerArrow.render(gl);
		}
		
		gl.glPopMatrix();
		gl.glPopAttrib();
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

	@Override
	public void renderSelect(GL2 gl) {
		// TODO Auto-generated method stub
		
	}

}
