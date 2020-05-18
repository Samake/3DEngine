#version 140

in vec2 textureCoords;

out vec4 outColour;

uniform sampler2D mainTexture;
uniform sampler2D brightTexture;
uniform float bloomLevel;

const float blurValue = 0.0035f;

void main(void) {
	float modifier1 = 1.25f;
	float modifier2 = 1.5f;
	
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
	baseTexture.rgb += blurredBrightness.rgb * bloomLevel;
	
	outColour = baseTexture;
}