package samake.engine.scene.environment.fog;

public class Fog {
	
	private float density;
	
	public Fog() {
		setDensity(0.0035f);
	}

	public float getDensity() {
		return density;
	}

	public void setDensity(float density) {
		this.density = density;
	}
}
