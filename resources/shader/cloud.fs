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
const float windSpeed = 6;

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
	
	f += 0.5000 * noise(p); p *= 2.00;
	f += 0.3500 * noise(p); p *= 2.04;
	f += 0.2250 * noise(p); p *= 2.02;
	f += 0.1625 * noise(p); p *= 1.97;
	f /= 0.9375;
	
	return f;
}

float volume(vec3 p, out float rawDens, float density, float randomness) {
	vec3 position = p;
	
	float dens = -position.y - 1.0;
	
	position.xz += windDirection * animValue * windSpeed;
	position.y -= windDirection.x * animValue * windSpeed;
	position.y += windDirection.y * animValue * windSpeed;
	
	rawDens = dens + (density * randomness * fbm(2.0 * position));
	dens = clamp(rawDens, 0.0, 1.0);
	
	return dens;
}

vec4 volumetric(vec3 ro, vec3 rd, vec3 col, float mt, float density, float randomness, int raySteps) {
	vec4 sum = vec4(0);
	
	float ste = 0.055;
	float t = ste;
	
	for (int i = 0; i < raySteps; i++) {
		vec3 pos = ro + rd * t;
		if(sum.a > 0.99) continue;
		
		float dens, rawDens;
		dens = volume(pos, rawDens, density, randomness);
		
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

vec4 calcCloudsV3() {
	int raySteps = 64;
	
	vec3 layerPos = modelPosition;
	layerPos.xy += windDirection * animValue / windSpeed;
	
	float density = fbm(64 * layerPos);
	density -= fbm(8 * layerPos);
	density *= fbm(32 * layerPos) * 16;

	density = clamp(density, 0.0f, 1.0f);
	
	float randomness = fbm(4 * layerPos);
	randomness -= fbm(8 * layerPos);
	randomness += fbm(16 * layerPos);
	
	randomness = clamp(randomness, 0.0f, 1.0f);
	
	vec3 ro = normalize(cameraPosition);
	vec3 rd = normalize(cameraPosition - worldPosition);
	
	vec3 col = vec3(0, 0, 0);
	
	float i = march(ro, rd);

	vec4 fog = volumetric(ro, rd, col, i, density, randomness, raySteps) * 1.5;
	
	fog.rgb = clamp(fog.rgb, 0.0f, 1.0f);
	fog.a *= fog.r;

	col = mix(fog.rgb, col, 1.0 - fog.a);
	col = pow(col, vec3(.454545));
	
	vec4 albedoMap = vec4(col, fog.a * density);
	//vec4 albedoMap = vec4(density, density, density, 1);
	//vec4 albedoMap = vec4(randomness, randomness, randomness, 1);
	//vec4 albedoMap = vec4(fog.rgb, 1);
	//vec4 albedoMap = vec4(density, density, density, 1);
	//vec3 normalMap = normal(albedoMap.rgb);
	
    return albedoMap;
}

vec4 calcClouds() {
	int raySteps = 64;

	float density = 1;
	float randomness = 1;
	
	vec3 ro = normalize(vec3(0.0, 50.0, 0.0));
	vec3 rd = normalize(cameraPosition - worldPosition);
	
	vec3 col = vec3(0, 0, 0);
	
	float i = march(ro, rd);

	vec4 fog = volumetric(ro, rd, col, i, density, randomness, raySteps);
	
	//fog.rgb = clamp(fog.rgb, 0.0f, 1.0f);
	//fog.a *= fog.r;

	col = mix(fog.rgb, col, 1.0 - fog.a);
	col = pow(col, vec3(.454545));
	
	vec4 albedoMap = vec4(col, fog.a);
    return albedoMap;
}

void main() {
    fragColor = calcClouds();
    //fragColor = vec4(1, 0, 0, 1);
}