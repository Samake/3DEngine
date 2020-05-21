package samake.engine.entity.objects;

import javax.vecmath.Vector2f;

import org.joml.Vector3f;

import com.bulletphysics.collision.dispatch.CollisionWorld.ClosestRayResultCallback;

import samake.engine.core.Engine;
import samake.engine.entity.Entity;
import samake.engine.models.Model;
import samake.engine.physics.bodys.PhysicHullMeshBody;

public class DynamicObject extends Entity {

	public DynamicObject() {
		setUpdatedEntity(true);
	}
	
	@Override
	public void setCollissionModel(Model model) {
		super.setCollissionModel(model);
		
		if (getPhysicBody() == null) {
			setPhysicBody(new PhysicHullMeshBody(model.getMesh(), 1.5f, 0.1f, 0.85f, new Vector2f(0.15f, 0.55f)));
		} else {
			getPhysicBody().destroy();
			setPhysicBody(new PhysicHullMeshBody(model.getMesh(), 1.5f, 0.1f, 0.85f, new Vector2f(0.15f, 0.55f)));
		}
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
			
			ClosestRayResultCallback rayTest = Engine.instance.getPhysics().getCollissionRayResult(physicPosition, new Vector3f(physicPosition.x, physicPosition.y - 5000, physicPosition.z));
		
			if (rayTest.hasHit()) {
				//System.err.println(rayTest.hitPointWorld);
			}
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
