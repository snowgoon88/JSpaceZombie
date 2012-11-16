/**
 * 
 */
package shape3D;

import java.util.ArrayList;

import javax.media.opengl.GL2;
import javax.media.opengl.GL;
import javax.vecmath.Color4f;
import javax.vecmath.Point3f;

import model.Graph;
import model.GraphIterator;
import model.GraphLink;

/**
 * @author @author snowgoon88ATgmailDOTcom
 *
 */
public class GraphGL {
	/** model linked */
	private Graph<Point3f> _graph;
	/** Color of the path */
	Color4f _color_fg = new Color4f( 0.3f, 0.3f, 0.3f, 0.5f);
	/** draw link ?? */
	public boolean _fg_draw_link = true;
	
	public GraphGL( Graph<Point3f> graph ) {
		this._graph = graph;	
	}
	
	public void render( GL2 gl )  {
		drawGraph(gl);
	}
	
	private void drawGraph( GL2 gl ) {
		
		
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
		GraphIterator<Point3f> explo = new GraphIterator<Point3f>(_graph);
		if (_fg_draw_link) {
			gl.glLineWidth(1.0f);
			gl.glBegin( GL.GL_LINE_STRIP ); {
				while (explo.hasNext()) {
					Graph<Point3f> g = (Graph<Point3f>) explo.getNext();
					ArrayList<GraphLink<Point3f>> link = g.getLink();
					for (GraphLink<Point3f> graph : link) {
						gl.glVertex3f(g.getElem().x, g.getElem().y, g.getElem().z);
						gl.glVertex3f(graph.getNode().getElem().x, graph.getNode().getElem().y, graph.getNode().getElem().z);
					}
				}
			}
			gl.glEnd();
			gl.glPointSize( 5.0f );
			explo = new GraphIterator<Point3f>(_graph);	
		}
		gl.glBegin( GL.GL_POINTS); {
			while (explo.hasNext()) {
				Graph<Point3f> g = (Graph<Point3f>) explo.getNext();
				gl.glVertex3f(g.getElem().x, g.getElem().y, g.getElem().z);
			}
		}
		gl.glEnd();
		
//		// highlight the current waypoint
//		Point3f pt = _path.getWaypoint();
//		GLUtils.drawCircle(gl, pt.x, pt.y, 0.3f, 20);
		
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
}
