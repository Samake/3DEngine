package samake.engine.rendering.shader;

import samake.engine.logging.Console;
import samake.engine.logging.Console.LOGTYPE;
import samake.engine.rendering.Shader;

public class SkyShader extends Shader {
	
	public SkyShader() throws Exception {
		createVertexShader("sky");
		createFragmentShader("sky");
		link();

		createUniform("projectionMatrix");
		createUniform("viewMatrix");
		createUniform("transformationMatrix");
		createUniform("skyTexture");
		createUniform("clipPlane");
		createUniform("cameraPosition");
		createUniform("sunPosition");
		createUniform("sunColor");
		createUniform("sunIntensity");
		createUniform("ambientColor");
		createUniform("ambientStrength");
	
		Console.print("SkyShader loaded!", LOGTYPE.OUTPUT, true);
	}
	
    public void destroy() {
        super.cleanup();
        
        Console.print("SkyShader stopped!", LOGTYPE.OUTPUT, true);
    }
}
