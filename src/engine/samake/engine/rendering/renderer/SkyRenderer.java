package samake.engine.rendering.renderer;

import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL43;

import samake.engine.camera.Camera;
import samake.engine.camera.Transformation;
import samake.engine.logging.Console;
import samake.engine.logging.Console.LOGTYPE;
import samake.engine.models.Mesh;
import samake.engine.rendering.shader.SkyShader;
import samake.engine.scene.Scene;
import samake.engine.scene.sky.Sky;

public class SkyRenderer {
	
	private SkyShader shader;
	
	public SkyRenderer() throws Exception {
		shader = new SkyShader();
		
		Console.print("SkyRenderer loaded!", LOGTYPE.OUTPUT, true);
	}
	
	public void update() {
		
	}
	
	public void render(Camera camera, Transformation transformation, Scene scene, Vector4f clipPlane) {
		Sky sky = scene.getEnvironment().getSky();
		
		if (sky != null) {
			GL43.glEnable(GL43.GL_CLIP_DISTANCE0);
			GL43.glEnable(GL43.GL_DEPTH_TEST);
	    	GL43.glEnable(GL43.GL_BLEND);
	    	GL43.glBlendFunc(GL43.GL_SRC_ALPHA, GL43.GL_ONE_MINUS_SRC_ALPHA);
	    	
	    	shader.bind();
	    	shader.setUniformInteger("skyTexture", 0);
	    	
	    	GL43.glActiveTexture(GL43.GL_TEXTURE0);
            GL43.glBindTexture(GL43.GL_TEXTURE_CUBE_MAP, sky.getNightSky().getID());
	    	
	    	Matrix4f projectionMatrix = transformation.getProjectionMatrix();
	    	Matrix4f viewMatrix = transformation.getViewMatrix(camera);
	    	
	    	shader.setUniformMatrix4f("projectionMatrix", projectionMatrix);
	    	shader.setUniformMatrix4f("viewMatrix", viewMatrix);
	    	shader.setUniformVector3f("cameraPosition", camera.getPosition());
	    	shader.setUniformVector3f("sunPosition", sky.getSun().getPosition());
	    	shader.setUniformVector3f("sunColor", sky.getSun().getColor());
	    	shader.setUniformFloat("sunIntensity", sky.getSun().getIntensity());
	    	shader.setUniformVector3f("ambientColor", scene.getEnvironment().getAmbientColor());
	    	shader.setUniformFloat("ambientStrength", scene.getEnvironment().getAmbientStrength());
	    	
	    	if (clipPlane != null) {
				shader.setUniformVector4f("clipPlane", clipPlane);
			}

    		Matrix4f transformationMatrix = transformation.getWorldMatrix(camera.getPosition(), new Vector3f(0.0f, 0.0f, 0.0f), sky.getScale());

    		shader.setUniformMatrix4f("transformationMatrix", transformationMatrix);

        	if (sky.getModel() != null) {
    	    	List<Mesh> meshes = sky.getModel().getMeshes();
    	    	
    	    	for (Mesh mesh : meshes) {

    	            GL43.glBindVertexArray(mesh.getVAO());
    	            
    	            GL43.glEnableVertexAttribArray(0);

    	            GL43.glDrawElements(GL43.GL_TRIANGLES, mesh.getNumVertices(), GL43.GL_UNSIGNED_INT, 0);
    	
    	            GL43.glDisableVertexAttribArray(0);
    	            
    	            GL43.glBindVertexArray(0);
    	    	}
        	}
	    	
	    	shader.unbind();
	    	
	    	GL43.glDisable(GL43.GL_DEPTH_TEST);
	    	GL43.glDisable(GL43.GL_BLEND);
	    	GL43.glDisable(GL43.GL_CLIP_DISTANCE0);
		}
	}

	public SkyShader getShader() {
		return shader;
	}

	public void setShader(SkyShader shader) {
		this.shader = shader;
	}
	
	public void destroy() {
		shader.destroy();
		
		Console.print("SkyRenderer stopped!", LOGTYPE.OUTPUT, true);
	}
}
