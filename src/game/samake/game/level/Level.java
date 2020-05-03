package samake.game.level;

import samake.engine.core.Engine;
import samake.engine.scene.Scene;
import samake.game.controls.Controls;

public class Level extends Scene {
	
	private Controls controls;
	
	public Level() {
		setControls(new Controls());
		Engine.instance.getRenderer().setScene(this);
	}
	
	@Override
	public void update() {
		super.update();
		controls.update(this);
	}

	public Controls getControls() {
		return controls;
	}

	public void setControls(Controls controls) {
		this.controls = controls;
	}

	@Override
	public void destroy() {
		clear();
		super.destroy();
		getEnvironment().destroy();
		Engine.instance.getRenderer().setScene(null);
	}
}
