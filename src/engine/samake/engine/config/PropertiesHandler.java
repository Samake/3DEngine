package samake.engine.config;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class PropertiesHandler {
	
	private static Properties properties;
	
	private static String engineVersion;
	private static String gameVersion;
	private static String gameTitle;
	
	private static int windowWidth;
	private static int windowHeight;
	private static boolean vSync;
	private static int fpsLimit;
	private static int fov;
	private static int anisotropicFiltering;
	private static int antialiasing;
	
	private static float bloomLevel;
	private static float saturation;
	private static float contrast;
	private static float brightness;
	
	public static void initialize() throws Exception {
		
		properties = new Properties();
		properties.load(new FileInputStream(new File(System.getProperty("basePath"), "settings/main.properties")));
		
		setEngineVersion(properties.getProperty("ENGINE_VERSION"));
		setGameVersion(properties.getProperty("GAME_VERSION"));
		setGameTitle(properties.getProperty("GAME_TITLE"));
		
		setWindowWidth(Integer.valueOf(properties.getProperty("WINDOW_WIDTH")));
		setWindowHeight(Integer.valueOf(properties.getProperty("WINDOW_HEIGHT")));
		setvSync(Boolean.valueOf(properties.getProperty("VSYNC")));
		setFpsLimit(Integer.valueOf(properties.getProperty("FPS_LIMIT")));
		setFOV(Integer.valueOf(properties.getProperty("FOV")));
		setAnisotropicFiltering(Integer.valueOf(properties.getProperty("ANISOTROPIC_FILTERING")));
		setAntialiasing(Integer.valueOf(properties.getProperty("ANTIALIASING")));
		setBloomLevel(Float.valueOf(properties.getProperty("BLOOMLEVEL")));
		setSaturation(Float.valueOf(properties.getProperty("SATURATION")));
		setContrast(Float.valueOf(properties.getProperty("CONTRAST")));
		setBrightness(Float.valueOf(properties.getProperty("BRIGHTNESS")));
	}

	public static int getWindowWidth() {
		return windowWidth;
	}

	public static void setWindowWidth(int windowWidth) {
		PropertiesHandler.windowWidth = windowWidth;
	}

	public static int getWindowHeight() {
		return windowHeight;
	}

	public static void setWindowHeight(int windowHeight) {
		PropertiesHandler.windowHeight = windowHeight;
	}

	public static boolean isvSync() {
		return vSync;
	}

	public static void setvSync(boolean vSync) {
		PropertiesHandler.vSync = vSync;
	}

	public static int getFpsLimit() {
		return fpsLimit;
	}

	public static void setFpsLimit(int fpsLimit) {
		PropertiesHandler.fpsLimit = fpsLimit;
	}

	public static String getEngineVersion() {
		return engineVersion;
	}

	public static void setEngineVersion(String engineVersion) {
		PropertiesHandler.engineVersion = engineVersion;
	}

	public static String getGameVersion() {
		return gameVersion;
	}

	public static void setGameVersion(String gameVersion) {
		PropertiesHandler.gameVersion = gameVersion;
	}

	public static String getGameTitle() {
		return gameTitle;
	}

	public static void setGameTitle(String gameTitle) {
		PropertiesHandler.gameTitle = gameTitle;
	}

	public static int getFOV() {
		return fov;
	}

	public static void setFOV(int fov) {
		PropertiesHandler.fov = fov;
	}

	public static int getAnisotropicFiltering() {
		return anisotropicFiltering;
	}

	public static void setAnisotropicFiltering(int anisotropicFiltering) {
		PropertiesHandler.anisotropicFiltering = anisotropicFiltering;
	}

	public static int getAntialiasing() {
		return antialiasing;
	}

	public static void setAntialiasing(int antialiasing) {
		PropertiesHandler.antialiasing = antialiasing;
	}

	public static float getBloomLevel() {
		return bloomLevel;
	}

	public static void setBloomLevel(float bloomLevel) {
		PropertiesHandler.bloomLevel = bloomLevel;
	}

	public static float getSaturation() {
		return saturation;
	}

	public static void setSaturation(float saturation) {
		PropertiesHandler.saturation = saturation;
	}

	public static float getContrast() {
		return contrast;
	}

	public static void setContrast(float contrast) {
		PropertiesHandler.contrast = contrast;
	}

	public static float getBrightness() {
		return brightness;
	}

	public static void setBrightness(float brightness) {
		PropertiesHandler.brightness = brightness;
	}
}
