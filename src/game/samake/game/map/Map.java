package samake.game.map;

import org.joml.Vector3f;
import org.json.JSONObject;

import samake.engine.core.Engine;
import samake.engine.entity.npc.Player;
import samake.engine.perlin.PerlinGenerator;
import samake.engine.resources.ResourceLoader;
import samake.engine.scene.Scene;
import samake.engine.scene.environment.terrain.Terrain;
import samake.engine.scene.environment.water.Water;
import samake.game.controls.Controls;

public class Map extends Scene {
	
	private MapData data;
	private Controls controls;
	private Player player;
	
	public Map() {
		setControls(new Controls());
		setData(new MapData());
		
		Engine.instance.getRenderer().setScene(this);
	}
	
	public void init() {
		getEnvironment().setGameSpeed(data.getGameSpeed());
		getEnvironment().setDayProgress(data.getDayProgess());
		getEnvironment().setFrozen(data.isTimeFrozen());
		
		getEnvironment().getFog().setDensity(data.getFogDensity());
		
		if (data.isAddEnvironmentLights()) {
			addLight(getEnvironment().getSky().getSun());
			addLight(getEnvironment().getSky().getMoon());
		}
		
		if (data.isGenerateTerrain()) {
			PerlinGenerator.setTurbolence(data.getTerrainTurbulence());
			PerlinGenerator.setGain(data.getTerrainGain());
			PerlinGenerator.setLacunarity(data.getTerrainLacunarity());
			PerlinGenerator.setOctaves(data.getTerrainOctaves());
			PerlinGenerator.setHeightModifier((int) data.getTerrainHeight());
			
			Terrain terrain = new Terrain();
			terrain.generateModel(new Vector3f(-data.getTerrainSize() / 2, 0.0f, -data.getTerrainSize() / 2), data.getTerrainSplits(), data.getTerrainSize(), true, true);
			terrain.getMaterial().setTexture(ResourceLoader.loadTexture("debug\\debug.png", true));
			terrain.getMaterial().setNormalMap(ResourceLoader.loadTexture("debug\\debug_n.png", true));
			terrain.getMaterial().setSpecularMap(ResourceLoader.loadTexture("debug\\debug_s.png", true));
			terrain.getMaterial().setTiling(data.getTerrainTiling());
			
			addTerrain(terrain);
		}

		if (data.isGenerateWater()) {
			Water water = new Water();
			water.generateModel(new Vector3f(-data.getWaterSize() / 2, 0.0f, -data.getWaterSize() / 2), data.getWaterSplits(), data.getWaterSize());
			water.getMaterial().setWaveheight(data.getWaveHeight());
			
			addWater(water);
		}
		
		for (int i = 0; i < data.getEntities().length(); i++) {
			JSONObject entity = data.getEntities().getJSONObject(i);
			
			if (entity != null) {
				String type = entity.getString("type");
				createEntitiy(type, entity);
			}
		}
	}
	
	private void createEntitiy(String type, JSONObject entity) {
		switch (type) {
		case "PointLight":
            System.err.println(entity);
            MapHelper.spawnPointLight(entity, this);
            break;
        default:
            break;
		}
	}
	
	@Override
	public void update() {
		super.update();
		controls.update(this, player);
	}
	
	@Override
	public void addPlayer(Player player) {
		super.addPlayer(player);
		
		if (getPlayer() == null) {
			setPlayer(player);
		}
	}

	public Controls getControls() {
		return controls;
	}

	public void setControls(Controls controls) {
		this.controls = controls;
	}

	public MapData getData() {
		return data;
	}

	public void setData(MapData data) {
		this.data = data;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	@Override
	public void destroy() {
		clear();
		super.destroy();
		getEnvironment().destroy();
		Engine.instance.getRenderer().setScene(null);
	}
}
