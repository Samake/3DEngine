package samake.engine.material;

import org.joml.Vector3f;

public class MaterialWorld {

    public static final Vector3f DEFAULT_DIFFUSE = new Vector3f(1.0f, 1.0f, 1.0f);
    public static final Vector3f DEFAULT_AMBIENT = new Vector3f(0.3f, 0.3f, 0.35f);
    public static final Vector3f DEFAULT_SPECULAR = DEFAULT_DIFFUSE;
    
    private Vector3f ambientColor;
    private Vector3f diffuseColor;
    private Vector3f specularColor;
    private float shininess;
    private float reflectance;
    private Texture texture;
    private Texture normalMap;
    private Texture specularMap;
    private int tiling;

    public MaterialWorld() {
    	setAmbientColor(DEFAULT_AMBIENT);
    	setDiffuseColor(DEFAULT_DIFFUSE);
    	setSpecularColor(DEFAULT_SPECULAR);
    	setShininess(16.0f);
    	setReflectance(0.3f);
        setTiling(1);
    }

    public Vector3f getAmbientColor() {
        return ambientColor;
    }

    public void setAmbientColor(Vector3f ambientColor) {
        this.ambientColor = ambientColor;
    }

    public Vector3f getDiffuseColor() {
        return diffuseColor;
    }

    public void setDiffuseColor(Vector3f diffuseColor) {
        this.diffuseColor = diffuseColor;
    }

    public Vector3f getSpecularColor() {
        return specularColor;
    }

    public void setSpecularColor(Vector3f specularColor) {
        this.specularColor = specularColor;
    }

    public float getReflectance() {
        return reflectance;
    }

    public void setReflectance(float reflectance) {
        this.reflectance = reflectance;
    }

    public float getShininess() {
		return shininess;
	}

	public void setShininess(float shininess) {
		this.shininess = shininess;
	}


    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
    
    public int hasTexture() {
        return (this.texture != null) ? 1 : 0;
    }

    public Texture getNormalMap() {
        return normalMap;
    }

    public void setNormalMap(Texture normalMap) {
        this.normalMap = normalMap;
    }
    
    public int hasNormalMap() {
        return (this.normalMap != null) ? 1 : 0;
    }

	public Texture getSpecularMap() {
		return specularMap;
	}

	public void setSpecularMap(Texture specularMap) {
		this.specularMap = specularMap;
	}

	public int hasSpecularMap() {
        return (this.specularMap != null) ? 1 : 0;
    }
	
	public int getTiling() {
		return tiling;
	}

	public void setTiling(int tiling) {
		this.tiling = tiling;
	}
}
