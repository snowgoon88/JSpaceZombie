package ogl3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

/**
 * Tutorial OpenGL3 : basic triangle
 * Draw a basic triangle using a VertexBufferObject (VBO) with an array of Vertex.
 * With vertex and fragment shaders
 *  - vertex shader as a String
 *  - fragment shader loaded as a file.
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

	int [] _vertexBuffers;
	float [] _vertexData;
	FloatBuffer _vData;
	
	private int _vertShader;
	private int _fragShader;
	private int _shaderProgram;
	/* Vertex Shader */
	static final String vertexShaderStr =
			"#version 330 core \n" +
	
			"// Input vertex data, different for all executions of this shader. \n" +		
			"layout(location = 0) in vec3 vertexPosition_modelspace; \n" +

			"void main(){ \n" +

			"    // Generate clip-space position x,y,z,w \n" +
			"    gl_Position.xyz = vertexPosition_modelspace; \n" +
			"    gl_Position.w = 1.0; \n" +

			"} \n";
	
	@Override
	public void display(GLAutoDrawable drawable) {
		GL3 gl = drawable.getGL().getGL3();
		
		// Clear Screen in Blue
		gl.glClearColor(0.0f, 0.0f, 1.0f, 0.0f);
		gl.glClear( GL3.GL_COLOR_BUFFER_BIT );

		// Use our shader
		gl.glUseProgram(_shaderProgram);

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
		
		// Clean up
		gl.glDisableVertexAttribArray(0);
		gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
		gl.glUseProgram(0);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		System.out.println("cleanup, remember to release shaders");
        GL3 gl = drawable.getGL().getGL3();
        gl.glUseProgram(0);
        gl.glDetachShader(_shaderProgram, _vertShader);
        gl.glDeleteShader(_vertShader);
        gl.glDetachShader(_shaderProgram, _fragShader);
        gl.glDeleteShader(_fragShader);
        gl.glDeleteProgram(_shaderProgram);
        System.exit(0);
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
		
		//Create shaders
        //OpenGL ES retuns a index id to be stored for future reference.
        _vertShader = gl.glCreateShader(GL3.GL_VERTEX_SHADER);
        _fragShader = gl.glCreateShader(GL3.GL_FRAGMENT_SHADER);

        //Compile the vertexShader String into a program.
        String[] vlines = new String[] { vertexShaderStr };
        int[] vlengths = new int[] { vlines[0].length() };
        gl.glShaderSource(_vertShader, vlines.length, vlines, vlengths, 0);
        gl.glCompileShader(_vertShader);

        //Check compile status.
        int[] compiled = new int[1];
        gl.glGetShaderiv(_vertShader, GL3.GL_COMPILE_STATUS, compiled,0);
        if(compiled[0]!=0){System.out.println("Horray! vertex shader compiled");}
        else {
            int[] logLength = new int[1];
            gl.glGetShaderiv(_vertShader, GL3.GL_INFO_LOG_LENGTH, logLength, 0);

            byte[] log = new byte[logLength[0]];
            gl.glGetShaderInfoLog(_vertShader, logLength[0], (int[])null, 0, log, 0);

            System.err.println("Error compiling the vertex shader: " + new String(log));
            System.exit(1);
        }

        //Load the fragmentShader from a file
        String fsrc = "";
        String line;
        try {
        	BufferedReader brf = new BufferedReader(
        			new FileReader("src/shaders/fragmentShader.glsl"));
		
			while ((line=brf.readLine()) != null) {
			  fsrc += line + "\n";
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //Compile the fragmentShader String into a program.
        String[] flines = new String[] { fsrc };
        int[] flengths = new int[] { flines[0].length() };
        gl.glShaderSource(_fragShader, flines.length, flines, flengths, 0);
        gl.glCompileShader(_fragShader);

        //Check compile status.
        gl.glGetShaderiv(_fragShader, GL3.GL_COMPILE_STATUS, compiled,0);
        if(compiled[0]!=0){System.out.println("Horray! fragment shader compiled");}
        else {
            int[] logLength = new int[1];
            gl.glGetShaderiv(_fragShader, GL3.GL_INFO_LOG_LENGTH, logLength, 0);

            byte[] log = new byte[logLength[0]];
            gl.glGetShaderInfoLog(_fragShader, logLength[0], (int[])null, 0, log, 0);

            System.err.println("Error compiling the fragment shader: " + new String(log));
            System.exit(1);
        }
        
        //Each shaderProgram must have
        //one vertex shader and one fragment shader.
        _shaderProgram = gl.glCreateProgram();
        
        gl.glAttachShader(_shaderProgram, _vertShader);
        gl.glAttachShader(_shaderProgram, _fragShader);
        
        gl.glLinkProgram(_shaderProgram);
        
        // Check link status
        int[] linked = new int[1];
        gl.glGetProgramiv(_shaderProgram, GL3.GL_LINK_STATUS, linked,0);
        if(linked[0]!=0){System.out.println("Horray! shader program linked");}
        else {
            int[] logLength = new int[1];
            gl.glGetProgramiv(_shaderProgram, GL3.GL_INFO_LOG_LENGTH, logLength, 0);

            byte[] log = new byte[logLength[0]];
            gl.glGetProgramInfoLog(_shaderProgram, logLength[0], (int[])null, 0, log, 0);

            System.err.println("Error linking the shader program: " + new String(log));
            System.exit(1);
        }
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

}
