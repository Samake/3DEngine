package samake.engine.core;

import org.lwjgl.opengl.GL;

import samake.engine.config.PropertiesHandler;
import samake.engine.display.Display;
import samake.engine.input.Input;
import samake.engine.logging.Console;
import samake.engine.logging.Console.LOGTYPE;
import samake.engine.rendering.Renderer;

public class Engine {
	
	public static Engine instance;
	
	private Display display;
	private Input input;
	private Renderer renderer;
	
    public Engine() throws Exception {
    	instance = this;
    	
    	Console.print("##### Start Engine! #####", LOGTYPE.OUTPUT, true);
    	
    	PropertiesHandler.initialize();
    	
    	setDisplay(new Display());
    	setInput(new Input());
    	setRenderer(new Renderer());
    	
    	Console.print("##### Engine was started! #####", LOGTYPE.OUTPUT, true);
    }
    
    public void start() {

        double secsPerUpdate  = 1.0d / 50.0d;
        double previous = getTime();
        double steps = 0.0;
        
        while (!display.shouldClose()) {
        	double loopStartTime = getTime();
        	double elapsed = loopStartTime - previous;
        	previous = loopStartTime;
        	steps += elapsed;
        	
        	while (steps >= secsPerUpdate) {
        	    steps -= secsPerUpdate;
        	    
        	    if (PropertiesHandler.isvSync()) {
            		sync(loopStartTime);
            	}
        	}
    
        	update();
        	render();
        	guiRender();
        }
        
        destroy();
    }
    
    private void sync(double loopStartTime) {
	   float loopSlot = 1f / PropertiesHandler.getFpsLimit();
	   double endTime = loopStartTime + loopSlot;
	   
	   while(getTime() < endTime) {
	       try {
	           Thread.sleep(1);
	       } catch (InterruptedException ie) {}
	   }
	}
    
    public double getTime() {
        return System.nanoTime() / 1000000000.0;
    }
    
    public void update() {
    	input.update();
    	display.update();
    	renderer.update();
    }
    
    public void render() {
		renderer.render();
	}
	
    public void guiRender() {
		renderer.guiRender();
	}

	public Display getDisplay() {
		return display;
	}

	public void setDisplay(Display display) {
		this.display = display;
	}
	
	public Input getInput() {
		return input;
	}

	public void setInput(Input input) {
		this.input = input;
	}

	public Renderer getRenderer() {
		return renderer;
	}

	public void setRenderer(Renderer renderer) {
		this.renderer = renderer;
	}

	public void destroy() {
		Console.print("##### Stop Engine! #####", LOGTYPE.OUTPUT, true);
		
		display.destroy();
		input.destroy();
		renderer.destroy();
		
		GL.destroy();

		Console.print("##### Engine was stopped! #####", LOGTYPE.OUTPUT, true);
		
		System.exit(-1);
	}
}
