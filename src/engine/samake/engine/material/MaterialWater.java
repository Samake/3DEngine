package samake.engine.material;

import org.joml.Vector3f;

import samake.engine.resources.ResourceLoader;

public class MaterialWater extends MaterialWorld {
	
	private Texture2D heightTexture;
	private Texture2D dudvTexture;
	private Texture2D normalTexture;
	private Texture2D foamTexture;
	private Vector3f color;
	private float animValue;
	private float animCounter = 0;
	private float waveheight = 0;
	
	public MaterialWater() {
		setHeightTexture(ResourceLoader.loadTexture("water\\height.png", true));
		setDUDVTexture(ResourceLoader.loadTexture("water\\dudv.png", true));
		setNormalTexture(ResourceLoader.loadTexture("water\\normal.png", true));
		setFoamTexture(ResourceLoader.loadTexture("water\\foam.png", true));
		setColor(new Vector3f(0.45f, 0.55f, 0.62f));
    	setShininess(256.0f);
    	setReflectance(1.2f);
    	setTiling(64);
	}
	
	public void update() {
		animCounter = (animCounter + 0.002f)%360;
		setAnimValue(animCounter);
	}

	public Texture2D getHeightTexture() {
		return heightTexture;
	}

	public void setHeightTexture(Texture2D heightTexture) {
		this.heightTexture = heightTexture;
	}

	public Texture2D getDUDVTexture() {
		return dudvTexture;
	}

	public void setDUDVTexture(Texture2D dudvTexture) {
		this.dudvTexture = dudvTexture;
	}

	public Texture2D getNormalTexture() {
		return normalTexture;
	}

	public void setNormalTexture(Texture2D normalTexture) {
		this.normalTexture = normalTexture;
	}

	public Texture2D getFoamTexture() {
		return foamTexture;
	}

	public void setFoamTexture(Texture2D foamTexture) {
		this.foamTexture = foamTexture;
	}

	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}
	
	public float getAnimValue() {
		return animValue;
	}

	public void setAnimValue(float animValue) {
		this.animValue = animValue;
	}

	public float getWaveheight() {
		return waveheight;
	}

	public void setWaveheight(float waveheight) {
		this.waveheight = waveheight;
	}

	public void destroy() {
		
	}
}
