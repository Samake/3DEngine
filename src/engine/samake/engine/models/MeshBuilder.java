package samake.engine.models;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

import samake.engine.perlin.PerlinGenerator;
import samake.engine.rendering.screenshot.TexturePrint;

public class MeshBuilder {
	
	public static Mesh generatePlane(float xOffset, float yOffset, float zOffset, int rows, float size, boolean useHeightGenerator, boolean colorGenerator) {
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
					vertexHeight -= PerlinGenerator.getHeightModifier() / 6;
					//yValue = (	PerlinGenerator.getRidgeNoise(xValue, yValue, zValue, 1.0f));
					//yValue = (	PerlinGenerator.getFBMNoise(xValue, yValue, zValue, 1.0f));
					yValue = (	PerlinGenerator.getTurbolenceNoise(xValue, yValue, zValue, 0.25f) +
								PerlinGenerator.getRidgeNoise(xValue, yValue, zValue, 0.75f) -
								PerlinGenerator.getFBMNoise(xValue, yValue, zValue, 0.5f) + 
								PerlinGenerator.getPerlinNoise(xValue, yValue, zValue, 1.0f)
							);
					
					yValue *= 0.5f;
					
					if (yValue < 0) {
						yValue = 0;
					}
					
					if (yValue > 1) {
						yValue = 1;
					}
				}
				
				heights[j][i] = vertexHeight + yValue * PerlinGenerator.getHeightModifier();
				rawHeights[j][i] = yValue;
				
				vertices.add(xValue);
				vertices.add(heights[j][i]);
				vertices.add(zValue);
				
				Vector3f color = new Vector3f(0.5f, 0.5f, 0.5f);
				
				if (colorGenerator) {
					calculateColor(color, yValue);
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

		return new Mesh(vertices, textureCoords, normals, colors, indices);
	}
	
	private static void calculateColor(Vector3f color, float value) {
		if (value > 0.05f && value < 0.25f) {
			MeshColors.getSandColors(color);
		} else if (value > 0.25f && value < 0.65f) {
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
}
