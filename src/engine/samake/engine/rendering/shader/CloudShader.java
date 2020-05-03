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
		createUniform("modelViewMatrix");
		createUniform("transformationMatrix");
		createUniform("noiseSampler");
		createUniform("frontSampler");
		createUniform("backSampler");
		createUniform("clipPlane");
		createUniform("cameraPosition");
		createUniform("renderMode");
		createUniform("windDirection");
		createUniform("animValue");
		
		Console.print("CloudShader loaded!", LOGTYPE.OUTPUT, true);
	}
	
    public void destroy() {
        super.cleanup();
        
        Console.print("CloudShader stopped!", LOGTYPE.OUTPUT, true);
    }
}
