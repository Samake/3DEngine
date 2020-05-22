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
	private float fogDensity;
	
	private boolean generateTerrain;
	private float terrainTurbulence;
	private float terrainGain;
	private float terrainLacunarity;
	private int terrainOctaves;
	private float terrainHeight;
	private int terrainSize;
	private int terrainSplits;
	private int terrainTiling;
	
	private boolean generateWater;
	private int waterSize;
	private int waterSplits;
	private float waveHeight;
	
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

	public float getFogDensity() {
		return fogDensity;
	}

	public void setFogDensity(float fogDensity) {
		this.fogDensity = fogDensity;
	}

	public boolean isGenerateTerrain() {
		return generateTerrain;
	}

	public void setGenerateTerrain(boolean generateTerrain) {
		this.generateTerrain = generateTerrain;
	}

	public float getTerrainTurbulence() {
		return terrainTurbulence;
	}

	public void setTerrainTurbulence(float terrainTurbulence) {
		this.terrainTurbulence = terrainTurbulence;
	}

	public float getTerrainGain() {
		return terrainGain;
	}

	public void setTerrainGain(float terrainGain) {
		this.terrainGain = terrainGain;
	}

	public float getTerrainLacunarity() {
		return terrainLacunarity;
	}

	public void setTerrainLacunarity(float terrainLacunarity) {
		this.terrainLacunarity = terrainLacunarity;
	}

	public int getTerrainOctaves() {
		return terrainOctaves;
	}

	public void setTerrainOctaves(int terrainOctaves) {
		this.terrainOctaves = terrainOctaves;
	}

	public float getTerrainHeight() {
		return terrainHeight;
	}

	public void setTerrainHeight(float terrainHeight) {
		this.terrainHeight = terrainHeight;
	}

	public int getTerrainSize() {
		return terrainSize;
	}

	public void setTerrainSize(int terrainSize) {
		this.terrainSize = terrainSize;
	}

	public int getTerrainSplits() {
		return terrainSplits;
	}

	public void setTerrainSplits(int terrainSplits) {
		this.terrainSplits = terrainSplits;
	}

	public int getTerrainTiling() {
		return terrainTiling;
	}

	public void setTerrainTiling(int terrainTiling) {
		this.terrainTiling = terrainTiling;
	}

	public boolean isGenerateWater() {
		return generateWater;
	}

	public void setGenerateWater(boolean generateWater) {
		this.generateWater = generateWater;
	}

	public int getWaterSize() {
		return waterSize;
	}

	public void setWaterSize(int waterSize) {
		this.waterSize = waterSize;
	}

	public int getWaterSplits() {
		return waterSplits;
	}

	public void setWaterSplits(int waterSplits) {
		this.waterSplits = waterSplits;
	}

	public float getWaveHeight() {
		return waveHeight;
	}

	public void setWaveHeight(float waveHeight) {
		this.waveHeight = waveHeight;
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
		setFogDensity(0.0025f);
		
		setGenerateTerrain(true);
		setTerrainTurbulence(512.0f);
		setTerrainGain(0.5f);
		setTerrainLacunarity(5.5f);
		setTerrainOctaves(4);
		setTerrainHeight(128.0f);
		setTerrainSize(2048);
		setTerrainSplits(256);
		setTerrainTiling(2048);
		
		setGenerateWater(true);
		setWaterSize(2048);
		setWaterSplits(256);
		setWaveHeight(0.5f);
	}
}
