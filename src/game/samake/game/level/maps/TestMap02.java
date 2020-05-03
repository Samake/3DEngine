package samake.game.level.maps;

import org.joml.Vector3f;

import samake.engine.logging.Console;
import samake.engine.logging.Console.LOGTYPE;
import samake.engine.resources.ResourceLoader;
import samake.engine.scene.terrain.Terrain;
import samake.engine.scene.water.Water;
import samake.game.level.Level;

public class TestMap02 extends Level {
	
	public TestMap02() {
		Console.print("TestMap02 started.", LOGTYPE.OUTPUT, true);
		
		getCamera().setPosition(0.0f, 4.0f, 18.0f);
		
		getEnvironment().setGameSpeed(1);
		getEnvironment().setDayProgress(0.5f);
		getEnvironment().setFrozen(false);
		getEnvironment().getFog().setDensity(0.0015f);
		
		addLight(getEnvironment().getSky().getSun());

		Terrain terrain = new Terrain();
		terrain.generateModel(new Vector3f(-64.0f, 0.0f, -64.0f), 32, 128.0f, false, false);
		terrain.getMaterial().setShininess(4.0f);
		terrain.getMaterial().setReflectance(0.5f);
		terrain.getMaterial().setTexture(ResourceLoader.loadTexture("debug\\debug.png", true));
		terrain.getMaterial().setNormalMap(ResourceLoader.loadTexture("debug\\debug_n.png", true));
		terrain.getMaterial().setSpecularMap(ResourceLoader.loadTexture("debug\\debug_s.png", true));
		terrain.getMaterial().setTiling(1024);
		
		addTerrain(terrain);
		
		Water water = new Water();
		water.setModel(ResourceLoader.load3DModel("sphere.fbx"));
		water.setPosition(new Vector3f(0.0f, -5.0f, 0.0f));
		
		addWater(water);
	}
	
	@Override
	public void update() {
		super.update();
	}

	@Override
	public void destroy() {
		super.destroy();
		
		Console.print("TestMap02 stopped", LOGTYPE.OUTPUT, true);
	}
}
