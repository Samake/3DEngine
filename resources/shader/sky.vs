#version 330

layout (location = 0) in vec3 inPosition;

out vec4 worldPosition;
out vec3 uv;

uniform mat4 transformationMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;
uniform vec4 clipPlane;

void main() {
	worldPosition = transformationMatrix * vec4(inPosition, 1.0);
	gl_ClipDistance[0] = dot(worldPosition, clipPlane);

	gl_Position = projectionMatrix * viewMatrix * worldPosition;
	
	uv = inPosition;
}