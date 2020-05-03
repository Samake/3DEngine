#version 330

out vec4 fragColor;

uniform vec3 color;
uniform float amount;

void main() {
    fragColor = vec4(color * amount * 1.2, 0.65);
}