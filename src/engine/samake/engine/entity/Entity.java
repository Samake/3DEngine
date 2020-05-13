package samake.engine.entity;

import org.joml.Vector3f;

import samake.engine.logging.Console;
import samake.engine.logging.Console.LOGTYPE;
import samake.engine.material.MaterialWorld;
import samake.engine.models.Model;
import samake.engine.physics.bodys.PhysicBody;

public class Entity {

	private Model model;
	private Vector3f position;
	private Vector3f rotation;
	private float scale;
	private PhysicBody physicBody;
	
	private boolean updatedEntity;
	private Vector3f updatePosition;
	private Vector3f updateRotation;
	
	public Entity() {
		setUpdatedEntity(false);
		setPosition(new Vector3f());
		setRotation(new Vector3f());
		setUpdatePosition(new Vector3f());
		setUpdateRotation(new Vector3f());
		setScale(1.0f);
	}
	
	public void update() {
		if (updatedEntity) {
			position.x = position.x + updatePosition.x;
			position.y = position.y + updatePosition.y;
			position.z = position.z + updatePosition.z;
			
			rotation.x = (rotation.x + updateRotation.x)%360;
			rotation.y = (rotation.y + updateRotation.y)%360;
			rotation.z = (rotation.z + updateRotation.z)%360;
		}
    }

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
		
		if (physicBody != null) {
			physicBody.setPosition(position);
		}
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}
	
	public PhysicBody getPhysicBody() {
		return physicBody;
	}

	public void setPhysicBody(PhysicBody physicBody) {
		this.physicBody = physicBody;
	}

	public boolean isUpdatedEntity() {
		return updatedEntity;
	}

	public void setUpdatedEntity(boolean updatedEntity) {
		this.updatedEntity = updatedEntity;
	}
	
	public Vector3f getUpdatePosition() {
		return updatePosition;
	}

	public void setUpdatePosition(Vector3f updatePosition) {
		this.updatePosition = updatePosition;
	}

	public Vector3f getUpdateRotation() {
		return updateRotation;
	}

	public void setUpdateRotation(Vector3f updateRotation) {
		this.updateRotation = updateRotation;
	}

	public MaterialWorld getMaterial() {
		return getMaterial(0);
	}
	
	public MaterialWorld getMaterial(int meshSlot) {
		if (model.getMeshes().size() > meshSlot) {
			return model.getMeshes().get(meshSlot).getMaterial();
		} else {
			Console.print("Calling getMaterial() for slot " + meshSlot + " failed! Entity: " + this.toString(), LOGTYPE.ERROR, true);
			return model.getMeshes().get(0).getMaterial();
		}
	}

	public void destroy() {
		if (model != null) {
			model.destroy();
		}
    }
}
