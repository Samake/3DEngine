#version 330

const int MAX_LIGHTS = 16;

in vec3 color;
in vec3 normal;
in vec2 uv;
in vec3 worldPosition;
in mat4 mViewMatrix;

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
    vec3 diffuse;
    float shininess;
   	float reflectance;
   	int hasTexture;
   	int hasNormal;
   	int hasSpecular;
   	int tiling;
};

uniform sampler2D textureSampler;
uniform sampler2D normalSampler;
uniform sampler2D specularSampler;

uniform Light light[MAX_LIGHTS];
uniform Material material;
uniform vec3 cameraPosition;
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
	
	vec4 albedoMap;
	vec3 normalMap;
	vec3 diffuseMap = vec3(0.0, 0.0, 0.0);
	vec3 specularMap = vec3(0.0, 0.0, 0.0);
	float specValue;
	
	if (material.hasTexture == 1) { albedoMap = texture(textureSampler, uv * material.tiling) * vec4(color * material.diffuse, 1.0); } else { albedoMap = vec4(color * material.diffuse, 1.0);}
	
	if (material.hasNormal == 1) {
		vec3 textureNormal = texture(normalSampler, uv * material.tiling).rgb;
        textureNormal = normalize(textureNormal * 2.0 - 1.0);
        textureNormal = 0.35f * normalize(mViewMatrix * vec4(textureNormal, 0.0)).xyz;
		
		normalMap = normalize(normal + textureNormal);
	} else { 
		normalMap = normalize(normal);
	}
	
	if (material.hasSpecular == 1) { specValue = texture(specularSampler, uv * material.tiling).r; } else { specValue = 1.0;}
	
   	for (int i = 0; i < MAX_LIGHTS; i++) {
   		vec3 lightDirection = light[i].position - worldPosition;
   		
		if (light[i].type == 0) {
			diffuseMap += addDirectionalLight(normalMap, light[i], lightDirection);
			specularMap += calcSpecularMap(light[i], lightDirection, normalMap, specValue, light[i].type);
		}
		
		if (light[i].type == 1) {
			diffuseMap += addPointLight(normalMap, light[i], lightDirection);
			specularMap += calcSpecularMap(light[i], lightDirection, normalMap, specValue, light[i].type);
		}
		
		if (light[i].type == 2) {
			diffuseMap += addSpotLight(normalMap, light[i], lightDirection);
			specularMap += calcSpecularMap(light[i], lightDirection, normalMap, specValue, light[i].type);
		}
	}
	
	float fogDistance = length(cameraPosition - worldPosition);
    float fogFactor = 1.0 / exp((fogDistance * fogDensity) * (fogDistance * fogDensity));
    fogFactor = clamp(fogFactor, 0.0, 1.0);
    
    brightColor = vec4(specularMap, 1);
    
    // DEFAULT, DEBUG, WIREFRAME, DIFFUSE, NORMALS, ALBEDO, DEPTH, COLOR
    if (renderMode == 0) {
    	albedoMap.rgb *= ambientColor * ambientStrength + diffuseMap;
    	outColor = vec4(albedoMap.rgb, 1);
    	outColor.rgb += specularMap;
    	
    	outColor.rgb = mix(ambientColor * ambientStrength, outColor.rgb, fogFactor);
    	
    	//outColor = vec4(specularMap, 1);
    } else if (renderMode == 1) {
    	albedoMap.rgb *= ambientColor * ambientStrength + diffuseMap;
    	outColor = vec4(albedoMap.rgb, 1);
    	outColor.rgb += specularMap;
    	
    	outColor.rgb = mix(ambientColor * ambientStrength, outColor.rgb, fogFactor);
    } else if (renderMode == 2) {
    	outColor = vec4(0.25, 1.0, 0.25, 1.0);
    } else if (renderMode == 3) {
    	outColor = vec4(diffuseMap + specularMap, 1);
    } else if (renderMode == 4) {
    	outColor = vec4(normalMap.rgb, 1);
    } else if (renderMode == 5) {
    	outColor = vec4(albedoMap.rgb, 1);
    } else if (renderMode == 6) {
        float depth = linearizeDepth(gl_FragCoord.z) / far;
   	 	outColor = vec4(vec3(depth), 1.0);
    } else if (renderMode == 7) {
    	outColor = vec4(worldPosition, 1);
    } else if (renderMode == 8) {
    	// TODO COLORS
    	outColor = vec4(0.25, 1.0, 0.25, 1.0);
    }
}