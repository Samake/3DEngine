package samake.engine.input;

import org.joml.Vector2d;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import samake.engine.core.Engine;

public class MouseInput {
	
	private Vector2d previousPos;
    private Vector2d currentPos;
    private Vector2f displVec;
    private boolean inWindow = false;
    private boolean leftButtonPressed = false;
    private boolean rightButtonPressed = false;
    
	public MouseInput() {
        previousPos = new Vector2d(-1, -1);
        currentPos = new Vector2d(0, 0);
        displVec = new Vector2f();
        
        long currentWindowHandle = Engine.instance.getDisplay().getHandle();
        
        GLFW.glfwSetCursorPosCallback(currentWindowHandle, (windowHandle, xpos, ypos) -> {
            currentPos.x = xpos;
            currentPos.y = ypos;
        });
        
        GLFW.glfwSetCursorEnterCallback(currentWindowHandle, (windowHandle, entered) -> {
            inWindow = entered;
        });
        
        GLFW.glfwSetMouseButtonCallback(currentWindowHandle, (handle, button, action, mode) -> {
            leftButtonPressed = button == GLFW.GLFW_MOUSE_BUTTON_1 && action == GLFW.GLFW_PRESS;
            rightButtonPressed = button == GLFW.GLFW_MOUSE_BUTTON_2 && action == GLFW.GLFW_PRESS;
        });
	}
	
    public void update() {
        displVec.x = 0;
        displVec.y = 0;
        
        if (previousPos.x > 0 && previousPos.y > 0 && inWindow) {
        	
            double deltax = currentPos.x - previousPos.x;
            double deltay = currentPos.y - previousPos.y;
            boolean rotateX = deltax != 0;
            boolean rotateY = deltay != 0;
            
            if (rotateX) {
                displVec.y = (float) deltax;
            }
            
            if (rotateY) {
                displVec.x = (float) deltay;
            }
        }
        
        previousPos.x = currentPos.x;
        previousPos.y = currentPos.y;
    }
    
	public Vector2f getMoveVector() {
        return displVec;
    }

    public boolean isLeftButtonPressed() {
        return leftButtonPressed;
    }

    public boolean isRightButtonPressed() {
        return rightButtonPressed;
    }
}
