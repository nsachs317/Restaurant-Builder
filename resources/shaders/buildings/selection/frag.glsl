#version 130

in vec3 pass_color;

out vec4 out_Color;

uniform vec3 objColor;

void main(void)	{

	out_Color = vec4(objColor, 1.0f);

}
