package samake.engine.entity.light;

import org.joml.Vector3f;

public class SpotLight extends Light {

	public SpotLight() {
		setType(LIGHTTYPE.SPOTLIGHT);
		setScale(0.2f);
		setIntensity(2.0f, true);
		setDirection(new Vector3f(0.0f, 0.05f, 0.4f));
		setCutOff(0.65f);
		getAttenuation().setExponent(0.25f);
	}
}
