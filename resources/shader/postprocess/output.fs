#version 140

in vec2 textureCoords;

out vec4 out_Colour;

uniform sampler2D colorTexture;
uniform float saturation;
uniform float contrast;
uniform float brightness;

void main(void) {
	vec4 baseColor = texture(colorTexture, textureCoords);
	
	vec3 luminanceWeights = vec3(0.299, 0.587, 0.114);
	float luminance = dot(baseColor.rgb, luminanceWeights);
	vec4 finalColor = vec4(mix(vec3(luminance, luminance, luminance), baseColor.rgb, saturation), 1.0f);
	
	finalColor.a = baseColor.a;
	finalColor.rgb = ((finalColor.rgb - 0.5f) * max(contrast, 0)) + 0.5f;
	finalColor.rgb *= brightness;
	
	out_Colour = finalColor;
}