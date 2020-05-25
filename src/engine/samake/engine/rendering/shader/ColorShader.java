package samake.engine.rendering.shader;

import samake.engine.logging.Console;
import samake.engine.logging.Console.LOGTYPE;
import samake.engine.rendering.Shader;

public class ColorShader extends Shader {
	
	public ColorShader() throws Exception {
		createVertexShader("color");
		createFragmentShader("color");
		link();
		
		createUniform("projectionMatrix");
		createUniform("viewMatrix");
		createUniform("transformationMatrix");
		createUniform("color");
		createUniform("amount");
		
		Console.print("ColorShader loaded!", LOGTYPE.OUTPUT, true);
	}
	
    public void destroy() {
        super.cleanup();
        
        Console.print("ColorShader stopped!", LOGTYPE.OUTPUT, true);
    }
}
