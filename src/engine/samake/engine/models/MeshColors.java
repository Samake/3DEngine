package samake.engine.models;

import org.joml.Vector3f;

import samake.engine.utils.Utils;

public class MeshColors {

	public static void getSandColors(Vector3f color) {
		color.x = Utils.getRandomValue(65.0f, 75.0f, 100.0f);
		color.y = Utils.getRandomValue(55.0f, 65.0f, 100.0f);
		color.z = Utils.getRandomValue(35.0f, 45.0f, 100.0f);
	}
	
	public static void getGrassColors(Vector3f color) {
		color.x = Utils.getRandomValue(25.0f, 35.0f, 100.0f);
		color.y = Utils.getRandomValue(70.0f, 80.0f, 100.0f);
		color.z = Utils.getRandomValue(25.0f, 35.0f, 100.0f);
	}
	
	public static void getStoneColors(Vector3f color) {
		color.x = Utils.getRandomValue(45.0f, 55.0f, 100.0f);
		color.y = Utils.getRandomValue(45.0f, 55.0f, 100.0f);
		color.z = Utils.getRandomValue(45.0f, 55.0f, 100.0f);
	}
}
