
package samake.engine.rendering.buffer;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL43;

import samake.engine.config.PropertiesHandler;

public class RenderTarget {

	public static final int NONE = 0;
	public static final int DEPTH_TEXTURE = 1;
	public static final int DEPTH_RENDER_BUFFER = 2;

	private int width;
	private int height;

	private int frameBuffer;
	private int depthBuffer;
	private int mainBuffer;
	private int brightBuffer;
	
	private int mainTexture;
	private int depthTexture;
	
	private boolean multiSampleAndMultiTarget;

	public RenderTarget(int width, int height, int downSampleRate, int depthBufferType) {
		this.width = (int) (width / downSampleRate);
		this.height = (int) (height / downSampleRate);
		initBuffer(depthBufferType);
		
		this.multiSampleAndMultiTarget = false;
	}
	
	public RenderTarget(int width, int height, int downSampleRate) {
		this.width = (int) (width / downSampleRate);
		this.height = (int) (height / downSampleRate);
		this.multiSampleAndMultiTarget = true;
		
		initBuffer(DEPTH_RENDER_BUFFER);
	}
	
	private void initBuffer(int type) {
		
		createFrameBuffer();
		
		if (this.multiSampleAndMultiTarget) {
			mainBuffer = createMultiSampleColorAttachment(GL43.GL_COLOR_ATTACHMENT0);
			brightBuffer = createMultiSampleColorAttachment(GL43.GL_COLOR_ATTACHMENT1);
		} else {
			createTextureAttachment();
		}
		
		if (type == DEPTH_RENDER_BUFFER) {
			createDepthBufferAttachment();
		} else if (type == DEPTH_TEXTURE) {
			createDepthTextureAttachment();
		}
		
		unbindBuffer();
	}
	
	private void createFrameBuffer() {
		frameBuffer = GL43.glGenFramebuffers();
		GL43.glBindFramebuffer(GL43.GL_FRAMEBUFFER, frameBuffer);

		determineDrawBuffers();
	}
	
	private void createTextureAttachment() {
		mainTexture = GL43.glGenTextures();
		
		GL43.glBindTexture(GL43.GL_TEXTURE_2D, mainTexture);
		GL43.glTexImage2D(GL43.GL_TEXTURE_2D, 0, GL43.GL_RGBA8, width, height, 0, GL43.GL_RGBA, GL43.GL_UNSIGNED_BYTE, (ByteBuffer) null);
		
		GL43.glTexParameteri(GL43.GL_TEXTURE_2D, GL43.GL_TEXTURE_MAG_FILTER, GL43.GL_LINEAR);
		GL43.glTexParameteri(GL43.GL_TEXTURE_2D, GL43.GL_TEXTURE_MIN_FILTER, GL43.GL_LINEAR);
		GL43.glTexParameteri(GL43.GL_TEXTURE_2D, GL43.GL_TEXTURE_WRAP_S, GL43.GL_CLAMP_TO_EDGE);
		GL43.glTexParameteri(GL43.GL_TEXTURE_2D, GL43.GL_TEXTURE_WRAP_T, GL43.GL_CLAMP_TO_EDGE);
		
		GL43.glFramebufferTexture2D(GL43.GL_FRAMEBUFFER, GL43.GL_COLOR_ATTACHMENT0, GL43.GL_TEXTURE_2D, mainTexture, 0);
	}

	private void createDepthTextureAttachment() {
		depthTexture = GL43.glGenTextures();
		
		GL43.glBindTexture(GL43.GL_TEXTURE_2D, depthTexture);
		GL43.glTexImage2D(GL43.GL_TEXTURE_2D, 0, GL43.GL_DEPTH_COMPONENT24, width, height, 0, GL43.GL_DEPTH_COMPONENT, GL43.GL_FLOAT, (ByteBuffer) null);
		
		GL43.glTexParameteri(GL43.GL_TEXTURE_2D, GL43.GL_TEXTURE_MAG_FILTER, GL43.GL_LINEAR);
		GL43.glTexParameteri(GL43.GL_TEXTURE_2D, GL43.GL_TEXTURE_MIN_FILTER, GL43.GL_LINEAR);
		
		GL43.glFramebufferTexture2D(GL43.GL_FRAMEBUFFER, GL43.GL_DEPTH_ATTACHMENT, GL43.GL_TEXTURE_2D, depthTexture, 0);
	}
	
