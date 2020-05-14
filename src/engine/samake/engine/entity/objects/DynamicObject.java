package samake.engine.entity.objects;

import org.joml.Vector3f;

import samake.engine.entity.Entity;
import samake.engine.physics.bodys.PhysicDynamicBody;

public class DynamicObject extends Entity {

	public DynamicObject() {
		setUpdatedEntity(true);
		setPhysicBody(new PhysicDynamicBody());
	}
	
	@Override
	public void update() {
		super.update();
		
		if (isUpdatedEntity()) {
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
		
		getPhysicBody().destroy();
	}
}
