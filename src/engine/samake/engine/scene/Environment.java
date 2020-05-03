package samake.engine.scene;

import java.util.HashMap;
import java.util.Map;

import org.joml.Vector3f;
import org.json.JSONArray;
import org.json.JSONObject;

import samake.engine.config.Configuration;
import samake.engine.logging.Console;
import samake.engine.logging.Console.LOGTYPE;
import samake.engine.resources.ResourceLoader;
import samake.engine.scene.fog.Fog;
import samake.engine.scene.sky.Moon;
import samake.engine.scene.sky.Sky;
import samake.engine.scene.sky.Sun;
import samake.engine.utils.Utils;

public class Environment {
	
	public enum WEATHERTYPE {
		SUNNY, CLOUDY, RAINY, STORMY
	}
	
	private final int DAYDURATION = 1440;
	
	private Sky sky;
	private Fog fog;
	private Vector3f ambientColor;
	private float ambientStrength;
	private JSONObject weatherSettings;
	private float currentProgress;
	private int gameSpeed;
	private float dayProgress;
	private int hour;
	private boolean frozen;
	private WEATHERTYPE weatherType;
	private Map<String, JSONObject[]> weathers = new HashMap<String, JSONObject[]>();
	private float lerpValue = 0.0f;

	public Environment() {
		setSky(new Sky());
		setFog(new Fog());
		setAmbientColor(new Vector3f(0.35f, 0.35f, 0.4f));
		setAmbientStrength(0.5f);
		setGameSpeed(1);
		setDayProgress(0.5f);
		setFrozen(true);
		setWeatherType(WEATHERTYPE.SUNNY);

		loadWeatherSettings();
	}
	
	private void loadWeatherSettings() {
		String weatherConfigPath = new String(System.getProperty("user.dir") + "\\" + Configuration.WEATHER).replace("/", "\\");
		
		try {
			weatherSettings = ResourceLoader.parseJSONFile(weatherConfigPath);
			weathers.put("sunny", new JSONObject[24]);
			//weathers.put("cloudy", new JSONObject[24]);
			//weathers.put("rainy", new JSONObject[24]);
			//weathers.put("stormy", new JSONObject[24]);
		} catch (Exception e) {
			Console.print("Could´nt load weather settings from: " + weatherConfigPath, LOGTYPE.ERROR, true);
		}
		
		JSONObject sunnyWeather = (JSONObject) weatherSettings.get("sunny");
		//JSONObject cloudyWeather = (JSONObject) weatherSettings.get("cloudy");
		//JSONObject rainyWeather = (JSONObject) weatherSettings.get("rainy");
		//JSONObject stormyWeather = (JSONObject) weatherSettings.get("stormy");

		for (int i = 0; i < 24; i++) {
			weathers.get("sunny")[i] = (JSONObject) sunnyWeather.get(String.valueOf(i));
			//weathers.get("cloudy")[i] = (JSONObject) cloudyWeather.get(String.valueOf(i));
			//weathers.get("rainy")[i] = (JSONObject) rainyWeather.get(String.valueOf(i));
			//weathers.get("stormy")[i] = (JSONObject) stormyWeather.get(String.valueOf(i));
		}
	}
	
	public void update() {
		if (!isFrozen()) {
			sky.update();
			currentProgress = (currentProgress + 0.001f * (float)gameSpeed)%DAYDURATION;
		}

		dayProgress = currentProgress / DAYDURATION;
		hour = (int) ((24 / (float) DAYDURATION) * currentProgress);
		lerpValue = ((currentProgress)%60) / 60;

		JSONObject currentWeather = weathers.get(weatherType.name().toLowerCase())[(hour + 1)%24];
		JSONObject nextWeather = weathers.get(weatherType.name().toLowerCase())[(hour + 2)%24];
		
		if (currentWeather != null && nextWeather != null) {
			updateAmbient(currentWeather, nextWeather);
			updateSun(currentWeather, nextWeather);
			updateMoon(currentWeather, nextWeather);
		}
	}
	
	private void updateAmbient(JSONObject currentWeather, JSONObject nextWeather) {
		JSONArray ambientColorCurrent = currentWeather.getJSONArray("ambientColor");
		JSONArray ambientColorNext = nextWeather.getJSONArray("ambientColor");
		
		setAmbientColor(Utils.lerp(
				new Vector3f(ambientColorCurrent.getFloat(0), ambientColorCurrent.getFloat(1), ambientColorCurrent.getFloat(2)), 
				new Vector3f(ambientColorNext.getFloat(0), ambientColorNext.getFloat(1), ambientColorNext.getFloat(2)), 
				lerpValue));
		
		setAmbientStrength(Utils.lerp(
				currentWeather.getFloat("ambientStrength"), 
				nextWeather.getFloat("ambientStrength"), 
				lerpValue));
	}
	
	private void updateSun(JSONObject currentWeather, JSONObject nextWeather) {
		Sun sun = sky.getSun();
		sun.setAngle(360.0f * dayProgress);
		
		JSONArray sunColorCurrent = currentWeather.getJSONArray("sunColor");
		JSONArray sunColorNext = nextWeather.getJSONArray("sunColor");
		
		sun.setColor(Utils.lerp(
				new Vector3f(sunColorCurrent.getFloat(0), sunColorCurrent.getFloat(1), sunColorCurrent.getFloat(2)), 
				new Vector3f(sunColorNext.getFloat(0), sunColorNext.getFloat(1), sunColorNext.getFloat(2)), 
				lerpValue));
		
		sun.setIntensity(Utils.lerp(
				currentWeather.getFloat("sunStrength"), 
				nextWeather.getFloat("sunStrength"), 
				lerpValue), true);
	}
	
	private void updateMoon(JSONObject currentWeather, JSONObject nextWeather) {
		Moon moon = sky.getMoon();
		Sun sun = sky.getSun();
		
		moon.setAngle((sun.getAngle() + 180)%360);
		
		float moonIntensity = 1 - Utils.lerp(currentWeather.getFloat("sunStrength"), nextWeather.getFloat("sunStrength"), lerpValue);
		
		moon.setIntensity(moonIntensity, true);
	}
	
	public Sky getSky() {
		return sky;
	}

	public void setSky(Sky sky) {
		this.sky = sky;
	}
	
	public Fog getFog() {
		return fog;
	}

	public void setFog(Fog fog) {
		this.fog = fog;
	}
	
	public Vector3f getAmbientColor() {
		return ambientColor;
	}

	public void setAmbientColor(Vector3f ambientColor) {
		this.ambientColor = ambientColor;
	}

	public float getAmbientStrength() {
		return ambientStrength;
	}

	public void setAmbientStrength(float ambientStrength) {
		this.ambientStrength = ambientStrength;
	}

	public int getGameSpeed() {
		return gameSpeed;
	}

	public void setGameSpeed(int gameSpeed) {
		this.gameSpeed = gameSpeed;
	}

	public float getDayProgress() {
		return dayProgress;
	}

	public void setDayProgress(float dayProgress) {
		this.dayProgress = dayProgress;
		currentProgress = DAYDURATION * dayProgress;
	}

	public int getHour() {
		return hour;
	}

	public boolean isFrozen() {
		return frozen;
	}

	public void setFrozen(boolean frozen) {
		this.frozen = frozen;
	}

	public WEATHERTYPE getWeatherType() {
		return weatherType;
	}

	public void setWeatherType(WEATHERTYPE weatherType) {
		this.weatherType = weatherType;
	}

	public void destroy() {
		
	}
}
