package samake.engine.scene.environment.sky;

import org.joml.Vector3f;

import samake.engine.entity.light.DirectionalLight;

public class Moon extends DirectionalLight {

	private float angle;
	private float distance;
	
	public Moon() {
		setIntensity(3.0f, true);
		setColor(new Vector3f(0.025f, 0.025f, 0.04f));
		setAngle(225.0f);
		setDistance(1000.0f);
	}
	
	@Override
	public void update() {

	}
	
	private void updatePosition() {
		getPosition().x = distance * (float) Math.sin(Math.toRadians(angle * 0.5));
		getPosition().y = distance * (float) Math.cos(Math.toRadians((angle * 0.5) + 270));
		getPosition().z = distance * (float) Math.cos(Math.toRadians(angle * 0.5));
	}
	
	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
		updatePosition();
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
		updatePosition();
	}

	@Override
	public void destroy() {
		
	}
}
