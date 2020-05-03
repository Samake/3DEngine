#version 330

layout (location = 0) in vec3 inPosition;
layout (location = 1) in vec3 inColor;
layout (location = 2) in vec3 inNormal;
layout (location = 3) in vec2 inUV;

out vec3 color;
out vec3 normal;
out vec2 uv;
out vec3 modelPositions;
out vec4 clipSpace;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelViewMatrix;
uniform mat4 transformationMatrix;

void main() {
	vec4 position = transformationMatrix * vec4(inPosition, 1.0);
    gl_Position = projectionMatrix * viewMatrix * position;

    color = inColor;
    normal = (transformationMatrix * vec4(inNormal, 0.0)).xyz;
    uv = inUV;
    modelPositions = normalize(position.xyz);
    
    clipSpace = projectionMatrix * viewMatrix * position;
}