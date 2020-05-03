package samake.engine.rendering.shader;

import samake.engine.logging.Console;
import samake.engine.logging.Console.LOGTYPE;
import samake.engine.rendering.Shader;

public class TerrainShader extends Shader {
	
	public TerrainShader() throws Exception {
		createVertexShader("terrain");
		createFragmentShader("terrain");
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
		
		Console.print("TerrainShader loaded!", LOGTYPE.OUTPUT, true);
	}
	
    public void destroy() {
        super.cleanup();
        
        Console.print("TerrainShader stopped!", LOGTYPE.OUTPUT, true);
    }
}
