#version 330

precision mediump float;

const int MAX_LIGHTS = 16;

in vec3 color;
in vec3 normalIn;
in vec2 uv;
in vec3 modelPosition;
in vec3 worldPosition;
in vec4 clipSpace;

out vec4 fragColor;

struct Attenuation {
    float constant;
    float linear;
    float exponent;
};

struct Light {
	int type;
    vec3 color;
    vec3 position;
    vec3 direction;
    vec3 rotation;
    float intensity;
    float cutOff;
    Attenuation attenuation;
};

uniform sampler3D noiseSampler;
uniform sampler2D frontSampler;
uniform sampler2D backSampler;

uniform Light light[MAX_LIGHTS];
uniform vec3 cameraPosition;
uniform vec3 ambientColor;
uniform float ambientStrength;
uniform float fogDensity;
uniform int renderMode;
uniform vec2 windDirection;
uniform float animValue;

const float near = 0.2; 
const float far = 10000.0;
const float windSpeed = 12;

vec3 addDirectionalLight(vec3 normalIn, Light light, vec3 lightDirection) {
	float diffuseFactor = max(dot(normalIn, normalize(lightDirection)), 0.0);

    return light.color * light.intensity * diffuseFactor;
}

float linearizeDepth(float depth) {
    float z = depth * 2.0f - 1.0f;
    return (2.0f * near * far) / (far + near - z * (far - near));	
}

float hash(float n) {
	return fract(sin(n)*43758.5435);
}

float noise(vec3 x) {
	vec3 p = floor(x);
	vec3 f = fract(x);
	
	f = f * f * (3.0 - 2.0 * f);
	float n = p.x + p.y*57.0 + p.z*113.0;
	
	return mix(
		mix(
			mix(hash(n + 000.0), hash(n + 001.0), f.x),
			mix(hash(n + 057.0), hash(n + 058.0), f.x),
			f.y),
		mix(
			mix(hash(n + 113.0), hash(n + 114.0), f.x),
			mix(hash(n + 170.0), hash(n + 171.0), f.x),
			f.y),
		f.z);
}

float fbm(vec3 p) {
	float f = 0.0;
	
	f += 0.5000 * noise(p); p *= 2.02;
	f += 0.2500 * noise(p); p *= 2.04;
	f += 0.1250 * noise(p); p *= 2.00;
	f += 0.0625 * noise(p); p *= 1.97;
	f /= 0.9375;
	
	return f;
}

float volume(vec3 p, out float rawDens, float density) {
	p.xz += windDirection * animValue * windSpeed;
	
	float dens = -p.y - 1.0;
	
	rawDens = dens + 1.0 * fbm(2.0 * p);
	dens = clamp(rawDens, 0.0, 1.0);
	
	return dens * density;
}

vec4 volumetric(vec3 ro, vec3 rd, vec3 col, float mt, float density, int raySteps) {
	vec4 sum = vec4(0);
	
	float ste = 0.055;
	float t = ste;
	
	for (int i = 0; i < raySteps; i++) {
		vec3 pos = ro + rd * t;
		if(sum.a > 0.99) continue;
		
		float dens, rawDens;
		dens = volume(pos, rawDens, density);
		
		vec4 col2 = vec4(mix(vec3(0.2), vec3(1.0), dens), dens);
		col2.rgb *= col2.a;
		sum = sum + col2*(1.0 - sum.a);
		
		float sm = 1. + 2.5*(1.0 - clamp(rawDens+1.0, 0.0, 1.0));
		t += ste*sm;
	}
	
	return clamp(sum, 0.0, 1.0);
}

float map(vec3 p) {
	p.x -= 1.5;
	p.y += 0.3;
	float s =  length(p) - 1.0;

	p.x += 3.0;
	p.y += 0.3;
	float s2 =  length(p) - 1.0;

	return min(s,s2);
}

