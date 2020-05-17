package samake.engine.rendering.postprocess;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import samake.engine.logging.Console;
import samake.engine.logging.Console.LOGTYPE;
import samake.engine.models.BasicMesh;
import samake.engine.models.MeshBuilder;
import samake.engine.rendering.shader.postprocess.OutputShader;

public class PostProcess {
	
	private static final float[] POSITIONS = { -1, 1, -1, -1, 1, 1, 1, -1 };	
	private static BasicMesh quad;
	
	private OutputShader shader;

	public PostProcess() throws Exception {
		Console.print("PostProcess started!", LOGTYPE.OUTPUT, true);
		
		quad = MeshBuilder.loadToVAO(POSITIONS, 2);
		setShader(new OutputShader());
	}
	
	public void update() {
		
	}
	
	public void render(int mainScene, int brightnessScene) {
		startFrame();
		
		shader.bind();
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, mainScene);
		
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		
		shader.unbind();
		
		endFrame();
	}
	
	private static void startFrame(){
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

	public void destroy() {
		quad.destroy();
		shader.destroy();
		
		Console.print("PostProcess stopped!", LOGTYPE.OUTPUT, true);
	}
}
