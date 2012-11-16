/**
 * 
 */
package shape3D;

import java.util.ArrayList;

import javax.media.opengl.GL2;
import javax.media.opengl.GL;
import javax.vecmath.Color4f;
import javax.vecmath.Point3f;


/**
 * @author @author snowgoon88ATgmailDOTcom
 *
 */
public class TrajectoryGL implements IObjectGL {
	/** model linked */
	private ArrayList<Point3f> _traj;
	/** Color of the trajectory */
	Color4f _color_fg = new Color4f( 0.3f, 0.3f, 0.3f, 0.5f);
	
	
	public TrajectoryGL( ArrayList<Point3f> traj ) {
		this._traj = traj;	
	}
	
	public void render( GL2 gl )  {
		drawTrajectory(gl);
	}
	
	private void drawTrajectory( GL2 gl ) {
		gl.glPushAttrib( GL2.GL_ENABLE_BIT );
		//gl.glDisable( GL2.GL_TEXTURE_2D );
		gl.glDisable( GL2.GL_LIGHTING );

		// set pose
		gl.glPushMatrix();
		//gl.glTranslatef( _traj.getPos().x, _traj.getPos().y, _traj.getPos().z );
		//gl.glRotatef( Matrix.rad2Deg(_traj.getAngOz()), 0.0f, 0.0f, 1.0f);

		// draw lines
		// Color of front face
		gl.glColor4f( _color_fg.x, _color_fg.y, _color_fg.z, _color_fg.w);
		gl.glLineWidth(1.0f);
		gl.glBegin( GL.GL_LINE_STRIP ); synchronized(_traj) {
			for (Point3f pos : _traj) {
				gl.glVertex3f(pos.x, pos.y, pos.z);
				//System.out.println("p="+pos.toString());
			}
		}
		gl.glEnd();
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
