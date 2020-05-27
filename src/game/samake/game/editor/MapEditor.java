package samake.game.editor;

import org.joml.Vector3f;

import samake.engine.entity.npc.Player;
import samake.engine.logging.Console;
import samake.engine.logging.Console.LOGTYPE;
import samake.game.debug.Debug;
import samake.game.map.Map;
import samake.game.map.MapHandler;

public class MapEditor {
	
	private Map map;
	private Debug debug;

	private Player player;
	
	public MapEditor() {
		Console.print("LevelEditor started.", LOGTYPE.OUTPUT, true);
		
		setMap(MapHandler.loadMap("testmap"));
		
		if (map != null) {
			debug = new Debug(map);
			map.getCamera().setPosition(0.0f, 32.0f, -32.0f);
			map.getCamera().setLookAt(0.0f, 5.0f, 0.0f);
			map.init();

			player = new Player();
			player.setPosition(new Vector3f(0.0f, 8.0f, 0.0f));
			player.setRotation(new Vector3f(270.0f, 0.0f, 0.0f));
			
			map.addPlayer(player);

			map.getCamera().setTarget(player);
		}
	}
	
	public void update() {
		if (map != null) {
			map.update();
		}
		
		if (player != null) {
			player.update();
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
		
		if (player != null) {
			player.destroy();
		}
		
		Console.print("LevelEditor stopped", LOGTYPE.OUTPUT, true);
	}
}
