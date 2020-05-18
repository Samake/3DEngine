package samake.game.debug;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import samake.engine.core.Engine;
import samake.engine.entity.objects.DynamicObject;
import samake.engine.input.Input;
import samake.engine.resources.ResourceLoader;
import samake.engine.utils.Utils;
import samake.game.level.Level;

public class Debug {
	
	private Level level;
	private boolean keyStateKeyM = false;
	private boolean keyStateKeyN = false;
	private boolean keyStateKeyB = false;
	
	public Debug(Level level) {
		this.level = level;
	}
	
	public void update() {
		Input input = Engine.instance.getInput();
		
		if (input != null && level != null) {
		    if (input.isKeyPressed(GLFW.GLFW_KEY_M)) {
		    	if (!keyStateKeyM) {
		    		spawnSphere();
		    		keyStateKeyM = !keyStateKeyM;
		    	}
		    } else {
		    	if (keyStateKeyM) {
		    		keyStateKeyM = !keyStateKeyM;
		    	}
		    }
		    
		    if (input.isKeyPressed(GLFW.GLFW_KEY_N)) {
		    	if (!keyStateKeyN) {
		    		spawnBox();
		    		keyStateKeyN = !keyStateKeyN;
		    	}
		    } else {
		    	if (keyStateKeyN) {
		    		keyStateKeyN = !keyStateKeyN;
		    	}
		    }
		    
		    if (input.isKeyPressed(GLFW.GLFW_KEY_B)) {
		    	if (!keyStateKeyB) {
		    		spawnCone();
		    		keyStateKeyB = !keyStateKeyB;
		    	}
		    } else {
		    	if (keyStateKeyB) {
		    		keyStateKeyB = !keyStateKeyB;
		    	}
		    }
		}
	}
	
	public void spawnSphere() {
		DynamicObject object = new DynamicObject();
		object.setModel(ResourceLoader.load3DModel("sphere.fbx", false));
		object.setCollissionModel(ResourceLoader.load3DModel("sphereHull.fbx", true));
		object.getMaterial(0).setShininess(256.0f);
		object.getMaterial(0).setReflectance(2.0f);
		object.getMaterial(0).setTexture(ResourceLoader.loadTexture("debug\\debug.png", true));
		object.getMaterial(0).setNormalMap(ResourceLoader.loadTexture("debug\\debug_n.png", true));
		object.getMaterial(0).setSpecularMap(ResourceLoader.loadTexture("debug\\debug_s.png", true));
		object.setRotation(new Vector3f(0.0f, 0.0f, 0.0f));
		object.setPosition(new Vector3f(Utils.getRandomValue(44.0f, 50.0f, 1), 85.0f, Utils.getRandomValue(-132.0f, -138.0f, 1)));
		object.setScale(1.0f);
		
		level.addEntity(object);
	}
	
	public void spawnBox() {
		DynamicObject object = new DynamicObject();
		object.setModel(ResourceLoader.load3DModel("box.fbx", false));
		object.setCollissionModel(ResourceLoader.load3DModel("boxHull.fbx", true));
		object.getMaterial(0).setShininess(32.0f);
		object.getMaterial(0).setReflectance(1.0f);
		object.getMaterial(0).setTexture(ResourceLoader.loadTexture("debug\\debug.png", true));
		object.getMaterial(0).setNormalMap(ResourceLoader.loadTexture("debug\\debug_n.png", true));
		object.getMaterial(0).setSpecularMap(ResourceLoader.loadTexture("debug\\debug_s.png", true));
		object.setRotation(new Vector3f(0.0f, 0.0f, 0.0f));
		object.setPosition(new Vector3f(Utils.getRandomValue(44.0f, 50.0f, 1), 85.0f, Utils.getRandomValue(-132.0f, -138.0f, 1)));
		object.setScale(1.0f);
		
		level.addEntity(object);
	}
	
	public void spawnCone() {
		DynamicObject object = new DynamicObject();
		object.setModel(ResourceLoader.load3DModel("player.fbx", false));
		object.setCollissionModel(ResourceLoader.load3DModel("playerHull.fbx", true));
		object.getMaterial(0).setShininess(128.0f);
		object.getMaterial(0).setReflectance(3.0f);
		object.getMaterial(0).setTexture(ResourceLoader.loadTexture("debug\\debug.png", true));
		object.getMaterial(0).setNormalMap(ResourceLoader.loadTexture("debug\\debug_n.png", true));
		object.getMaterial(0).setSpecularMap(ResourceLoader.loadTexture("debug\\debug_s.png", true));
		object.setRotation(new Vector3f(270.0f, 0.0f, 0.0f));
		object.setPosition(new Vector3f(Utils.getRandomValue(44.0f, 50.0f, 1), 85.0f, Utils.getRandomValue(-132.0f, -138.0f, 1)));
		object.setScale(1.0f);
		
		level.addEntity(object);
	}

	public void destroy() {

	}
}
