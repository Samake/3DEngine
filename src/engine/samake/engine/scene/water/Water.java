package samake.engine.scene.water;

import org.joml.Vector3f;

import samake.engine.entity.Entity;
import samake.engine.material.MaterialWater;
import samake.engine.models.MeshBuilder;
import samake.engine.models.Model;

public class Water extends Entity {

	private MaterialWater material;
	
	public Water() {
		setMaterial(new MaterialWater());
		setUpdatedEntity(true);
	}
	
	public void generateModel(Vector3f position, int rows, float size) {
		Model model = new Model();
		model.addMesh(MeshBuilder.generatePlane(position.x, position.y, position.z, rows, size, false, false));
		
		setModel(model);
	}
	
	@Override
	public void update() {
		material.update();
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
	}
}