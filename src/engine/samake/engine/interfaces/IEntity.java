package samake.engine.interfaces;

import org.joml.Vector3f;

import samake.engine.material.MaterialWorld;
import samake.engine.models.Model;
import samake.engine.physics.bodys.PhysicBody;

public interface IEntity {
	
	public void update();
	public Model getModel();
	public void setModel(Model model, boolean updatePhysicModel);
	public Model getCollissionModel();
	public void setCollissionModel(Model collissionModel);
	public Vector3f getPosition();
	public void setPosition(Vector3f position);
	public Vector3f getRotation();
	public void setRotation(Vector3f rotation);
	public float getScale();
	public void setScale(float scale);
	public PhysicBody getPhysicBody();
	public void setPhysicBody(PhysicBody physicBody);
	public boolean isUpdatedEntity();
	public void setUpdatedEntity(boolean updatedEntity);
	public Vector3f getUpdatePosition();
	public void setUpdatePosition(Vector3f updatePosition);
	public Vector3f getUpdateRotation();
	public void setUpdateRotation(Vector3f updateRotation);
	public MaterialWorld getMaterial();
	public MaterialWorld getMaterial(int meshSlot);
	public void destroy();
}
