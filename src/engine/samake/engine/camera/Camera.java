package samake.engine.camera;

import org.joml.Vector3f;

import samake.engine.entity.Entity;
import samake.engine.logging.Console;
import samake.engine.logging.Console.LOGTYPE;
import samake.engine.utils.Utils;

public class Camera {

	private Vector3f position;
    private Vector3f rotation;
    private Vector3f lookAt;
    private Entity target;

    public Camera() {
    	setPosition(new Vector3f(0.0f, 0.0f, 0.0f));
    	setRotation(new Vector3f(0.0f, 0.0f, 0.0f));
    	setLookAt(new Vector3f(0.0f, 0.0f, 0.0f));
        
        Console.print("Camera loaded!", LOGTYPE.OUTPUT, true);
    }

    public Camera(Vector3f position, Vector3f lookAt) {
    	setPosition(position);
        setRotation(new Vector3f(0.0f, 0.0f, 0.0f));
        setLookAt(lookAt);
    }
    
	public void update() {
		if (target != null) {
			setLookAt(target.getPosition());
		}
	}

    public Vector3f getPosition() {
        return position;
    }
    
    public void setPosition(Vector3f position) {
    	this.position = position;
    	
    	updateRotation();
    }

    public void setPosition(float x, float y, float z) {
        position.x = x;
        position.y = y;
        position.z = z;
        
        updateRotation();
    }

    public void movePosition(float offsetX, float offsetY, float offsetZ) {
        if (offsetZ != 0) {
            position.x += (float) Math.sin(Math.toRadians(rotation.y)) * -1.0f * offsetZ;
            position.z += (float) Math.cos(Math.toRadians(rotation.y)) * offsetZ;
            position.y += offsetY;
        }
        
        if (offsetX != 0) {
            position.x += (float )Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * offsetX;
            position.z += (float) Math.cos(Math.toRadians(rotation.y - 90)) * offsetX;
            position.y += offsetY;
        }

        position.y += offsetY;
    }
    
    public Vector3f getLookAt() {
		return lookAt;
	}

	public void setLookAt(Vector3f lookAt) {
		this.lookAt = lookAt;
		updateRotation();
	}
	
	public void setLookAt(float x, float y, float z) {
		lookAt.x = x;
		lookAt.y = y;
		lookAt.z = z;
	        
	    updateRotation();
	}
    
    public void updateRotation() {
    	rotation = Utils.getRotation(position, lookAt);
    }

    public Vector3f getRotation() {
        return rotation;
    }
    
    public void setRotation(Vector3f rotation) {
    	this.rotation = rotation;
    }

    public void setRotation(float x, float y, float z) {
        rotation.x = x;
        rotation.y = y;
        rotation.z = z;
    }

	public void moveRotation(float offsetX, float offsetY, float offsetZ) {
        rotation.x += offsetX;
        rotation.y += offsetY;
        rotation.z += offsetZ;
    }

	public Entity getTarget() {
		return target;
	}

	public void setTarget(Entity target) {
		this.target = target;
	}
}
