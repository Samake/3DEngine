package samake.engine.rendering.shader;

import samake.engine.logging.Console;
import samake.engine.logging.Console.LOGTYPE;
import samake.engine.rendering.Shader;

public class EntityShader extends Shader {
	
	public EntityShader() throws Exception {
		createVertexShader("entity");
		createFragmentShader("entity");
		link();
		
		createUniform("projectionMatrix");
		createUniform("viewMatrix");
		createUniform("modelViewMatrix");
		createUniform("transformationMatrix");
		createUniform("textureSampler");
		createUniform("normalSampler");
		createUniform("specularSampler");
		createUniform("clipPlane");
		createUniform("cameraPosition");
		createUniform("renderMode");
		createUniform("ambientColor");
		createUniform("ambientStrength");
		createUniform("fogDensity");
		
		for (int i = 0; i < 16; i++) {
			createLightUniform("light[" + i + "]");
		}
		
		createUniformMaterialWorld();
		
		Console.print("EntityShader loaded!", LOGTYPE.OUTPUT, true);
	}
	
    public void destroy() {
        super.cleanup();
        
        Console.print("EntityShader stopped!", LOGTYPE.OUTPUT, true);
    }
}
