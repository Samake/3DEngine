package samake.engine.rendering.postprocess;

import samake.engine.logging.Console;
import samake.engine.logging.Console.LOGTYPE;

public class PostProcess {
	
	public PostProcess() {
		Console.print("PostProcess started!", LOGTYPE.OUTPUT, true);
	}
	
	public void update() {
		
	}
	
	public void render() {
		
	}
	
	public void destroy() {
		
		Console.print("PostProcess stopped!", LOGTYPE.OUTPUT, true);
	}
}
