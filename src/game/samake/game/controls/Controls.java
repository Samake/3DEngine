package samake.game.controls;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import samake.engine.camera.Camera;
import samake.engine.camera.Camera.CAMERATYPE;
import samake.engine.core.Engine;
import samake.engine.entity.npc.Player;
import samake.engine.input.Input;
import samake.engine.rendering.Renderer.RENDERMODE;
import samake.game.map.Map;

public class Controls {
	
	private final float POS_STEP = 0.1f;
	private final float MOUSE_SENSITIVITY = 0.25f;
	
	private Vector3f moveVector = new Vector3f();
	private float speedValue = 1.0f;
	private float moveSpeed = 0.0f;
	
	private boolean jumpReady = true;
	
	public Controls() {
		
	}

	public void update(Map map, Player player) {
		Input input = Engine.instance.getInput();
		Camera camera = map.getCamera();
		
		if (input != null && camera != null) {
		    if (input.isKeyPressed(GLFW.GLFW_KEY_W)) {
		        moveVector.z = -1;
		    } else if (input.isKeyPressed(GLFW.GLFW_KEY_S)) {
		        moveVector.z = 1;
		    } else {
		    	moveVector.z = 0;
		    }
		    
		    if (input.isKeyPressed(GLFW.GLFW_KEY_A)) {
		        moveVector.x = -1;
		    } else if (input.isKeyPressed(GLFW.GLFW_KEY_D)) {
		        moveVector.x = 1;
		    } else {
		    	moveVector.x = 0;
		    }
		    
		    if (input.isKeyPressed(GLFW.GLFW_KEY_Z)) {
		        moveVector.y = -1;
		    } else if (input.isKeyPressed(GLFW.GLFW_KEY_X)) {
		        moveVector.y = 1;
		    } else {
		    	moveVector.y = 0;
		    }
			
		    moveSpeed = speedValue;
		    
		    if (input.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT)) {
		    	moveSpeed = speedValue * 8;
		    }
		    
		    if (input.isKeyPressed(GLFW.GLFW_KEY_LEFT_CONTROL)) {
		    	moveSpeed = speedValue / 8;
		    }
		    
		    updateMovement(input, camera, player);
		    
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
			
			if (input.isKeyPressed(GLFW.GLFW_KEY_SPACE)) {
				if (jumpReady) {
					player.jump(25.0f);
					jumpReady = !jumpReady;
				}
			} else {
				if (!jumpReady) {
					jumpReady = true;
				}
			}
		}
	}
	
	private void updateMovement(Input input, Camera camera, Player player) {
		CAMERATYPE camType = camera.getType();
		
		if (camType.equals(CAMERATYPE.CINEMATIC)) {
			camera.movePosition(moveVector.x * POS_STEP * moveSpeed, moveVector.y * POS_STEP * moveSpeed, moveVector.z * POS_STEP * moveSpeed);
	
			if (input.isRightMouseButtonPressed()) {
		        Vector2f rotVec = input.getCursorMoveVector();
		        
		        camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
		    }
		} else if (camType.equals(CAMERATYPE.FREECAM)) {
			camera.movePosition(moveVector.x * POS_STEP * moveSpeed, moveVector.y * POS_STEP * moveSpeed, moveVector.z * POS_STEP * moveSpeed);
			
			if (input.isRightMouseButtonPressed()) {
		        Vector2f rotVec = input.getCursorMoveVector();
		        
		        camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
		    }
		} else if (camType.equals(CAMERATYPE.FIRSTPERSON)) {
			if (player != null) {
		    	player.movePosition(moveVector.x * POS_STEP * moveSpeed, moveVector.z * POS_STEP * moveSpeed);
		    	
		    	if (input.isRightMouseButtonPressed()) {
			        Vector2f rotVec = input.getCursorMoveVector();
			        
			        player.moveRotation(rotVec.y * MOUSE_SENSITIVITY);
			        camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, 0, 0);
			    }
		    }
		} else if (camType.equals(CAMERATYPE.THIRDPERSON)) {
			if (player != null) {
		    	player.movePosition(moveVector.x * POS_STEP * moveSpeed, moveVector.z * POS_STEP * moveSpeed);
		    	
		    	if (input.isRightMouseButtonPressed()) {
			        Vector2f rotVec = input.getCursorMoveVector();
			        
			        player.moveRotation(rotVec.y * MOUSE_SENSITIVITY);
			        camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, 0, 0);
			    }
		    }
		}
	}
}
