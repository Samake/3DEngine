package samake.engine.physics.bodys;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;

import samake.engine.core.Engine;
import samake.engine.utils.Utils;

public class PhysicBody {
	
	private CollisionShape collissionShape;
	private Transform transform;
	private Quat4f rotation;
	private RigidBody rigidBody;
	private Vector3f inertia;
	private Vector3f scale;
	private DefaultMotionState motionState;
	private float mass;
	private float restitution;
	private float friction;
	private Vector2f damping;
	
	public PhysicBody() {
		setCollissionShape(new SphereShape(1.0f));
		setTransform(new Transform());
		setRotationQuaternion(new Quat4f(0.0f, 0.0f, 0.0f, 1.0f));
		setInertia(new Vector3f(0.0f, 0.0f, 0.0f));
		setScale(new Vector3f(1.0f, 1.0f, 1.0f));
		setMass(1.0f);
		setRestitution(0.5f);
		setFriction(0.1f);
		setDamping(new Vector2f(0.2f, 0.5f));
		
		init();
	}

	public void init() {
		transform.setIdentity();
		transform.origin.set(0.0f, 0.0f, 0.0f);
		transform.setRotation(rotation);

		collissionShape.calculateLocalInertia(mass, inertia);
	   
		setMotionState(new DefaultMotionState(transform));
		
		setRigidBody(new RigidBody(new RigidBodyConstructionInfo(mass, motionState, collissionShape, inertia)));
		rigidBody.setRestitution(restitution);
		rigidBody.setFriction(friction);
		rigidBody.setDamping(damping.x, damping.y);
	    
		rigidBody.getCenterOfMassPosition(new Vector3f(scale.x * 0.5f, scale.y * 0.5f, scale.z * 0.5f));
		
		Engine.instance.getPhysics().addRigidBody(rigidBody);
	}
	
	public void update() {
		//Quat4f currentRotation = transform.getRotation(rotation);
		//System.err.println(currentRotation);
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
		
		Vector3f currentPosition = rigidBody.getWorldTransform(transform).origin;
		
		position.x = currentPosition.x;
		position.y = currentPosition.y;
		position.z = currentPosition.z;
		
		return position;
	}
	
	public void setPosition(org.joml.Vector3f position) {
		transform.origin.x = position.x;
		transform.origin.y = position.y;
		transform.origin.z = position.z;
		
		rigidBody.setWorldTransform(transform);
	}
	
	public org.joml.Vector3f getRotation() {
		return Utils.quaternionToEuler(transform.getRotation(rotation));
	}

	public void destroy() {
		Engine.instance.getPhysics().removeRigidBody(rigidBody);
	}
}
