package samake.engine.rendering;

import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL43;

import samake.engine.config.Configuration;
import samake.engine.entity.light.Light;
import samake.engine.entity.light.Light.LIGHTTYPE;
import samake.engine.logging.Console;
import samake.engine.logging.Console.LOGTYPE;
import samake.engine.material.MaterialWater;
import samake.engine.material.MaterialWorld;
import samake.engine.resources.ResourceLoader;
import samake.engine.utils.Utils;

public class Shader {
	
	private int id;
    private int vertexShader;
    private int fragmentShader;
    
    private final Map<String, Integer> uniforms = new HashMap<String, Integer>();

    public Shader() throws Exception {
    	id = GL43.glCreateProgram();
    	
        if (id == 0) {
            Console.print("Could not create Shader!", LOGTYPE.ERROR, true);
        }
    }

    public void createVertexShader(String fileName) throws Exception {
    	vertexShader = createShader(Configuration.SHADER + fileName + ".vs", GL43.GL_VERTEX_SHADER);
    }

    public void createFragmentShader(String fileName) throws Exception {
        fragmentShader = createShader(Configuration.SHADER + fileName + ".fs", GL43.GL_FRAGMENT_SHADER);
    }

    protected int createShader(String filename, int shaderType) throws Exception {
        int shaderId = GL43.glCreateShader(shaderType);
        
        if (shaderId == 0) {
            Console.print("Error creating shader! Type: " + shaderType, LOGTYPE.ERROR, true);
        }

        GL43.glShaderSource(shaderId, ResourceLoader.loadShader(filename));
        GL43.glCompileShader(shaderId);

        if (GL43.glGetShaderi(shaderId, GL43.GL_COMPILE_STATUS) == 0) {
            Console.print("Error compiling shader! Code: " + GL43.glGetShaderInfoLog(shaderId, 1024), LOGTYPE.ERROR, true);
        }

        GL43.glAttachShader(id, shaderId);

        return shaderId;
    }

    public void link() throws Exception {
    	GL43.glLinkProgram(id);
        
        if (GL43.glGetProgrami(id, GL43.GL_LINK_STATUS) == 0) {
            Console.print("Error linking shader! Code: " + GL43.glGetProgramInfoLog(id, 1024), LOGTYPE.ERROR, true);
        }

        if (vertexShader != 0) {
        	GL43.glDetachShader(id, vertexShader);
        }
        
        if (fragmentShader != 0) {
        	GL43.glDetachShader(id, fragmentShader);
        }

        GL43.glValidateProgram(id);
        
        if (GL43.glGetProgrami(id, GL43.GL_VALIDATE_STATUS) == 0) {
        	Console.print("Warning validating shader! Code: " + GL43.glGetProgramInfoLog(id, 1024), LOGTYPE.ERROR, true);
        }
    }
    
    public void createUniform(String uniformName) throws Exception {
        int uniformLocation = GL43.glGetUniformLocation(id, uniformName);
        
        if (uniformLocation < 0) {
            Console.print("Could not find uniform:" + uniformName, LOGTYPE.ERROR, true);
        }
        
        uniforms.put(uniformName, uniformLocation);
    }
    
    public void setUniformMatrix4f(String uniformName, Matrix4f matrix) {
    	GL43.glUniformMatrix4fv(uniforms.get(uniformName), false, Utils.toFloatBuffer(matrix));
    }
    
    public void setUniformInteger(String uniformName, int value) {
    	GL43.glUniform1i(uniforms.get(uniformName), value);
    }
    
    public void setUniformFloat(String uniformName, float value) {
    	GL43.glUniform1f(uniforms.get(uniformName), value);
    }
    
    public void setUniformVector2f(String uniformName, Vector2f value) {
    	GL43.glUniform2f(uniforms.get(uniformName), value.x, value.y);
    }
    
    public void setUniformVector3f(String uniformName, Vector3f value) {
    	GL43.glUniform3f(uniforms.get(uniformName), value.x, value.y, value.z);
    }
    
