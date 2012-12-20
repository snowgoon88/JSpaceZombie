package ogl3;

import java.nio.FloatBuffer;
import com.jogamp.common.nio.Buffers;
import com.jogamp.graph.geom.Vertex;

import javax.media.opengl.GL;
import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

/**
 * Tutorial OpenGL3 : moving multicolor triangle, 2 buffers (position, color)
 * Draw a multicolor triangle using a VertexBufferObject (VBO) with an array of Vertex.
 * Use a manual version of glVertexAttribPointer, in order to "replace" updated Vertices.
 * With vertex and fragment shaders loaded using ShaderProgramm
 * 
 * Based on :
 *  - http://www.arcsynthesis.org/gltut/Basics/Tutorial%2002.html
 *  - http://ogldev.atspace.co.uk/www/tutorial03/tutorial03.html
 *  - http://www.arcsynthesis.org/gltut/Basics/Tut01%20Making%20Shaders.html
 *  - ~/Projets/OpenGLTutorial/RawGL2ES2demo.java (http://jogamp.org/git/?p=jogl-demos.git;a=blob;f=src/demos/es2/RawGL2ES2demo.java;hb=HEAD)
 *  
 * @author snowgoon88@gmail.com
 *
 */
public class SceneGL3 implements GLEventListener {
	
	float [] _vertexPosition;
	FloatBuffer _vertexPosBuffer;
	int [] _glVertexPosBuffers;
	FloatBuffer _vertexColBuffer;
	ShaderProgram _shaderProg;
	
	long _startTime = 0;
	
	/**
	 * Update the vertices position to make them go round.
	 * In GL memory, the Buffer is not created and memory allocated,
	 * but juste replaced by the new values.
	 */
	private void update(GL3 gl) {
		// duration of periodic mouvement -> amplitude of move
		final float period = 5.0f; // seconds
		final float scale = (float)Math.PI * 2f / period;
		// time
		long curTime = System.currentTimeMillis();
		float elapsedTime = (float) (curTime - _startTime) / 1000.0f; // in seconds
		
		// offset to add to points
		float loopTime = elapsedTime % period;
		float xOffset = (float) Math.cos(loopTime * scale) * 0.5f;
		float yOffset = (float) Math.sin(loopTime * scale) * 0.5f;
		
		_vertexPosBuffer = Buffers.newDirectFloatBuffer(_vertexPosition);
		for (int i = 0; i < 3; i++) {
			_vertexPosBuffer.put(i*3+0, _vertexPosBuffer.get(i*3+0)+xOffset);
			_vertexPosBuffer.put(i*3+1, _vertexPosBuffer.get(i*3+1)+yOffset);
		}
		// Replace in GL Memory (BufferSubData) without new allocation
		gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, _glVertexPosBuffers[0]);
	    gl.glBufferSubData(GL3.GL_ARRAY_BUFFER, 0,
	    		_vertexPosBuffer.capacity() * Float.SIZE/8, _vertexPosBuffer );
	    gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
		
	}
	
	@Override
	public void display(GLAutoDrawable drawable) {
		GL3 gl = drawable.getGL().getGL3();
		
		// update the Vertex
		update(gl);
		
		// Clear Screen in Blue
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); // Purple
		gl.glClear(GL3.GL_STENCIL_BUFFER_BIT |
				GL3.GL_COLOR_BUFFER_BIT   |
				GL3.GL_DEPTH_BUFFER_BIT   );

		// Use our shader
		gl.glUseProgram(_shaderProg.getShaderProgram());

		// 1rst attribute buffer : vertices
		gl.glEnableVertexAttribArray(0);
		// This binds the buffer and tell about the data position to the shaders
		gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, _glVertexPosBuffers[0]);
		gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 0, 0L);
		gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
		
		// 2nd attribute buffer : color
		gl.glEnableVertexAttribArray(1);
		// This binds the buffer and tell about the data position to the shaders
		gl.glVertexAttribPointer(1, 4, GL.GL_FLOAT, false, 0, _vertexColBuffer);
		
		// Draw the triangle !
		gl.glDrawArrays(GL.GL_TRIANGLES, 0, 3); // 3 indices starting at 0 -> 1 triangle
		
		// Clean up
		gl.glDisableVertexAttribArray(0);
		gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
		gl.glDisableVertexAttribArray(1);
		
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
		// Store starting time
		_startTime = System.currentTimeMillis();
		//testBuffer();
		
		GL3 gl = drawable.getGL().getGL3();
		
		// An array of 3 vectors which represents 3 vertices
		// In fact, should us a fourth vertice representing the W in clip space,
		// but it will be completed by the vertex shader.
		_vertexPosition  = new float [] {
				-1.0f, -1.0f, 0.0f,
				1.0f, -1.0f, 0.0f,
				0.0f,  1.0f, 0.0f,
		};
		_vertexPosBuffer = Buffers.newDirectFloatBuffer(_vertexPosition);
		// Create the GL Vertex Buffer
		_glVertexPosBuffers = new int[1];
		gl.glGenBuffers(1, _glVertexPosBuffers, 0 /*offset*/);
		gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, _glVertexPosBuffers[0]);
		gl.glBufferData(GL3.GL_ARRAY_BUFFER, _vertexPosBuffer.capacity() * Float.SIZE/8,
				_vertexPosBuffer, GL3.GL_STREAM_DRAW);
		// Unbind it
		gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
		
		// An array of 3 vectors which represents 3 vertices colors in RGBA
		float [] vertexColor  = {
                0.0f, 0.0f, 1.0f, 1.0f, //Bottom Left color (blue)
                0.0f, 1.0f, 0.0f, 1.0f,  //Bottom Right color (green)
                1.0f, 0.0f, 0.0f, 1.0f //Top color (red)
		};
		_vertexColBuffer = Buffers.newDirectFloatBuffer(vertexColor);
		
		//Create shaders and programm
		_shaderProg = new ShaderProgram(gl, "src/shaders/vertexShader.glsl",
				"src/shaders/fragmentShader.glsl");
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	/** To test the behavior of FloatBuffers */
	private void testBuffer() {
		float [] vertexPosition  = { 0.0f, 1.0f, 2.0f, 3.0f, 4.0f };
		FloatBuffer fb = Buffers.newDirectFloatBuffer(vertexPosition);
		System.out.println("fb="+fb.toString());
		fb.rewind();
		int index = 0;
		while (fb.hasRemaining()) {
			System.out.println("["+index+"]="+fb.get());
			index += 1;
		}
		
		fb.put(3, -3.0f);
		System.out.println("fb="+fb.toString());
		fb.rewind();
		index = 0;
		while (fb.hasRemaining()) {
			System.out.println("["+index+"]="+fb.get());
			index += 1;
		}
		
	}
}
