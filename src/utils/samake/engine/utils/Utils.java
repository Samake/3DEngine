package samake.engine.utils;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.List;

import javax.vecmath.Quat4f;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import samake.engine.logging.Console;
import samake.engine.logging.Console.LOGTYPE;

public class Utils {

	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(4 * 4);
	
	public static float[] toArray(List<Float> list) {
		
		float[] array = new float[list.size()];
		for(int i = 0; i < list.size(); i++) {
			array[i] = list.get(i);
		}
		
		return array;
	}
	
	public static int[] toIntArray(List<Integer> list) {
		
		int[] array = new int[list.size()];
		for(int i = 0; i < list.size(); i++) {
			array[i] = list.get(i);
		}
		
		return array;
	}
	
	public static FloatBuffer toFloatBuffer(Matrix4f matrix) {
		matrix.get(matrixBuffer);
		
		return matrixBuffer;
	}
	
	public static ByteBuffer resourceToByteBuffer(InputStream source, int bufferSize) {
		ByteBuffer buffer = null;
		
		try (ReadableByteChannel rbc = Channels.newChannel(source)) {
			buffer = BufferUtils.createByteBuffer(bufferSize);
			
			while (true) {
				int bytes = rbc.read(buffer);
				
				if (bytes == -1)
					break;
				if (buffer.remaining() == 0) {
					buffer = resizeBuffer(buffer, buffer.capacity() * 2);
				}
			}
		} catch (Exception ex) {
			Console.print(ex.toString(), LOGTYPE.ERROR, true);
		}
		
		buffer.flip();
		
		return buffer;
	}
	
	private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
		ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
		buffer.flip();
		newBuffer.put(buffer);
		
		return newBuffer;
	}
	
	public static float[] convertFloatListToArray(List<Float> listResult) {
        float[] result = new float[listResult.size()];
        int i= 0;
        
        for (float num : listResult) {
            result[i++] = num;
        }
        
        return result;
    }
	
	public static int[] convertIntegerListToArray(List<Integer> listResult) {
        int[] result = new int[listResult.size()];
        int i= 0;
        
        for (int num : listResult) {
            result[i++] = num;
        }
        
        return result;
    }
	
	public static float getRandomValue(float min, float max, float dividor) {
		return (float) (min + Math.random() * (max - min)) / dividor;
	}
	
	public static float lerp(float a, float b, float progress) {
	    return (a * (1.0f - progress)) + (b * progress);
	}
	
	public static Vector3f lerp(Vector3f a, Vector3f b, float progress) {
		Vector3f lerpVector = new Vector3f();
		lerpVector.x = (a.x * (1.0f - progress)) + (b.x * progress);
		lerpVector.y = (a.y * (1.0f - progress)) + (b.y * progress);
		lerpVector.z = (a.z * (1.0f - progress)) + (b.z * progress);
		
	    return lerpVector;
	}
	
	public static float getPitch(Quat4f q){
		return (float)(Math.atan2(2.0 * (q.y * q.z + q.w * q.x), q.w * q.w - q.x * q.x - q.y * q.y + q.z * q.z));
	}
	
	public static float getYaw(Quat4f q){
		return (float)(Math.asin(-2.0 * (q.x * q.z - q.w * q.y)));
	}

	public static float getRoll(Quat4f q){
		return (float)(Math.atan2(2.0 * (q.x * q.y + q.w * q.z), q.w * q.w + q.x * q.x - q.y * q.y - q.z * q.z));
	}
	
	public static Vector3f quaternionToEuler(Quat4f quat) {
	    Vector3f p = new Vector3f();

	    double sqw = quat.w * quat.w;
	    double sqx = quat.x * quat.x;
	    double sqy = quat.y * quat.y;
	    double sqz = quat.z * quat.z;

	    double unit = sqx + sqy + sqz + sqw; 
	    double test = quat.x * quat.y + quat.z * quat.w;
	    
	    if (test > 0.499 * unit) { // singularity at north pole
	        p.y = (float) (2 * Math.atan2(quat.x, quat.w));
	        p.z = (float) (Math.PI * 0.5f);
	        p.x = 0;
	    } else if (test < -0.499 * unit) { // singularity at south pole
	        p.y = (float) (-2 * (Math.atan2(quat.x, quat.w)));
	        p.z = (float) (-Math.PI * 0.5f);
	        p.x = 0;
	    } else {
	        p.y = (float) Math.toDegrees((Math.atan2(2 * quat.y * quat.w - 2 * quat.x * quat.z, sqx - sqy - sqz + sqw)));
	        p.z = (float) Math.toDegrees((Math.asin(2 * test / unit)));
	        p.x = (float) Math.toDegrees((Math.atan2(2 * quat.x * quat.w - 2 * quat.y * quat.z, -sqx + sqy - sqz + sqw)));
	    }

	    return p;
	}
	
	public static Quat4f eulerToQuaternion(Vector3f rotation) {
	    Quat4f quat = new Quat4f();
	    float pitch = (float) Math.toRadians(rotation.x);
	    float yaw = (float) Math.toRadians(rotation.y);
	    float roll = (float) Math.toRadians(rotation.z);

	    final Vector3f coss = new Vector3f();
	    coss.x = (float) Math.cos(pitch * 0.5f);
	    coss.y = (float) Math.cos(yaw * 0.5f);
	    coss.z = (float) Math.cos(roll * 0.5f);
	    final Vector3f sins = new Vector3f();
	    sins.x = (float) Math.sin(pitch * 0.5f);
	    sins.y = (float) Math.sin(yaw * 0.5f);
	    sins.z = (float) Math.sin(roll * 0.5f);

	    quat.w = coss.x * coss.y * coss.z + sins.x * sins.y * sins.z;
	    quat.x = sins.x * coss.y * coss.z + coss.x * sins.y * sins.z;
	    quat.y = coss.x * sins.y * coss.z - sins.x * coss.y * sins.z;
	    quat.z = coss.x * coss.y * sins.z - sins.x * sins.y * coss.z;
	    
	    return quat;
	}
}
