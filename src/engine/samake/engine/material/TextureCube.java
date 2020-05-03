package samake.engine.material;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL43;
import org.lwjgl.stb.STBImage;

import samake.engine.logging.Console;
import samake.engine.logging.Console.LOGTYPE;

public class TextureCube extends Texture {
	
	private static final String TEXTURE_FOLDER = "/resources/textures/";
	
	public TextureCube(String filename) {
		
		String[] files = {
				filename + "_xneg.png",
				filename + "_xpos.png",
				filename + "_yneg.png",
				filename + "_xpos.png",
				filename + "_zneg.png",
				filename + "_zpos.png"
		};
		
		id = GL43.glGenTextures();
		GL43.glBindTexture(GL43.GL_TEXTURE_CUBE_MAP, id);
		
		for (int i = 0; i < files.length; i++) {
			String filePath = new String(System.getProperty("user.dir") + TEXTURE_FOLDER + files[i]).replace("/", "\\");
			File textureFile = new File(filePath);
			
			if (textureFile.exists()) {
				try {
					IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
					IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);
					IntBuffer channelsBuffer = BufferUtils.createIntBuffer(1);
					
					ByteBuffer decodedImage = STBImage.stbi_load(textureFile.getAbsolutePath(), widthBuffer, heightBuffer, channelsBuffer, 0);
					
					if (decodedImage != null) {
						width = widthBuffer.get();
						height = heightBuffer.get();
						channels = channelsBuffer.get();

						if (channels > 3) {
							GL43.glTexImage2D(GL43.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL43.GL_RGBA, width, height, 0, GL43.GL_RGBA, GL43.GL_UNSIGNED_BYTE, decodedImage);
						} else {
							GL43.glTexImage2D(GL43.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL43.GL_RGB, width, height, 0, GL43.GL_RGB, GL43.GL_UNSIGNED_BYTE, decodedImage);
						}
						
						STBImage.stbi_image_free(decodedImage);
					} else {
						Console.print("TexturCube " + filePath + " could not be decoded!" + System.lineSeparator() + STBImage.stbi_failure_reason() + System.lineSeparator(), LOGTYPE.ERROR, true);
					}
				} catch (Exception ex) {
					Console.print(ex.toString(), LOGTYPE.ERROR, true);
				}
			} else {
				Console.print("TexturCube not found: " + filePath + System.lineSeparator(), LOGTYPE.ERROR, true);
			}
		}
		
		GL43.glTexParameteri(GL43.GL_TEXTURE_CUBE_MAP, GL43.GL_TEXTURE_MIN_FILTER, GL43.GL_LINEAR);
		GL43.glTexParameteri(GL43.GL_TEXTURE_CUBE_MAP, GL43.GL_TEXTURE_MAG_FILTER, GL43.GL_LINEAR);
		GL43.glTexParameteri(GL43.GL_TEXTURE_CUBE_MAP, GL43.GL_TEXTURE_WRAP_S, GL43.GL_CLAMP_TO_EDGE);
		GL43.glTexParameteri(GL43.GL_TEXTURE_CUBE_MAP, GL43.GL_TEXTURE_WRAP_T, GL43.GL_CLAMP_TO_EDGE);
	}
	
	public void bind() {
		GL43.glBindTexture(GL43.GL_TEXTURE_CUBE_MAP, id);
	}
	
	public void unbind() {
		GL43.glBindTexture(GL43.GL_TEXTURE_CUBE_MAP, 0);
	}
	
	public void destroy() {
		unbind();
		GL43.glDeleteTextures(id);
	}
}
