package samake.engine.physics.bodys;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;

import samake.engine.core.Engine;
import samake.engine.utils.Utils;

public class PhysicBody {

	private Transform transform = new Transform();
	private Quat4f rotation = new Quat4f(0.0f, 0.0f, 0.0f, 1.0f);
	private Vector3f inertia = new Vector3f(0.0f, 0.0f, 0.0f);
	private Vector3f scale = new Vector3f(1.0f, 1.0f, 1.0f);
	private float mass = 1.0f;
	private float restitution = 0.5f;
	private float friction = 0.5f;
	private Vector2f damping = new Vector2f(0.1f, 0.1f);
	
	private DefaultMotionState motionState;
	private CollisionShape collissionShape;
	private RigidBody rigidBody;
	
	public PhysicBody() {
		transform.setIdentity();
		transform.origin.set(0.0f, 0.0f, 0.0f);
		transform.setRotation(rotation);
	}

	public void init() {
//		collissionShape.calculateLocalInertia(mass, inertia);
	   
		setMotionState(new DefaultMotionState(transform));
		
		setRigidBody(new RigidBody(new RigidBodyConstructionInfo(mass, motionState, collissionShape, inertia)));
		rigidBody.setRestitution(restitution);
		rigidBody.setFriction(friction);
		rigidBody.setDamping(damping.x, damping.y);

		Engine.instance.getPhysics().addRigidBody(rigidBody);
	}
	
	public void update() {

	}
	
	public CollisionShape getCollissionShape() {
		return collissionShape;
	}

	public void setCollissionShape(CollisionShape collissionShape) {
		this.collissionShape = collissionShape;
	}

	public Transform getTransform() {
		return transform;
	}

	public void setTransform(Transform transform) {
		this.transform = transform;
	}

	public Quat4f getRotationQuaternion() {
		return rotation;
	}

	public void setRotationQuaternion(Quat4f rotation) {
		this.rotation = rotation;
	}

	public RigidBody getRigidBody() {
		return rigidBody;
	}

	public void setRigidBody(RigidBody rigidBody) {
		this.rigidBody = rigidBody;
	}

	public Vector3f getInertia() {
		return inertia;
	}

	public void setInertia(Vector3f inertia) {
		this.inertia = inertia;
	}

	public Vector3f getScale() {
		return scale;
	}

	public void setScale(Vector3f scale) {
		this.scale = scale;
	}

	public DefaultMotionState getMotionState() {
		return motionState;
	}

	public void setMotionState(DefaultMotionState motionState) {
		this.motionState = motionState;
	}

	public float getMass() {
		return mass;
	}

	public void setMass(float mass) {
		this.mass = mass;
		collissionShape.calculateLocalInertia(mass, inertia);
	}

	public float getRestitution() {
		return restitution;
	}

	public void setRestitution(float restitution) {
		this.restitution = restitution;
		
		if (rigidBody != null) {
			rigidBody.setRestitution(restitution);
		}
	}

	public float getFriction() {
		return friction;
	}

	public void setFriction(float friction) {
		this.friction = friction;
		
		if (rigidBody != null) {
			rigidBody.setFriction(friction);
		}
	}

	public Vector2f getDamping() {
		return damping;
	}

	public void setDamping(Vector2f damping) {
		this.damping = damping;
		
		if (rigidBody != null) {
			rigidBody.setDamping(damping.x, damping.y);
		}
	}
	
	public void setDamping(org.joml.Vector2f damping) {
		this.damping = new Vector2f(damping.x, damping.y);
		
		if (rigidBody != null) {
			rigidBody.setDamping(damping.x, damping.y);
		}
	}
	
	public org.joml.Vector3f getPosition() {
		org.joml.Vector3f position = new org.joml.Vector3f();
		
		if (rigidBody != null) {
			Vector3f currentPosition = rigidBody.getWorldTransform(transform).origin;
			
			position.x = currentPosition.x;
			position.y = currentPosition.y;
			position.z = currentPosition.z;
		}
		
		return position;
	}
	
	public void setPosition(org.joml.Vector3f position) {
		transform.origin.x = position.x;
		transform.origin.y = position.y;
		transform.origin.z = position.z;
		
		if (rigidBody != null) {
			rigidBody.setWorldTransform(transform);
		}
	}
	
	public org.joml.Vector3f getRotation() {
		rotation = transform.getRotation(rotation);
		
		float pitch = Utils.getPitch(rotation);
		float yaw = Utils.getYaw(rotation);
		float roll = Utils.getRoll(rotation);
		
		pitch = (float) (Math.toDegrees(pitch) + 180)%360;
		yaw = (float) (Math.toDegrees(yaw) + 180)%360;
		roll = (float) (Math.toDegrees(roll) + 180)%360;
		
		return new org.joml.Vector3f(pitch, yaw, roll);
	}

	public void destroy() {
		if (rigidBody != null) {
			Engine.instance.getPhysics().removeRigidBody(rigidBody);
		}
	}
}
