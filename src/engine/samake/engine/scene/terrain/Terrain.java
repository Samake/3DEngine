package samake.engine.scene.terrain;

import org.joml.Vector3f;

import samake.engine.entity.Entity;
import samake.engine.material.MaterialWorld;
import samake.engine.models.Mesh;
import samake.engine.models.MeshBuilder;
import samake.engine.models.Model;
import samake.engine.physics.bodys.PhysicTriangleMeshBody;

public class Terrain extends Entity {

	private MaterialWorld material;
	
	public Terrain() {
		setMaterial(new MaterialWorld());
		setPhysicBody(new PhysicTriangleMeshBody());
	}
	
	public void generateModel(Vector3f position, int rows, float size, boolean useHeightGenerator, boolean colorGenerator) {
		Model model = new Model();
		
		Mesh mesh = MeshBuilder.generatePlane(position.x, position.y, position.z, rows, size, useHeightGenerator, colorGenerator);
		model.addMesh(mesh);
		
		setModel(model);
		
		((PhysicTriangleMeshBody) getPhysicBody()).calculateTriangleMesh(mesh);
	}
	
	@Override
	public void update() {
		
	}

	@Override
	public MaterialWorld getMaterial() {
		return this.material;
	}

	public void setMaterial(MaterialWorld material) {
		this.material = material;
	}

	@Override
	public void destroy() {
		super.destroy();
		
		getPhysicBody().destroy();
	}
}
