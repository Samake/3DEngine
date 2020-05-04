package samake.engine.scene.sky;

import samake.engine.material.TextureCube;
import samake.engine.models.Model;
import samake.engine.resources.ResourceLoader;

public class Sky {
	
	private Sun sun;
	private Moon moon;
	private Model model;
	private Clouds clouds;
	private float scale;
	private TextureCube nightSky;
	
	public Sky() {
		setSun(new Sun());
		setMoon(new Moon());
		setClouds(new Clouds());
		setModel(ResourceLoader.load3DModel("sphere.fbx"));
		setNightSky(new TextureCube("sky\\night"));
		setScale(4096.0f);
	}
	
	public void update() {
		clouds.update();
	}
	
	public Sun getSun() {
		return sun;
	}

	public void setSun(Sun sun) {
		this.sun = sun;
	}

	public Moon getMoon() {
		return moon;
	}

	public void setMoon(Moon moon) {
		this.moon = moon;
	}

	public Clouds getClouds() {
		return clouds;
	}

	public void setClouds(Clouds clouds) {
		this.clouds = clouds;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public TextureCube getNightSky() {
		return nightSky;
	}

	public void setNightSky(TextureCube nightSky) {
		this.nightSky = nightSky;
	}

	public void destroy() {
		model.destroy();
		nightSky.destroy();
		clouds.destroy();
	}
}
