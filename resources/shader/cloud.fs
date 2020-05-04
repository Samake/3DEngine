#version 330

const int MAX_LIGHTS = 16;

in vec3 color;
in vec3 normal;
in vec2 uv;
in vec3 modelPositions;
in vec4 clipSpace;

out vec4 fragColor;

uniform sampler3D noiseSampler;
uniform sampler2D frontSampler;
uniform sampler2D backSampler;

uniform vec3 cameraPosition;
uniform vec3 ambientColor;
uniform float ambientStrength;
uniform float fogDensity;
uniform int renderMode;
uniform vec2 windDirection;
uniform float animValue;

const float near = 0.2; 
const float far = 10000.0;


float linearizeDepth(float depth) {
    float z = depth * 2.0f - 1.0f;
    return (2.0f * near * far) / (far + near - z * (far - near));	
}

void main() {
	vec2 ndc = (clipSpace.xy / clipSpace.w) / 2.0f + 0.5f;
	vec2 projectionCoords = vec2(ndc.x, ndc.y);

	vec3 front = texture(frontSampler, projectionCoords).rgb;
	vec3 back = texture(backSampler, projectionCoords).rgb;
	vec3 dir = normalize(front - back);
	
	 if (renderMode == 90) {
    	fragColor = vec4(modelPositions, 1);
    } else if (renderMode == 100) {
    	fragColor = vec4(modelPositions, 1);
    } else {
    	vec3 pos = modelPositions;//back;
 		
	    vec4 dst = vec4(0, 0, 0, 0);
	    vec4 src = vec4(0, 0, 0, 0);
	 
	    float value = 0;
	 
	    vec3 Step = dir * 0.0005f;
	 
	    for(int i = 0; i < 48; i++) {
	    	int tiling = 1;
	    	vec3 layerPos1 = pos * tiling;
	    	vec3 layerPos2 = pos * tiling;
	    	vec3 layerPos3 = pos * tiling;
	    	layerPos1.xy += windDirection * animValue * 0.5;
	    	layerPos2.xy += windDirection * animValue * 0.75;
	    	layerPos3.xy += windDirection * animValue;
	    	
	        value = textureLod(noiseSampler, 1.0 * layerPos1, 1.0f).r * textureLod(noiseSampler, 2.0 * layerPos1, 0.0f).r;
	        value += sin(textureLod(noiseSampler, 2 * layerPos2, 1.0f).r * textureLod(noiseSampler, 1.5 * layerPos2, 0.0f).r);
	       	value += sin(textureLod(noiseSampler, 0.25 * layerPos3, 1.0f).r * textureLod(noiseSampler, 0.5 * layerPos3, 0.0f).r);
	       	
	        src = vec4(value, value, value, value);
	        src.a *= sin(i / 48.0f) * 0.5f;
	            
	        src.rgb *= src.a;
	        dst = (1.0f - dst.a) * src + dst;     
	     
	        if (dst.a >= .95f) break; 

	        pos += Step;
	     
	        if (pos.x > 1.0f || pos.y > 1.0f || pos.z > 1.0f) break;
	    }
	 
    	fragColor = vec4(dst.rgb, dst.r);
    	//fragColor = vec4(dir, 1);
    	//fragColor = textureLod(noiseSampler, modelPositions, 0.0);
    }
}