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
uniform float waveStrength;

out vec3 color;
out vec3 normal;
out vec2 uv;
out vec3 worldPosition;
out vec4 clipSpace;
out vec3 cameraVector;
out float movingCoords;
out float modelHeight;

vec3 getWavePositions(vec3 positions, float moveBase) {
	vec3 pos = vec3(0.0f, -waveStrength, 0.0f) + positions;

	float waveValue = sin((pos.x / 64 + moveBase * 8)) * cos(pos.z / 64 + moveBase * 4);
	waveValue += sin((pos.x / 16 + moveBase * 12)) * cos(pos.z / 16 + moveBase * 8);
	waveValue -= sin((pos.x / 4 + moveBase * 12)) * cos(pos.z / 4 + moveBase * 16);
	
    pos.y *= waveStrength * waveValue * 0.25f;

    return pos;
}

void main() {
 	movingCoords = animValue * 0.75f * gameSpeed;
 	uv = inUV;
	
	vec4 wavePosition = vec4(getWavePositions(inPosition, movingCoords), 1.0);
	vec4 position = transformationMatrix * wavePosition;
    gl_Position = projectionMatrix * viewMatrix * position;

	clipSpace = projectionMatrix * viewMatrix * position;
    
    color = inColor;
    normal = (transformationMatrix * vec4(inNormal, 0.0)).xyz;
    worldPosition = position.xyz;
    modelHeight = wavePosition.y * 0.5;
    cameraVector = cameraPosition - position.xyz;
}