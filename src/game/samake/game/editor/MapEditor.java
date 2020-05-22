package samake.game.editor;

import org.joml.Vector3f;

import samake.engine.entity.light.PointLight;
import samake.engine.logging.Console;
import samake.engine.logging.Console.LOGTYPE;
import samake.engine.perlin.PerlinGenerator;
import samake.engine.resources.ResourceLoader;
import samake.engine.scene.terrain.Terrain;
import samake.engine.scene.water.Water;
import samake.game.debug.Debug;
import samake.game.map.Map;
import samake.game.map.MapData;

public class MapEditor {
	
	private Map map;
	private Debug debug;
	
	public MapEditor() {
		Console.print("LevelEditor started.", LOGTYPE.OUTPUT, true);
		
		setMap(MapHandler.loadMap("testmap"));
		
		if (map != null) {
			MapData mapData = map.getData();
			debug = new Debug(map);
			
			map.getCamera().setPosition(0.0f, 6.0f, 0.0f);
			
			map.getEnvironment().setGameSpeed(mapData.getGameSpeed());
			map.getEnvironment().setDayProgress(mapData.getDayProgess());
			map.getEnvironment().setFrozen(mapData.isTimeFrozen());
			
			map.getEnvironment().getFog().setDensity(mapData.getFogDensity());
			
			if (mapData.isAddEnvironmentLights()) {
				map.addLight(map.getEnvironment().getSky().getSun());
				map.addLight(map.getEnvironment().getSky().getMoon());
			}
			
			if (mapData.isGenerateTerrain()) {
				PerlinGenerator.setTurbolence(mapData.getTerrainTurbulence());
				PerlinGenerator.setGain(mapData.getTerrainGain());
				PerlinGenerator.setLacunarity(mapData.getTerrainLacunarity());
				PerlinGenerator.setOctaves(mapData.getTerrainOctaves());
				PerlinGenerator.setHeightModifier((int) mapData.getTerrainHeight());
				
				Terrain terrain = new Terrain();
				terrain.generateModel(new Vector3f(-mapData.getTerrainSize() / 2, 0.0f, -mapData.getTerrainSize() / 2), mapData.getTerrainSplits(), mapData.getTerrainSize(), true, true);
				terrain.getMaterial().setTexture(ResourceLoader.loadTexture("debug\\debug.png", true));
				terrain.getMaterial().setNormalMap(ResourceLoader.loadTexture("debug\\debug_n.png", true));
				terrain.getMaterial().setSpecularMap(ResourceLoader.loadTexture("debug\\debug_s.png", true));
				terrain.getMaterial().setTiling(mapData.getTerrainTiling());
				
				map.addTerrain(terrain);
			}

			if (mapData.isGenerateWater()) {
				Water water = new Water();
				water.generateModel(new Vector3f(-mapData.getWaterSize() / 2, 0.0f, -mapData.getWaterSize() / 2), mapData.getWaterSplits(), mapData.getWaterSize());
				water.getMaterial().setWaveheight(mapData.getWaveHeight());
				
				map.addWater(water);
			}
		
			PointLight light = new PointLight();
			light.setModel(ResourceLoader.load3DModel("sphere.fbx", false));
			light.setPosition(new Vector3f(48.0f, 95.0f, -136.0f));
			light.setColor(new Vector3f(1.0f, 0.25f, 0.25f));
			light.setIntensity(128.0f, true);
			
			map.addLight(light);
		}
	}
	
	public void update() {
		if (map != null) {
			map.update();
		}
		
		if (debug != null) {
			debug.update();
		}
	}


	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public void destroy() {
		MapHandler.saveMap("testmap", map);
		
		if (map != null) {
			map.destroy();
		}
		
		if (debug != null) {
			debug.destroy();
		}
		
		Console.print("LevelEditor stopped", LOGTYPE.OUTPUT, true);
	}
}
