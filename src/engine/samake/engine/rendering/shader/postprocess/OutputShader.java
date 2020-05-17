package samake.engine.rendering.shader.postprocess;

import samake.engine.logging.Console;
import samake.engine.logging.Console.LOGTYPE;
import samake.engine.rendering.Shader;

public class OutputShader extends Shader {
	
	public OutputShader() throws Exception {
		createVertexShader("postprocess//output");
		createFragmentShader("postprocess//output");
		link();
		
		Console.print("OutputShader loaded!", LOGTYPE.OUTPUT, true);
	}
	
    public void destroy() {
        super.cleanup();
        
        Console.print("OutputShader stopped!", LOGTYPE.OUTPUT, true);
    }
}
