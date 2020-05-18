package samake.engine.physics.bodys;

import javax.vecmath.Vector2f;

import com.bulletphysics.collision.shapes.BvhTriangleMeshShape;

import samake.engine.models.Mesh;

public class PhysicTriangleMeshBody extends PhysicBody {
	
	public PhysicTriangleMeshBody(Mesh mesh, float mass, float restitution, float friction, Vector2f damping) {
		if (mesh.getCollissionMesh() != null) {
			setCollissionShape(new BvhTriangleMeshShape(mesh.getCollissionMesh(), true, true));
			setMass(mass);
			setRestitution(mass);
			setFriction(mass);
			setDamping(damping);
			
			init();
		}
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
