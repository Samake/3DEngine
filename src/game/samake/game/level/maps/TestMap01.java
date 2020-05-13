package samake.game.level.maps;

import org.joml.Vector3f;

import samake.engine.entity.light.PointLight;
import samake.engine.entity.objects.StaticObject;
import samake.engine.logging.Console;
import samake.engine.logging.Console.LOGTYPE;
import samake.engine.perlin.PerlinGenerator;
import samake.engine.resources.ResourceLoader;
import samake.engine.scene.terrain.Terrain;
import samake.engine.scene.water.Water;
import samake.game.debug.Debug;
import samake.game.level.Level;

public class TestMap01 extends Level {
	
	private Debug debug;
	
	public TestMap01() {
		Console.print("TestMap01 started.", LOGTYPE.OUTPUT, true);
		
		debug = new Debug(this);
		
		getCamera().setPosition(0.0f, 6.0f, 0.0f);
		
		getEnvironment().setGameSpeed(1);
		getEnvironment().setDayProgress(0.8f);
		getEnvironment().setFrozen(false);
		getEnvironment().getFog().setDensity(0.0025f);
		
		addLight(getEnvironment().getSky().getSun());
		addLight(getEnvironment().getSky().getMoon());
		
		PerlinGenerator.setTurbolence(512.0f);
		PerlinGenerator.setGain(0.45f);
		PerlinGenerator.setLacunarity(7.5f);
		PerlinGenerator.setOctaves(8);
		PerlinGenerator.setHeightModifier(128);
		
		Terrain terrain = new Terrain();
		terrain.generateModel(new Vector3f(-512.0f, 0.0f, -512.0f), 512, 1024.0f, true, true);
		terrain.getMaterial().setShininess(16.0f);
		terrain.getMaterial().setReflectance(0.8f);
		terrain.getMaterial().setTexture(ResourceLoader.loadTexture("debug\\debug.png", true));
		terrain.getMaterial().setNormalMap(ResourceLoader.loadTexture("debug\\debug_n.png", true));
		terrain.getMaterial().setSpecularMap(ResourceLoader.loadTexture("debug\\debug_s.png", true));
		terrain.getMaterial().setTiling(1024);
		
		addTerrain(terrain);
		
		Water water = new Water();
		water.generateModel(new Vector3f(-512.0f, 0.0f, -512.0f), 256, 1024.0f);
		
		addWater(water);
		
		StaticObject object0 = new StaticObject();
		object0.setModel(ResourceLoader.load3DModel("box.fbx"));
		object0.getMaterial(0).setShininess(32.0f);
		object0.getMaterial(0).setReflectance(1.0f);
		object0.getMaterial(0).setTexture(ResourceLoader.loadTexture("debug\\debug.png", true));
		object0.getMaterial(0).setNormalMap(ResourceLoader.loadTexture("debug\\debug_n.png", true));
		object0.getMaterial(0).setSpecularMap(ResourceLoader.loadTexture("debug\\debug_s.png", true));
		object0.setRotation(new Vector3f(270.0f, 0.0f, 0.0f));
		object0.setPosition(new Vector3f(0.0f, 8.0f, 0.0f));
		object0.setScale(1.0f);

		addEntity(object0);
	
		PointLight light = new PointLight();
		light.setModel(ResourceLoader.load3DModel("sphere.fbx"));
		light.setPosition(new Vector3f(0.0f, 12.0f, 24.0f));
		light.setColor(new Vector3f(1.0f, 0.7f, 0.3f));
		light.setIntensity(128.0f, true);
		
		addLight(light);
		
		light = new PointLight();
		light.setModel(ResourceLoader.load3DModel("sphere.fbx"));
		light.setPosition(new Vector3f(-24.0f, 12.0f, 0.0f));
		light.setColor(new Vector3f(0.3f, 1.0f, 0.7f));
		light.setIntensity(128.0f, true);
		
		addLight(light);
		
		light = new PointLight();
		light.setModel(ResourceLoader.load3DModel("sphere.fbx"));
		light.setPosition(new Vector3f(24.0f, 12.0f, 0.0f));
		light.setColor(new Vector3f(0.7f, 0.3f, 1.0f));
		light.setIntensity(128.0f, true);
		
		addLight(light);
	}
	
	@Override
	public void update() {
		super.update();
		
		if (debug != null) {
			debug.update();
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		
		if (debug != null) {
			debug.destroy();
		}
		
		Console.print("TestMap01 stopped", LOGTYPE.OUTPUT, true);
	}
}
