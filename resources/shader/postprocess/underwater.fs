#version 140
#define PI 3.14159265359

in vec2 textureCoords;

out vec4 outColour;

uniform sampler2D mainTexture;
uniform sampler2D depthTexture;
uniform vec3 cameraPosition;
uniform float waterHeight;
uniform float animValue;

const float near = 1; 
const float far = 250;

float linearizeDepth(float depth) {
    float z = depth * 2.0 - 1.0;
    return (2.0 * near * far) / (far + near - z * (far - near));	
}

void main(void) {
	float clip = waterHeight + 0.35f - cameraPosition.y;
	clip = clamp(clip, 0.0, 1.0);
	vec3 waterColor = vec3(0.35f, 0.45f, 0.55f);
	
	vec2 uv = textureCoords;
	uv += (sin(uv.x * 16.0 * PI + animValue * 2) + sin(uv.y * 8.0 * PI + animValue)) * 0.0025f;

	float depth = texture(depthTexture, textureCoords).r;
	float floorDistance = (linearizeDepth(depth) * 0.01f);
	floorDistance = clamp(floorDistance, 0.1, 1.0);
	
	float floorDistanceNeg = 1 - floorDistance;
	
	vec4 baseTexture = texture(mainTexture, textureCoords);
	vec4 underWaterTexture = texture(mainTexture, uv);
	
	vec4 underWaterColor = vec4(underWaterTexture.rgb * waterColor * floorDistanceNeg, 1.0);
	underWaterColor.rgb += (waterColor * floorDistance) * 0.5f;
	underWaterColor.rgb *= 0.5f;
	
	outColour = mix(baseTexture, underWaterColor, clip);
}