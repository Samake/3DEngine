package samake.engine.perlin;

import org.lwjgl.stb.STBPerlin;

public class PerlinGenerator {

    private static float turbolence = 100.0f;
    private static float gain = 0.5f;
    private static float lacunarity = 2.0f;
    private static int octaves = 4;
    private static int heightModifier = 32;

    public static float getTurbulenceNoise(float x, float y, float z) {
		return STBPerlin.stb_perlin_turbulence_noise3(x / turbolence, y / turbolence, z / turbolence, lacunarity, gain, octaves);
	}
    
	public static float getTurbulenceNoise(float x, float y, float z, float turbolence) {
		return STBPerlin.stb_perlin_turbulence_noise3(x / turbolence, y / turbolence, z / turbolence, lacunarity, gain, octaves);
	}
	
	public static float getTurbulenceNoise(float x, float y, float z, float turbolence, float lacunarity) {
		return STBPerlin.stb_perlin_turbulence_noise3(x / turbolence, y / turbolence, z / turbolence, lacunarity, gain, octaves);
	}
	
	public static float getTurbulenceNoise(float x, float y, float z, float turbolence, float lacunarity, float gain) {
		return STBPerlin.stb_perlin_turbulence_noise3(x / turbolence, y / turbolence, z / turbolence, lacunarity, gain, octaves);
	}
	
	public static float getTurbulenceNoise(float x, float y, float z, float turbolence, float lacunarity, float gain, int octaves) {
		return STBPerlin.stb_perlin_turbulence_noise3(x / turbolence, y / turbolence, z / turbolence, lacunarity, gain, octaves);
	}

	public static float getTurbolence() {
		return turbolence;
	}

	public static void setTurbolence(float turbolence) {
		PerlinGenerator.turbolence = turbolence;
	}

	public static float getGain() {
		return gain;
	}

	public static void setGain(float gain) {
		PerlinGenerator.gain = gain;
	}

	public static float getLacunarity() {
		return lacunarity;
	}

	public static void setLacunarity(float lacunarity) {
		PerlinGenerator.lacunarity = lacunarity;
	}

	public static int getOctaves() {
		return octaves;
	}

	public static void setOctaves(int octaves) {
		PerlinGenerator.octaves = octaves;
	}

	public static int getHeightModifier() {
		return heightModifier;
	}

	public static void setHeightModifier(int heightModifier) {
		PerlinGenerator.heightModifier = heightModifier;
	}
}
