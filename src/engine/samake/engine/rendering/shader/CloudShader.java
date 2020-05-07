package samake.engine.rendering.shader;

import samake.engine.logging.Console;
import samake.engine.logging.Console.LOGTYPE;
import samake.engine.rendering.Shader;

public class CloudShader extends Shader {
	
	public CloudShader() throws Exception {
		createVertexShader("cloud");
		createFragmentShader("cloud");
		link();
		
		createUniform("projectionMatrix");
		createUniform("viewMatrix");
		createUniform("transformationMatrix");
		createUniform("clipPlane");
		createUniform("cameraPosition");
		createUniform("windDirection");
		createUniform("animValue");
		
		for (int i = 0; i < 16; i++) {
			createLightUniform("light[" + i + "]");
		}
		
		Console.print("CloudShader loaded!", LOGTYPE.OUTPUT, true);
	}
	
    public void destroy() {
        super.cleanup();
        
        Console.print("CloudShader stopped!", LOGTYPE.OUTPUT, true);
    }
}
