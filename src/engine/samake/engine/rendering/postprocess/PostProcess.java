package samake.engine.rendering.postprocess;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL43;

import samake.engine.config.PropertiesHandler;
import samake.engine.logging.Console;
import samake.engine.logging.Console.LOGTYPE;
import samake.engine.models.BasicMesh;
import samake.engine.models.MeshBuilder;
import samake.engine.rendering.shader.postprocess.OutputShader;
import samake.engine.scene.Scene;

public class PostProcess {
	
	private static final float[] POSITIONS = { -1, 1, -1, -1, 1, 1, 1, -1 };	
	private static BasicMesh quad;
	
	private OutputShader shader;
	
	private UnderWater underWater;
	private Bloom bloom;

	public PostProcess() throws Exception {
		Console.print("PostProcess started!", LOGTYPE.OUTPUT, true);
		
		quad = MeshBuilder.loadToVAO(POSITIONS, 2);
		
		setShader(new OutputShader());
		setUnderWater(new UnderWater());
		setBloom(new Bloom());
	}
	
	public void update() {
		underWater.update();
		bloom.update();
	}
	
	public void render(Scene scene, int mainTexture, int brightnessTexture, int depthTexture, int renderMode) {
		int texture = mainTexture;
		
		startFrame();
		
		texture = underWater.render(scene, mainTexture, depthTexture);
		
		if (renderMode == 0) {
			texture = bloom.render(scene, texture, brightnessTexture);
		}
		
		shader.bind();
		shader.setUniformFloat("saturation", PropertiesHandler.getSaturation());
		shader.setUniformFloat("contrast", PropertiesHandler.getContrast());
		shader.setUniformFloat("brightness", PropertiesHandler.getBrightness());
		
		GL43.glActiveTexture(GL43.GL_TEXTURE0);
		GL43.glBindTexture(GL43.GL_TEXTURE_2D, texture);
		
		GL43.glClear(GL43.GL_COLOR_BUFFER_BIT);
		GL43.glDrawArrays(GL43.GL_TRIANGLE_STRIP, 0, 4);
		
		shader.unbind();
		
		endFrame();
	}
	
	private static void startFrame(){
		GL43.glPolygonMode(GL43.GL_FRONT_AND_BACK, GL43.GL_FILL);
		
		GL30.glBindVertexArray(quad.getVAO());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}
	
	private static void endFrame(){
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}

	public OutputShader getShader() {
		return shader;
	}

	public void setShader(OutputShader shader) {
		this.shader = shader;
	}

	public UnderWater getUnderWater() {
		return underWater;
	}

	public void setUnderWater(UnderWater underWater) {
		this.underWater = underWater;
	}

	public Bloom getBloom() {
		return bloom;
	}

	public void setBloom(Bloom bloom) {
		this.bloom = bloom;
	}

	public void destroy() {
		quad.destroy();
		shader.destroy();
		
		Console.print("PostProcess stopped!", LOGTYPE.OUTPUT, true);
	}
}
