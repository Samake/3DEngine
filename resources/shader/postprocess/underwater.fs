#version 140

in vec2 textureCoords;

out vec4 outColour;

uniform sampler2D mainTexture;
uniform sampler2D depthTexture;
uniform vec3 cameraPosition;
uniform float waterHeight;
uniform float animValue;

void main(void) {
	float clip = waterHeight + 0.35f - cameraPosition.y;
	clip = clamp(clip, 0.0, 1.0);
	
	vec2 uv = textureCoords;
	//uv.x *= 2 * sin(animValue) +  0.5 * cos(animValue);
	
	vec4 baseTexture = texture(mainTexture, textureCoords);
	vec4 underWaterTexture = texture(mainTexture, uv);
	underWaterTexture.rgb *= vec3(0.35f, 0.45f, 0.55f);
	
	outColour = mix(baseTexture, underWaterTexture, clip);
}