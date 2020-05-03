package samake.engine.scene.sky.clouds;

import org.joml.Vector3f;

import samake.engine.utils.Utils;

public class CloudParticle {

	private float scale;
	private Vector3f offset = new Vector3f();
	
	public CloudParticle(float cloudScale) {
		setScale(Utils.getRandomValue(1000.0f, 6000.0f, 1000.0f));
		
		offset.x = Utils.getRandomValue(-3000.0f, 3000.0f, 1000.0f) * scale * cloudScale;
		offset.y = Utils.getRandomValue(-500.0f, 500.0f, 1000.0f) * scale * cloudScale;
		offset.z = Utils.getRandomValue(-3000.0f, 3000.0f, 1000.0f) * scale * cloudScale;
	}
	
	public void update() {
		
	}
	
	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public Vector3f getOffset() {
		return offset;
	}

	public void setOffset(Vector3f offset) {
		this.offset = offset;
	}

	public void destroy() {

	}
}
