package samake.engine.entity.light;

import org.joml.Vector3f;

import samake.engine.entity.Entity;
import samake.engine.resources.ResourceLoader;
import samake.engine.utils.Utils;

public class Light extends Entity {

	public static enum LIGHTTYPE {NONE, DIRECTIONALLIGHT, POINTLIGHT, SPOTLIGHT}
	
	public LIGHTTYPE type;
	private Vector3f color;
	private Vector3f direction;
    private float intensity;
    private float baseIntensity;
    protected float intensityAmount = 1.0f;
    private float cutOff;
    private Attenuation attenuation;
    private boolean flickering;
    private float flickeringMin;
    private float flickeringMax;
    private boolean pulsing;
    private float pulsingMin;
    private float pulsingMax;
    private float pulsingSpeed;
	private double pulsingBase = 0;
	
	public Light() {
		setModel(ResourceLoader.load3DModel("sphere.fbx", false), false);
		setScale(1.0f);
		setType(LIGHTTYPE.NONE);
		setColor(new Vector3f(1.0f, 1.0f, 1.0f));
		setDirection(new Vector3f());
		setIntensity(1.0f, true);
		setCutOff(0.0f);
		setAttenuation(new Attenuation());
		setFlickering(false);
		setFlickeringMin(0.0f);
		setFlickeringMax(1.0f);
		setPulsing(false);
		setPulsingMin(0.0f);
		setPulsingMax(1.0f);
		setPulsingSpeed(1.0f);
		setUpdatedEntity(true);
	}
	
	@Override
	public void update() {
		super.update();
		
		if (isFlickering()) {
			intensityAmount = Utils.getRandomValue(flickeringMin * 1000.0f, flickeringMax * 1000.0f, 1000.0f);
			setIntensity(baseIntensity * intensityAmount, false);
		} else if (isPulsing()) {
			pulsingBase = (pulsingBase + (pulsingSpeed / 1000))%360;
			intensityAmount = (float) Math.sin(pulsingBase);
			
			if (intensityAmount > pulsingMax) {
				intensityAmount = pulsingMax;
			} else if(intensityAmount < pulsingMin) {
				intensityAmount = pulsingMin;
			}
			
			setIntensity(baseIntensity * intensityAmount, false);
		} else {
			intensityAmount = 1.0f;
		}
		
		getDirection().x = (float) Math.sin(getRotation().y) + (float) Math.cos(getRotation().z);
		getDirection().y = (float) Math.sin(getRotation().y);
		getDirection().z = (float) Math.sin(getRotation().x) + (float) Math.cos(getRotation().z);
	}

	public LIGHTTYPE getType() {
		return type;
	}

	public void setType(LIGHTTYPE type) {
		this.type = type;
	}

	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}
	
	public Vector3f getDirection() {
		return direction;
	}

	public void setDirection(Vector3f direction) {
		this.direction = direction;
	}

	public float getIntensity() {
		return intensity;
	}

	public void setIntensity(float intensity, boolean freeze) {
		this.intensity = intensity;
		
		if (freeze) {
			this.baseIntensity = intensity;
		}
	}

	public float getCutOff() {
		return cutOff;
	}

	public void setCutOff(float cutOff) {
		this.cutOff = cutOff;
	}

	public Attenuation getAttenuation() {
		return attenuation;
	}

	public void setAttenuation(Attenuation attenuation) {
		this.attenuation = attenuation;
	}

	public boolean isFlickering() {
		return flickering;
	}

	public void setFlickering(boolean flickering) {
		this.flickering = flickering;
	}

	public float getFlickeringMin() {
		return flickeringMin;
	}

	public void setFlickeringMin(float flickeringMin) {
		this.flickeringMin = flickeringMin;
	}

	public float getFlickeringMax() {
		return flickeringMax;
	}

	public void setFlickeringMax(float flickeringMax) {
		this.flickeringMax = flickeringMax;
	}

	public boolean isPulsing() {
		return pulsing;
	}

	public void setPulsing(boolean pulsing) {
		this.pulsing = pulsing;
	}

	public float getPulsingMin() {
		return pulsingMin;
	}

	public void setPulsingMin(float pulsingMin) {
		this.pulsingMin = pulsingMin;
	}

	public float getPulsingMax() {
		return pulsingMax;
	}

	public void setPulsingMax(float pulsingMax) {
		this.pulsingMax = pulsingMax;
	}

	public float getPulsingSpeed() {
		return pulsingSpeed;
	}

	public void setPulsingSpeed(float pulsingSpeed) {
		this.pulsingSpeed = pulsingSpeed;
	}

	public float getIntensityAmount() {
		return intensityAmount;
	}
}
