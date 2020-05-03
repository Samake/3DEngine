#version 330

layout (location = 0) in vec3 inPosition;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 transformationMatrix;

void main() {
	vec4 position = transformationMatrix * vec4(inPosition, 1.0);
    gl_Position = projectionMatrix * viewMatrix * position;
}