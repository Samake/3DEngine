package samake.engine.entity.objects;

import samake.engine.entity.Entity;

public class StaticObject extends Entity {

	public StaticObject() {
		setUpdatedEntity(false);
	}
	
	public void update() {
		super.update();
	}
	
	public void destroy() {
		super.destroy();
	}
}
