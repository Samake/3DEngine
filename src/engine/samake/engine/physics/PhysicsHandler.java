package samake.engine.physics;

import javax.vecmath.Vector3f;

import com.bulletphysics.collision.broadphase.AxisSweep3;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;

import samake.engine.logging.Console;
import samake.engine.logging.Console.LOGTYPE;

public class PhysicsHandler {
	
	private DefaultCollisionConfiguration config;
	private CollisionDispatcher dispatcher;
	private SequentialImpulseConstraintSolver solver;
	private DiscreteDynamicsWorld world;
	
	private RigidBody test;
	private float startValue;
	private Transform groundTransform;
	
	public PhysicsHandler() {
		createWorldPhysic();
		createTestTerrain();
		test = createTestObject();
		
		startValue = System.nanoTime();
	      
		Console.print("PhysicsHandler started!", LOGTYPE.OUTPUT, true);
	}

	private void createWorldPhysic() {
		config = new DefaultCollisionConfiguration();
		dispatcher = new CollisionDispatcher(config);
		
		Vector3f worldAabbMin = new Vector3f(-10000,-10000,-10000);
		Vector3f worldAabbMax = new Vector3f(10000,10000,10000);
		AxisSweep3 overlappingPairCache = new AxisSweep3(worldAabbMin, worldAabbMax);
		solver = new SequentialImpulseConstraintSolver();
		
		world = new DiscreteDynamicsWorld(dispatcher, overlappingPairCache, solver, config);
		world.setGravity(new Vector3f(0,-10,0));
		world.getDispatchInfo().allowedCcdPenetration = 0f;
		
		world.stepSimulation(1000 / 1000000f);
	}
	
	private void createTestTerrain() {
		CollisionShape groundShape = new BoxShape(new Vector3f(100.f, 0.f, 100.f));
		groundTransform = new Transform();
		groundTransform.setIdentity();
		groundTransform.origin.set(new Vector3f(0.f, 0.f, 0.f));
		
		float mass = 0f;
		Vector3f localInertia = new Vector3f(0, 0, 0);
		
		DefaultMotionState myMotionState = new DefaultMotionState(groundTransform);
		RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo(mass, myMotionState, groundShape, localInertia);
		
		RigidBody body = new RigidBody(rbInfo);
		addRigidBody(body);
	}
	
	private RigidBody createTestObject() {
		CollisionShape testShape = new BoxShape(new Vector3f(1.0f, 1.0f, 1.0f));
		
		Transform testTransform = new Transform();
		testTransform.setIdentity();
		testTransform.origin.set(0, 50, 0);
	    
	    float mass = 15.0f;
		Vector3f localInertia = new Vector3f(0, 0, 0);
		testShape.calculateLocalInertia(mass, localInertia);
	   
		DefaultMotionState ms = new DefaultMotionState(testTransform);
	   
		RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo(mass, ms, testShape, localInertia);
		RigidBody body = new RigidBody(rbInfo);
	    body.setRestitution(0.2f);
	    body.setFriction(0.80f);
	    body.setDamping(0.2f, 0.2f);
	    
	    body.getCenterOfMassPosition(new Vector3f(0.5f, 0.5f, 0.5f));
	    
	    addRigidBody(body);
	    
	    return body;
	}
	
	public void update() {
		float animationTime = ((float) (System.nanoTime() - startValue)) / 10000000000.0f;

		world.stepSimulation(animationTime);
		
		System.err.println(animationTime + " - " + test.getWorldTransform(groundTransform).origin);
	}
	
	public void addRigidBody(RigidBody body) {
		world.addRigidBody(body);
	}
	
	public void removeRigidBody(RigidBody body) {
		world.removeRigidBody(body);
	}
	
	public DefaultCollisionConfiguration getConfig() {
		return config;
	}

	public void setConfig(DefaultCollisionConfiguration config) {
		this.config = config;
	}

	public CollisionDispatcher getDispatcher() {
		return dispatcher;
	}

	public void setDispatcher(CollisionDispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	public DiscreteDynamicsWorld getWorld() {
		return world;
	}

	public void setWorld(DiscreteDynamicsWorld world) {
		this.world = world;
	}

	public void destroy() {

		Console.print("PhysicsHandler stopped!", LOGTYPE.OUTPUT, true);
	}
}
