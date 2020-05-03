#version 330

layout (location = 0) in vec3 inPosition;
layout (location = 1) in vec3 inColor;
layout (location = 2) in vec3 inNormal;
layout (location = 3) in vec2 inUV;

out vec3 color;
out vec3 normal;
out vec2 uv;
out vec3 worldPosition;
out mat4 mViewMatrix;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelViewMatrix;
uniform mat4 transformationMatrix;
uniform vec4 clipPlane;

void main() {
	vec4 position = transformationMatrix * vec4(inPosition, 1.0);
    gl_Position = projectionMatrix * viewMatrix * position;
	gl_ClipDistance[0] = dot(position, clipPlane);
    
    color = inColor;
    normal = (transformationMatrix * vec4(inNormal, 0.0)).xyz;
    uv = inUV;
    worldPosition = position.xyz;
    mViewMatrix = modelViewMatrix;
}