	private int createMultiSampleColorAttachment(int attachment) {
		int colorBuffer = GL43.glGenRenderbuffers();
		
		GL43.glBindRenderbuffer(GL43.GL_RENDERBUFFER, colorBuffer);
		GL43.glRenderbufferStorageMultisample(GL43.GL_RENDERBUFFER, PropertiesHandler.getAntialiasing(), GL43.GL_RGBA8, width, height);
		
		GL43.glFramebufferRenderbuffer(GL43.GL_FRAMEBUFFER, attachment, GL43.GL_RENDERBUFFER, colorBuffer);
	
		return colorBuffer;
	}

	private void createDepthBufferAttachment() {
		depthBuffer = GL43.glGenRenderbuffers();
		
		GL43.glBindRenderbuffer(GL43.GL_RENDERBUFFER, depthBuffer);
		
		if (!multiSampleAndMultiTarget) {
			GL43.glRenderbufferStorage(GL43.GL_RENDERBUFFER, GL43.GL_DEPTH_COMPONENT24, width, height);
		} else {
			GL43.glRenderbufferStorageMultisample(GL43.GL_RENDERBUFFER, PropertiesHandler.getAntialiasing(), GL43.GL_DEPTH_COMPONENT24, width, height);
		}
		
		GL43.glFramebufferRenderbuffer(GL43.GL_FRAMEBUFFER, GL43.GL_DEPTH_ATTACHMENT, GL43.GL_RENDERBUFFER, depthBuffer);
	}
	

	private void determineDrawBuffers() {
		IntBuffer drawBuffers = BufferUtils.createIntBuffer(3);
		drawBuffers.put(GL43.GL_COLOR_ATTACHMENT0);

		if (this.multiSampleAndMultiTarget) {
			drawBuffers.put(GL43.GL_COLOR_ATTACHMENT1);
		}
		
		drawBuffers.flip();
		GL43.glDrawBuffers(drawBuffers);
	}
	
	public void drawBuffer(int readBuffer, RenderTarget outputFBO) {
		GL43.glBindFramebuffer(GL43.GL_DRAW_FRAMEBUFFER, outputFBO.frameBuffer);
		GL43.glBindFramebuffer(GL43.GL_READ_FRAMEBUFFER, this.frameBuffer);
		
		GL43.glReadBuffer(readBuffer);
		
		GL43.glBlitFramebuffer(0, 0, width, height, 0, 0, outputFBO.width, outputFBO.height, GL43.GL_COLOR_BUFFER_BIT | GL43.GL_DEPTH_BUFFER_BIT, GL43.GL_NEAREST);
		
		this.unbindBuffer();
	}
	
	
	public void drawScene() {
		GL43.glBindFramebuffer(GL43.GL_DRAW_FRAMEBUFFER, 0);
		GL43.glBindFramebuffer(GL43.GL_READ_FRAMEBUFFER, this.frameBuffer);
		GL43.glDrawBuffer(GL43.GL_BACK);
		GL43.glBlitFramebuffer(0, 0, width, height, 0, 0, PropertiesHandler.getWindowWidth(), PropertiesHandler.getWindowHeight(), GL43.GL_COLOR_BUFFER_BIT, GL43.GL_NEAREST);
		
		this.unbindBuffer();
	}
	
	public void bindBuffer() {
		GL43.glBindFramebuffer(GL43.GL_DRAW_FRAMEBUFFER, frameBuffer);
		GL43.glViewport(0, 0, width, height);
	}

	public void unbindBuffer() {
		GL43.glBindFramebuffer(GL43.GL_FRAMEBUFFER, 0);
		GL43.glViewport(0, 0, PropertiesHandler.getWindowWidth(), PropertiesHandler.getWindowHeight());
	}

	public void bindToRead() {
		GL43.glBindTexture(GL43.GL_TEXTURE_2D, 0);
		GL43.glBindFramebuffer(GL43.GL_READ_FRAMEBUFFER, frameBuffer);
		GL43.glReadBuffer(GL43.GL_COLOR_ATTACHMENT0);
	}

	public int getMainTexture() {
		return mainTexture;
	}


	public int getDepthTexture() {
		return depthTexture;
	}
	
	public void destroy() {
		GL43.glDeleteFramebuffers(frameBuffer);
		GL43.glDeleteTextures(mainTexture);
		GL43.glDeleteTextures(depthTexture);
		GL43.glDeleteRenderbuffers(depthBuffer);
		GL43.glDeleteRenderbuffers(mainBuffer);
		GL43.glDeleteRenderbuffers(brightBuffer);
	}
}
