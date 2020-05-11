package samake.engine.utils;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.List;

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
}
