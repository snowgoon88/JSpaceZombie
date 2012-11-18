/**
 * 
 */
package shape3D;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.vecmath.Color4f;
import javax.vecmath.Point3f;

import model.Scanner;
import model.Wall;

/**
 * @author snowgoon88ATgmailDOTcom
 *
 */
public class ScannerGL implements IObjectGL {
	
	/** Model */
	Scanner _scan;
	Color4f _color_fg = new Color4f(new float [] {1.0f,0.0f,0.0f,1.0f});
	
	public ScannerGL( Scanner model ) {
		_scan = model;
	}

	@Override
	public void render(GL2 gl) {
		// save Light Attributes and remove lighting to get "full" RGB colors
		//gl.glPushAttrib( GL2.GL_LIGHTING_BIT ); 
		gl.glPushAttrib(GL2.GL_ENABLE_BIT);
		gl.glDisable( GL2.GL_LIGHTING );

		// draw line
		gl.glColor4f( _color_fg.x, _color_fg.y, _color_fg.z, _color_fg.w );
		gl.glLineWidth( 1.0f );
		gl.glBegin( GL.GL_LINES ); {
			Point3f from = _scan.getPos();
			Point3f end = new Point3f(from);
			end.add(_scan.getObstacle());
			gl.glVertex3f(from.x, from.y, from.z);
			gl.glVertex3f(end.x, end.y, end.z);
		}
		gl.glEnd();

		gl.glPopAttrib();

	}

	@Override
	public void renderSelect(GL2 gl) {
		// TODO Auto-generated method stub

	}
	
	
	
}
