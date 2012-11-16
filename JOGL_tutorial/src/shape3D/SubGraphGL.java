/**
 * 
 */
package shape3D;

import javax.media.opengl.GL2;
import javax.media.opengl.GL;
import javax.vecmath.Color4f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import utils.GLUtils;

import model.Graph;

/**
 * @author @author snowgoon88ATgmailDOTcom
 *
 */
public class SubGraphGL {
	/** model linked */
	private Graph<Point3f> _graph;
	/** Color of the path */
	Color4f _color_fg = new Color4f( 0.3f, 0.3f, 0.3f, 0.5f);
	/** Which index of Link to highlight */
	private int _indexChosen;
	// DEBUG
	public boolean _fg_verb = false;
	
	public SubGraphGL( Graph<Point3f> graph ) {
		this._graph = graph;
		_indexChosen = 0; // First or None chosen
	}
	
	public void render( GL2 gl )  {
		drawGraph(gl);
	}
	
	private void drawGraph( GL2 gl ) {
		
		
		gl.glPushAttrib( GL2.GL_ENABLE_BIT );
		//gl.glDisable( GL2.GL_TEXTURE_2D );
		//gl.glPushAttrib( GL2.GL_LIGHTING_BIT ); 
		gl.glDisable( GL2.GL_LIGHTING );

		// set pose
		gl.glPushMatrix();
		//gl.glTranslatef( _traj.getPos().x, _traj.getPos().y, _traj.getPos().z );
		//gl.glRotatef( Matrix.rad2Deg(_traj.getAngOz()), 0.0f, 0.0f, 1.0f);

		// draw lines
		// Color of front face
		gl.glLineWidth(1.0f);
		for (int i = 0; i < _graph.getLink().size(); i++) {
			if (i==_indexChosen) {
				Arrow arrow = new Arrow(_color_fg);
				Vector3f vec = new Vector3f(_graph.getLink().get(i).getNode().getElem());
				vec.sub(_graph.getElem());
				arrow.setup(vec);
				arrow.setPos(_graph.getElem());
				if (_fg_verb) {
					System.out.println("sub i="+i);
					System.out.println("VecPos="+_graph.getElem().toString()+"  VecVec="+vec.toString());
					arrow._fg_verb = true;
				}
				arrow.render(gl);
//				Point3f end = _graph.getLink().get(i).getNode().getElem();
//				gl.glBegin( GL.GL_LINE_STRIP ); {
//					gl.glColor4f( _color_fg.x, _color_fg.y, _color_fg.z, _color_fg.w );
//					gl.glVertex3f(_graph.getElem().x, _graph.getElem().y, _graph.getElem().z);
//					gl.glVertex3f(end.x, end.y, end.z);
//				}
//				gl.glEnd();
//				
				
			}
			else {
				Point3f end = _graph.getLink().get(i).getNode().getElem();
				gl.glBegin( GL.GL_LINE_STRIP ); {
					gl.glColor3f(0.3f,0.3f,0.3f);
					gl.glVertex3f(_graph.getElem().x, _graph.getElem().y, _graph.getElem().z);
					gl.glVertex3f(end.x, end.y, end.z);
				}
				gl.glEnd();
				if (_fg_verb) {
					System.out.println("sub i="+i);
					System.out.println("Pos="+_graph.getElem().toString()+"  End="+end.toString());
				}
			}
		}
		_fg_verb = false;
		
		// highlight the current waypoint
		Point3f pt = _graph.getElem();
		gl.glColor4f(_color_fg.x, _color_fg.y, _color_fg.z, _color_fg.w);
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

	public void incIndex() {
		_indexChosen = (_indexChosen+1) % _graph.getLink().size();
		System.out.println("index="+_indexChosen);
	}
	public void decIndex() {
		_indexChosen = (_indexChosen-1) % _graph.getLink().size();
		if (_indexChosen < 0) _indexChosen=_graph.getLink().size()-1;
		System.out.println("index="+_indexChosen);
	}

	public void advance() {
		_graph = _graph.getLink().get(_indexChosen).getNode();
		_indexChosen = 0;
		System.out.println(_graph.toString());
	}

	/**
	 * Return the current node pointed at
	 * @return Graph<Point3f> _graph
	 */
	public Graph<Point3f> getCurrentGraph() {
		return _graph;
	}
}
