package samake.engine.entity.light;

public class DirectionalLight extends Light {

	public DirectionalLight() {
		setType(LIGHTTYPE.DIRECTIONALLIGHT);
		setScale(8.0f);
	}
}
