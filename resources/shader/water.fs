#version 330

const int MAX_LIGHTS = 16;

in vec3 color;
in vec3 normal;
in vec2 uv;
in vec3 worldPosition;
in vec4 clipSpace;
in vec3 cameraVector;
in float movingCoords;
in float modelHeight;

layout (location = 0) out vec4 outColor;
layout (location = 1) out vec4 brightColor;

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

struct Material {
    vec3 color;
    float shininess;
   	float reflectance;
   	int tiling;
};

uniform Light light[MAX_LIGHTS];
uniform Material material;
uniform sampler2D heightSampler;
uniform sampler2D dudvSampler;
uniform sampler2D normalSampler;
uniform sampler2D depthSampler;
uniform sampler2D refractionSampler;
uniform sampler2D reflectionSampler;
uniform sampler2D foamSampler;
uniform vec3 cameraPosition;
uniform float alpha;
uniform vec3 ambientColor;
uniform float ambientStrength;
uniform float fogDensity;
uniform int renderMode;

const float near = 0.5; 
const float far = 4096;

float linearizeDepth(float depth) {
    float z = depth * 2.0 - 1.0;
    return (2.0 * near * far) / (far + near - z * (far - near));	
}

float hash(float n) {
	return fract(sin(n) * 43758.5435);
}

