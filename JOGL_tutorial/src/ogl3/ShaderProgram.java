/**
 * 
 */
package ogl3;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.media.opengl.GL3;

/**
 * @author snowgoon88ATgmailDOTcom
 *
 */
public class ShaderProgram {
	
	/** handle to ShaderProgram */
	private int _shaderProgram;
	/** handle to VertexShader */
	private int _vertShader;
	/** handle to FragmentShader */
	private int _fragShader;
	
	public ShaderProgram( GL3 gl, String vertexShaderFileName,
			String fragmentShaderFileName ) {
		
		//Create shaders
        //OpenGL3 retuns a index id to be stored for future reference.
        _vertShader = gl.glCreateShader(GL3.GL_VERTEX_SHADER);
        _fragShader = gl.glCreateShader(GL3.GL_FRAGMENT_SHADER);
        
        // read and compile
        loadAndCompile(gl, vertexShaderFileName, _vertShader);
        loadAndCompile(gl, fragmentShaderFileName, _fragShader);
        
        //Each shaderProgram must have
        //one vertex shader and one fragment shader.
        _shaderProgram = gl.glCreateProgram();

        gl.glAttachShader(_shaderProgram, _vertShader);
        gl.glAttachShader(_shaderProgram, _fragShader);
        
        // Link and check link status
        gl.glLinkProgram(_shaderProgram);
        checkLinkStatus(gl);
	}
	
	/**
	 * Detach and delete shaders, then program.
	 * @param gl
	 */
	public void cleanUp(GL3 gl) {
        gl.glDetachShader(_shaderProgram, _vertShader);
        gl.glDeleteShader(_vertShader);
        gl.glDetachShader(_shaderProgram, _fragShader);
        gl.glDeleteShader(_fragShader);
        gl.glDeleteProgram(_shaderProgram);
	}
	
	/**
	 * Load the shader (in glsl language) in fileName and tries to compile it.
	 */
	private void loadAndCompile( GL3 gl, String shaderFileName, int shaderID ) {
		//Load the fragmentShader from a file
        String fsrc = "";
        String line;
        BufferedReader buffReader = null;
        try {
			buffReader= new BufferedReader(new FileReader(shaderFileName));
		} catch (FileNotFoundException e) {
			System.err.println("ShaderProgram.loadAndCompile : File not found ("+shaderFileName+")");
			e.printStackTrace();
			System.exit(1);
		}

        try {
			while ((line=buffReader.readLine()) != null) {
				fsrc += line + "\n";
			}
		} catch (IOException e) {
			System.err.println("ShaderProgram.loadAndCompile : Error readinf "+shaderFileName);
			e.printStackTrace();
			System.exit(1);
		}
        //Compile the shader String into a program.
        String[] slines = new String[] { fsrc };
        int[] slengths = new int[] { slines[0].length() };
        gl.glShaderSource(shaderID, slines.length, slines, slengths, 0);
        gl.glCompileShader(shaderID);

        //Check compile status.
        int[] compiled = new int[1];
        gl.glGetShaderiv(shaderID, GL3.GL_COMPILE_STATUS, compiled, 0);
        if(compiled[0]!=0){
        	System.out.println("Horray! "+shaderFileName+"  shader compiled");
        }
        else {
            int[] logLength = new int[1];
            gl.glGetShaderiv(shaderID, GL3.GL_INFO_LOG_LENGTH, logLength, 0);

            byte[] log = new byte[logLength[0]];
            gl.glGetShaderInfoLog(shaderID, logLength[0], (int[])null, 0, log, 0);

            System.err.println("Error compiling the "+ shaderFileName+" shader: " + new String(log));
            System.exit(1);
        }
	}
	
	/** Check if shaders are correcly linked */
	private void checkLinkStatus(GL3 gl) {
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

	/**
	 * Get the ID of this program.
	 * @return the _shaderProgram
	 */
	public int getShaderProgram() {
		return _shaderProgram;
	}

}
