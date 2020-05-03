package samake.engine.models;

import java.util.ArrayList;
import java.util.List;

public class Model {
	
	private List<Mesh> meshes = new ArrayList<Mesh>();
	
	public Model() {

	}
	
	public void addMesh(Mesh mesh) {
		meshes.add(mesh);
	}
	
	public void removeMesh(Mesh mesh) {
		meshes.remove(mesh);
	}
	
	public List<Mesh> getMeshes() {
		return meshes;
	}

	public void setMeshes(List<Mesh> meshes) {
		this.meshes = meshes;
	}

	public void destroy() {
		for (Mesh mesh : meshes) {
			mesh.destroy();
		}
    }
}
