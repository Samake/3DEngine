package samake.engine.scene;

import java.util.ArrayList;
import java.util.List;

import samake.engine.camera.Camera;
import samake.engine.entity.Entity;
import samake.engine.entity.light.Light;
import samake.engine.entity.npc.NPC;
import samake.engine.entity.npc.Player;
import samake.engine.scene.environment.Environment;
import samake.engine.scene.environment.terrain.Terrain;
import samake.engine.scene.environment.water.Water;

public class Scene {

	private Environment environment;
	private Camera camera;
	private List<Terrain> terrains = new ArrayList<Terrain>();
	private List<Entity> entities = new ArrayList<Entity>();
	private List<NPC> npcs = new ArrayList<NPC>();
	private List<Player> players = new ArrayList<Player>();
	private List<Water> waters = new ArrayList<Water>();
	private List<Light> lights = new ArrayList<Light>();
	
	public Scene() {
		setEnvironment(new Environment());
		setCamera(new Camera());
	}
	
	public void update() {
		environment.update();
		
		for (Terrain terrain : terrains) {
			if (terrain.isUpdatedEntity()) {
				terrain.update();
			}
		}
		
		for (Entity entity : entities) {
			if (entity.isUpdatedEntity()) {
				entity.update();
			}
		}
		
		for (NPC npc : npcs) {
			if (npc.isUpdatedEntity()) {
				npc.update();
			}
		}
		
		for (Player player : players) {
			if (player.isUpdatedEntity()) {
				player.update();
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
		
		if (camera != null) {
			camera.update();
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

	public List<NPC> getNPCs() {
		return npcs;
	}

	public void setNPCs(List<NPC> npcs) {
		this.npcs = npcs;
	}
	
	public void addNPC(NPC npc) {
		if (!npcs.contains(npc)) {
			npcs.add(npc);
		}
	}
	
	public void removeNPC(NPC npc) {
		if (npcs.contains(npc)) {
			npcs.remove(npc);
		}
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}
	
	public void addPlayer(Player player) {
		if (!players.contains(player)) {
			players.add(player);
		}
	}
	
	public void removePlayer(Player player) {
		if (players.contains(player)) {
			players.remove(player);
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
		npcs.clear();
		players.clear();
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
		
		for (NPC npc : npcs) {
			npc.destroy();
		}
		
		for (Player player : players) {
			player.destroy();
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
