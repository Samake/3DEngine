package samake.engine.camera;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import samake.engine.config.PropertiesHandler;
import samake.engine.entity.Entity;
import samake.engine.logging.Console;
import samake.engine.logging.Console.LOGTYPE;

public class Transformation {
	
	private float fov;
    private float zNear = 0.1f;
    private float zFar = 2000.0f;

    private Matrix4f projectionMatrix;
    private Matrix4f worldMatrix;
    private Matrix4f viewMatrix;
    private Matrix4f modelViewMatrix;
    private Vector3f upVector;
    private Vector3f forwardVector;
    
	public Transformation() {
		setFOV((float) Math.toRadians(PropertiesHandler.getFOV()));
		setProjectionMatrix(new Matrix4f());
		setWorldMatrix(new Matrix4f());
		setViewMatrix(new Matrix4f());
		setModelViewMatrix(new Matrix4f());
		
		upVector = new Vector3f(0, 1, 0);
		forwardVector = new Vector3f(1, 0, 0);
		
		Console.print("Transformation loaded!", LOGTYPE.OUTPUT, true);
	}
	
	public void update() {

    }

	public float getFOV() {
		return fov;
	}

	public void setFOV(float fov) {
		this.fov = fov;
	}

	public Matrix4f getProjectionMatrix() {
        float aspectRatio = (float) PropertiesHandler.getWindowWidth() / (float) PropertiesHandler.getWindowHeight();
        
        projectionMatrix.identity();
        projectionMatrix.perspective(fov, aspectRatio, zNear, zFar);
        
        return projectionMatrix;
    }

	public void setProjectionMatrix(Matrix4f projectionMatrix) {
		this.projectionMatrix = projectionMatrix;
	}

	public Matrix4f getWorldMatrix(Vector3f position, Vector3f rotation, float scale) {
		worldMatrix.identity().translate(position).
			rotateX((float)Math.toRadians(rotation.x)).
			rotateY((float)Math.toRadians(rotation.y)).
			rotateZ((float)Math.toRadians(rotation.z)).
			scale(scale);
        
        return worldMatrix;
    }

	public void setWorldMatrix(Matrix4f worldMatrix) {
		this.worldMatrix = worldMatrix;
	}

	public Matrix4f getViewMatrix(Camera camera) {
	    Vector3f cameraPos = camera.getPosition();
	    Vector3f rotation = camera.getRotation();

	    viewMatrix.identity();
	    viewMatrix.rotate((float)Math.toRadians(rotation.x), forwardVector)
	        .rotate((float)Math.toRadians(rotation.y), upVector);
	    viewMatrix.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
	    
	    return viewMatrix;
	}

	public void setViewMatrix(Matrix4f viewMatrix) {
		this.viewMatrix = viewMatrix;
	}

	public Matrix4f getModelViewMatrix(Entity entity, Matrix4f viewMatrix) {
	    Vector3f rotation = entity.getRotation();
	    
	    modelViewMatrix.identity().translate(entity.getPosition()).
	        rotateX((float)Math.toRadians(-rotation.x)).
	        rotateY((float)Math.toRadians(-rotation.y)).
	        rotateZ((float)Math.toRadians(-rotation.z)).
	        scale(entity.getScale());
	    
	    Matrix4f viewCurr = new Matrix4f(viewMatrix);
	    
	    return viewCurr.mul(modelViewMatrix);
	}

	public void setModelViewMatrix(Matrix4f modelViewMatrix) {
		this.modelViewMatrix = modelViewMatrix;
	}
	
	public void invert() {
		//upVector.y = -upVector.y;
		forwardVector.x = -forwardVector.x;
	}
}