float march(vec3 ro, vec3 rd) {
	float t = 0.0;
	for(int i = 0; i < 64; i++) {
		float h = map(ro + rd*t);
		t += h;
	}
	
	return t;
}

vec3 normal(vec3 p) {
	vec2 h = vec2(0.001, 0.0);
	vec3 n = vec3(
		map(p + h.xyy) - map(p - h.xyy),
		map(p + h.yxy) - map(p - h.yxy),
		map(p + h.yyx) - map(p - h.yyx)
	);
	
	return normalize(n);
}

mat3 camera(vec3 eye, vec3 lat) {
	vec3 ww = normalize(lat - eye);
	vec3 uu = normalize(cross(vec3(0, 1, 0), ww));
	vec3 vv = normalize(cross(ww, uu));
		
	return mat3(uu, vv, ww);	
}

vec4 calcCloudsV1() {
	vec2 ndc = (clipSpace.xy / clipSpace.w) / 2.0f + 0.5f;
	vec2 projectionCoords = vec2(ndc.x, ndc.y);
	
	vec3 front = texture(frontSampler, projectionCoords).rgb;
	vec3 back = texture(backSampler, projectionCoords).rgb;
	vec3 dir = normalize(front - back);
	
    vec3 pos = modelPosition;
	
    vec4 dst = vec4(0, 0, 0, 0);
    vec4 src = vec4(0, 0, 0, 0);
 
    float value = 0;
 
    vec3 Step = dir * 0.0005f;
 
    for(int i = 0; i < 64; i++) {
    	int tiling = 1;
    	vec3 layerPos1 = pos * tiling;
    	vec3 layerPos2 = pos * tiling;
    	vec3 layerPos3 = pos * tiling;
    	vec3 layerPos4 = pos * tiling;
    	layerPos1.xy += windDirection * animValue * 0.15;
    	layerPos2.xy += windDirection * animValue * 0.25;
    	layerPos3.xy += windDirection * animValue * 0.65;
    	layerPos4.xy += windDirection * animValue * 0.75;
    	
        value = textureLod(noiseSampler, 0.25f * layerPos1, 3.0f).r * textureLod(noiseSampler, 4.0f * layerPos2, 3.0f).r;
        value += sin(textureLod(noiseSampler, 0.5f * layerPos2, 3.0f).r * textureLod(noiseSampler, 2.0f * layerPos3, 3.0f).r);
       	value += sin(textureLod(noiseSampler, 0.75f * layerPos3, 3.0f).r * textureLod(noiseSampler, 1.0f * layerPos4, 3.0f).r);
       	value += sin(textureLod(noiseSampler, 1.0f * layerPos4, 3.0f).r * textureLod(noiseSampler, 0.5f * layerPos1, 3.0f).r);
       	
        src = vec4(value, value, value, value);
        src.a *= sin(i / 64.0f) * 0.75f;
            
        src.rgb *= src.a;
        dst = (1.0f - dst.a) * src + dst;     
     
        if (dst.a >= .95f) break; 

        pos += Step;
     
        if (pos.x > 1.0f || pos.y > 1.0f || pos.z > 1.0f) break;
    }
    
    vec4 albedoMap = vec4(dst.rgb, dst.r);
    vec3 normalMap = normal(dst.rgb);
    vec3 diffuseMap = vec3(0.0, 0.0, 0.0);
    
    for (int i = 0; i < MAX_LIGHTS; i++) {
   		vec3 lightDirection = light[i].position - worldPosition;
   		
		if (light[i].type == 0) {
			diffuseMap += addDirectionalLight(normalMap, light[i], lightDirection);
		}
	}
    
    albedoMap.rgb *= ambientColor * ambientStrength + diffuseMap;
    

    albedoMap = vec4(dst.rgb, dst.r);
    //albedoMap = vec4(value, value, value, 1);
    //albedoMap = vec4(diffuseMap, 1);
	//albedoMap = vec4(normalMap, 1);
	//albedoMap = textureLod(noiseSampler, modelPosition, 0.0);
	
	return albedoMap;
}

