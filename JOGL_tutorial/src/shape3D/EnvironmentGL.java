/**
 * 
 */
package shape3D;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.vecmath.Color3f;
import javax.vecmath.Color4f;

import utils.Matrix;

import model.Environment;
import model.Wall;

/**
 * @author snowgoon88ATgmailDOTcom
 *
 */
public class EnvironmentGL implements IObjectGL {

	Environment _env;
	Color4f _color_fg = new Color4f(new float [] {0.0f,0.0f,1.0f,1.0f});
	
	public EnvironmentGL( Environment env ) {
		this._env = env;
	}
	
	/* (non-Javadoc)
	 * @see shape3D.IObjectGL#render(javax.media.opengl.GL2)
	 */
	@Override
	public void render(GL2 gl) {
		// save Light Attributes and remove lighting to get "full" RGB colors
		//gl.glPushAttrib( GL2.GL_LIGHTING_BIT ); 
		gl.glPushAttrib(GL2.GL_ENABLE_BIT);
		gl.glDisable( GL2.GL_LIGHTING );

		// draw line
		gl.glColor4f( _color_fg.x, _color_fg.y, _color_fg.z, _color_fg.w );
		gl.glLineWidth( 2.0f );
		gl.glBegin( GL.GL_LINES ); {
			for (Wall w : _env.getWalls()) {
				gl.glVertex3d(w.getOrigin().x, w.getOrigin().y, w.getOrigin().z);
				gl.glVertex3d(w.getEnd().x, w.getEnd().y, w.getEnd().z);
			}
		}
		gl.glEnd();

		gl.glPopAttrib();
	}

	/* (non-Javadoc)
	 * @see shape3D.IObjectGL#renderSelect(javax.media.opengl.GL2)
	 */
	@Override
	public void renderSelect(GL2 gl) {
		// TODO Auto-generated method stub

	}

}
