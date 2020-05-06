package samake.engine.rendering.renderer;

import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL43;

import samake.engine.camera.Camera;
import samake.engine.camera.Transformation;
import samake.engine.entity.light.Light;
import samake.engine.logging.Console;
import samake.engine.logging.Console.LOGTYPE;
import samake.engine.models.Mesh;
import samake.engine.rendering.buffer.CloudBuffer;
import samake.engine.rendering.shader.CloudShader;
import samake.engine.scene.Scene;
import samake.engine.scene.sky.Clouds;
import samake.engine.scene.sky.clouds.Cloud;
import samake.engine.scene.sky.clouds.CloudParticle;

public class CloudRenderer {
	
	private CloudShader shader;
	
	public CloudRenderer() throws Exception {
		shader = new CloudShader();
		
		Console.print("CloudRenderer loaded!", LOGTYPE.OUTPUT, true);
	}
	
	public void update() {
		
	}
	
	public void render(Camera camera, Transformation transformation, Scene scene, int renderMode, CloudBuffer cloudBuffer, Vector4f clipPlane) {
		Clouds clouds = scene.getEnvironment().getSky().getClouds();
		
		if (!clouds.getClouds().isEmpty()) {
			GL43.glEnable(GL43.GL_CLIP_DISTANCE0);
			GL43.glEnable(GL43.GL_DEPTH_TEST);
	    	GL43.glEnable(GL43.GL_BLEND);
	    	GL43.glBlendFunc(GL43.GL_SRC_ALPHA, GL43.GL_ONE_MINUS_SRC_ALPHA);
	    	
	    	shader.bind();
	    	shader.setUniformInteger("noiseSampler", 0);
	    	shader.setUniformInteger("frontSampler", 1);
	    	shader.setUniformInteger("backSampler", 2);
	    	shader.setUniformInteger("renderMode", renderMode);
	    	
	    	Matrix4f projectionMatrix = transformation.getProjectionMatrix();
	    	Matrix4f viewMatrix = transformation.getViewMatrix(camera);
	    	
	    	shader.setUniformMatrix4f("projectionMatrix", projectionMatrix);
	    	shader.setUniformMatrix4f("viewMatrix", viewMatrix);
	    	shader.setUniformVector3f("cameraPosition", camera.getPosition());
	    	
	    	shader.setUniformVector2f("windDirection", clouds.getWindDirection());
	    	shader.setUniformFloat("animValue", clouds.getAnimValue());
	    	
	    	int lightID = 0;
	    	for (Light light : scene.getLights()) {
	    		shader.setUniformLight("light[" + lightID + "]", light);
	    		lightID++;
	    	}

	    	for (Cloud cloud : clouds.getClouds()) {
	    		for (CloudParticle particle : cloud.getParticles()) {
	    			Vector3f particlePosition = new Vector3f();
	    			particlePosition.x = cloud.getPosition().x + particle.getOffset().x;
	    			particlePosition.y = cloud.getPosition().y + particle.getOffset().y;
	    			particlePosition.z = cloud.getPosition().z + particle.getOffset().z;
	    			
		    		Matrix4f transformationMatrix = transformation.getWorldMatrix(particlePosition, cloud.getRotation(), cloud.getScale() * particle.getScale());
		    		Matrix4f modelViewMatrix = transformation.getModelViewMatrix(cloud, viewMatrix);
		    		
		    		shader.setUniformMatrix4f("transformationMatrix", transformationMatrix);
		    		shader.setUniformMatrix4f("modelViewMatrix", modelViewMatrix);
		    		
		        	if (clouds.getModel() != null) {
		    	    	List<Mesh> meshes = clouds.getModel().getMeshes();
		    	    	
		    	    	for (Mesh mesh : meshes) {
		    	            GL43.glBindVertexArray(mesh.getVAO());
		    	            
		    	            GL43.glEnableVertexAttribArray(0);
		    	            GL43.glEnableVertexAttribArray(1);
		    	            GL43.glEnableVertexAttribArray(2);
		    	            GL43.glEnableVertexAttribArray(3);
		    	            
		    	            if (clouds.getMaterial() != null) {
			    	            GL43.glActiveTexture(GL43.GL_TEXTURE0);
		    	                GL43.glBindTexture(GL43.GL_TEXTURE_3D, clouds.getMaterial().getNoise().getID());
		    	                
		    	                GL43.glActiveTexture(GL43.GL_TEXTURE1);
		    	                GL43.glBindTexture(GL43.GL_TEXTURE_2D, cloudBuffer.getFrontTexture());
		    	                
		    	                GL43.glActiveTexture(GL43.GL_TEXTURE2);
		    	                GL43.glBindTexture(GL43.GL_TEXTURE_2D, cloudBuffer.getBackTexture());
		    	            }
		    	            
		    	            // TODO: Replace with instanced rendering instead glDrawElements
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
	    	GL43.glDisable(GL43.GL_CLIP_DISTANCE0);
		}
	}

	public CloudShader getShader() {
		return shader;
	}

	public void setShader(CloudShader shader) {
		this.shader = shader;
	}
	
	public void destroy() {
		shader.destroy();
		
		Console.print("CloudRenderer stopped!", LOGTYPE.OUTPUT, true);
	}
}
