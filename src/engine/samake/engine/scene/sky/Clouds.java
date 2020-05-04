package samake.engine.scene.sky;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import samake.engine.material.MaterialClouds;
import samake.engine.models.Model;
import samake.engine.resources.ResourceLoader;
import samake.engine.scene.sky.clouds.Cloud;
import samake.engine.utils.Utils;

public class Clouds {
	
	private Model model;
	private MaterialClouds material;
	private Vector2f windDirection;
	private int maxClouds;
	private float density;
	private float animValue;
	private float animCounter = 0;
	
	private List<Cloud> clouds = new ArrayList<Cloud>();
	
	public Clouds() {
		setMaxClouds(1);
		setDensity(0.5f);
		setWindDirection(new Vector2f());
		setModel(ResourceLoader.load3DModel("clouds.fbx"));
		setMaterial(new MaterialClouds());
		
		getMaterial().setNoise(ResourceLoader.loadTexture3D("clouds\\cloud.png"));

		windDirection.x = 0.01f;
		windDirection.y = -0.025f;
		
		
	}
	
	public void update() {
		for (Cloud cloud : clouds) {
			cloud.update(windDirection);
			if (!cloud.isAlive()) {
				removeCloud(cloud);
			}
		}
		
		for (int i = 0; i < maxClouds; i++) {
			if (i < clouds.size()) {
				if (clouds.get(i) == null) {
					addCloud();
				}
			} else {
				addCloud();
			}
		}
		
		animCounter = (animCounter + 0.01f)%360;
		setAnimValue(animCounter);
	}
	
	private void addCloud() {
		Cloud cloud = new Cloud();
		//cloud.getPosition().x = Utils.getRandomValue(-200.0f, 200.0f, 1.0f);
		cloud.getPosition().y = Utils.getRandomValue(1000.0f, 1000.0f, 1.0f);
		//cloud.getPosition().z = Utils.getRandomValue(-200.0f, 200.0f, 1.0f);
		
		clouds.add(cloud);
	}
	
	private void removeCloud(Cloud cloud) {
		if (clouds.contains(cloud)) {
			clouds.remove(cloud);
		}
	}
	
	public List<Cloud> getClouds() {
		return clouds;
	}

	public void setClouds(List<Cloud> clouds) {
		this.clouds = clouds;
	}

	public int getMaxClouds() {
		return maxClouds;
	}

	public void setMaxClouds(int maxClouds) {
		this.maxClouds = maxClouds;
	}

	public float getDensity() {
		return density;
	}

	public void setDensity(float density) {
		this.density = density;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}
	
	public MaterialClouds getMaterial() {
		return material;
	}

	public void setMaterial(MaterialClouds material) {
		this.material = material;
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
		for (Cloud cloud : clouds) {
			cloud.destroy();
		}
		
		clouds.clear();
	}
}
