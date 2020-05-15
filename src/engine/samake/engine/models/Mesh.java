package samake.engine.models;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL43;
import org.lwjgl.system.MemoryUtil;

import com.bulletphysics.collision.shapes.IndexedMesh;
import com.bulletphysics.collision.shapes.TriangleIndexVertexArray;
import com.bulletphysics.util.ObjectArrayList;

import samake.engine.material.MaterialWorld;
import samake.engine.utils.Utils;

public class Mesh {
	
	private int vao;
	private int[] vbos;
	private int indexBuffer;
	
	private float[] vertices;
	private float[] colors;
	private float[] textureCoords;
	private float[] normals;
	private int[] indices;
	
	private int position;
	
	private MaterialWorld material;
	private TriangleIndexVertexArray collissionMesh;
	private ObjectArrayList<javax.vecmath.Vector3f> hullArrayList;
	
	public Mesh(List<Float> vertices, List<Float> textureCoords, List<Float> normals, List<Float> colors, List<Integer> indices, boolean calculateCollisionMesh) {
		this.vertices = Utils.convertFloatListToArray(vertices);
		this.colors = Utils.convertFloatListToArray(colors);
		this.textureCoords = Utils.convertFloatListToArray(textureCoords);
		this.normals = Utils.convertFloatListToArray(normals);
		this.indices = Utils.convertIntegerListToArray(indices);
		
		generateMesh();
		
		if (calculateCollisionMesh) {
			generateIndexedMesh();
		}
	}
	
	public void changeScale(Vector3f scale) {
		
		float[] verticesNew = new float[vertices.length];
		int id = 0;
		int counter = 0;
		
		for (float value : vertices) {
			counter++;

			if (counter == 1) {
				verticesNew[id] = value * scale.x;
			}
			
			if (counter == 2) {
				verticesNew[id] = value * scale.y;
			}
			
			if (counter == 3) {
				verticesNew[id] = value * scale.z;
				counter = 0;
			}
			
			id++;
		}
		
		changeStaticAttribute(position, 0, verticesNew, 3);
	}

	private void generateMesh() {
		vao = GL43.glGenVertexArrays();
		GL43.glBindVertexArray(vao);
		
		indexBuffer = attachIndexBuffer(this.indices);
		
		position = addStaticAttribute(0, this.vertices, 3);
		int color = addStaticAttribute(1, this.colors, 3);
		int normal = addStaticAttribute(2, this.normals, 3);
		int uv = addStaticAttribute(3, this.textureCoords, 2);
		
		vbos = new int[] { position, color, uv, normal };
	}
	
	private void generateIndexedMesh() {
		int totalVerts = getNumVertices();
		ByteBuffer gVertices = ByteBuffer.allocateDirect(totalVerts * 3 * 4).order(ByteOrder.nativeOrder());
		ByteBuffer gIndices = ByteBuffer.allocateDirect(totalVerts * 4).order(ByteOrder.nativeOrder());
		setHullArrayList(new ObjectArrayList<javax.vecmath.Vector3f>());

		float[] vertices = getVertices();
		
		int counter = 0;
		for (float value : vertices) {
			counter++;

			javax.vecmath.Vector3f vertex = new javax.vecmath.Vector3f();
			
			if (counter == 1) {
				gVertices.putFloat(value);
				vertex.x = value;
			}
			
			if (counter == 2) {
				gVertices.putFloat(value);
				vertex.y = value;
			}
			
			if (counter == 3) {
				gVertices.putFloat(value);
				vertex.z = value;
				counter = 0;
			}
			
			hullArrayList.add(vertex);
		}
		
		int[] indices = getIndices();
		
		for (int value : indices) {
			 gIndices.putInt(value);
		}
		
		gVertices.flip();
		gIndices.flip();

		IndexedMesh indexedMesh = new IndexedMesh();
		indexedMesh.numVertices = totalVerts;
		indexedMesh.vertexBase = gVertices;
		indexedMesh.vertexStride = 3 * 4;
		indexedMesh.numTriangles = totalVerts / 3;
		indexedMesh.triangleIndexBase = gIndices;
		indexedMesh.triangleIndexStride = 3 * 4;

		TriangleIndexVertexArray vertArray = new TriangleIndexVertexArray();
		vertArray.addIndexedMesh(indexedMesh);
		
		setCollissionMesh(vertArray);
	}
	
	public float[] getVertices() {
		return vertices;
	}

	public float[] getColors() {
		return colors;
	}

	public float[] getTextureCoords() {
		return textureCoords;
	}

	public float[] getNormals() {
		return normals;
	}

	public int[] getIndices() {
		return indices;
	}
	
	private int addStaticAttribute(int index, float[] data, int dataSize) {
		int vbo = GL43.glGenBuffers();
		GL43.glBindBuffer(GL43.GL_ARRAY_BUFFER, vbo);
		
		FloatBuffer dataBuffer = MemoryUtil.memAllocFloat(data.length);
		dataBuffer.put(data).flip();
        
		GL43.glBufferData(GL43.GL_ARRAY_BUFFER, dataBuffer, GL43.GL_STATIC_DRAW);
		GL43.glVertexAttribPointer(index, dataSize, GL43.GL_FLOAT, false, 0, 0);
		
		return vbo;
	}
	
	private void changeStaticAttribute(int vbo, int index, float[] data, int dataSize) {
		GL43.glBindBuffer(GL43.GL_ARRAY_BUFFER, vbo);
		
		FloatBuffer dataBuffer = MemoryUtil.memAllocFloat(data.length);
		dataBuffer.put(data).flip();
        
		GL43.glBufferData(GL43.GL_ARRAY_BUFFER, dataBuffer, GL43.GL_STATIC_DRAW);
		GL43.glVertexAttribPointer(index, dataSize, GL43.GL_FLOAT, false, 0, 0);
	}
	
	private int attachIndexBuffer(int[] indices) {
		int vbo = GL43.glGenBuffers();
		
		IntBuffer indicesBuffer = MemoryUtil.memAllocInt(indices.length);
        indicesBuffer.put(indices).flip();
		GL43.glBindBuffer(GL43.GL_ELEMENT_ARRAY_BUFFER, vbo);
		GL43.glBufferData(GL43.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL43.GL_STATIC_DRAW);
		
		return vbo;
	}
	
	public int getVAO() {
		return vao;
	}
	
	public void setVAO(int vao) {
		this.vao = vao;
	}

	public int[] getVBOs() {
		return vbos;
	}

	public int getNumVertices() {
		return indices.length;
	}

	public int getIndexBuffer() {
		return indexBuffer;
	}
	
	public MaterialWorld getMaterial() {
		return material;
	}

	public void setMaterial(MaterialWorld material) {
		this.material = material;
	}

	public TriangleIndexVertexArray getCollissionMesh() {
		return collissionMesh;
	}

	public void setCollissionMesh(TriangleIndexVertexArray collissionMesh) {
		this.collissionMesh = collissionMesh;
	}

	public ObjectArrayList<javax.vecmath.Vector3f> getHullArrayList() {
		return hullArrayList;
	}

	public void setHullArrayList(ObjectArrayList<javax.vecmath.Vector3f> hullArrayList) {
		this.hullArrayList = hullArrayList;
	}

	public void destroy() {
		GL43.glBindBuffer(GL43.GL_ARRAY_BUFFER, 0);
		GL43.glDeleteVertexArrays(vao);
		
		for (int id : vbos) {
			GL43.glDeleteBuffers(id);
		}
		
		GL43.glBindBuffer(GL43.GL_ELEMENT_ARRAY_BUFFER, 0);
	}
}
