package samake.game.map;

public class MapData {
	
	private String mapName;
	private String description;
	private String version;
	private String author;
	
	private int gameSpeed;
	private float dayProgess;
	private boolean freezeTime;
	
	private boolean addEnvironmentLights;
	
	private boolean generateTerrain;
	
	private boolean generateWater;
	
	public MapData() {
		setDefaults();
	}
	
	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getGameSpeed() {
		return gameSpeed;
	}

	public void setGameSpeed(int gameSpeed) {
		this.gameSpeed = gameSpeed;
	}

	public float getDayProgess() {
		return dayProgess;
	}

	public void setDayProgess(float dayProgess) {
		this.dayProgess = dayProgess;
	}
	
	public boolean isTimeFrozen() {
		return freezeTime;
	}

	public void setFreezeTime(boolean freezeTime) {
		this.freezeTime = freezeTime;
	}
	
	public boolean isAddEnvironmentLights() {
		return addEnvironmentLights;
	}

	public void setAddEnvironmentLights(boolean addEnvironmentLights) {
		this.addEnvironmentLights = addEnvironmentLights;
	}

	public boolean isGenerateTerrain() {
		return generateTerrain;
	}

	public void setGenerateTerrain(boolean generateTerrain) {
		this.generateTerrain = generateTerrain;
	}

	public boolean isGenerateWater() {
		return generateWater;
	}

	public void setGenerateWater(boolean generateWater) {
		this.generateWater = generateWater;
	}

	public void setDefaults() {
		setMapName("EMPTY");
		setDescription("Empty map!");
		setVersion("1.0");
		setAuthor("Editor");
		
		setGameSpeed(1);
		setDayProgess(0.5f);
		setFreezeTime(false);
		
		setAddEnvironmentLights(true);
		
		setGenerateTerrain(true);
		
		setGenerateWater(true);
	}
}
