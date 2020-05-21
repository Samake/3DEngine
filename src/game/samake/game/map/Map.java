package samake.game.map;

import samake.engine.core.Engine;
import samake.engine.scene.Scene;
import samake.game.controls.Controls;

public class Map extends Scene {
	
	private MapData data;
	private Controls controls;
	
	public Map() {
		setControls(new Controls());
		setData(new MapData());
		
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

	public MapData getData() {
		return data;
	}

	public void setData(MapData data) {
		this.data = data;
	}

	@Override
	public void destroy() {
		clear();
		super.destroy();
		getEnvironment().destroy();
		Engine.instance.getRenderer().setScene(null);
	}
}
