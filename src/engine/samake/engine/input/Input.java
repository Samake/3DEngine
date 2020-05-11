package samake.engine.input;

import org.joml.Vector2d;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import samake.engine.core.Engine;
import samake.engine.logging.Console;
import samake.engine.logging.Console.LOGTYPE;

public class Input {
	
	private Vector2d previousCursorPosition;
    private Vector2d currentCursorPosition;
    private Vector2f cursorMoveVector;
    private Vector2f cursorPosition;
    private boolean cursorInWindow = false;
    private boolean leftMouseButtonPressed = false;
    private boolean rightMouseButtonPressed = false;
	
	public Input() {
		Console.print("Initialize Input...", LOGTYPE.OUTPUT, true);
		
        previousCursorPosition = new Vector2d(-1, -1);
        currentCursorPosition = new Vector2d(0, 0);
        cursorMoveVector = new Vector2f();
        cursorPosition = new Vector2f();
        
        long currentWindowHandle = Engine.instance.getDisplay().getHandle();
        
        GLFW.glfwSetCursorPosCallback(currentWindowHandle, (windowHandle, xpos, ypos) -> {
            currentCursorPosition.x = xpos;
            currentCursorPosition.y = ypos;
            cursorPosition.x = (float) xpos;
            cursorPosition.y = (float) ypos;
        });
        
        GLFW.glfwSetCursorEnterCallback(currentWindowHandle, (windowHandle, entered) -> {
            cursorInWindow = entered;
        });
        
        GLFW.glfwSetMouseButtonCallback(currentWindowHandle, (handle, button, action, mode) -> {
            leftMouseButtonPressed = button == GLFW.GLFW_MOUSE_BUTTON_1 && action == GLFW.GLFW_PRESS;
            rightMouseButtonPressed = button == GLFW.GLFW_MOUSE_BUTTON_2 && action == GLFW.GLFW_PRESS;
        });
		
		Console.print("Input loaded!", LOGTYPE.OUTPUT, true);
	}
	
	public void update() {
        cursorMoveVector.x = 0;
        cursorMoveVector.y = 0;
        
        if (previousCursorPosition.x > 0 && previousCursorPosition.y > 0 && cursorInWindow) {
        	
            double deltax = currentCursorPosition.x - previousCursorPosition.x;
            double deltay = currentCursorPosition.y - previousCursorPosition.y;
            boolean rotateX = deltax != 0;
            boolean rotateY = deltay != 0;
            
            if (rotateX) {
                cursorMoveVector.y = (float) deltax;
            }
            
            if (rotateY) {
                cursorMoveVector.x = (float) deltay;
            }
        }
        
        previousCursorPosition.x = currentCursorPosition.x;
        previousCursorPosition.y = currentCursorPosition.y;
    }
	
	public boolean isKeyPressed(int keyCode) {
        return GLFW.glfwGetKey(Engine.instance.getDisplay().getHandle(), keyCode) == GLFW.GLFW_PRESS;
    }

    public boolean isLeftMouseButtonPressed() {
        return leftMouseButtonPressed;
    }

    public boolean isRightMouseButtonPressed() {
        return rightMouseButtonPressed;
    }

	public boolean isCursorInWindow() {
		return cursorInWindow;
	}
	
	public Vector2f getCursorMoveVector() {
        return cursorMoveVector;
    }

	public Vector2f getCursorPosition() {
		return cursorPosition;
	}

	public void destroy() {
		Console.print("Input stopped!", LOGTYPE.OUTPUT, true);
	}
}
