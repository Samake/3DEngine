package samake.engine.material;

public class Texture {
	
	protected int id;
	protected int width;
	protected int height;
	protected int channels;
	
	public int getID() {
		return id;
	}
	
	public void setID(int id) {
		this.id = id;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public int getChannels() {
		return channels;
	}
	
	public void setChannels(int channels) {
		this.channels = channels;
	}
}
