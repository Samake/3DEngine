package samake.engine.physics.bodys;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.linearmath.Transform;

public class PhysicStaticBody extends PhysicBody {
	
	public PhysicStaticBody(org.joml.Vector3f size) {
		setCollissionShape(new BoxShape(new Vector3f(size.x, size.y, size.z)));
		setTransform(new Transform());
		setRotationQuaternion(new Quat4f(0.0f, 0.0f, 0.0f, 1.0f));
		setInertia(new Vector3f(0.0f, 0.0f, 0.0f));
		setScale(new Vector3f(1.0f, 1.0f, 1.0f));
		setMass(0.0f);
		setRestitution(0.5f);
		setFriction(0.5f);
		setDamping(new Vector2f(0.25f, 0.25f));

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
