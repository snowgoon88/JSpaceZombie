#version 330 core
	
// Input vertex data, different for all executions of this shader.
layout(location = 0) in vec3 vertexPosition_modelspace;
layout(location = 1) in vec4 vertexColor;

// Output
smooth out vec4 theColor;

void main(){

    // Generate clip-space position x,y,z,w
    gl_Position.xyz = vertexPosition_modelspace;
    gl_Position.w = 1.0;
    // Copy Color
	theColor = vertexColor;
}
