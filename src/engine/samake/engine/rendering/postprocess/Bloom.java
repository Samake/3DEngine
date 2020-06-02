package samake.engine.rendering.postprocess;

import org.lwjgl.opengl.GL43;

import samake.engine.config.PropertiesHandler;
import samake.engine.rendering.buffer.RenderTarget;
import samake.engine.rendering.shader.postprocess.BloomShader;
import samake.engine.scene.Scene;
import samake.engine.scene.environment.water.Water;

public class Bloom {
	
	private RenderTarget renderTarget;
	private BloomShader shader;
	
	public Bloom() throws Exception {
		setRenderTarget(new RenderTarget(PropertiesHandler.getWindowWidth(), PropertiesHandler.getWindowHeight(), 1, RenderTarget.NONE));
		setShader(new BloomShader());
	}
	
	public void update() {
		
	}

	public int render(Scene scene, int mainScene, int brightnessScene) {
		renderTarget.bindBuffer();
		shader.bind();
		shader.setUniformInteger("mainTexture", 0);
    	shader.setUniformInteger("brightTexture", 1);
    	shader.setUniformFloat("bloomLevel", PropertiesHandler.getBloomLevel());
    	shader.setUniformVector3f("cameraPosition", scene.getCamera().getPosition());
    	shader.setUniformFloat("waterHeight", getWaterHeight(scene));
    	
		GL43.glActiveTexture(GL43.GL_TEXTURE0);
		GL43.glBindTexture(GL43.GL_TEXTURE_2D, mainScene);
		
		GL43.glActiveTexture(GL43.GL_TEXTURE1);
		GL43.glBindTexture(GL43.GL_TEXTURE_2D, brightnessScene);
		
		GL43.glClear(GL43.GL_COLOR_BUFFER_BIT);
		GL43.glDrawArrays(GL43.GL_TRIANGLE_STRIP, 0, 4);
			
		shader.unbind();
		renderTarget.unbindBuffer();
		
		return getResult();
	}
	
	private float getWaterHeight(Scene scene) {
		if (!scene.getWaters().isEmpty()) {
    		Water water = scene.getWaters().get(0);
    		
    		if (water != null) {
    			return water.getPosition().y;
    		}
		}
		
		return 0;
	}
	
	public BloomShader getShader() {
		return shader;
	}

	public void setShader(BloomShader shader) {
		this.shader = shader;
	}

	public int getResult() {
		return renderTarget.getMainTexture();
	}

	public RenderTarget getRenderTarget() {
		return renderTarget;
	}

	public void setRenderTarget(RenderTarget renderTarget) {
		this.renderTarget = renderTarget;
	}

	public void destroy() {
		shader.destroy();
		renderTarget.destroy();
	}
}
