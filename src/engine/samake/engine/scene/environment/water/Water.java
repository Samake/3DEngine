package samake.engine.scene.environment.water;

import javax.vecmath.Vector2f;

import org.joml.Vector3f;

import samake.engine.entity.Entity;
import samake.engine.material.MaterialWater;
import samake.engine.models.Mesh;
import samake.engine.models.MeshBuilder;
import samake.engine.models.Model;
import samake.engine.physics.bodys.PhysicTriangleMeshBody;

public class Water extends Entity {

	private MaterialWater material;
	
	public Water() {
		setMaterial(new MaterialWater());
		setUpdatedEntity(true);
	}
	
	public void generateModel(Vector3f position, int rows, float size) {
		Model model = new Model();
		
		Mesh mesh = MeshBuilder.generatePlane(position.x, position.y, position.z, rows, size, false, false, true);
		model.addMesh(mesh);
		
		setModel(model);
		
		setPhysicBody(new PhysicTriangleMeshBody(mesh, 0.0f, 0.0f, 0.95f, new Vector2f(0.25f, 0.25f)));
	}
	
	@Override
	public void update() {
		super.update();
		material.update();
		
		if (getPhysicBody() != null) {
			getPhysicBody().update();
		}
	}

	@Override
	public MaterialWater getMaterial() {
		return this.material;
	}

	public void setMaterial(MaterialWater material) {
		this.material = material;
	}
	
	@Override
	public void destroy() {
		super.destroy();
		material.destroy();
		
		if (getPhysicBody() != null) {
			getPhysicBody().destroy();
		}
	}
}
