package samake.game.map;

import org.json.JSONArray;
import org.json.JSONObject;

import samake.engine.config.Configuration;
import samake.engine.logging.Console;
import samake.engine.logging.Console.LOGTYPE;
import samake.engine.resources.ResourceLoader;

public class MapHandler {
	
	public static Map loadMap(String mapName) {
			String mapPath = new String(System.getProperty("user.dir") + "\\" + Configuration.MAPS + mapName + ".dat").replace("/", "\\");
			
			Map map = new Map();
			
			try {
				JSONObject mapRoot = ResourceLoader.parseJSONFile(mapPath);
				map.getData().setMapName(mapRoot.getString("mapname").toUpperCase());
				map.getData().setDescription(mapRoot.getString("description"));
				map.getData().setVersion(mapRoot.getString("version"));
				map.getData().setAuthor(mapRoot.getString("author"));
				
				JSONObject mapSettings = mapRoot.getJSONObject("settings");
				map.getData().setGameSpeed(mapSettings.getInt("gamespeed"));
				map.getData().setDayProgess(mapSettings.getFloat("dayprogress"));
				map.getData().setFreezeTime(mapSettings.getBoolean("freezetime"));
	
				JSONObject mapEnvironment = mapRoot.getJSONObject("environment");
				map.getData().setAddEnvironmentLights(mapEnvironment.getBoolean("addenvironmentlights"));
				map.getData().setFogDensity(mapEnvironment.getFloat("fogdensity"));
				
				JSONObject mapTerrain = mapRoot.getJSONObject("terrain");
				map.getData().setGenerateTerrain(mapTerrain.getBoolean("generateterrain"));
				map.getData().setTerrainTurbulence(mapTerrain.getFloat("terrainturbulence"));
				map.getData().setTerrainGain(mapTerrain.getFloat("terraingain"));
				map.getData().setTerrainLacunarity(mapTerrain.getFloat("terrainlacunarity"));
				map.getData().setTerrainOctaves(mapTerrain.getInt("terrainoctaves"));
				map.getData().setTerrainHeight(mapTerrain.getFloat("terrainheight"));
				map.getData().setTerrainSize(mapTerrain.getInt("terrainsize"));
				map.getData().setTerrainSplits(mapTerrain.getInt("terrainsplits"));
				map.getData().setTerrainTiling(mapTerrain.getInt("terraintiling"));
				
				JSONObject mapWater = mapRoot.getJSONObject("water");
				map.getData().setGenerateWater(mapWater.getBoolean("generatewater"));
				map.getData().setWaterSize(mapWater.getInt("watersize"));
				map.getData().setWaterSplits(mapWater.getInt("watersplits"));
				map.getData().setWaveHeight(mapWater.getFloat("waveheight"));
				
				JSONArray mapEntities = mapRoot.getJSONArray("entities");
				map.getData().setEntities(mapEntities);
			} catch (Exception ex) {
				Console.print("Loading map " + mapName + " failed! -> " + ex.getMessage(), LOGTYPE.ERROR, false);
			}

			Console.print(		map.getData().getMapName() 
								+ " (" + map.getData().getDescription() + ")" 
								+ " v" + map.getData().getVersion()
								+ " by " + map.getData().getAuthor()
								+ " was loaded!"
								
			, LOGTYPE.OUTPUT, true);
			
			return map;
	}
	
	public static void saveMap(String mapName, Map map) {
		//for debug only;
		mapName = mapName + "2";
		
		String mapPath = new String(System.getProperty("user.dir") + "\\" + Configuration.MAPS + mapName + ".dat").replace("/", "\\");
		
		MapData mapData = map.getData();
		
		JSONObject mapRoot = new JSONObject();
		mapRoot.put("mapname", mapData.getMapName());
		mapRoot.put("description", mapData.getDescription());
		mapRoot.put("version", mapData.getVersion());
		mapRoot.put("author", mapData.getAuthor());
		
		JSONObject mapSettings = new JSONObject();
		mapSettings.put("gamespeed", mapData.getGameSpeed());
		mapSettings.put("dayprogress", mapData.getDayProgess());
		mapSettings.put("freezetime", mapData.isTimeFrozen());
		
		mapRoot.put("settings", mapSettings);
		
		JSONObject mapEnvironment = new JSONObject();
		mapEnvironment.put("addenvironmentlights", mapData.isAddEnvironmentLights());
		mapEnvironment.put("fogdensity", mapData.getFogDensity());

		mapRoot.put("environment", mapEnvironment);
		
		JSONObject mapTerrain = new JSONObject();
		mapTerrain.put("generateterrain", mapData.isGenerateTerrain());
		mapTerrain.put("terrainturbulence", mapData.getTerrainTurbulence());
		mapTerrain.put("terraingain", mapData.getTerrainGain());
		mapTerrain.put("terrainlacunarity", mapData.getTerrainLacunarity());
		mapTerrain.put("terrainoctaves", mapData.getTerrainOctaves());
		mapTerrain.put("terrainheight", mapData.getTerrainHeight());
		mapTerrain.put("terrainsize", mapData.getTerrainSize());
		mapTerrain.put("terrainsplits", mapData.getTerrainSplits());
		mapTerrain.put("terraintiling", mapData.getTerrainTiling());

		mapRoot.put("terrain", mapTerrain);
		
		JSONObject mapWater = new JSONObject();
		mapWater.put("generatewater", mapData.isGenerateWater());
		mapWater.put("watersize", mapData.getWaterSize());
		mapWater.put("watersplits", mapData.getWaterSplits());
		mapWater.put("waveheight", mapData.getWaveHeight());

		mapRoot.put("water", mapWater);
		
		JSONArray mapEntities = new JSONArray();
		
		for (int i = 0; i < mapEntities.length(); i++) {
			//JSONObject entity = mapEntities.getJSONObject(i);
		}

		mapRoot.put("entities", mapEntities);
		
		ResourceLoader.saveJSONFile(mapPath, mapRoot);
	}
}