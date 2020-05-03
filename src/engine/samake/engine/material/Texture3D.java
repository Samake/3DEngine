package samake.engine.material;

import org.lwjgl.opengl.GL43;

public class Texture3D extends Texture {
	
	public Texture3D() {
		id = GL43.glGenTextures();
		
		GL43.glBindTexture(GL43.GL_TEXTURE_3D, id);
	}
	
	public Texture3D(int id, int width, int height, int channels) {
		this.id = id;
		this.width = width;
		this.height = height;
		this.channels = channels;
		
		bind();
	}

	public void bind() {
		GL43.glBindTexture(GL43.GL_TEXTURE_3D, id);
	}
	
	public void unbind() {
		GL43.glBindTexture(GL43.GL_TEXTURE_3D, 0);
	}
	
	public void destroy() {
		unbind();
		GL43.glDeleteTextures(id);
	}
}
