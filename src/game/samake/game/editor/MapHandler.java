package samake.game.editor;

import org.json.JSONObject;

import samake.engine.config.Configuration;
import samake.engine.logging.Console;
import samake.engine.logging.Console.LOGTYPE;
import samake.engine.resources.ResourceLoader;
import samake.game.map.Map;
import samake.game.map.MapData;

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
				
				JSONObject mapTerrain = mapRoot.getJSONObject("terrain");
				map.getData().setGenerateTerrain(mapTerrain.getBoolean("generateterrain"));
				
				JSONObject mapWater = mapRoot.getJSONObject("water");
				map.getData().setGenerateWater(mapWater.getBoolean("generatewater"));
				
				JSONObject mapEntities = mapRoot.getJSONObject("entities");
				
			} catch (Exception ex) {
				Console.print(ex.toString(), LOGTYPE.ERROR, false);
			}

			Console.print(		map.getData().getMapName() 
								+ " (" + map.getData().getDescription() + ")" 
								+ " v" + map.getData().getVersion()
								+ " by " + map.getData().getAuthor()
								+ " was parsed!"
								
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
		
		mapRoot.put("environment", mapEnvironment);
		
		JSONObject mapTerrain = new JSONObject();
		mapTerrain.put("generateterrain", mapData.isGenerateTerrain());
		
		mapRoot.put("terrain", mapTerrain);
		
		JSONObject mapWater = new JSONObject();
		mapWater.put("generatewater", mapData.isGenerateWater());
		
		mapRoot.put("water", mapWater);
		
		JSONObject mapEntities = new JSONObject();

		mapRoot.put("entities", mapEntities);
		
		ResourceLoader.saveJSONFile(mapPath, mapRoot);
	}
}