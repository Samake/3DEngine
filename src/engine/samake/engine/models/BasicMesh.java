package samake.engine.models;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL43;

public class BasicMesh {
	
	private int vao;
	private int vbo;
	private int vertexCount;
	
	public BasicMesh(int vao, int vbo, int vertexCount){
		this.vao = vao;
		this.vbo = vbo;
		this.vertexCount = vertexCount;
	}
	
	public int getVAO() {
		return vao;
	}
	
	public int getVBO() {
		return vbo;
	}

	public int getVertexCount() {
		return vertexCount;
	}

	public void destroy() {
		GL43.glDeleteVertexArrays(vao);
		GL15.glDeleteBuffers(vbo);
	}
}
