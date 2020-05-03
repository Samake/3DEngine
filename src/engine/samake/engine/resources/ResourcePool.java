package samake.engine.resources;

import java.util.HashMap;
import java.util.Map.Entry;

import samake.engine.material.Texture2D;
import samake.engine.models.Model;


public class ResourcePool {

	private static HashMap<String, Texture2D> textures = new HashMap<String, Texture2D>();
	private static HashMap<String, Model> models = new HashMap<String, Model>();
	
	public static void addTexture(String filePath, Texture2D texture) {
		textures.put(filePath, texture);
	}
	
	public static void addModel(String filePath, Model model) {
		models.put(filePath, model);
	}
	
	public static Texture2D getTexture(String filePath) {
		return textures.get(filePath);
	}
	
	public static Model getModel(String filePath) {
		return models.get(filePath);
	}

	public static void terminate() {
		for (Entry<String, Texture2D> entry : textures.entrySet()) {
			if (entry.getValue() != null) {
				entry.getValue().destroy();
			}
		}
	}
}
