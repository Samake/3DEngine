#version 330

layout (location = 0) in vec3 inPosition;
layout (location = 1) in vec3 inColor;
layout (location = 2) in vec3 inNormal;
layout (location = 3) in vec2 inUV;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelViewMatrix;
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
out mat4 mViewMatrix;
out vec4 clipSpace;
out vec3 cameraVector;
out float movingCoords;

const float waveStrength = 0.75f;

vec3 getWavePositions(vec2 uv, float moveBase) {
	vec3 pos = vec3(0, -waveStrength, 0);
	float moveFactor = moveBase / 3;
	vec2 baseUV = inUV * 32;
	
	vec2 distortedTexCoords = texture(dudvSampler, vec2(baseUV.x + moveFactor, baseUV.y)).rg * 0.25f;
	distortedTexCoords = baseUV + vec2(distortedTexCoords.x, distortedTexCoords.y + moveFactor);

	float height = texture(heightSampler, distortedTexCoords).r;
	height += texture(heightSampler, distortedTexCoords / 4).g;
	height += texture(heightSampler, distortedTexCoords / 2).b;
	height *= waveStrength;
	
    pos.x += sin(height); 
    pos.y += height;
    pos.z += cos(height); 
    
    return pos;
}

void main() {
 	movingCoords = animValue * gameSpeed;
 	uv = inUV;

	vec4 position = transformationMatrix * vec4(inPosition + getWavePositions(uv, movingCoords), 1.0);
    gl_Position = projectionMatrix * viewMatrix * position;

	clipSpace = projectionMatrix * viewMatrix * position;
    
    color = inColor;
    normal = (transformationMatrix * vec4(inNormal, 0.0)).xyz;
    worldPosition = position.xyz;
    mViewMatrix = modelViewMatrix;
    cameraVector = cameraPosition - position.xyz;
}