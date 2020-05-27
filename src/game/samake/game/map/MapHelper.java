package samake.game.map;

import org.joml.Vector3f;
import org.json.JSONObject;

import samake.engine.entity.light.PointLight;
import samake.engine.resources.ResourceLoader;

public class MapHelper {

	public static void spawnPointLight(JSONObject entity, Map map) {
		JSONObject position = (JSONObject) entity.get("position");
		JSONObject color = (JSONObject) entity.get("color");
				
		PointLight light = new PointLight();
		light.setModel(ResourceLoader.load3DModel(entity.getString("model"), false), false);
		light.setPosition(new Vector3f(position.getFloat("x"), position.getFloat("y"), position.getFloat("z")));
		light.setColor(new Vector3f(color.getFloat("r"), color.getFloat("g"), color.getFloat("b")));
		light.setIntensity(entity.getFloat("intensity"), true);
		
		map.addLight(light);
	}
}
