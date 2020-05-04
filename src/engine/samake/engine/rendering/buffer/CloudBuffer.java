package samake.engine.rendering.buffer;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL43;
 
public class CloudBuffer {
 
	private int width;
    private int height;
    private int bufferWith;
    private int bufferHeight;
 
    private int frontBuffer;
    private int frontTexture;

    private int backBuffer;
    private int backTexture;
 
    public CloudBuffer(int width, int height, double divisor) {
    	this.width = width;
    	this.height = height;
    	this.bufferWith = (int) ( width / divisor);
    	this.bufferHeight = (int) (height / divisor);
        
        initFrontBuffer();
        initBackBuffer();
    }
 
    public void bindFrontBuffer() {
        bindBuffer(frontBuffer, bufferWith, bufferHeight);
    }
    
    public void bindBackBuffer() {
        bindBuffer(backBuffer, bufferWith, bufferHeight);
    }
     
    public void unbind() {
        GL43.glBindFramebuffer(GL43.GL_FRAMEBUFFER, 0);
        GL43.glViewport(0, 0, width, height);
    }

    private void initFrontBuffer() {
        frontBuffer = createBuffer();
        frontTexture = createTextureAttachment(bufferWith, bufferHeight);

        unbind();
    }
        
    private void initBackBuffer() {
        backBuffer = createBuffer();
        backTexture = createTextureAttachment(bufferWith, bufferHeight);

        unbind();
    } 
     
    private void bindBuffer(int frameBuffer, int width, int height){
        GL43.glBindTexture(GL43.GL_TEXTURE_2D, 0);
        GL43.glBindFramebuffer(GL43.GL_FRAMEBUFFER, frameBuffer);
        GL43.glViewport(0, 0, width, height);
    }
    
    private int createBuffer() {
        int frameBuffer = GL43.glGenFramebuffers();
        GL43.glBindFramebuffer(GL43.GL_FRAMEBUFFER, frameBuffer);
        GL43.glDrawBuffer(GL43.GL_COLOR_ATTACHMENT0);

        return frameBuffer;
    }  
 
    private int createTextureAttachment( int width, int height) {
        int texture = GL43.glGenTextures();
        GL43.glBindTexture(GL43.GL_TEXTURE_2D, texture);
        GL43.glTexImage2D(GL43.GL_TEXTURE_2D, 0, GL43.GL_RGBA, width, height, 0, GL43.GL_RGBA, GL43.GL_UNSIGNED_BYTE, (ByteBuffer) null);
        GL43.glTexParameteri(GL43.GL_TEXTURE_2D, GL43.GL_TEXTURE_MAG_FILTER, GL43.GL_LINEAR);
        GL43.glTexParameteri(GL43.GL_TEXTURE_2D, GL43.GL_TEXTURE_MIN_FILTER, GL43.GL_LINEAR);
        GL43.glFramebufferTexture(GL43.GL_FRAMEBUFFER, GL43.GL_COLOR_ATTACHMENT0, texture, 0);
       
        return texture;
    }
    
    public int getFrontTexture() {
        return frontTexture;
    }
        
    public int getBackTexture() {
        return backTexture;
    }
    
    public void destroy() {
        GL43.glDeleteFramebuffers(frontBuffer);
        GL43.glDeleteTextures(frontTexture);
        GL43.glDeleteFramebuffers(backBuffer);
        GL43.glDeleteTextures(backTexture);
    }
}
