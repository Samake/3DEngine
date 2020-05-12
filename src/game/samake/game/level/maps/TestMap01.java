package samake.game.level.maps;

import org.joml.Vector2f;
import org.joml.Vector3f;

import samake.engine.entity.light.PointLight;
import samake.engine.entity.objects.DynamicObject;
import samake.engine.entity.objects.StaticObject;
import samake.engine.logging.Console;
import samake.engine.logging.Console.LOGTYPE;
import samake.engine.perlin.PerlinGenerator;
import samake.engine.resources.ResourceLoader;
import samake.engine.scene.terrain.Terrain;
import samake.engine.scene.water.Water;
import samake.game.level.Level;

public class TestMap01 extends Level {
	
	public TestMap01() {
		Console.print("TestMap01 started.", LOGTYPE.OUTPUT, true);
		
		getCamera().setPosition(0.0f, 6.0f, 0.0f);
		
		getEnvironment().setGameSpeed(1);
		getEnvironment().setDayProgress(0.65f);
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
		terrain.getMaterial().setShininess(8.0f);
		terrain.getMaterial().setReflectance(0.4f);
		terrain.getMaterial().setTexture(ResourceLoader.loadTexture("debug\\debug.png", true));
		terrain.getMaterial().setNormalMap(ResourceLoader.loadTexture("debug\\debug_n.png", true));
		terrain.getMaterial().setSpecularMap(ResourceLoader.loadTexture("debug\\debug_s.png", true));
		terrain.getMaterial().setTiling(1024);
		
		addTerrain(terrain);
		
		Water water = new Water();
		water.generateModel(new Vector3f(-512.0f, 0.0f, -512.0f), 256, 1024.0f);
		
		addWater(water);
		
		StaticObject object0 = new StaticObject();
		object0.setModel(ResourceLoader.load3DModel("sphere.fbx"));
		object0.getMaterial(0).setShininess(32.0f);
		object0.getMaterial(0).setReflectance(1.0f);
		object0.getMaterial(0).setTexture(ResourceLoader.loadTexture("debug\\debug.png", true));
		object0.getMaterial(0).setNormalMap(ResourceLoader.loadTexture("debug\\debug_n.png", true));
		object0.getMaterial(0).setSpecularMap(ResourceLoader.loadTexture("debug\\debug_s.png", true));
		object0.setRotation(new Vector3f(270.0f, 0.0f, 0.0f));
		object0.setPosition(new Vector3f(0.0f, 10.0f, -15.0f));
		object0.setScale(2.0f);

		addEntity(object0);
		
		DynamicObject object1 = new DynamicObject();
		object1.setModel(ResourceLoader.load3DModel("sphere.fbx"));
		object1.getMaterial(0).setShininess(32.0f);
		object1.getMaterial(0).setReflectance(1.0f);
		object1.getMaterial(0).setTexture(ResourceLoader.loadTexture("debug\\debug.png", true));
		object1.getMaterial(0).setNormalMap(ResourceLoader.loadTexture("debug\\debug_n.png", true));
		object1.getMaterial(0).setSpecularMap(ResourceLoader.loadTexture("debug\\debug_s.png", true));
		object1.setRotation(new Vector3f(270.0f, 0.0f, 0.0f));
		object1.setPosition(new Vector3f(0.0f, 100.0f, 0.0f));
		object1.setScale(1.0f);
		object1.getPhysicBody().setMass(20.0f);
		object1.getPhysicBody().setRestitution(0.4f);
		object1.getPhysicBody().setFriction(0.4f);
		object1.getPhysicBody().setDamping(new Vector2f(0.15f, 0.25f));

		addEntity(object1);
		
		DynamicObject object2 = new DynamicObject();
		object2.setModel(ResourceLoader.load3DModel("sphere.fbx"));
		object2.getMaterial(0).setShininess(128.0f);
		object2.getMaterial(0).setReflectance(2.0f);
		object2.getMaterial(0).setTexture(ResourceLoader.loadTexture("debug\\debug.png", true));
		object2.getMaterial(0).setNormalMap(ResourceLoader.loadTexture("debug\\debug_n.png", true));
		object2.getMaterial(0).setSpecularMap(ResourceLoader.loadTexture("debug\\debug_s.png", true));
		object2.setRotation(new Vector3f(270.0f, 0.0f, 0.0f));
		object2.setPosition(new Vector3f(0.0f, 150.0f, 0.0f));
		object2.setScale(1.0f);
		object2.getPhysicBody().setMass(50.0f);
		object2.getPhysicBody().setRestitution(0.8f);
		object2.getPhysicBody().setFriction(1.0f);
		object2.getPhysicBody().setDamping(new Vector2f(0.05f, 0.5f));

		addEntity(object2);
		
		DynamicObject object3 = new DynamicObject();
		object3.setModel(ResourceLoader.load3DModel("sphere.fbx"));
		object3.getMaterial(0).setShininess(256.0f);
		object3.getMaterial(0).setReflectance(4.0f);
		object3.getMaterial(0).setTexture(ResourceLoader.loadTexture("debug\\debug.png", true));
		object3.getMaterial(0).setNormalMap(ResourceLoader.loadTexture("debug\\debug_n.png", true));
		object3.getMaterial(0).setSpecularMap(ResourceLoader.loadTexture("debug\\debug_s.png", true));
		object3.setRotation(new Vector3f(270.0f, 0.0f, 0.0f));
		object3.setPosition(new Vector3f(0.0f, 125.0f, 0.0f));
		object3.setScale(1.0f);
		object3.getPhysicBody().setMass(1.0f);
		object3.getPhysicBody().setRestitution(0.2f);
		object3.getPhysicBody().setFriction(0.2f);
		object3.getPhysicBody().setDamping(new Vector2f(0.1f, 0.1f));

		addEntity(object3);
		
		PointLight light = new PointLight();
		light.setPosition(new Vector3f(0.0f, 12.0f, 32.0f));
		light.setColor(new Vector3f(1.0f, 0.7f, 0.3f));
		light.setIntensity(256.0f, true);
		
		addLight(light);
	}
	
	@Override
	public void update() {
		super.update();
	}

	@Override
	public void destroy() {
		super.destroy();
		
		Console.print("TestMap01 stopped", LOGTYPE.OUTPUT, true);
	}
}
