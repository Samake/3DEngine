package samake.engine.entity.objects;

import samake.engine.entity.Entity;
import samake.engine.physics.bodys.PhysicStaticBody;

public class StaticObject extends Entity {

	public StaticObject() {
		setUpdatedEntity(false);
		setPhysicBody(new PhysicStaticBody());
	}
	
	public void update() {
		super.update();
	}
	
	public void destroy() {
		super.destroy();
		
		getPhysicBody().destroy();
	}
}
