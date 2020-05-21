package samake.game;

import samake.engine.core.Engine;
import samake.engine.logging.Console;
import samake.engine.logging.Console.LOGTYPE;
import samake.game.editor.MapEditor;

public class Game extends Engine {
	
	public static Game instance;
	
	private MapEditor editor;
	
	public Game() throws Exception {
		instance = this;
		
		Console.print("********** Start Game... **********", LOGTYPE.OUTPUT, true);
		
		editor = new MapEditor();
		
		Console.print("********** Game started! **********", LOGTYPE.OUTPUT, true);
		
		super.start();
	}

	public static void main(String[] args) throws Exception {
		new Game();
	}
	
	@Override
	public void update() {
		super.update();
		editor.update();
	}

	@Override
	public void destroy() {
		Console.print("********** Stop Game... **********", LOGTYPE.OUTPUT, true);
		
		editor.destroy();
		
		Console.print("********** Game stopped! **********", LOGTYPE.OUTPUT, true);
		
		super.destroy();
	}
}
