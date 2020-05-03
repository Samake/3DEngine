package samake.engine.display;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;

import samake.engine.config.PropertiesHandler;
import samake.engine.logging.Console;
import samake.engine.logging.Console.LOGTYPE;

public class Display {

	private long handle;
	
	public Display() {
		//TODO: Resizable Window
		
		Console.print("Initialize display...", LOGTYPE.OUTPUT, true);
		
		GLFWErrorCallback.createPrint(System.err).set();
		
		if (GLFW.glfwInit()) {
			
			Console.print("GLFW started!", LOGTYPE.OUTPUT, true);
			
			GLFW.glfwDefaultWindowHints();
			GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
			GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);

			String windowTitle = PropertiesHandler.getGameTitle() 
					+ " | EngineVersion: " + PropertiesHandler.getEngineVersion() 
					+ " | GameVersion: " + PropertiesHandler.getGameVersion();
			
			handle = GLFW.glfwCreateWindow(PropertiesHandler.getWindowWidth(), PropertiesHandler.getWindowHeight(), windowTitle, MemoryUtil.NULL, MemoryUtil.NULL);
			
			if (handle == MemoryUtil.NULL)
				throw new RuntimeException("Failed to create the GLFW window");
			
			GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

			GLFW.glfwSetWindowPos(handle, (vidmode.width() - PropertiesHandler.getWindowWidth()) / 2, (vidmode.height() - PropertiesHandler.getWindowHeight()) / 2);

			GLFW.glfwMakeContextCurrent(handle);
			GLFW.glfwSwapInterval(PropertiesHandler.isvSync() ? 1 : 0);
			GLFW.glfwShowWindow(handle);
			
			Console.print("Window created! Size: " + PropertiesHandler.getWindowWidth() + " x " + PropertiesHandler.getWindowHeight(), LOGTYPE.OUTPUT, true);
		}
			
		GL.createCapabilities();
		
		Console.print("OpenGL context was set!", LOGTYPE.OUTPUT, true);
		Console.print("Display initialized!", LOGTYPE.OUTPUT, true);
	}
	
	public void update() {
		GLFW.glfwSwapBuffers(handle);
		GLFW.glfwPollEvents();
    }

	public long getHandle() {
		return handle;
	}

	public void setHandle(long handle) {
		this.handle = handle;
	}
	
	public boolean shouldClose() {
		return GLFW.glfwWindowShouldClose(handle);
	}
	
	public void destroy() {
		Callbacks.glfwFreeCallbacks(handle);
		GLFW.glfwDestroyWindow(handle);
		
		Console.print("Window destroyed!", LOGTYPE.OUTPUT, true);

		GLFW.glfwTerminate();
		GLFW.glfwSetErrorCallback(null).free();
		
		Console.print("GLFW stopped!", LOGTYPE.OUTPUT, true);
	}
}
