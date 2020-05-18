package samake.engine.rendering.shader.postprocess;

import samake.engine.logging.Console;
import samake.engine.logging.Console.LOGTYPE;
import samake.engine.rendering.Shader;

public class BloomShader extends Shader {
	
	public BloomShader() throws Exception {
		createVertexShader("postprocess//bloom");
		createFragmentShader("postprocess//bloom");
		link();
		
		createUniform("mainTexture");
		createUniform("brightTexture");
		createUniform("bloomLevel");
		
		Console.print("BloomShader bloom!", LOGTYPE.OUTPUT, true);
	}
	
    public void destroy() {
        super.cleanup();
        
        Console.print("BloomShader stopped!", LOGTYPE.OUTPUT, true);
    }
}
