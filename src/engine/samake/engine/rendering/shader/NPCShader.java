package samake.engine.rendering.shader;

import samake.engine.logging.Console;
import samake.engine.logging.Console.LOGTYPE;
import samake.engine.rendering.Shader;

public class NPCShader extends Shader {
	
	public NPCShader() throws Exception {
		createVertexShader("npc");
		createFragmentShader("npc");
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
		
		Console.print("NPCShader loaded!", LOGTYPE.OUTPUT, true);
	}
	
    public void destroy() {
        super.cleanup();
        
        Console.print("NPCShader stopped!", LOGTYPE.OUTPUT, true);
    }
}
