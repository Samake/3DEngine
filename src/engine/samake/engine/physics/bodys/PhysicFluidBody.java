package samake.engine.physics.bodys;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.linearmath.Transform;

public class PhysicFluidBody extends PhysicBody {
	
	public PhysicFluidBody(org.joml.Vector3f size) {
		setCollissionShape(new BoxShape(new Vector3f(size.x, size.y, size.z)));
		setTransform(new Transform());
		setRotationQuaternion(new Quat4f(0.0f, 0.0f, 0.0f, 1.0f));
		setInertia(new Vector3f(0.0f, 0.0f, 0.0f));
		setScale(new Vector3f(1.0f, 1.0f, 1.0f));
		setMass(0.0f);
		setRestitution(0.55f);
		setFriction(0.75f);
		setDamping(new Vector2f(0.95f, 0.25f));

		init();
		
		getRigidBody().applyTorqueImpulse(new Vector3f(32.0f, 128.0f, -32.0f));
	}

	@Override
	public void update() {
		super.update();
	}

	@Override
	public void destroy() {
		super.destroy();
	}
}
