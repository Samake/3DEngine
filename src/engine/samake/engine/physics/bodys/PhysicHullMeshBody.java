package samake.engine.physics.bodys;

import javax.vecmath.Vector2f;

import com.bulletphysics.collision.shapes.ConvexHullShape;

import samake.engine.models.Mesh;

public class PhysicHullMeshBody extends PhysicBody {
	
	public PhysicHullMeshBody(Mesh mesh, float mass, float restitution, float friction, Vector2f damping) {
		setCollissionShape(new ConvexHullShape(mesh.getHullArrayList()));
		setMass(mass);
		setRestitution(mass);
		setFriction(mass);
		setDamping(damping);
		
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
