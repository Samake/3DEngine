package samake.engine.rendering.shader;

import samake.engine.logging.Console;
import samake.engine.logging.Console.LOGTYPE;
import samake.engine.rendering.Shader;

public class WaterShader extends Shader {
	
	public WaterShader() throws Exception {
		createVertexShader("water");
		createFragmentShader("water");
		link();
		
		createUniform("projectionMatrix");
		createUniform("viewMatrix");
		createUniform("transformationMatrix");
		createUniform("heightSampler");
		createUniform("dudvSampler");
		createUniform("depthSampler");
		createUniform("normalSampler");
		createUniform("refractionSampler");
		createUniform("reflectionSampler");
		createUniform("foamSampler");
		createUniform("cameraPosition");
		createUniform("animValue");
		createUniform("renderMode");
		createUniform("gameSpeed");
		createUniform("ambientColor");
		createUniform("ambientStrength");
		createUniform("fogDensity");
		
		for (int i = 0; i < 16; i++) {
			createLightUniform("light[" + i + "]");
		}
		
		createUniformMaterialWater();
		
		Console.print("WaterShader loaded!", LOGTYPE.OUTPUT, true);
	}
	
    public void destroy() {
        super.cleanup();
        
        Console.print("WaterShader stopped!", LOGTYPE.OUTPUT, true);
    }
}
