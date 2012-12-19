package ogl3;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.channels.InterruptedByTimeoutException;

import javax.media.opengl.GL;
import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

/**
 * Tutorial OpenGL3 : basic triangle
 * Draw a basic triangle using a VertexBufferObject (VBO) with an array of Vertex.
 * No Shader yet.
 * 
 * Based on :
 *  - http://www.arcsynthesis.org/gltut/Basics/Tutorial%2001.html
 *  - http://ogldev.atspace.co.uk/www/tutorial03/tutorial03.html
 *  
 * @author snowgoon88@gmail.com
 *
 */
public class SceneGL3 implements GLEventListener {

	int [] _vertexBuffers;
	float [] _vertexData;
	FloatBuffer _vData;
	
	@Override
	public void display(GLAutoDrawable drawable) {
		GL3 gl = drawable.getGL().getGL3();
		
		// Clear Screen in Blue
		gl.glClearColor(0.0f, 0.0f, 1.0f, 0.0f);
		gl.glClear( GL3.GL_COLOR_BUFFER_BIT );

		// Use our shader
		//glUseProgram(programID);

		// 1rst attribute buffer : vertices
		gl.glEnableVertexAttribArray(0);
		// Either we bind the Array and tell where is the first data
		// and not forget to unbind it after
		gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, _vertexBuffers[0]);
		gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 0, 0L);
		// or directly use this form that does the binding I guess.
//		gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 0, _vData);
		
		// Draw the triangle !
		gl.glDrawArrays(GL.GL_TRIANGLES, 0, 3); // 3 indices starting at 0 -> 1 triangle
		gl.glDisableVertexAttribArray(0);
		gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL3 gl = drawable.getGL().getGL3();
		
		// An array of 3 vectors which represents 3 vertices
		// In fact, should us a fourth vertice representing the W in clip space,
		// but I guess it is set by default to 1.0f.
		_vertexData  = new float [] {
				-1.0f, -1.0f, 0.0f,
				1.0f, -1.0f, 0.0f,
				0.0f,  1.0f, 0.0f,
		};
		_vData = FloatBuffer.allocate(_vertexData.length);
		_vData.put(_vertexData);
		_vData.rewind();
		
		// Create the Vertex Buffer
		_vertexBuffers = new int[1];
		gl.glGenBuffers(1, _vertexBuffers, 0 /*offset*/);
		gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, _vertexBuffers[0]);
		System.out.println("glBufferData size="+_vData.capacity()*Float.SIZE/8);
		gl.glBufferData(GL3.GL_ARRAY_BUFFER, _vData.capacity() * Float.SIZE/8, _vData,
				GL3.GL_STATIC_DRAW);
		// Unbind it
		gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

}
