#version 330

layout (location = 0) out vec4 outColor;
layout (location = 1) out vec4 brightColor;

uniform vec3 color;
uniform float amount;

void main() {
    outColor = vec4(color * amount * 1.2, 0.65);
    brightColor = vec4(color * amount * 1.2, 1.0);
}