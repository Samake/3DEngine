package samake.engine.scene.sky;

import org.joml.Vector2f;
import org.joml.Vector3f;

import samake.engine.entity.Entity;
import samake.engine.resources.ResourceLoader;

public class CloudLayer extends Entity {
	
	private Vector2f windDirection;
	private float density;
	private float animValue;
	private float animCounter = 0;

	public CloudLayer() {
		setPosition(new Vector3f(0.0f, 200.0f, 0.0f));
		setRotation(new Vector3f(270.0f, 0.0f, 0.0f));
		setScale(12.0f);
		setDensity(0.5f);
		setWindDirection(new Vector2f());
		setModel(ResourceLoader.load3DModel("clouds.fbx", false));

		windDirection.x = 0.01f;
		windDirection.y = -0.025f;
	}
	
	public void update() {
		super.update();
		animCounter = (animCounter + 0.01f)%360;
		setAnimValue(animCounter);
	}

	public float getDensity() {
		return density;
	}

	public void setDensity(float density) {
		this.density = density;
	}

	public Vector2f getWindDirection() {
		return windDirection;
	}

	public void setWindDirection(Vector2f windDirection) {
		this.windDirection = windDirection;
	}
	
	public float getAnimValue() {
		return animValue;
	}

	public void setAnimValue(float animValue) {
		this.animValue = animValue;
	}

	public void destroy() {
		super.destroy();
	}
}
