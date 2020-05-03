package samake.game;

import samake.engine.core.Engine;
import samake.engine.logging.Console;
import samake.engine.logging.Console.LOGTYPE;
import samake.game.level.maps.TestMap01;

public class Game extends Engine {
	
	public static Game instance;
	
	private TestMap01 scene;
	
	public Game() throws Exception {
		instance = this;
		
		Console.print("********** Start Game... **********", LOGTYPE.OUTPUT, true);
		
		scene = new TestMap01();
		
		Console.print("********** Game started! **********", LOGTYPE.OUTPUT, true);
		
		start();
	}

	public static void main(String[] args) throws Exception {
		new Game();
	}
	
	@Override
	public void update() {
		super.update();
		
		scene.update();
	}

	@Override
	public void destroy() {
		super.destroy();
		
		Console.print("********** Stop Game... **********", LOGTYPE.OUTPUT, true);
		
		scene.destroy();
		
		Console.print("********** Game stopped! **********", LOGTYPE.OUTPUT, true);
	}
}
