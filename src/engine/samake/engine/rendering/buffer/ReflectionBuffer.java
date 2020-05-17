package samake.engine.rendering.buffer;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL43;
 
public class ReflectionBuffer {
 
	private int width;
    private int height;
    private int bufferWith;
    private int bufferHeight;
 
    private int reflectionBuffer;
    private int reflectionTexture;

    private int refractionBuffer;
    private int refractionTexture;
    
    private int depthBuffer;
    private int depthTexture;
 
    public ReflectionBuffer(int width, int height, int dividor) {
    	this.width = width;
    	this.height = height;
    	this.bufferWith = (int) width / dividor;
    	this.bufferHeight = (int) height / dividor;
        
        initReflectionBuffer();
        initRefractionBuffer();
    }
 
    public void bindReflectionBuffer() {
        bindBuffer(reflectionBuffer, bufferWith, bufferHeight);
    }
    
    public void bindRefractionBuffer() {
        bindBuffer(refractionBuffer, bufferWith, bufferHeight);
    }
     
    public void unbind() {
        GL43.glBindFramebuffer(GL43.GL_FRAMEBUFFER, 0);
        GL43.glViewport(0, 0, width, height);
    }

    private void initReflectionBuffer() {
        reflectionBuffer = createBuffer();
        reflectionTexture = createTextureAttachment(bufferWith, bufferHeight);
        depthBuffer = createDepthBufferAttachment(bufferWith, bufferHeight);
        
        unbind();
    }
        
    private void initRefractionBuffer() {
        refractionBuffer = createBuffer();
        refractionTexture = createTextureAttachment(bufferWith, bufferHeight);
        depthTexture = createDepthTextureAttachment(bufferWith, bufferHeight);
       
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
       
    private int createDepthTextureAttachment(int width, int height){
        int texture = GL43.glGenTextures();
        GL43.glBindTexture(GL43.GL_TEXTURE_2D, texture);
        GL43.glTexImage2D(GL43.GL_TEXTURE_2D, 0, GL43.GL_DEPTH_COMPONENT32, width, height, 0, GL43.GL_DEPTH_COMPONENT, GL43.GL_FLOAT, (ByteBuffer) null);
        GL43.glTexParameteri(GL43.GL_TEXTURE_2D, GL43.GL_TEXTURE_MAG_FILTER, GL43.GL_LINEAR);
        GL43.glTexParameteri(GL43.GL_TEXTURE_2D, GL43.GL_TEXTURE_MIN_FILTER, GL43.GL_LINEAR);
        GL43.glFramebufferTexture(GL43.GL_FRAMEBUFFER, GL43.GL_DEPTH_ATTACHMENT, texture, 0);
        
        return texture;
    }  
 
    private int createDepthBufferAttachment(int width, int height) {
        int depthBuffer = GL43.glGenRenderbuffers();
        GL43.glBindRenderbuffer(GL43.GL_RENDERBUFFER, depthBuffer);
        GL43.glRenderbufferStorage(GL43.GL_RENDERBUFFER, GL43.GL_DEPTH_COMPONENT, width, height);
        GL43.glFramebufferRenderbuffer(GL43.GL_FRAMEBUFFER, GL43.GL_DEPTH_ATTACHMENT, GL43.GL_RENDERBUFFER, depthBuffer);
        
        return depthBuffer;
    }
    
    public int getReflectionTexture() {
        return reflectionTexture;
    }
        
    public int getRefractionTexture() {
        return refractionTexture;
    }
     
    public int getDepthTexture() {
        return depthTexture;
    }
    
    public void destroy() {
        GL43.glDeleteFramebuffers(reflectionBuffer);
        GL43.glDeleteTextures(reflectionTexture);
        GL43.glDeleteRenderbuffers(depthBuffer);
        GL43.glDeleteFramebuffers(refractionBuffer);
        GL43.glDeleteTextures(refractionTexture);
        GL43.glDeleteTextures(depthTexture);
    }
}
