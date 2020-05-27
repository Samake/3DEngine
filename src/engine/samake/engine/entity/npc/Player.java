package samake.engine.entity.npc;

import javax.vecmath.Vector2f;

import org.joml.Vector3f;

import com.bulletphysics.collision.dispatch.CollisionWorld.ClosestRayResultCallback;

import samake.engine.core.Engine;
import samake.engine.models.Model;
import samake.engine.physics.bodys.PhysicHullMeshBody;
import samake.engine.resources.ResourceLoader;

public class Player extends NPC {

	public Player() {
		setUpdatedEntity(true);
		setModel(ResourceLoader.load3DModel("player.fbx", false), true);
		setCollissionModel(ResourceLoader.load3DModel("sphereHull.fbx", true));
		getMaterial().setTexture(ResourceLoader.loadTexture("debug\\debug.png", true));
		getMaterial().setNormalMap(ResourceLoader.loadTexture("debug\\debug_n.png", true));
		getMaterial().setSpecularMap(ResourceLoader.loadTexture("debug\\debug_s.png", true));
	}
	
	@Override
	public void update() {
		super.update();
		
		if (isUpdatedEntity() && getPhysicBody() != null) {
			getPhysicBody().update();
			
			getPhysicBody().getRotation().x = getRotation().x;
			getPhysicBody().getRotation().y = getRotation().y;
			getPhysicBody().getRotation().z = getRotation().z;
			
			float heightValue = getPhysicBody().getPosition().y;
			
			ClosestRayResultCallback rayTest = Engine.instance.getPhysics().getCollissionRayResult(new Vector3f(getPosition().x, getPosition().y + 5000, getPosition().z), new Vector3f(getPosition().x, getPosition().y - 5000, getPosition().z));
			
			if (rayTest.hasHit()) {
				if (!rayTest.collisionObject.equals(getPhysicBody().getRigidBody())) {
					if (heightValue < rayTest.hitPointWorld.y) {
						heightValue = rayTest.hitPointWorld.y;
					}
				}
			}
			
			getPosition().y = heightValue;
			
			getPhysicBody().setPosition(new Vector3f(getPosition().x, heightValue, getPosition().z));
		}
	}
	
	public void movePosition(float offsetX, float offsetZ) {
        if (offsetZ != 0) {
        	getPosition().x += (float) Math.sin(Math.toRadians(getRotation().z)) * -1.0f * offsetZ;
        	getPosition().z += (float) Math.cos(Math.toRadians(getRotation().z)) * offsetZ;
        }
        
        if (offsetX != 0) {
        	getPosition().x += (float )Math.sin(Math.toRadians(getRotation().z - 90)) * -1.0f * offsetX;
        	getPosition().z += (float) Math.cos(Math.toRadians(getRotation().z - 90)) * offsetX;
        }
    }
	
	public void moveRotation(float offset) {
        getRotation().z += offset;
    }
	
	@Override
	public void setCollissionModel(Model model) {
		super.setCollissionModel(model);
		
		if (getPhysicBody() == null) {
			setPhysicBody(new PhysicHullMeshBody(model.getMesh(), 15.0f, 0.5f, 0.25f, new Vector2f(0.25f, 0.25f)));
		} else {
			getPhysicBody().destroy();
			setPhysicBody(new PhysicHullMeshBody(model.getMesh(), 15.0f, 0.5f, 0.25f, new Vector2f(0.25f, 0.25f)));
		}
	}
	
	@Override
	public void destroy() {
		super.destroy();
	}
}
