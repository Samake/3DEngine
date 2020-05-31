package samake.engine.rendering.renderer;

import java.util.List;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL43;

import samake.engine.camera.Camera;
import samake.engine.camera.Transformation;
import samake.engine.entity.light.Light;
import samake.engine.logging.Console;
import samake.engine.logging.Console.LOGTYPE;
import samake.engine.models.Mesh;
import samake.engine.rendering.shader.ColorShader;
import samake.engine.scene.Scene;

public class ColorRenderer {
	
	private ColorShader shader;
	
	public ColorRenderer() throws Exception {
		shader = new ColorShader();
		
		Console.print("ColorRenderer loaded!", LOGTYPE.OUTPUT, true);
	}
	
	public void update() {
		
	}
	
	public void render(Camera camera, Transformation transformation, Scene renderList) {
		if (!renderList.getLights().isEmpty()) {
			GL43.glEnable(GL43.GL_DEPTH_TEST);
	    	GL43.glEnable(GL43.GL_BLEND);
	    	GL43.glBlendFunc(GL43.GL_SRC_ALPHA, GL43.GL_ONE_MINUS_SRC_ALPHA);
	    	
	    	shader.bind();

	    	Matrix4f projectionMatrix = transformation.getProjectionMatrix();
	    	Matrix4f viewMatrix = transformation.getViewMatrix(camera);
	    	
	    	shader.setUniformMatrix4f("projectionMatrix", projectionMatrix);
	    	shader.setUniformMatrix4f("viewMatrix", viewMatrix);
	    	
	    	for (Light light : renderList.getLights()) {
	    		Matrix4f transformationMatrix = transformation.getWorldMatrix(light.getPosition(), light.getRotation(), light.getScale());
	
	    		shader.setUniformMatrix4f("transformationMatrix", transformationMatrix);
	    		shader.setUniformVector3f("color", light.getColor());
	    		shader.setUniformFloat("amount", light.getIntensityAmount());
	    		
	        	if (light.getModel() != null) {
	    	    	List<Mesh> meshes = light.getModel().getMeshes();
	    	    	
	    	    	for (Mesh mesh : meshes) {
	    	            GL43.glBindVertexArray(mesh.getVAO());
	    	            
	    	            GL43.glEnableVertexAttribArray(0);

	    	            GL43.glDrawElements(GL43.GL_TRIANGLES, mesh.getNumVertices(), GL43.GL_UNSIGNED_INT, 0);
	    	
	    	            GL43.glDisableVertexAttribArray(0);
	    	            
	    	            GL43.glBindVertexArray(0);
	    	    	}
	        	}
	    	}
	    	
	    	shader.unbind();
	    	
	    	GL43.glDisable(GL43.GL_DEPTH_TEST);
	    	GL43.glDisable(GL43.GL_BLEND);
		}
	}

	public ColorShader getShader() {
		return shader;
	}

	public void setShader(ColorShader shader) {
		this.shader = shader;
	}
	
	public void destroy() {
		shader.destroy();
		
		Console.print("ColorRenderer stopped!", LOGTYPE.OUTPUT, true);
	}
}
