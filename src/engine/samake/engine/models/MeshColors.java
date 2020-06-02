package samake.engine.models;

import org.joml.Vector3f;

import samake.engine.utils.Utils;

public class MeshColors {

	public static void getSandColors(Vector3f color) {
		color.x = Utils.getRandomValue(75.0f, 70.0f, 100.0f);
		color.y = Utils.getRandomValue(65.0f, 60.0f, 100.0f);
		color.z = Utils.getRandomValue(65.0f, 50.0f, 100.0f);
	}
	
	public static void getGrassColors(Vector3f color) {
		color.x = Utils.getRandomValue(5.0f, 10.0f, 100.0f);
		color.y = Utils.getRandomValue(40.0f, 50.0f, 100.0f);
		color.z = Utils.getRandomValue(5.0f, 10.0f, 100.0f);
	}
	
	public static void getStoneColors(Vector3f color) {
		float random = Utils.getRandomValue(25.0f, 45.0f, 100.0f);
		color.x = random;
		color.y = random;
		color.z = random;
	}
}
