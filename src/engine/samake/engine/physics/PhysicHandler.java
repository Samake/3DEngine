package samake.engine.physics;

import javax.vecmath.Vector3f;

import com.bulletphysics.collision.broadphase.AxisSweep3;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;

import samake.engine.logging.Console;
import samake.engine.logging.Console.LOGTYPE;

public class PhysicHandler {
	
	private DefaultCollisionConfiguration config;
	private CollisionDispatcher dispatcher;
	private SequentialImpulseConstraintSolver solver;
	private DiscreteDynamicsWorld world;
	
	private float startValue;
	private Vector3f worldAabbMin;
	private Vector3f worldAabbMax;
	
	public PhysicHandler() {
		createWorldPhysic();

		startValue = System.nanoTime();
	      
		Console.print("PhysicsHandler started!", LOGTYPE.OUTPUT, true);
	}

	private void createWorldPhysic() {
		setConfig(new DefaultCollisionConfiguration());
		setDispatcher(new CollisionDispatcher(config));
		setWorldAabbMin(new Vector3f(-1000.0f, -1000.0f, -1000.0f));
		setWorldAabbMax(new Vector3f(1000.0f, 1000.0f, 1000.0f));
		
		AxisSweep3 overlappingPairCache = new AxisSweep3(worldAabbMin, worldAabbMax);
		solver = new SequentialImpulseConstraintSolver();
		
		setWorld(new DiscreteDynamicsWorld(dispatcher, overlappingPairCache, solver, config));
		
		world.setGravity(new Vector3f(0.0f, -9.81f, 0.0f));
		world.getDispatchInfo().allowedCcdPenetration = 0.0f;
	}
	
	public void update() {
		float animationTime = ((float) (System.nanoTime() - startValue)) / 1000000000.0f;

		world.stepSimulation(animationTime);
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

	public Vector3f getWorldAabbMin() {
		return worldAabbMin;
	}

	public void setWorldAabbMin(Vector3f worldAabbMin) {
		this.worldAabbMin = worldAabbMin;
	}

	public Vector3f getWorldAabbMax() {
		return worldAabbMax;
	}

	public void setWorldAabbMax(Vector3f worldAabbMax) {
		this.worldAabbMax = worldAabbMax;
	}

	public void destroy() {

		Console.print("PhysicsHandler stopped!", LOGTYPE.OUTPUT, true);
	}
}