vec4 calcCloudsV2() {
	int raySteps = 64;
	vec2 ndc = (clipSpace.xy / clipSpace.w) / 2.0f + 0.5f;
	vec2 projectionCoords = vec2(ndc.x, ndc.y);
	
	vec3 front = texture(frontSampler, projectionCoords).rgb;
	vec3 back = texture(backSampler, projectionCoords).rgb;
	vec3 dir = normalize(back - front);
	
    vec3 pos = modelPosition;
	
    vec4 dst = vec4(0, 0, 0, 0);
    vec4 src = vec4(0, 0, 0, 0);
 
    float value = 0;
 
    vec3 Step = dir * (1.0f / raySteps) * 0.05f;

    for(int i = 0; i < raySteps; i++) {
    	vec3 layerPos = pos;
    	layerPos.xy += windDirection * animValue;
    	
        value = fbm(layerPos * 16);

        src = vec4(value, value, value, value);
       	//src.a *= value;
       	src.a *= sin(i / 64.0f) * 0.75f;
       	
        src.rgb *= src.a;
        dst = (1.0f - dst.a) * src + dst;     
     
        if (dst.a >= .95f) break; 

        pos += Step;
     
        if (pos.x > 1.0f || pos.y > 1.0f || pos.z > 1.0f) break;
    }
    
    vec3 normalMap = normal(dst.rgb);
	vec3 diffuseMap = vec3(0.0, 0.0, 0.0);
    
    for (int i = 0; i < MAX_LIGHTS; i++) {
   		vec3 lightDirection = light[i].position - worldPosition;
   		
		if (light[i].type == 0) {
			diffuseMap += addDirectionalLight(normalMap, light[i], lightDirection);
		}
	}
	
	vec4 albedoMap = vec4(dst.rgb, dst.r);
	//vec4 albedoMap = vec4(dst.rgb, 1);
	//vec4 albedoMap = vec4(normalMap, 1);
	//vec4 albedoMap = vec4(diffuseMap, 1);
	
    return albedoMap;
}

vec4 calcCloudsV3() {
	int raySteps = 64;
	
	vec3 layerPos = modelPosition;
	layerPos.xy += windDirection * animValue / windSpeed;
	
	float density = fbm(16 * layerPos);
	density -= fbm(8 * layerPos);
	density *= fbm(4 * layerPos) * 32;
	
	density = clamp(density, 0.0f, 1.0f);
	
	vec3 ro = normalize(cameraPosition);
	vec3 rd = normalize(cameraPosition - worldPosition);
	
	vec3 col = vec3(0, 0, 0);
	
	float i = march(ro, rd);
	vec3 pos = ro + rd * i;
	
	vec4 fog = volumetric(ro, rd, col, i, density, raySteps);
	fog.r *= density;
	
	fog *= 1.5;
	
	col = mix(fog.xyz, col, 1.0 - fog.w);
	col = pow(col, vec3(.454545));
	
	vec4 albedoMap = vec4(col, fog.r);
	//vec4 albedoMap = vec4(density, density, density, 1);
	//vec3 normalMap = normal(albedoMap.rgb);
	
    return albedoMap;
}

void main() {
	if (renderMode == 90) {
		//float depth = linearizeDepth(gl_FragCoord.z) / far;
		//fragColor = vec4(depth, depth, depth, 1);
    	fragColor = vec4(modelPosition, 1);
    } else if (renderMode == 100) {
    	//float depth = linearizeDepth(gl_FragCoord.z) / far;
		//fragColor = vec4(depth, depth, depth, 1);
    	fragColor = vec4(modelPosition, 1);
    } else {
    	//fragColor = calcCloudsV1();
    	//fragColor = calcCloudsV2();
    	fragColor = calcCloudsV3();
    }
}