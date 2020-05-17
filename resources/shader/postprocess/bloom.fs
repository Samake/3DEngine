#version 140

in vec2 textureCoords;

out vec4 out_Colour;

uniform sampler2D mainTexture;
uniform sampler2D brightTexture;

const float blurValue = 0.0025f;

void main(void) {
	float modifier1 = 1.5f;
	float modifier2 = 2.0f;
	
	vec2 uv = textureCoords;
	
	vec4 blurredBrightness = texture(brightTexture, vec2(uv.x, uv.y));
	blurredBrightness += texture(brightTexture, vec2(uv.x + blurValue, uv.y));
	blurredBrightness += texture(brightTexture, vec2(uv.x, uv.y + blurValue));
	blurredBrightness += texture(brightTexture, vec2(uv.x + blurValue, uv.y + blurValue));
	blurredBrightness += texture(brightTexture, vec2(uv.x - blurValue, uv.y));
	blurredBrightness += texture(brightTexture, vec2(uv.x, uv.y - blurValue));
	blurredBrightness += texture(brightTexture, vec2(uv.x - blurValue, uv.y - blurValue));
	blurredBrightness += texture(brightTexture, vec2(uv.x + blurValue, uv.y - blurValue));
	blurredBrightness += texture(brightTexture, vec2(uv.x - blurValue, uv.y + blurValue));
	
	blurredBrightness += texture(brightTexture, vec2(uv.x + blurValue * modifier1, uv.y));
	blurredBrightness += texture(brightTexture, vec2(uv.x, uv.y + blurValue * modifier1));
	blurredBrightness += texture(brightTexture, vec2(uv.x + blurValue * modifier1, uv.y + blurValue * modifier1));
	blurredBrightness += texture(brightTexture, vec2(uv.x - blurValue * modifier1, uv.y));
	blurredBrightness += texture(brightTexture, vec2(uv.x, uv.y - blurValue * modifier1));
	blurredBrightness += texture(brightTexture, vec2(uv.x - blurValue * modifier1, uv.y - blurValue * modifier1));
	blurredBrightness += texture(brightTexture, vec2(uv.x + blurValue * modifier1, uv.y - blurValue * modifier1));
	blurredBrightness += texture(brightTexture, vec2(uv.x - blurValue * modifier1, uv.y + blurValue * modifier1));
	
	blurredBrightness += texture(brightTexture, vec2(uv.x + blurValue * modifier2, uv.y));
	blurredBrightness += texture(brightTexture, vec2(uv.x, uv.y + blurValue * modifier2));
	blurredBrightness += texture(brightTexture, vec2(uv.x + blurValue * modifier2, uv.y + blurValue * modifier2));
	blurredBrightness += texture(brightTexture, vec2(uv.x - blurValue * modifier2, uv.y));
	blurredBrightness += texture(brightTexture, vec2(uv.x, uv.y - blurValue * modifier2));
	blurredBrightness += texture(brightTexture, vec2(uv.x - blurValue * modifier2, uv.y - blurValue * modifier2));
	blurredBrightness += texture(brightTexture, vec2(uv.x + blurValue * modifier2, uv.y - blurValue * modifier2));
	blurredBrightness += texture(brightTexture, vec2(uv.x - blurValue * modifier2, uv.y + blurValue * modifier2));
	
	blurredBrightness /= 25;
	
	vec4 baseTexture = texture(mainTexture, textureCoords);
	
	
	out_Colour = baseTexture + blurredBrightness;
	//out_Colour.rgb *= vec3(1.0, 0.2, 0.2);
}