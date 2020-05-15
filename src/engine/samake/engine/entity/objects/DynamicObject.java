package samake.engine.entity.objects;

import javax.vecmath.Vector2f;

import org.joml.Vector3f;

import samake.engine.entity.Entity;
import samake.engine.models.Model;
import samake.engine.physics.bodys.PhysicHullMeshBody;

public class DynamicObject extends Entity {

	public DynamicObject() {
		setUpdatedEntity(true);
	}
	
	@Override
	public void setModel(Model model) {
		super.setModel(model);
		
		setPhysicBody(new PhysicHullMeshBody(model.getMesh(), 1.0f, 0.35f, 0.45f, new Vector2f(0.05f, 0.55f)));
	}
	
	@Override
	public void update() {
		super.update();
		
		if (isUpdatedEntity() && getPhysicBody() != null) {
			getPhysicBody().update();
			
			Vector3f physicPosition = getPhysicBody().getPosition();
			Vector3f physicRotation = getPhysicBody().getRotation();
			
			setPosition(physicPosition);
			setRotation(physicRotation);
		}	
	}
	
	@Override
	public void destroy() {
		super.destroy();
		
		if (getPhysicBody() != null) {
			getPhysicBody().destroy();
		}
	}
}
