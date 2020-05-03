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
const float far = 2000.0;


float linearizeDepth(float depth) {
    float z = depth * 2.0 - 1.0;
    return (2.0 * near * far) / (far + near - z * (far - near));	
}

void main() {
	vec2 ndc = (clipSpace.xy / clipSpace.w) / 2.0 + 0.5;
	vec2 projectionCoords = vec2(ndc.x, ndc.y);

	vec3 front = texture(frontSampler, projectionCoords).rgb;
	vec3 back = texture(backSampler, projectionCoords).rgb;
	
	 if (renderMode == 90) {
    	fragColor = vec4(modelPositions, 1);
    } else if (renderMode == 100) {
    	fragColor = vec4(modelPositions, 1);
    } else {
    	vec3 dir = normalize(back - front);
    	vec3 pos = front;
 		
	    vec4 dst = vec4(0, 0, 0, 0);
	    vec4 src = vec4(0, 0, 0, 0);
	 
	    float value = 0;
	 
	    vec3 Step = dir * 0.0125;
	 
	    for(int i = 0; i < 80; i++) {
	    	vec3 layerPos = pos;
	    	layerPos.xy += windDirection * animValue;
	    	
	        value = textureLod(noiseSampler, 4 * layerPos, 0.0f).r;
	             
	        src = vec4(value, value, value, value);
	        src.a *= 0.5f;
	            
	        src.rgb *= src.a;
	        dst = (1.0f - dst.a) * src + dst;     
	     
	        if (dst.a >= .95f) break; 

	        pos += Step;
	     
	        if (pos.x > 1.0f || pos.y > 1.0f || pos.z > 1.0f) break;
	    }
	 
    	fragColor = vec4(dst.rgb, dst.r);
    	//fragColor = textureLod(noiseSampler, modelPositions, 0.0);
    }
}