package samake.game.controls;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import samake.engine.camera.Camera;
import samake.engine.core.Engine;
import samake.engine.input.Input;
import samake.engine.rendering.Renderer.RENDERMODE;
import samake.game.map.Map;

public class Controls {
	
	private final float CAMERA_POS_STEP = 0.1f;
	private final float MOUSE_SENSITIVITY = 0.25f;
	
	private Vector3f cameraInc = new Vector3f();
	private float speedValue = 1.0f;
	private float currentSpeed = 0.0f;
	
	public Controls() {
		
	}

	public void update(Map map) {
		Input input = Engine.instance.getInput();
		
		if (input != null) {
		    if (input.isKeyPressed(GLFW.GLFW_KEY_W)) {
		        cameraInc.z = -1;
		    } else if (input.isKeyPressed(GLFW.GLFW_KEY_S)) {
		        cameraInc.z = 1;
		    } else {
		    	cameraInc.z = 0;
		    }
		    
		    if (input.isKeyPressed(GLFW.GLFW_KEY_A)) {
		        cameraInc.x = -1;
		    } else if (input.isKeyPressed(GLFW.GLFW_KEY_D)) {
		        cameraInc.x = 1;
		    } else {
		    	cameraInc.x = 0;
		    }
		    
		    if (input.isKeyPressed(GLFW.GLFW_KEY_Z)) {
		        cameraInc.y = -1;
		    } else if (input.isKeyPressed(GLFW.GLFW_KEY_X)) {
		        cameraInc.y = 1;
		    } else {
		    	cameraInc.y = 0;
		    }
		    
		    Camera camera = map.getCamera();
			
		    currentSpeed = speedValue;
		    
		    if (input.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT)) {
		    	currentSpeed = speedValue * 8;
		    }
		    
		    if (input.isKeyPressed(GLFW.GLFW_KEY_LEFT_CONTROL)) {
		    	currentSpeed = speedValue / 8;
		    }
		    
			if (camera != null) {
				// Update camera position
			    camera.movePosition(
			    	cameraInc.x * CAMERA_POS_STEP * currentSpeed,
			        cameraInc.y * CAMERA_POS_STEP * currentSpeed,
			        cameraInc.z * CAMERA_POS_STEP * currentSpeed);
			    
			    // Update camera based on mouse            
			    if (input.isRightMouseButtonPressed()) {
			        Vector2f rotVec = input.getCursorMoveVector();
			        
			        camera.moveRotation(
			        		rotVec.x * MOUSE_SENSITIVITY, 
			        		rotVec.y * MOUSE_SENSITIVITY, 
			        		0);
			    }
			}
			
			if (input.isKeyPressed(GLFW.GLFW_KEY_ESCAPE)) {
				Engine.instance.destroy();
			}
			
			if (input.isKeyPressed(GLFW.GLFW_KEY_F1)) {
				Engine.instance.getRenderer().changeRenderMode(RENDERMODE.DEFAULT);
			}
			
			if (input.isKeyPressed(GLFW.GLFW_KEY_F2)) {
				Engine.instance.getRenderer().changeRenderMode(RENDERMODE.DEBUG);
			}
			
			if (input.isKeyPressed(GLFW.GLFW_KEY_F3)) {
				Engine.instance.getRenderer().changeRenderMode(RENDERMODE.WIREFRAME);
			}
			
			if (input.isKeyPressed(GLFW.GLFW_KEY_F4)) {
				Engine.instance.getRenderer().changeRenderMode(RENDERMODE.DIFFUSE);
			}
			
			if (input.isKeyPressed(GLFW.GLFW_KEY_F5)) {
				Engine.instance.getRenderer().changeRenderMode(RENDERMODE.NORMALS);
			}
			
			if (input.isKeyPressed(GLFW.GLFW_KEY_F6)) {
				Engine.instance.getRenderer().changeRenderMode(RENDERMODE.ALBEDO);
			}
			
			if (input.isKeyPressed(GLFW.GLFW_KEY_F7)) {
				Engine.instance.getRenderer().changeRenderMode(RENDERMODE.DEPTH);
			}
			
			if (input.isKeyPressed(GLFW.GLFW_KEY_F8)) {
				Engine.instance.getRenderer().changeRenderMode(RENDERMODE.POSITION);
			}
			
			if (input.isKeyPressed(GLFW.GLFW_KEY_F9)) {
				Engine.instance.getRenderer().changeRenderMode(RENDERMODE.COLOR);
			}
		}
	}
}
