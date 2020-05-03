package samake.engine.entity.light;

public class PointLight extends Light {

    
	public PointLight() {
		setType(LIGHTTYPE.POINTLIGHT);
		setScale(0.2f);
		setIntensity(1.0f, true);
	}
}
