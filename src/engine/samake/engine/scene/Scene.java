package samake.engine.scene;

import java.util.ArrayList;
import java.util.List;

import samake.engine.camera.Camera;
import samake.engine.entity.Entity;
import samake.engine.entity.light.Light;
import samake.engine.scene.terrain.Terrain;
import samake.engine.scene.water.Water;

public class Scene {

	private Environment environment;
	private Camera camera;
	private List<Terrain> terrains = new ArrayList<Terrain>();
	private List<Entity> entities = new ArrayList<Entity>();
	private List<Water> waters = new ArrayList<Water>();
	private List<Light> lights = new ArrayList<Light>();
	
	public Scene() {
		setEnvironment(new Environment());
		setCamera(new Camera());
	}
	
	public void update() {
		environment.update();
		
		for (Entity entity : entities) {
			if (entity.isUpdatedEntity()) {
				entity.update();
			}
		}
		
		for (Water water : waters) {
			if (water.isUpdatedEntity()) {
				water.update();
			}
		}
		
		for (Light light : lights) {
			if (light.isUpdatedEntity()) {
				light.update();
			}
		}
	}

	public Environment getEnvironment() {
		return environment;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}
	
	public List<Terrain> getTerrains() {
		return terrains;
	}

	public void setTerrains(List<Terrain> terrains) {
		this.terrains = terrains;
	}
	
	public void addTerrain(Terrain terrain) {
		if (!terrains.contains(terrain)) {
			terrains.add(terrain);
		}
	}
	
	public void removeTerrain(Terrain terrain) {
		if (terrains.contains(terrain)) {
			terrains.remove(terrain);
		}
	}

	public List<Entity> getEntities() {
		return entities;
	}

	public void setEntities(List<Entity> entities) {
		this.entities = entities;
	}
	
	public void addEntity(Entity entity) {
		if (!entities.contains(entity)) {
			entities.add(entity);
		}
	}
	
	public void removeEntity(Entity entity) {
		if (entities.contains(entity)) {
			entities.remove(entity);
		}
	}

	public List<Water> getWaters() {
		return waters;
	}

	public void setWaters(List<Water> waters) {
		this.waters = waters;
	}
	
	public void addWater(Water water) {
		if (!waters.contains(water)) {
			waters.add(water);
		}
	}
	
	public void removeWater(Water water) {
		if (waters.contains(water)) {
			waters.remove(water);
		}
	}

	public List<Light> getLights() {
		return lights;
	}

	public void setLights(List<Light> lights) {
		this.lights = lights;
	}
	
	public void addLight(Light light) {
		if (!lights.contains(light)) {
			lights.add(light);
		}
	}
	
	public void removeLight(Light light) {
		if (lights.contains(light)) {
			lights.remove(light);
		}
	}
	
	public void clear() {
		entities.clear();
		lights.clear();
		waters.clear();
		terrains.clear();
	}

	public void destroy() {	
		for (Terrain terrain : terrains) {
			terrain.destroy();
		}
		
		for (Entity entity : entities) {
			entity.destroy();
		}
		
		for (Water water : waters) {
			water.destroy();
		}
		
		for (Light light : lights) {
			light.destroy();
		}
		
		clear();
	}
}
