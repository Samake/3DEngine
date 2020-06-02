package samake.engine.rendering.shader.postprocess;

import samake.engine.logging.Console;
import samake.engine.logging.Console.LOGTYPE;
import samake.engine.rendering.Shader;

public class UnderWaterShader extends Shader {
	
	public UnderWaterShader() throws Exception {
		createVertexShader("postprocess//underwater");
		createFragmentShader("postprocess//underwater");
		link();
		
		createUniform("mainTexture");
		createUniform("depthTexture");
		createUniform("cameraPosition");
		createUniform("waterHeight");
		createUniform("animValue");

		Console.print("UnderWaterShader started!", LOGTYPE.OUTPUT, true);
	}
	
    public void destroy() {
        super.cleanup();
        
        Console.print("UnderWaterShader stopped!", LOGTYPE.OUTPUT, true);
    }
}
