package samake.engine.resources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;
import org.json.JSONException;
import org.json.JSONObject;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIColor4D;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMaterial;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIString;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.assimp.Assimp;
import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL43;
import org.lwjgl.stb.STBImage;

import samake.engine.config.Configuration;
import samake.engine.config.PropertiesHandler;
import samake.engine.logging.Console;
import samake.engine.logging.Console.LOGTYPE;
import samake.engine.material.MaterialWorld;
import samake.engine.material.Texture2D;
import samake.engine.material.Texture3D;
import samake.engine.models.Mesh;
import samake.engine.models.Model;

public class ResourceLoader {

	public static JSONObject parseJSONFile(String filename) throws JSONException, IOException {
        String content = new String(Files.readAllBytes(Paths.get(filename)));
        return new JSONObject(content);
    }
	
	public static String loadShader(String filename) {
        StringBuilder shaderSource = new StringBuilder();
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            
            while((line = reader.readLine())!=null){
                shaderSource.append(line).append("//\n");
            }
            
            reader.close();
            
        } catch (Exception e){
            e.printStackTrace();
            System.exit(-1);
        }
        
        return shaderSource.toString();
	}
	
	public static Texture2D loadTexture(String fileName, boolean mipmaps) {
		if (!fileName.isEmpty()) {
			if (ResourcePool.getTexture(fileName) == null) {
				String filePath = new String(System.getProperty("user.dir") + "\\" + Configuration.TEXTURES + fileName).replace("/", "\\");
				
				File textureFile = new File(filePath);
				
				int id;
				int width;
				int height;
				int channels;
				
				Console.print("Loading texture: " + filePath, LOGTYPE.OUTPUT, true);
				
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
	
							id = GL43.glGenTextures();
							GL43.glBindTexture(GL43.GL_TEXTURE_2D, id);
							
							if (channels > 3) {
								GL43.glTexImage2D(GL43.GL_TEXTURE_2D, 0, GL43.GL_RGBA, width, height, 0, GL43.GL_RGBA, GL43.GL_UNSIGNED_BYTE, decodedImage);
							} else {
								GL43.glTexImage2D(GL43.GL_TEXTURE_2D, 0, GL43.GL_RGB, width, height, 0, GL43.GL_RGB, GL43.GL_UNSIGNED_BYTE, decodedImage);
							}
							
							GL43.glTexParameterf(GL43.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, 0.0f);
							
							if (GL.getCapabilities().GL_EXT_texture_filter_anisotropic) {
								float amount = Math.min(PropertiesHandler.getAnisotropicFiltering(), GL43.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT));
								
								GL43.glTexParameterf(GL43.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT, amount);
							} else {
								System.out.println("WARNING: Anisotropic filtering not supported!");
							}
							
							STBImage.stbi_image_free(decodedImage);
							
							Texture2D texture = new Texture2D(id, width, height, channels);
							
							if (mipmaps) {
								texture.trilinearFilter();
							} else {
								texture.bilinearFilter();
							}
							
							ResourcePool.addTexture(fileName, texture);
							
							return texture;
						} else {
							Console.print("Texture " + fileName + " could not be decoded!" + System.lineSeparator(), LOGTYPE.ERROR, true);
						}
					} catch (Exception ex) {
						Console.print(ex.toString(), LOGTYPE.ERROR, true);
					}
				} else {
					Console.print("Texture not found: " + filePath, LOGTYPE.ERROR, true);
				}
			} else {
				return ResourcePool.getTexture(fileName);
			}
		}
		
		return null;
	}
	
	public static Texture3D loadTexture3D(String fileName) {
		if (!fileName.isEmpty()) {
			String filePath = new String(System.getProperty("user.dir") + "\\" + Configuration.TEXTURES + fileName).replace("/", "\\");
			
			File textureFile = new File(filePath);
			
			int id;
			int width;
			int height;
			int channels;
			
			Console.print("Loading texture: " + filePath, LOGTYPE.OUTPUT, true);
			
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

						id = GL43.glGenTextures();
						GL43.glBindTexture(GL43.GL_TEXTURE_3D, id);
						
						GL43.glTexParameterf(GL43.GL_TEXTURE_3D, GL14.GL_TEXTURE_LOD_BIAS, 0.0f);
						
						GL43.glTexParameteri(GL43.GL_TEXTURE_3D, GL43.GL_TEXTURE_MIN_FILTER, GL43.GL_LINEAR);
						GL43.glTexParameteri(GL43.GL_TEXTURE_3D, GL43.GL_TEXTURE_MAG_FILTER, GL43.GL_LINEAR);
						GL43.glTexParameteri(GL43.GL_TEXTURE_3D, GL43.GL_TEXTURE_WRAP_S, GL43.GL_REPEAT);
						GL43.glTexParameteri(GL43.GL_TEXTURE_3D, GL43.GL_TEXTURE_WRAP_T, GL43.GL_REPEAT);
						GL43.glTexParameteri(GL43.GL_TEXTURE_3D, GL43.GL_TEXTURE_WRAP_R, GL43.GL_REPEAT);

						if (channels > 3) {
							GL43.glTexImage3D(GL43.GL_TEXTURE_3D, 0, GL43.GL_RGBA, width, height, 1, 0, GL43.GL_RGBA, GL43.GL_UNSIGNED_BYTE, decodedImage);
						} else {
							GL43.glTexImage3D(GL43.GL_TEXTURE_3D, 0, GL43.GL_RGB, width, height, 1, 0, GL43.GL_RGB, GL43.GL_UNSIGNED_BYTE, decodedImage);
						}
						
						STBImage.stbi_image_free(decodedImage);
						
						Texture3D texture = new Texture3D(id, width, height, channels);
						texture.trilinearFilter();
						
						return texture;
					} else {
						Console.print("Texture " + fileName + " could not be decoded!" + System.lineSeparator(), LOGTYPE.ERROR, true);
					}
				} catch (Exception ex) {
					Console.print(ex.toString(), LOGTYPE.ERROR, true);
				}
			} else {
				Console.print("Texture not found: " + filePath, LOGTYPE.ERROR, true);
			}
		}
		
		return null;
	}
	
	public static Model load3DModel(String fileName) {
		if (!fileName.isEmpty()) {	
			String filePath = new String(System.getProperty("user.dir") + "\\" + Configuration.MODELS + fileName).replace("/", "\\");
	
			AIScene aiScene = Assimp.aiImportFile(filePath, Assimp.aiProcess_JoinIdenticalVertices | Assimp.aiProcess_Triangulate | Assimp.aiProcess_FixInfacingNormals);
			
			if (aiScene == null) {
				Console.print("Failed to import model: " + filePath + System.lineSeparator(), LOGTYPE.ERROR, true);
				return null;
			}
			
			int numMaterials = aiScene.mNumMaterials();
			PointerBuffer aiMaterials = aiScene.mMaterials();
			List<MaterialWorld> materials = new ArrayList<MaterialWorld>();
			
			for (int i = 0; i < numMaterials; i++) {
			    AIMaterial aiMaterial = AIMaterial.create(aiMaterials.get(i));
			    processMaterial(aiMaterial, materials);
			}
			
			int numMeshes = aiScene.mNumMeshes();
			PointerBuffer aiMeshes = aiScene.mMeshes();
			Mesh[] meshes = new Mesh[numMeshes];
			
			for (int i = 0; i < numMeshes; i++) {
			    AIMesh aiMesh = AIMesh.create(aiMeshes.get(i));
			    Mesh mesh = processMesh(fileName + "_" + i, aiMesh, materials);
			    meshes[i] = mesh;
			}
			
			aiScene.free();
			
			Model model = new Model();
			
			for (Mesh mesh : meshes) {
				model.addMesh(mesh);
			}
			
			ResourcePool.addModel(fileName, model);
			
			return model;
		}
		
		return null;
	}
	
	private static Mesh processMesh(String fileName, AIMesh aiMesh, List<MaterialWorld> materials) {
		List<Float> vertices = new ArrayList<Float>();
        List<Float> textureCoords = new ArrayList<Float>();
        List<Float> normals = new ArrayList<Float>();
        List<Float> colors = new ArrayList<Float>();
        List<Integer> indices = new ArrayList<Integer>();

        int materialIdx = aiMesh.mMaterialIndex();
        
        MaterialWorld material = new MaterialWorld();
        
        if (materialIdx >= 0 && materialIdx < materials.size()) {
        	material = materials.get(materialIdx);
        }
        
        processVertices(aiMesh, vertices);
        processColors(material, colors);
        processNormals(aiMesh, normals);
        processTextCoords(aiMesh, textureCoords);
        processIndices(aiMesh, indices);
        
        Mesh mesh = new Mesh(vertices, textureCoords, normals, colors, indices);
        mesh.setMaterial(material);

        return mesh;
	}

	private static void processMaterial(AIMaterial aiMaterial, List<MaterialWorld> materials) {
    	MaterialWorld material = new MaterialWorld();
    	AIColor4D color = AIColor4D.create();

    	/** Load diffuse texture */
    	AIString path = AIString.calloc();
	    Assimp.aiGetMaterialTexture(aiMaterial, Assimp.aiTextureType_DIFFUSE, 0, path, (IntBuffer) null, null, null, null, null, null);
	    String textureName = path.dataString().trim();
	    
	    /** Load diffuse color */
	    Vector3f diffuse = new Vector3f(1.0f, 1.0f, 1.0f);
	    int result = Assimp.aiGetMaterialColor(aiMaterial, Assimp.AI_MATKEY_COLOR_DIFFUSE, Assimp.aiTextureType_NONE, 0, color);
	    
	    if (result == 0) {
	        diffuse = new Vector3f(color.r(), color.g(), color.b());
	    }
	    
	    /** Load ambient color */
	    Vector3f ambient = new Vector3f(0.2f, 0.2f, 0.2f);
	    result = Assimp.aiGetMaterialColor(aiMaterial, Assimp.AI_MATKEY_COLOR_AMBIENT, Assimp.aiTextureType_NONE, 0, color);
	   
	    if (result == 0) {
	        ambient = new Vector3f(color.r(), color.g(), color.b());
	    }
	    
	    /** Load specular color */
	    Vector3f specular = new Vector3f(1.0f, 1.0f, 1.0f);
	    result = Assimp.aiGetMaterialColor(aiMaterial, Assimp.AI_MATKEY_COLOR_SPECULAR, Assimp.aiTextureType_NONE, 0, color);
	    
	    if (result == 0) {
	        specular = new Vector3f(color.r(), color.g(), color.b());
	    }
	    
	    material.setTexture(loadTexture(textureName, true));
	    material.setAmbientColor(ambient);
	    material.setDiffuseColor(diffuse);
	    material.setSpecularColor(specular);
	    materials.add(material);
	}
	
	private static void processVertices(AIMesh aiMesh, List<Float> vertices) {
        AIVector3D.Buffer aiVertices = aiMesh.mVertices();
        
        while (aiVertices.remaining() > 0) {
            AIVector3D aiVertex = aiVertices.get();
            vertices.add(aiVertex.x());
            vertices.add(aiVertex.y());
            vertices.add(aiVertex.z());
        }
    }

    private static void processNormals(AIMesh aiMesh, List<Float> normals) {
        AIVector3D.Buffer aiNormals = aiMesh.mNormals();
        
        while (aiNormals != null && aiNormals.remaining() > 0) {
            AIVector3D aiNormal = aiNormals.get();
            normals.add(aiNormal.x());
            normals.add(aiNormal.y());
            normals.add(aiNormal.z());
        }
    }
    
    private static void processColors(MaterialWorld material, List<Float> colors) {
    	colors.add(material.getDiffuseColor().x);
    	colors.add(material.getDiffuseColor().y);
    	colors.add(material.getDiffuseColor().z);
    }

    private static void processTextCoords(AIMesh aiMesh, List<Float> textureCoords) {
    	AIVector3D.Buffer textCoords = aiMesh.mTextureCoords(0);
        int numTextCoords = textCoords != null ? textCoords.remaining() : 0;
        
        for (int i = 0; i < numTextCoords; i++) {
        	AIVector3D textCoord = textCoords.get();
        	textureCoords.add(textCoord.x());
        	textureCoords.add(1 - textCoord.y());
        }
    }

    private static void processIndices(AIMesh aiMesh, List<Integer> indices) {
        int numFaces = aiMesh.mNumFaces();
        AIFace.Buffer aiFaces = aiMesh.mFaces();
        
        for (int i = 0; i < numFaces; i++) {
            AIFace aiFace = aiFaces.get(i);
            IntBuffer buffer = aiFace.mIndices();
            
            while (buffer.remaining() > 0) {
                indices.add(buffer.get());
            }
        }
    }
}
