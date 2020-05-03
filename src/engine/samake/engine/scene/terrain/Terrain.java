package samake.engine.scene.terrain;

import org.joml.Vector3f;

import samake.engine.entity.Entity;
import samake.engine.material.MaterialWorld;
import samake.engine.models.MeshBuilder;
import samake.engine.models.Model;

public class Terrain extends Entity {

	private MaterialWorld material;
	
	public Terrain() {
		setMaterial(new MaterialWorld());
	}
	
	public void generateModel(Vector3f position, int rows, float size, boolean useHeightGenerator, boolean colorGenerator) {
		Model model = new Model();
		model.addMesh(MeshBuilder.generatePlane(position.x, position.y, position.z, rows, size, useHeightGenerator, colorGenerator));
		
		setModel(model);
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
	}
}
