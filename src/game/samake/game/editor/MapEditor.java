package samake.game.editor;

import org.joml.Vector3f;

import samake.engine.entity.objects.StaticObject;
import samake.engine.logging.Console;
import samake.engine.logging.Console.LOGTYPE;
import samake.engine.resources.ResourceLoader;
import samake.game.debug.Debug;
import samake.game.map.Map;
import samake.game.map.MapHandler;

public class MapEditor {
	
	private Map map;
	private Debug debug;
	
	private StaticObject testObject;
	
	public MapEditor() {
		Console.print("LevelEditor started.", LOGTYPE.OUTPUT, true);
		
		setMap(MapHandler.loadMap("testmap"));
		
		if (map != null) {
			debug = new Debug(map);
			map.getCamera().setPosition(0.0f, 128.0f, -256.0f);
			map.getCamera().setLookAt(0.0f, 5.0f, 0.0f);
			map.init();
			
			testObject = new StaticObject();
			testObject.setModel(ResourceLoader.load3DModel("sphere.fbx", false));
			testObject.setUpdatedEntity(true);
			testObject.setPosition(new Vector3f(-1024, 64, 0));
			testObject.setUpdatePosition(new Vector3f(1.0f, 0.0f, 0.0f));
			testObject.getMaterial().setTexture(ResourceLoader.loadTexture("debug\\debug.png", true));
			testObject.getMaterial().setNormalMap(ResourceLoader.loadTexture("debug\\debug_n.png", true));
			testObject.getMaterial().setSpecularMap(ResourceLoader.loadTexture("debug\\debug_s.png", true));
			testObject.setScale(10);
			
			map.addEntity(testObject);
			
			map.getCamera().setTarget(testObject);
		}
	}
	
	public void update() {
		if (map != null) {
			map.update();
		}
		
		if (debug != null) {
			debug.update();
		}
	}


	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public void destroy() {
		MapHandler.saveMap("testmap", map);
		
		if (map != null) {
			map.destroy();
		}
		
		if (debug != null) {
			debug.destroy();
		}
		
		if (testObject != null) {
			testObject.destroy();
		}
		
		Console.print("LevelEditor stopped", LOGTYPE.OUTPUT, true);
	}
}
