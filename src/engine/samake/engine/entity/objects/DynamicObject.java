package samake.engine.entity.objects;

import org.joml.Vector3f;

import samake.engine.entity.Entity;
import samake.engine.physics.PhysicBody;

public class DynamicObject extends Entity {

	public DynamicObject() {
		setUpdatedEntity(true);
		setPhysicBody(new PhysicBody());
	}
	
	public void update() {
		super.update();
		
		getPhysicBody().update();
		
		if (isUpdatedEntity()) {
			Vector3f physicPosition = getPhysicBody().getPosition();
			setPosition(physicPosition);
		}
			
	}
	
	public void destroy() {
		super.destroy();
		
		getPhysicBody().destroy();
	}
}
