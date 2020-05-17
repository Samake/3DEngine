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
import samake.engine.rendering.buffer.ReflectionBuffer;
import samake.engine.rendering.shader.WaterShader;
import samake.engine.scene.Scene;
import samake.engine.scene.water.Water;

public class WaterRenderer {
	
	private WaterShader shader;
	
	public WaterRenderer() throws Exception {
		shader = new WaterShader();
		
		Console.print("WaterRenderer loaded!", LOGTYPE.OUTPUT, true);
	}
	
	public void update() {
		
	}
	
	public void render(ReflectionBuffer waterFrameBuffer, Camera camera, Transformation transformation, Scene scene, int renderMode) {
		GL43.glEnable(GL43.GL_DEPTH_TEST);
    	GL43.glEnable(GL43.GL_BLEND);
    	GL43.glBlendFunc(GL43.GL_SRC_ALPHA, GL43.GL_ONE_MINUS_SRC_ALPHA);
    	
    	shader.bind();
    	shader.setUniformInteger("heightSampler", 0);
    	shader.setUniformInteger("dudvSampler", 1);
    	shader.setUniformInteger("normalSampler", 2);
    	shader.setUniformInteger("depthSampler", 3);
    	shader.setUniformInteger("refractionSampler", 4);
    	shader.setUniformInteger("reflectionSampler", 5);
    	shader.setUniformInteger("foamSampler", 6);
    	
    	Matrix4f projectionMatrix = transformation.getProjectionMatrix();
    	Matrix4f viewMatrix = transformation.getViewMatrix(camera);
    	
    	shader.setUniformMatrix4f("projectionMatrix", projectionMatrix);
    	shader.setUniformMatrix4f("viewMatrix", viewMatrix);
    	shader.setUniformVector3f("cameraPosition", camera.getPosition());
    	shader.setUniformInteger("renderMode", renderMode);
    	shader.setUniformFloat("gameSpeed", 1.0f);//scene.getEnvironment().getGameSpeed());
    	shader.setUniformVector3f("ambientColor", scene.getEnvironment().getAmbientColor());
    	shader.setUniformFloat("ambientStrength", scene.getEnvironment().getAmbientStrength());
    	shader.setUniformFloat("fogDensity", scene.getEnvironment().getFog().getDensity());
    	
    	int lightID = 0;
    	for (Light light : scene.getLights()) {
    		shader.setUniformLight("light[" + lightID + "]", light);
    		lightID++;
    	}
		
		if (!scene.getWaters().isEmpty()) {
	    	for (Water water : scene.getWaters()) {
	    		Matrix4f transformationMatrix = transformation.getWorldMatrix(water.getPosition(), water.getRotation(), water.getScale());
	
	    		shader.setUniformMatrix4f("transformationMatrix", transformationMatrix);

	    		shader.setUniformMaterialWater(water.getMaterial());
	    		shader.setUniformFloat("animValue", water.getMaterial().getAnimValue());
	    		
	        	if (water.getModel() != null) {
	    	    	List<Mesh> meshes = water.getModel().getMeshes();
	    	    	
	    	    	for (Mesh mesh : meshes) {

	    	            GL43.glBindVertexArray(mesh.getVAO());
	    	            
	    	            GL43.glActiveTexture(GL43.GL_TEXTURE0);
	    	            GL43.glBindTexture(GL43.GL_TEXTURE_2D, water.getMaterial().getHeightTexture().getID());
	    	            
	    	            GL43.glActiveTexture(GL43.GL_TEXTURE1);
	    	            GL43.glBindTexture(GL43.GL_TEXTURE_2D, water.getMaterial().getDUDVTexture().getID());

	    	            GL43.glActiveTexture(GL43.GL_TEXTURE2);
	    	            GL43.glBindTexture(GL43.GL_TEXTURE_2D, water.getMaterial().getNormalTexture().getID());
	    	            
	    	            GL43.glActiveTexture(GL43.GL_TEXTURE3);
	    	            GL43.glBindTexture(GL43.GL_TEXTURE_2D, waterFrameBuffer.getDepthTexture());
	    	            
	    	            GL43.glActiveTexture(GL43.GL_TEXTURE4);
	    	            GL43.glBindTexture(GL43.GL_TEXTURE_2D, waterFrameBuffer.getRefractionTexture());
	    	            
	    	            GL43.glActiveTexture(GL43.GL_TEXTURE5);
	    	            GL43.glBindTexture(GL43.GL_TEXTURE_2D, waterFrameBuffer.getReflectionTexture());
	    	            
	    	            GL43.glActiveTexture(GL43.GL_TEXTURE6);
	    	            GL43.glBindTexture(GL43.GL_TEXTURE_2D, water.getMaterial().getFoamTexture().getID());
	    	            
	    	            GL43.glEnableVertexAttribArray(0);
	    	            GL43.glEnableVertexAttribArray(1);
	    	            GL43.glEnableVertexAttribArray(2);
	    	            GL43.glEnableVertexAttribArray(3);
	    	        
	    	            GL43.glDrawElements(GL43.GL_TRIANGLES, mesh.getNumVertices(), GL43.GL_UNSIGNED_INT, 0);
	    	
	    	            GL43.glDisableVertexAttribArray(0);
	    	            GL43.glDisableVertexAttribArray(1);
	    	            GL43.glDisableVertexAttribArray(2);
	    	            GL43.glDisableVertexAttribArray(3);
	    	            
	    	            GL43.glBindVertexArray(0);
	    	    	}
	        	}
	    	}
		}
		
		shader.unbind();
    	
    	GL43.glDisable(GL43.GL_DEPTH_TEST);
    	GL43.glDisable(GL43.GL_BLEND);
	}

	public WaterShader getShader() {
		return shader;
	}

	public void setShader(WaterShader shader) {
		this.shader = shader;
	}
	
	public void destroy() {
		shader.destroy();
		
		Console.print("WaterRenderer stopped!", LOGTYPE.OUTPUT, true);
	}
}