float noise(vec3 x) {
	vec3 p = floor(x);
	vec3 f = fract(x);
	
	f = f * f * (3.0 - 2.0 * f);
	float n = p.x + p.y * 57.0 + p.z * 113.0;
	
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

vec3 calcSpecularMap(Light light, vec3 lightDirection, vec3 normalIn, float specValue, int lightType) {
	vec3 lightDir   = normalize(lightDirection);
	vec3 viewDir    = normalize(cameraPosition - worldPosition);
	vec3 halfwayDir = normalize(lightDir + viewDir);
	
	float spec = pow(max(dot(normalIn, halfwayDir), 0.0), material.shininess);
	spec += pow(max(dot(normalIn, halfwayDir), 0.0), material.shininess / 8) * 0.75f;
	spec += pow(max(dot(normalIn, halfwayDir), 0.0), material.shininess / 16) * 0.5;
	
	vec3 specularLight = light.color * spec / 3;

    if (lightType == 0) {
    	float diffuseFactor = max(dot(normalIn, normalize(lightDirection)), 0.0);
    	specularLight *= diffuseFactor * specValue * light.intensity * material.reflectance;
    }
    
	if (lightType == 1) {
		specularLight *= specValue * light.intensity * material.reflectance;
		
		float distance = length(lightDirection);
    	float attenuation = 1.0 / (light.attenuation.constant + (light.attenuation.linear * distance) + (light.attenuation.exponent * distance* distance));
    	
    	specularLight *= 2 * attenuation;
	}
	
	if (lightType == 2) {
		specularLight *= specValue * light.intensity * material.reflectance;
		
		float distance = length(lightDirection);
    	float attenuation = 1.0 / (light.attenuation.constant + (light.attenuation.linear * distance) + (light.attenuation.exponent * distance* distance));
    	
    	specularLight *= 2 * attenuation;;
    	
    	float spotAlfa = dot(normalize(lightDirection), normalize(light.direction));
    	
    	if (spotAlfa > light.cutOff) {
	    	specularLight *= (1.0 - (1.0 - spotAlfa) / (1.0 - light.cutOff));
	    } else {
	    	specularLight = vec3(0.0, 0.0, 0.0);
	    }
	}
    
    return specularLight * normalIn.g * normalIn.g;
}

vec3 addDirectionalLight(vec3 normalIn, Light light, vec3 lightDirection) {
	float diffuseFactor = max(dot(normalIn, normalize(lightDirection)), 0.0);

    return light.color * light.intensity * diffuseFactor;
}

vec3 addPointLight(vec3 normalIn, Light light, vec3 lightDirection) {
	float diffuseFactor = max(dot(normalIn, normalize(lightDirection)), 0.0);
    float distance = length(lightDirection);
    float attenuation = 1.0 / (light.attenuation.constant + (light.attenuation.linear * distance) + (light.attenuation.exponent * distance* distance));
	vec3 diffuse = light.color * light.intensity * diffuseFactor;

	return diffuse * attenuation;;
}

vec3 addSpotLight(vec3 normalIn, Light light, vec3 lightDirection) {
	float diffuseFactor = max(dot(normalIn, normalize(lightDirection)), 0.0);
    float distance = length(lightDirection);
    float attenuation = 1.0 / (light.attenuation.constant + (light.attenuation.linear * distance) + (light.attenuation.exponent * distance* distance));
    vec3 diffuse = light.color * light.intensity * diffuseFactor;
    float spotAlfa = dot(normalize(lightDirection), normalize(light.direction));
    
    vec3 lightValue = vec3(0.0, 0.0, 0.0);
    
    if (spotAlfa > light.cutOff) {
    	lightValue = diffuse * attenuation;
    	lightValue *= (1.0 - (1.0 - spotAlfa) / (1.0 - light.cutOff));
    }

	return lightValue;
}

vec4 calculateLuminance(vec4 color) {
    float lum = (color.r + color.g + color.b) / 3;
    float adj = lum - 0.1;
    adj = adj / (1.01 - 0.3);
    color = color * adj;
    color += 0.17;
	
	return color;
}

void main() {
	vec3 diffuseMap = vec3(0.0, 0.0, 0.0);
	vec3 specularMap = vec3(0.0, 0.0, 0.0);
	float specValue = 1.0f;
	
	vec2 ndc = 0.5f * (clipSpace.xy / clipSpace.w) + 0.5f;
	vec2 refractionTexCoords = vec2(ndc.x, ndc.y);
	vec2 reflectionTexCoords = vec2(ndc.x, -ndc.y);
	
	float depth = texture(depthSampler, refractionTexCoords).r;
	float floorDistance = linearizeDepth(depth);
	depth = gl_FragCoord.z;
	float waterDistance = linearizeDepth(depth);
	float depthBase = (floorDistance - waterDistance);
	float depthBorder = clamp(depthBase * 2, 0.0f, 1.0f);
	float depthBorderNeg = 1 - depthBorder;
	float waterDepth = clamp(depthBase / 128.0f, 0.0f, 1.0f);
	float waterDepthNeg = 1 - waterDepth;
	
	vec2 baseUV = uv * material.tiling;
	vec2 distortedTexCoords = texture(dudvSampler, vec2(baseUV.x + movingCoords, baseUV.y) * 2).rg * 0.5f;
	distortedTexCoords = baseUV + vec2(distortedTexCoords.x - movingCoords, distortedTexCoords.y + movingCoords);
	vec2 totalDistortion = (texture(dudvSampler, distortedTexCoords / 2).rg * 2.0f - 1.0f) * 0.03f;
	
	float blurValue = 0.0015f;
	
	refractionTexCoords += totalDistortion * 0.5f;
	refractionTexCoords = clamp(refractionTexCoords, 0.001f, 0.999f);
	
	vec4 refractionBase = texture(refractionSampler, vec2(refractionTexCoords.x, refractionTexCoords.y));
	vec4 refraction = refractionBase;
	refraction.rgb *= waterDepthNeg;
	
	reflectionTexCoords += totalDistortion * 0.5f;
	reflectionTexCoords.x = clamp(reflectionTexCoords.x, 0.001f, 0.999f);
	reflectionTexCoords.y = clamp(reflectionTexCoords.y, -0.999f, -0.001f);
	
	vec4 reflection = texture(reflectionSampler, vec2(reflectionTexCoords.x, reflectionTexCoords.y));
	reflection += texture(reflectionSampler, vec2(reflectionTexCoords.x + blurValue, reflectionTexCoords.y));
	reflection += texture(reflectionSampler, vec2(reflectionTexCoords.x, reflectionTexCoords.y + blurValue));
	reflection += texture(reflectionSampler, vec2(reflectionTexCoords.x + blurValue, reflectionTexCoords.y + blurValue));
	reflection += texture(reflectionSampler, vec2(reflectionTexCoords.x - blurValue, reflectionTexCoords.y));
	reflection += texture(reflectionSampler, vec2(reflectionTexCoords.x, reflectionTexCoords.y - blurValue));
	reflection += texture(reflectionSampler, vec2(reflectionTexCoords.x - blurValue, reflectionTexCoords.y - blurValue));
	reflection += texture(reflectionSampler, vec2(reflectionTexCoords.x + blurValue, reflectionTexCoords.y - blurValue));
	reflection += texture(reflectionSampler, vec2(reflectionTexCoords.x - blurValue, reflectionTexCoords.y + blurValue));
	
	reflection /= 9;
	
	reflection = calculateLuminance(reflection);
	
	vec4 normalMap = texture(normalSampler, totalDistortion);
	vec3 texNormal = vec3(normalMap.r * 2.0f - 1.0f, normalMap.b * 5.0f, normalMap.g * 2.0f - 1.0f);
	texNormal = normalize(texNormal);
	
	vec3 viewVector = normalize(cameraVector);
	float refractiveValue = dot(viewVector, vec3(0.5f, 1.0f, 0.5f) * texNormal);
	
	for (int i = 0; i < MAX_LIGHTS; i++) {
   		vec3 lightDirection = light[i].position - worldPosition;
   		
		if (light[i].type == 0) {
			diffuseMap += addDirectionalLight(texNormal, light[i], lightDirection);
			specularMap += calcSpecularMap(light[i], lightDirection, texNormal, specValue, light[i].type);
		}
		
		if (light[i].type == 1) {
			diffuseMap += addPointLight(texNormal, light[i], lightDirection);
			specularMap += calcSpecularMap(light[i], lightDirection, texNormal, specValue, light[i].type);
		}
		
		if (light[i].type == 2) {
			diffuseMap += addSpotLight(texNormal, light[i], lightDirection);
			specularMap += calcSpecularMap(light[i], lightDirection, texNormal, specValue, light[i].type);
		}
		
		specularMap *= depthBorder;
	}
	
	vec2 foamUV = uv * (material.tiling * 16);
	vec3 foamColor = texture(foamSampler, vec2(foamUV.x + movingCoords * 2, foamUV.y) * 1.25).rgb;
	foamColor += texture(foamSampler, vec2(foamUV.x, foamUV.y + movingCoords * 3) * 1.5).rgb;
	foamColor += texture(foamSampler, vec2(foamUV.x + movingCoords * 4, foamUV.y + movingCoords * 2) * 1.75).rgb;
	vec3 foamColor1 = clamp(foamColor * ambientStrength * depthBorderNeg * ((1 - modelHeight) - 0.75f), 0.0f, 1.0f);

	vec3 basePosition = worldPosition;
	basePosition.x += movingCoords * 50;
	basePosition.z += movingCoords * 20;
	
	float noiseValue = noise(basePosition * 0.1f);
	
	basePosition.x -= movingCoords * 75;
	basePosition.z -= movingCoords * 80;
	
	noiseValue *= noise(basePosition * 0.05f);
	
	basePosition.x += movingCoords * 135;
	basePosition.z -= movingCoords * 115;
	
	noiseValue *= noise(basePosition * 0.05f) * 2;
	noiseValue *= (1 - refractiveValue);
	
	vec3 foamColor2 = clamp(foamColor * ambientStrength * modelHeight * noiseValue, 0.0f, 1.0f);
	vec3 waterOutput = clamp(mix(reflection.rgb, refraction.rgb, refractiveValue), 0.0f, 1.0f) * material.color;
	
	if (cameraPosition.y < worldPosition.y) {
		waterOutput = refractionBase.rgb * material.color;
	}
	
	diffuseMap *= (1 - modelHeight);
	
	vec3 lightMap = ambientColor * ambientStrength + diffuseMap + specularMap;
	
	waterOutput += foamColor1;
	waterOutput += foamColor2;
	waterOutput *= lightMap * 1.5;

	float fogDistance = length(cameraPosition - worldPosition);
    float fogFactor = 1.0 / exp((fogDistance * fogDensity) * (fogDistance * fogDensity));
    fogFactor = clamp(fogFactor, 0.0, 1.0);
    
    brightColor = vec4(specularMap, 1);
    
    // DEFAULT, DEBUG, WIREFRAME, DIFFUSE, NORMALS, ALBEDO, DEPTH, COLOR
    if (renderMode == 0) {
    	outColor = vec4(mix(ambientColor, waterOutput, fogFactor), depthBorder);
    } else if (renderMode == 1) {
    	outColor = vec4(mix(ambientColor, waterOutput, fogFactor), depthBorder);
    } else if (renderMode == 2) {
    	outColor = vec4(0.25, 0.25, 1.0, 1.0);
    } else if (renderMode == 3) {
    	outColor = vec4(diffuseMap, 1);
    } else if (renderMode == 4) {
    	outColor = vec4(texNormal.rgb, 1);
    } else if (renderMode == 5) {
    	outColor = vec4(material.color, 1);
    } else if (renderMode == 6) {
        float depth = linearizeDepth(gl_FragCoord.z) / far;
   	 	outColor = vec4(vec3(depth), 1.0);
    } else if (renderMode == 7) {
    	outColor = vec4(worldPosition, 1);
    } else if (renderMode == 8) {
    	outColor = vec4(0.25, 0.25, 1.0, 1.0);
    }
    
    //refraction
    //reflection
    //outColor = vec4(refraction.rgb, 1.0);
}