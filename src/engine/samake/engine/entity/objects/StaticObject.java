package samake.engine.entity.objects;

import org.joml.Vector3f;

import samake.engine.entity.Entity;
import samake.engine.physics.bodys.PhysicStaticBody;

public class StaticObject extends Entity {

	public StaticObject() {
		setUpdatedEntity(false);
		setPhysicBody(new PhysicStaticBody(new Vector3f(1.0f, 1.0f, 1.0f)));
	}
	
	public void update() {
		super.update();
	}
	
	public void destroy() {
		super.destroy();
		
		getPhysicBody().destroy();
	}
}
