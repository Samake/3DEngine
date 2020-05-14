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
import samake.engine.rendering.shader.CloudShader;
import samake.engine.scene.Scene;
import samake.engine.scene.sky.CloudLayer;

public class CloudRenderer {
	
	private CloudShader shader;
	
	public CloudRenderer() throws Exception {
		shader = new CloudShader();
		
		Console.print("CloudRenderer loaded!", LOGTYPE.OUTPUT, true);
	}
	
	public void update() {
		
	}
	
	public void render(Camera camera, Transformation transformation, Scene scene, int renderMode, Vector4f clipPlane) {
		CloudLayer cloudLayer = scene.getEnvironment().getSky().getCloudLayer();
		
		if (cloudLayer != null) {
			GL43.glEnable(GL43.GL_CLIP_DISTANCE0);
			GL43.glEnable(GL43.GL_DEPTH_TEST);
	    	GL43.glEnable(GL43.GL_BLEND);
	    	GL43.glBlendFunc(GL43.GL_SRC_ALPHA, GL43.GL_ONE_MINUS_SRC_ALPHA);
	    	
	    	shader.bind();

	    	Matrix4f projectionMatrix = transformation.getProjectionMatrix();
	    	Matrix4f viewMatrix = transformation.getViewMatrix(camera);
	    	
	    	shader.setUniformMatrix4f("projectionMatrix", projectionMatrix);
	    	shader.setUniformMatrix4f("viewMatrix", viewMatrix);
	    	shader.setUniformVector3f("cameraPosition", camera.getPosition());
	    	
	    	shader.setUniformVector2f("windDirection", cloudLayer.getWindDirection());
	    	shader.setUniformFloat("animValue", cloudLayer.getAnimValue());
	    	
	    	int lightID = 0;
	    	for (Light light : scene.getLights()) {
	    		shader.setUniformLight("light[" + lightID + "]", light);
	    		lightID++;
	    	}

	    	Vector3f position = new Vector3f();
	    	position.x = cloudLayer.getPosition().x + camera.getPosition().x;
	    	position.y = cloudLayer.getPosition().y + camera.getPosition().y;
	    	position.z = cloudLayer.getPosition().z + camera.getPosition().z;
	    	
    		Matrix4f transformationMatrix = transformation.getWorldMatrix(position, cloudLayer.getRotation(), cloudLayer.getScale());
    		
    		shader.setUniformMatrix4f("transformationMatrix", transformationMatrix);
    		
        	if (cloudLayer.getModel() != null) {
    	    	List<Mesh> meshes = cloudLayer.getModel().getMeshes();
    	    	
    	    	for (Mesh mesh : meshes) {
    	            GL43.glBindVertexArray(mesh.getVAO());
    	            
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
