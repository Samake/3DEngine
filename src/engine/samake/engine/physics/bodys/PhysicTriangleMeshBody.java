package samake.engine.physics.bodys;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import com.bulletphysics.collision.shapes.BvhTriangleMeshShape;
import com.bulletphysics.collision.shapes.IndexedMesh;
import com.bulletphysics.collision.shapes.TriangleIndexVertexArray;
import com.bulletphysics.linearmath.Transform;

import samake.engine.models.Mesh;

public class PhysicTriangleMeshBody extends PhysicBody {
	
	public PhysicTriangleMeshBody() {
		setTransform(new Transform());
		setRotationQuaternion(new Quat4f(0.0f, 0.0f, 0.0f, 1.0f));
		setInertia(new Vector3f(0.0f, 0.0f, 0.0f));
		setScale(new Vector3f(1.0f, 1.0f, 1.0f));
		setMass(0.0f);
		setRestitution(0.15f);
		setFriction(0.55f);
		setDamping(new Vector2f(0.55f, 0.55f));
	}
	
	public void calculateTriangleMesh(Mesh mesh) {
		int totalVerts = mesh.getNumVertices();
		ByteBuffer gVertices = ByteBuffer.allocateDirect(totalVerts * 3 * 4).order(ByteOrder.nativeOrder());
		ByteBuffer gIndices = ByteBuffer.allocateDirect(totalVerts * 4).order(ByteOrder.nativeOrder());

		float[] vertices = mesh.getVertices();
		
		int counter = 0;
		for (float value : vertices) {
			counter++;

			if (counter == 1) {
				gVertices.putFloat(value);
			}
			
			if (counter == 2) {
				gVertices.putFloat(value);
			}
			
			if (counter == 3) {
				gVertices.putFloat(value);
				counter = 0;
			}
		}
		
		int[] indices = mesh.getIndices();
		
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
		
		setCollissionShape(new BvhTriangleMeshShape(vertArray, true));
		
		init();
	}
	
	@Override
	public void update() {
		super.update();
	}

	@Override
	public void destroy() {
		super.destroy();
	}
}
