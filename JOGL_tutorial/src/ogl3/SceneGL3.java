package ogl3;

import java.nio.FloatBuffer;
import com.jogamp.common.nio.Buffers;

import javax.media.opengl.GL;
import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

/**
 * Tutorial OpenGL3 : basic triangle
 * Draw a basic triangle using a VertexBufferObject (VBO) with an array of Vertex.
 * Use a "direct" version of glVertexAttribPointer.
 * With vertex and fragment shaders loaded using ShaderProgramm
 * 
 * Based on :
 *  - http://www.arcsynthesis.org/gltut/Basics/Tutorial%2001.html
 *  - http://ogldev.atspace.co.uk/www/tutorial03/tutorial03.html
 *  - http://www.arcsynthesis.org/gltut/Basics/Tut01%20Making%20Shaders.html
 *  - ~/Projets/OpenGLTutorial/RawGL2ES2demo.java (http://jogamp.org/git/?p=jogl-demos.git;a=blob;f=src/demos/es2/RawGL2ES2demo.java;hb=HEAD)
 *  
 * @author snowgoon88@gmail.com
 *
 */
public class SceneGL3 implements GLEventListener {
	
	FloatBuffer _vertexBuffer;
	ShaderProgram _shaderProg;
	
	@Override
	public void display(GLAutoDrawable drawable) {
		GL3 gl = drawable.getGL().getGL3();
		
		// Clear Screen in Blue
		gl.glClearColor(0.0f, 0.0f, 1.0f, 0.0f);
		gl.glClear( GL3.GL_COLOR_BUFFER_BIT );

		// Use our shader
		gl.glUseProgram(_shaderProg.getShaderProgram());

		// 1rst attribute buffer : vertices
		gl.glEnableVertexAttribArray(0);
		// This binds the buffer and tell about the data position to the shaders
		gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 0, _vertexBuffer);
		
		// Draw the triangle !
		gl.glDrawArrays(GL.GL_TRIANGLES, 0, 3); // 3 indices starting at 0 -> 1 triangle
		
		// Clean up
		gl.glDisableVertexAttribArray(0);
		//gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
		gl.glUseProgram(0);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		GL3 gl = drawable.getGL().getGL3();
		_shaderProg.cleanUp(gl);
        System.exit(0);
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL3 gl = drawable.getGL().getGL3();
		
		// An array of 3 vectors which represents 3 vertices
		// In fact, should us a fourth vertice representing the W in clip space,
		// but it will be completed by the vertex shader.
		float [] vertexData  = {
				-1.0f, -1.0f, 0.0f,
				1.0f, -1.0f, 0.0f,
				0.0f,  1.0f, 0.0f
		};
		_vertexBuffer = Buffers.newDirectFloatBuffer(vertexData);
		
		//Create shaders and programm
		_shaderProg = new ShaderProgram(gl, "src/shaders/vertexShader.glsl",
				"src/shaders/fragmentShader.glsl");
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

}
