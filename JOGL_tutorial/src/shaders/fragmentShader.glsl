#version 330 core

// Input
smooth in vec4 theColor;

// Ouput data
out vec4 outputColor;

void main()
{

	// Output color = copy with interpolation from vertex (smooth) 
	outputColor = theColor;

}
