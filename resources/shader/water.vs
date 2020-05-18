#version 330

layout (location = 0) in vec3 inPosition;
layout (location = 1) in vec3 inColor;
layout (location = 2) in vec3 inNormal;
layout (location = 3) in vec2 inUV;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 transformationMatrix;
uniform vec3 cameraPosition;
uniform float animValue;
uniform float gameSpeed;
uniform sampler2D heightSampler;
uniform sampler2D dudvSampler;

out vec3 color;
out vec3 normal;
out vec2 uv;
out vec3 worldPosition;
out vec4 clipSpace;
out vec3 cameraVector;
out float movingCoords;

const float waveStrength = 2.9f;

vec3 getWavePositions(vec3 positions, vec2 uv, float moveBase) {
	vec3 pos = vec3(0.0f, -waveStrength * 0.5f, 0.0f) + positions;
	float moveFactor = moveBase / 6;
	vec2 baseUV = inUV * 32;
	
	vec2 distortedTexCoords = texture(dudvSampler, vec2(baseUV.x + moveFactor, baseUV.y) / 2).rg * 0.75f;
	distortedTexCoords = baseUV + vec2(distortedTexCoords.x, distortedTexCoords.y + moveFactor);
	vec2 totalDistortion = (texture(dudvSampler, distortedTexCoords / 2).rg * 2.0f - 1.0f) * 0.035f;

	float height = texture(heightSampler, totalDistortion).r;
	height -= texture(heightSampler, totalDistortion / 4).r;
	height += texture(heightSampler, totalDistortion / 2).r;
	height *= waveStrength;
	
    pos.x += sin(height); 
    pos.y += height;
    pos.z += cos(height); 
    
    return pos;
}

void main() {
 	movingCoords = animValue * gameSpeed;
 	uv = inUV;

	vec4 position = transformationMatrix * vec4(getWavePositions(inPosition, uv, movingCoords), 1.0);
    gl_Position = projectionMatrix * viewMatrix * position;

	clipSpace = projectionMatrix * viewMatrix * position;
    
    color = inColor;
    normal = (transformationMatrix * vec4(inNormal, 0.0)).xyz;
    worldPosition = position.xyz;
    cameraVector = cameraPosition - position.xyz;
}