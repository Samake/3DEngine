#version 330

const int MAX_LIGHTS = 16;

in vec3 color;
in vec3 normal;
in vec2 uv;
in vec3 worldPosition;
in mat4 mViewMatrix;
in vec4 clipSpace;
in vec3 cameraVector;
in float movingCoords;

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

const float near = 0.1; 
const float far = 2000.0;

float linearizeDepth(float depth) {
    float z = depth * 2.0 - 1.0;
    return (2.0 * near * far) / (far + near - z * (far - near));	
}

vec3 calcSpecularMap(Light light, vec3 lightDirection, vec3 normalIn, float specValue, int lightType) {
	vec3 lightDir   = normalize(lightDirection);
	vec3 viewDir    = normalize(cameraPosition - worldPosition);
	vec3 halfwayDir = normalize(lightDir + viewDir);
	
	float spec = pow(max(dot(normalIn, halfwayDir), 0.0), material.shininess);
	spec += pow(max(dot(normalIn, halfwayDir), 0.0), material.shininess / 8);
	spec += pow(max(dot(normalIn, halfwayDir), 0.0), material.shininess / 16);
	
	vec3 specularLight = light.color * spec;

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
    
    return specularLight / 3;
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

void main() {
	vec3 diffuseMap = material.color;
	vec3 specularMap = vec3(0.0, 0.0, 0.0);
	float specValue = 1.0f;
	
	vec2 ndc = (clipSpace.xy / clipSpace.w) / 2.0 + 0.5;
	vec2 refractionTexCoords = vec2(ndc.x, ndc.y);
	vec2 reflectionTexCoords = vec2(ndc.x, -ndc.y);
	
	float depth = texture(depthSampler, refractionTexCoords).r;
	float floorDistance = linearizeDepth(depth);
	depth = gl_FragCoord.z;
	float waterDistance = linearizeDepth(depth);
	float depthBase = (floorDistance - waterDistance);
	float depthBorder = clamp(depthBase * 2, 0.0f, 1.0f);
	float depthBorderNeg = 1 - depthBorder;
	float waterDepth = clamp(depthBase / 64.0f, 0.0f, 1.0f);
	float waterDepthNeg = 1 - waterDepth;
	
	vec2 baseUV = uv * material.tiling;
	vec2 distortedTexCoords = texture(dudvSampler, vec2(baseUV.x + movingCoords, baseUV.y)).rg * 0.35f;
	distortedTexCoords = baseUV + vec2(distortedTexCoords.x, distortedTexCoords.y + movingCoords);
	vec2 totalDistortion = (texture(dudvSampler, distortedTexCoords).rg * 2.0f - 1.0f) * 0.025f;
	
	float blurValue = 0.0020f;
	
	refractionTexCoords += totalDistortion;
	refractionTexCoords = clamp(refractionTexCoords, 0.001f, 0.999f);
	
	vec4 refraction = texture(refractionSampler, vec2(refractionTexCoords.x, refractionTexCoords.y));
	refraction += texture(refractionSampler, vec2(refractionTexCoords.x + blurValue, refractionTexCoords.y));
	refraction += texture(refractionSampler, vec2(refractionTexCoords.x, refractionTexCoords.y + blurValue));
	refraction += texture(refractionSampler, vec2(refractionTexCoords.x + blurValue, refractionTexCoords.y + blurValue));
	refraction += texture(refractionSampler, vec2(refractionTexCoords.x - blurValue, refractionTexCoords.y));
	refraction += texture(refractionSampler, vec2(refractionTexCoords.x, refractionTexCoords.y - blurValue));
	refraction += texture(refractionSampler, vec2(refractionTexCoords.x - blurValue, refractionTexCoords.y - blurValue));
	refraction += texture(refractionSampler, vec2(refractionTexCoords.x + blurValue, refractionTexCoords.y - blurValue));
	refraction += texture(refractionSampler, vec2(refractionTexCoords.x - blurValue, refractionTexCoords.y + blurValue));
	refraction /= 9;
	refraction.rgb *= waterDepthNeg;
	
	reflectionTexCoords += totalDistortion;
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
	
	reflection += texture(reflectionSampler, vec2(reflectionTexCoords.x + blurValue * 2, reflectionTexCoords.y));
	reflection += texture(reflectionSampler, vec2(reflectionTexCoords.x, reflectionTexCoords.y + blurValue * 2));
	reflection += texture(reflectionSampler, vec2(reflectionTexCoords.x + blurValue * 2, reflectionTexCoords.y + blurValue * 2));
	reflection += texture(reflectionSampler, vec2(reflectionTexCoords.x - blurValue * 2, reflectionTexCoords.y));
	reflection += texture(reflectionSampler, vec2(reflectionTexCoords.x, reflectionTexCoords.y - blurValue * 2));
	reflection += texture(reflectionSampler, vec2(reflectionTexCoords.x - blurValue * 2, reflectionTexCoords.y - blurValue * 2));
	reflection += texture(reflectionSampler, vec2(reflectionTexCoords.x + blurValue * 2, reflectionTexCoords.y - blurValue * 2));
	reflection += texture(reflectionSampler, vec2(reflectionTexCoords.x - blurValue * 2, reflectionTexCoords.y + blurValue * 2));
	reflection /= 18;
	
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
	
	vec2 foamUV = uv * (material.tiling * 8);
	vec3 foamColor = texture(foamSampler, vec2(foamUV.x + movingCoords * 2, foamUV.y)).rgb;
	foamColor += texture(foamSampler, vec2(foamUV.x, foamUV.y + movingCoords * 3) / 2).rgb;
	foamColor += texture(foamSampler, vec2(foamUV.x + movingCoords * 4, foamUV.y + movingCoords * 2) * 2).rgb;
	foamColor *= ambientStrength * depthBorderNeg * diffuseMap;

	vec3 waterOutput = clamp(mix(reflection.rgb, refraction.rgb, refractiveValue), 0.0f, 1.0f);
	waterOutput *= diffuseMap;
	waterOutput += specularMap;
	waterOutput += foamColor;

	float fogDistance = length(cameraPosition - worldPosition);
    float fogFactor = 1.0 / exp((fogDistance * fogDensity) * (fogDistance * fogDensity));
    fogFactor = clamp(fogFactor, 0.0, 1.0);
    
    // DEFAULT, DEBUG, WIREFRAME, DIFFUSE, NORMALS, ALBEDO, DEPTH, COLOR
    if (renderMode == 0) {
    	fragColor = vec4(mix(ambientColor * ambientStrength, waterOutput, fogFactor), depthBorder);
    } else if (renderMode == 1) {
    	fragColor = vec4(mix(ambientColor * ambientStrength, waterOutput, fogFactor), depthBorder);
    } else if (renderMode == 2) {
    	fragColor = vec4(0.25, 0.25, 1.0, 1.0);
    } else if (renderMode == 3) {
    	fragColor = vec4(diffuseMap + specularMap, 1);
    } else if (renderMode == 4) {
    	fragColor = vec4(texNormal.rgb, 1);
    } else if (renderMode == 5) {
    	fragColor = vec4(material.color, 1);
    } else if (renderMode == 6) {
        float depth = linearizeDepth(gl_FragCoord.z) / far;
   	 	fragColor = vec4(vec3(depth), 1.0);
    } else if (renderMode == 7) {
    	fragColor = vec4(worldPosition, 1);
    } else if (renderMode == 8) {
    	fragColor = vec4(0.25, 0.25, 1.0, 1.0);
    }
}