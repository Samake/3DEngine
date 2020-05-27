package samake.engine.scene.spawn;

import org.joml.Vector3f;

public class SpawnPoint {
	
	private Vector3f position;
	private Vector3f rotation;
	private float radius;
	
	public SpawnPoint() {
		setPosition(new Vector3f(0.0f, 0.0f, 0.0f));
		setRotation(new Vector3f(0.0f, 0.0f, 0.0f));
		setRadius(1.0f);
	}
	
	public void update() {
		
	}
	
	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public void destroy() {
		
	}
}
