package samake.engine.rendering.renderer;

import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL43;

import samake.engine.camera.Camera;
import samake.engine.camera.Transformation;
import samake.engine.entity.light.Light;
import samake.engine.entity.npc.Player;
import samake.engine.logging.Console;
import samake.engine.logging.Console.LOGTYPE;
import samake.engine.models.Mesh;
import samake.engine.rendering.shader.PlayerShader;
import samake.engine.scene.Scene;

public class PlayerRenderer {
	
	private PlayerShader shader;
	
	public PlayerRenderer() throws Exception {
		shader = new PlayerShader();
		
		Console.print("PlayerRenderer loaded!", LOGTYPE.OUTPUT, true);
	}
	
	public void update() {
		
	}
	
	public void render(Camera camera, Transformation transformation, Scene scene, int renderMode, Vector4f clipPlane) {
		if (!scene.getEntities().isEmpty()) {
			GL43.glEnable(GL43.GL_CLIP_DISTANCE0);
			GL43.glEnable(GL43.GL_DEPTH_TEST);
	    	GL43.glEnable(GL43.GL_BLEND);
	    	GL43.glBlendFunc(GL43.GL_SRC_ALPHA, GL43.GL_ONE_MINUS_SRC_ALPHA);
	    	
	    	shader.bind();
	    	shader.setUniformInteger("textureSampler", 0);
	    	shader.setUniformInteger("normalSampler", 1);
	    	shader.setUniformInteger("specularSampler", 2);
	    	shader.setUniformInteger("renderMode", renderMode);
	    	
	    	Matrix4f projectionMatrix = transformation.getProjectionMatrix();
	    	Matrix4f viewMatrix = transformation.getViewMatrix(camera);
	    	
	    	shader.setUniformMatrix4f("projectionMatrix", projectionMatrix);
	    	shader.setUniformMatrix4f("viewMatrix", viewMatrix);
	    	shader.setUniformVector3f("cameraPosition", camera.getPosition());
	    	shader.setUniformVector3f("ambientColor", scene.getEnvironment().getAmbientColor());
	    	shader.setUniformFloat("ambientStrength", scene.getEnvironment().getAmbientStrength());
	    	shader.setUniformFloat("fogDensity", scene.getEnvironment().getFog().getDensity());
	    	
	    	int lightID = 0;
	    	for (Light light : scene.getLights()) {
	    		shader.setUniformLight("light[" + lightID + "]", light);
	    		lightID++;
	    	}
	    	
	    	if (clipPlane != null) {
				shader.setUniformVector4f("clipPlane", clipPlane);
			}

	    	for (Player player : scene.getPlayers()) {
	    		Matrix4f transformationMatrix = transformation.getWorldMatrix(player.getPosition(), player.getRotation(), player.getScale());
	    		Matrix4f modelViewMatrix = transformation.getModelViewMatrix(player, viewMatrix);
	    		
	    		shader.setUniformMatrix4f("transformationMatrix", transformationMatrix);
	    		shader.setUniformMatrix4f("modelViewMatrix", modelViewMatrix);
	    		
	        	if (player.getModel() != null) {
	    	    	List<Mesh> meshes = player.getModel().getMeshes();
	    	    	
	    	    	for (Mesh mesh : meshes) {
	    	    		shader.setUniformMaterialWorld(mesh.getMaterial());
	    	    		
	    	            GL43.glBindVertexArray(mesh.getVAO());
	    	            
	    	            GL43.glEnableVertexAttribArray(0);
	    	            GL43.glEnableVertexAttribArray(1);
	    	            GL43.glEnableVertexAttribArray(2);
	    	            GL43.glEnableVertexAttribArray(3);
	    	            
	    	            if (mesh.getMaterial() != null) {
		    	            if (mesh.getMaterial().hasTexture() > 0) {
		    	            	GL43.glActiveTexture(GL43.GL_TEXTURE0);
		    	                GL43.glBindTexture(GL43.GL_TEXTURE_2D, mesh.getMaterial().getTexture().getID());
		    	            }
		    	            
		    	            if (mesh.getMaterial().hasNormalMap() > 0) {
		    	            	GL43.glActiveTexture(GL43.GL_TEXTURE1);
		    	                GL43.glBindTexture(GL43.GL_TEXTURE_2D, mesh.getMaterial().getNormalMap().getID());
		    	            }
		    	            
		    	            if (mesh.getMaterial().hasSpecularMap() > 0) {
		    	            	GL43.glActiveTexture(GL43.GL_TEXTURE2);
		    	                GL43.glBindTexture(GL43.GL_TEXTURE_2D, mesh.getMaterial().getSpecularMap().getID());
		    	            }
	    	            }
	    	            
	    	            GL43.glDrawElements(GL43.GL_TRIANGLES, mesh.getNumVertices(), GL43.GL_UNSIGNED_INT, 0);
	    	
	    	            GL43.glDisableVertexAttribArray(0);
	    	            GL43.glDisableVertexAttribArray(1);
	    	            GL43.glDisableVertexAttribArray(2);
	    	            GL43.glDisableVertexAttribArray(3);
	    	            
	    	            GL43.glBindVertexArray(0);
	    	    	}
	        	}
	    	}
	    	
	    	shader.unbind();
	    	
	    	GL43.glDisable(GL43.GL_DEPTH_TEST);
	    	GL43.glDisable(GL43.GL_BLEND);
	    	GL43.glDisable(GL43.GL_CLIP_DISTANCE0);
		}
	}

	public PlayerShader getShader() {
		return shader;
	}

	public void setShader(PlayerShader shader) {
		this.shader = shader;
	}
	
	public void destroy() {
		shader.destroy();
		
		Console.print("PlayerRenderer stopped!", LOGTYPE.OUTPUT, true);
	}
}
