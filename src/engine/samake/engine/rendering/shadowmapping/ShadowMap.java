package samake.engine.rendering.shadowmapping;

import samake.engine.logging.Console;
import samake.engine.logging.Console.LOGTYPE;
import samake.engine.material.Texture2D;

public class ShadowMap {
	
	private final static int SHADOWMAPSIZE = 1024;
	
	private Texture2D shadowTexture;
	
	public ShadowMap() {
		setShadowTexture(new Texture2D());
		
		Console.print("ShadowMap started!", LOGTYPE.OUTPUT, true);
	}
	
	public void update() {
		
	}
	
	public void render() {
		
	}
	
	public Texture2D getShadowTexture() {
		return shadowTexture;
	}

	public void setShadowTexture(Texture2D shadowTexture) {
		this.shadowTexture = shadowTexture;
	}

	public void destroy() {
		
		shadowTexture.destroy();
		
		Console.print("ShadowMap stopped!", LOGTYPE.OUTPUT, true);
	}
}
