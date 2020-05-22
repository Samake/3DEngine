package samake.engine.models;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL43;

import samake.engine.perlin.PerlinGenerator;
import samake.engine.rendering.screenshot.TexturePrint;
import samake.engine.utils.Utils;

public class MeshBuilder {
	
	public static Mesh generatePlane(float xOffset, float yOffset, float zOffset, int rows, float size, boolean useHeightGenerator, boolean colorGenerator, boolean calculateCollisionMesh) {
		int vertexesPerLine = (rows + 1);
		float[][] heights = new float[vertexesPerLine][vertexesPerLine];
		float[][] rawHeights = new float[vertexesPerLine][vertexesPerLine];
		
		List<Float> vertices = new ArrayList<Float>();
	    List<Float> textureCoords = new ArrayList<Float>();
	    List<Float> normals = new ArrayList<Float>();
	    List<Float> colors = new ArrayList<Float>();
	    List<Integer> indices = new ArrayList<Integer>();
		
		for (int i = 0; i < vertexesPerLine; i++) {
			for (int j = 0; j < vertexesPerLine; j++) {
				
				float vertexHeight = yOffset;
				
				float xValue = xOffset + ((float) j / ((float) vertexesPerLine - 1) * size);
				float zValue = zOffset + ((float) i / ((float) vertexesPerLine - 1) * size);
				float yValue = 0;
				
				if (useHeightGenerator) {
					yValue += PerlinGenerator.getTurbolenceNoise(xValue, yValue, zValue, 0.8f); // base
					yValue += PerlinGenerator.getFBMNoise(xValue, yValue, zValue, 0.5f); // rocks
					yValue += PerlinGenerator.getRidgeNoise(xValue, yValue, zValue, 0.5f); // hills
					yValue *= PerlinGenerator.getPerlinNoise(xValue, yValue, zValue, 2.0f); // flatten
				}
				
				heights[j][i] = vertexHeight + yValue * PerlinGenerator.getHeightModifier();
				rawHeights[j][i] = yValue;
				
				vertices.add(xValue);
				vertices.add(heights[j][i]);
				vertices.add(zValue);
				
				Vector3f color = new Vector3f(0.5f, 0.5f, 0.5f);
				
				if (colorGenerator) {
					calculateColor(color, yValue + Utils.getRandomValue(-500.0f, 500.0f, 10000.0f));
				}
				
				colors.add(color.x);
				colors.add(color.y);
				colors.add(color.z);
	
				textureCoords.add((float) j / ((float) vertexesPerLine - 1));
				textureCoords.add((float) i / ((float) vertexesPerLine - 1));
			}
		}
		
		if (useHeightGenerator) {
			TexturePrint.saveTerrainHeightMap(vertexesPerLine - 1, vertexesPerLine - 1, rawHeights);
		}
		
		
		for (int i = 0; i < vertexesPerLine; i++) {
			for (int j = 0; j < vertexesPerLine; j++) {
				Vector3f normal = calculateNormal(heights, j, i);
				normals.add(normal.x);
				normals.add(normal.y);
				normals.add(normal.z);
			}
		}
		
		for(int gz = 0; gz < vertexesPerLine - 1; gz++){
			for (int gx = 0; gx < vertexesPerLine - 1; gx++){
				int topLeft = (gz * vertexesPerLine) + gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz + 1) * vertexesPerLine) + gx;
				int bottomRight = bottomLeft + 1;
				
				indices.add(topLeft);
				indices.add(bottomLeft);
				indices.add(topRight);
				indices.add(topRight);
				indices.add(bottomLeft);
				indices.add(bottomRight);
			}
		}

		return new Mesh(vertices, textureCoords, normals, colors, indices, calculateCollisionMesh);
	}
	
	private static void calculateColor(Vector3f color, float value) {
		if (value > -0.1f && value < 0.1f) {
			MeshColors.getSandColors(color);
		} else if (value > 0.1f && value < 0.5f) {
			MeshColors.getGrassColors(color);
		} else {
			MeshColors.getStoneColors(color);
		}
	}

	private static Vector3f calculateNormal(float[][] heights, int x, int z) {
		float heightL;
		float heightR;
		float heightD;
		float heightU;
		
		int currentX = x;
		int nextX = x + 1;
		int lastX = x - 1;
		int currentZ = z;
		int nextZ = z + 1;
		int lastZ = z - 1;
		
		if (nextX > heights.length - 1) { nextX = heights.length - 1; }
		if (lastX < 0) { lastX = 0; }
		if (nextZ > heights[0].length - 1) { nextZ = heights[0].length - 1; }
		if (lastZ < 0) { lastZ = 0; }
		
		heightL = heights[lastX][currentZ];
		heightR = heights[nextX][currentZ];
		heightD = heights[currentX][lastZ];
		heightU = heights[currentX][nextZ];
		
		Vector3f normal = new Vector3f(heightL - heightR, 2.0f, heightD - heightU);
		normal.normalize(normal);
		
		return normal;
	}

	public static BasicMesh loadToVAO(float[] positions, int dimensions) {
		int vao = createVAO();
		int vbo = storeDataInAttributeList(0, dimensions, positions);
		unbindVAO();
		
		return new BasicMesh(vao, vbo, positions.length / dimensions);
	}
	
	private static int createVAO(){
		int vao = GL43.glGenVertexArrays();
		GL43.glBindVertexArray(vao);
		return vao;
	}
	
	private static int storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
		int vbo = GL43.glGenBuffers();
		
		GL15.glBindBuffer(GL43.GL_ARRAY_BUFFER, vbo);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL43.glBufferData(GL43.GL_ARRAY_BUFFER, buffer, GL43.GL_STATIC_DRAW);
		GL43.glVertexAttribPointer(attributeNumber, coordinateSize, GL43.GL_FLOAT, false, 0, 0);
		GL43.glBindBuffer(GL43.GL_ARRAY_BUFFER, 0);
		
		return vbo;
	}
	
	private static FloatBuffer storeDataInFloatBuffer(float[] data){
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		
		return buffer;
	}
	
	private static void unbindVAO(){
		GL43.glBindVertexArray(0);
	}
}
