package samake.engine.scene.sky.clouds;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;

import samake.engine.entity.Entity;
import samake.engine.utils.Utils;

public class Cloud extends Entity {
	
	private int maxParticles;
	private float randomSpeed;
	private boolean alive;

	private List<CloudParticle> particles = new ArrayList<CloudParticle>();
	
	public Cloud() {
		setAlive(true);
		setMaxParticles(1);
		setScale(1.0f);
		setRotation(new Vector3f(270.0f, 0.0f, 0.0f));
		//setScale(Utils.getRandomValue(100000.0f, 100000.0f, 1000.0f));
		
		int randomValue = (int) Utils.getRandomValue(1, maxParticles, 1);
		randomSpeed = Utils.getRandomValue(250.0f, 2500.0f, 1000.0f);
		
		for (int i = 0; i < randomValue; i++) {
			addParticle();
		}
	}
	
	public void update(Vector2f windDirection) {
		//getPosition().x += windDirection.x * randomSpeed;
		//getPosition().z += windDirection.y * randomSpeed;
	}
	
	private void addParticle() {
		CloudParticle cloud = new CloudParticle(getScale());
		particles.add(cloud);
	}

	public int getMaxParticles() {
		return maxParticles;
	}

	public void setMaxParticles(int maxParticles) {
		this.maxParticles = maxParticles;
	}

	public List<CloudParticle> getParticles() {
		return particles;
	}

	public void setParticles(List<CloudParticle> particles) {
		this.particles = particles;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public void destroy() {
		for (CloudParticle particle : particles) {
			particle.destroy();
		}
		
		particles.clear();
	}
}
