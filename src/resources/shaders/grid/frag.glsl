#version 130

in vec4 gridPosition;
in vec4 toCamPosition;

out vec4 out_Color;

const float lineSize = 0.0125;
const float gridDivision = 4;
const float bigLineFactor = 1.25f;
const vec3 lineColor = vec3(0.8f);
const float gridDistance = 30;
const float thickLineSize = lineSize * bigLineFactor;

void main(void)	{

	vec4 baseColor = vec4(0,0,0,1);
	
	float xPosition = abs(gridPosition.x) + 0.5f;
	float zPosition = abs(gridPosition.z) + 0.5f;
	
	
	//if(abs(roundEven(xPosition) - xPosition) - (thickLineSize) < thickLineSize*2) {
		//baseColor.a = 1 - ((roundEven(xPosition) - (xPosition) - (thickLineSize)));
		//baseColor.xyz = lineColor;
	//}
	
	//X Lines - Thin
	if(abs(roundEven(xPosition*gridDivision) - xPosition*gridDivision) < lineSize) {
		baseColor.xyz = lineColor;
	}
	//X Lines - Thick
	if(abs(roundEven(xPosition) - xPosition) < lineSize*bigLineFactor) {
		baseColor.xyz = lineColor;
		baseColor.a = 1;
	}
	//Z Lines - Thin
	if(abs(roundEven(zPosition*gridDivision) - zPosition*gridDivision) < lineSize) {
		baseColor.xyz = lineColor;
	}
	//Z Lines - Thick
	if(abs(roundEven(zPosition) - zPosition) < lineSize*bigLineFactor) {
		baseColor.xyz = lineColor;
		baseColor.a = 1;
	}
	
	if(baseColor.xyz == vec3(0)) {
		discard;
	}
	if(length(toCamPosition) > gridDistance) {
		discard;
	}
	
	out_Color = baseColor;
	out_Color.a = out_Color.a - length(toCamPosition)/gridDistance;

}