    public void setUniformVector4f(String uniformName, Vector4f value) {
    	GL43.glUniform4f(uniforms.get(uniformName), value.x, value.y, value.z, value.w);
    }
    
    public void createLightUniform(String uniformName) throws Exception {
    	createUniform(uniformName + ".type");
        createUniform(uniformName + ".color");
        createUniform(uniformName + ".position");
        createUniform(uniformName + ".direction");
        createUniform(uniformName + ".rotation");
        createUniform(uniformName + ".intensity");
        createUniform(uniformName + ".cutOff");
        createUniform(uniformName + ".attenuation.constant");
        createUniform(uniformName + ".attenuation.linear");
        createUniform(uniformName + ".attenuation.exponent");
    }
    
    public void setUniformLight(String uniformName, Light light) {
    	if (light != null) {
        	setUniformInteger(uniformName + ".type", getLightType(light.getType()));
        	setUniformVector3f(uniformName + ".position", light.getPosition());
        	setUniformVector3f(uniformName + ".direction", light.getDirection());
        	setUniformVector3f(uniformName + ".color", light.getColor());
        	setUniformVector3f(uniformName + ".rotation", light.getRotation());
            setUniformFloat(uniformName + ".intensity", light.getIntensity());
            setUniformFloat(uniformName + ".cutOff", light.getCutOff());
            setUniformFloat(uniformName + ".attenuation.constant", light.getAttenuation().getConstant());
            setUniformFloat(uniformName + ".attenuation.linear", light.getAttenuation().getLinear());
            setUniformFloat(uniformName + ".attenuation.exponent", light.getAttenuation().getExponent());
    	}
    }
    
    public void createUniformMaterialWorld() throws Exception {
    	createUniform("material.ambient");
    	createUniform("material.diffuse");
    	createUniform("material.specular");
    	createUniform("material.shininess");
    	createUniform("material.reflectance");
    	createUniform("material.hasTexture");
    	createUniform("material.hasNormal");
    	createUniform("material.hasSpecular");
    	createUniform("material.tiling");
    }
    
    public void createUniformMaterialWater() throws Exception {
    	createUniform("material.color");
    	createUniform("material.shininess");
    	createUniform("material.reflectance");
    	createUniform("material.tiling");
    }
    
    public void setUniformMaterialWorld(MaterialWorld material) {
    	if (material != null) {
        	setUniformVector3f("material.ambient", material.getAmbientColor());
        	setUniformVector3f("material.diffuse", material.getDiffuseColor());
        	setUniformVector3f("material.specular", material.getSpecularColor());
        	setUniformFloat("material.shininess", material.getShininess());
        	setUniformFloat("material.reflectance", material.getReflectance());
        	setUniformInteger("material.hasTexture", material.hasTexture());
        	setUniformInteger("material.hasNormal", material.hasNormalMap());
        	setUniformInteger("material.hasSpecular", material.hasSpecularMap());
        	setUniformInteger("material.tiling", material.getTiling());
    	}
    }
    
    public void setUniformMaterialWater(MaterialWater material) {
    	if (material != null) {
        	setUniformVector3f("material.color", material.getColor());
        	setUniformFloat("material.shininess", material.getShininess());
        	setUniformFloat("material.reflectance", material.getReflectance());
        	setUniformInteger("material.tiling", material.getTiling());
    	}
    }
    
    private int getLightType(LIGHTTYPE type) {
    	switch(type) {
    	case DIRECTIONALLIGHT:
    		return 0;
    	case POINTLIGHT:
    		return 1;
    	case SPOTLIGHT:
    		return 2;
		default:
			return -1;
    	}
    }

    public void bind() {
    	GL43.glUseProgram(id);
    }

    public void unbind() {
    	GL43.glUseProgram(0);
    }

    public Map<String, Integer> getUniforms() {
		return uniforms;
	}

	public void cleanup() {
        unbind();
        
        if (id != 0) {
        	GL43.glDeleteProgram(id);
        }
    }
}
