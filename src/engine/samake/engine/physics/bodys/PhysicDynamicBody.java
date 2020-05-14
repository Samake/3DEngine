package samake.engine.physics.bodys;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.linearmath.Transform;

public class PhysicDynamicBody extends PhysicBody {
	
	public PhysicDynamicBody() {
		setCollissionShape(new SphereShape(1.0f));
		setTransform(new Transform());
		setRotationQuaternion(new Quat4f(0.0f, 0.0f, 0.0f, 1.0f));
		setInertia(new Vector3f(0.0f, 0.0f, 0.0f));
		setScale(new Vector3f(1.0f, 1.0f, 1.0f));
		setMass(5.0f);
		setRestitution(0.15f);
		setFriction(0.55f);
		setDamping(new Vector2f(0.05f, 0.45f));

		init();
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
