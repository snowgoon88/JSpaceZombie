/**
 * 
 */
package shape3D;

import java.util.ArrayList;

import javax.media.opengl.GL2;
import javax.media.opengl.GL;
import javax.vecmath.Color4f;
import javax.vecmath.Point3f;
import javax.vecmath.Point3f;

import utils.GLUtils;

import model.Path;


/**
 * @author @author snowgoon88ATgmailDOTcom
 *
 */
public class PathGL implements IObjectGL {
	/** model linked */
	private Path _path;
	/** Color of the path */
	Color4f _color_fg = new Color4f( 0.3f, 0.3f, 0.3f, 0.5f);
	/** Path as a cycle ? */
	private boolean _fg_cycle = false;
	
	public PathGL( Path path ) {
		this._path = path;	
	}
	
	public void render( GL2 gl )  {
		drawPath(gl);
	}
	@Override
	public void renderSelect(GL2 gl) {
		// TODO Auto-generated method stub
		
	}
	
	private void drawPath( GL2 gl ) {
		
		if (_path == null) return;
		
		gl.glPushAttrib( GL2.GL_ENABLE_BIT );
		//gl.glDisable( GL2.GL_TEXTURE_2D );
		gl.glDisable( GL2.GL_LIGHTING );

		// set pose
		gl.glPushMatrix();
		//gl.glTranslatef( _traj.getPos().x, _traj.getPos().y, _traj.getPos().z );
		//gl.glRotatef( Matrix.rad2Deg(_traj.getAngOz()), 0.0f, 0.0f, 1.0f);

		// draw lines
		// Color of front face
		ArrayList<Point3f> listWP = _path.getListWP();
		gl.glColor4f( _color_fg.x, _color_fg.y, _color_fg.z, _color_fg.w);
		gl.glLineWidth(1.0f);
		gl.glBegin( GL.GL_LINE_STRIP ); {
			for (Point3f pos : listWP) {
				gl.glVertex3f(pos.x, pos.y, pos.z);
				//System.out.println("p="+pos.toString());
			}
			if (_fg_cycle) {
				gl.glVertex3f(listWP.get(0).x, listWP.get(0).y, listWP.get(0).z);
			}
		}
		gl.glEnd();
		gl.glPointSize( 5.0f );
		gl.glBegin( GL.GL_POINTS); {
			for (Point3f pos : listWP) {
				gl.glVertex3f(pos.x, pos.y, pos.z);
				//System.out.println("p="+pos.toString());
			}
		}
		gl.glEnd();
		
		// highlight the current waypoint
		Point3f pt = _path.getWaypoint();
		GLUtils.drawCircle(gl, pt.x, pt.y, 0.3f, 20);
		
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

	public boolean isCycle() {
		return _fg_cycle;
	}

	public void setCycle(boolean fg_cycle) {
		this._fg_cycle = fg_cycle;
	}
}
