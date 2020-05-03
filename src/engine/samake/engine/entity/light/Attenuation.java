package samake.engine.entity.light;

public class Attenuation {

    private float constant;
    private float linear;
    private float exponent;
    
    public Attenuation() {
    	setConstant(0.0f);
    	setLinear(0.2f);
    	setExponent(4.0f);
    }

	public float getConstant() {
		return constant;
	}

	public void setConstant(float constant) {
		this.constant = constant;
	}

	public float getLinear() {
		return linear;
	}

	public void setLinear(float linear) {
		this.linear = linear;
	}

	public float getExponent() {
		return exponent;
	}

	public void setExponent(float exponent) {
		this.exponent = exponent;
	}
}
