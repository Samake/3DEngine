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
	public void setModel(Model model, boolean updatePhysicModel) {
		super.setModel(model, updatePhysicModel);
		
		if (getPhysicBody() == null) {
			setPhysicBody(new PhysicHullMeshBody(getCollissionModel().getMesh(), 0.0f, 0.5f, 0.5f, new Vector2f(0.15f, 0.15f)));
		} else {
			getPhysicBody().destroy();
			setPhysicBody(new PhysicHullMeshBody(getCollissionModel().getMesh(), 0.0f, 0.5f, 0.5f, new Vector2f(0.15f, 0.15f)));
		}
	}
	
	@Override
	public void setCollissionModel(Model model) {
		super.setCollissionModel(model);
		
		if (getPhysicBody() == null) {
			setPhysicBody(new PhysicHullMeshBody(model.getMesh(), 0.0f, 0.5f, 0.5f, new Vector2f(0.15f, 0.15f)));
		} else {
			getPhysicBody().destroy();
			setPhysicBody(new PhysicHullMeshBody(model.getMesh(), 0.0f, 0.5f, 0.5f, new Vector2f(0.15f, 0.15f)));
		}
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
