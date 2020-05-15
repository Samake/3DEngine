package samake.engine.entity.objects;

import javax.vecmath.Vector2f;

import samake.engine.entity.Entity;
import samake.engine.models.Model;
import samake.engine.physics.bodys.PhysicHullMeshBody;

public class StaticObject extends Entity {

	public StaticObject() {
		setUpdatedEntity(false);
	}
	
	@Override
	public void setModel(Model model) {
		super.setModel(model);
		
		setPhysicBody(new PhysicHullMeshBody(model.getMesh(), 0.0f, 0.55f, 0.55f, new Vector2f(0.45f, 0.45f)));
	}
	
	@Override
	public void update() {
		super.update();
	}
	
	@Override
	public void destroy() {
		super.destroy();
		
		if (getPhysicBody() != null) {
			getPhysicBody().destroy();
		}
	}
}
