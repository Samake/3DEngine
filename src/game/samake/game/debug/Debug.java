package samake.game.debug;

import org.joml.Vector2f;
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
		}
	}
	
	public void spawnSphere() {
		DynamicObject object = new DynamicObject();
		object.setModel(ResourceLoader.load3DModel("sphere.fbx"));
		object.getMaterial(0).setTexture(ResourceLoader.loadTexture("debug\\debug.png", true));
		object.getMaterial(0).setNormalMap(ResourceLoader.loadTexture("debug\\debug_n.png", true));
		object.getMaterial(0).setSpecularMap(ResourceLoader.loadTexture("debug\\debug_s.png", true));
		object.setRotation(new Vector3f(270.0f, 0.0f, 0.0f));
		object.setPosition(new Vector3f(Utils.getRandomValue(31.5f, 32.5f, 1), 50.0f, Utils.getRandomValue(-63.5f, -64.5f, 1)));
		object.setScale(1.0f);
		object.getPhysicBody().setMass(25.0f);
		object.getPhysicBody().setRestitution(0.55f);
		object.getPhysicBody().setFriction(0.85f);
		object.getPhysicBody().setDamping(new Vector2f(0.10f, 0.45f));
		
		level.addEntity(object);
	}

	public void destroy() {

	}
}
