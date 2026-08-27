#version 130

in vec3 in_position;

uniform mat4 transform;
uniform mat4 view;
uniform mat4 projection;

void main(void){

	vec4 worldPosition = transform * vec4(in_position, 1.0f);
	vec4 toCamPos = worldPosition * view;
	gl_Position = toCamPos * projection;	
}