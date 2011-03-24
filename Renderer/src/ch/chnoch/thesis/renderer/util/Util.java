package ch.chnoch.thesis.renderer.util;

import java.util.List;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import ch.chnoch.thesis.renderer.BoundingBox;
import ch.chnoch.thesis.renderer.Camera;
import ch.chnoch.thesis.renderer.Frustum;
import ch.chnoch.thesis.renderer.Ray;
import ch.chnoch.thesis.renderer.RenderContext;
import ch.chnoch.thesis.renderer.RenderItem;
import ch.chnoch.thesis.renderer.SceneManagerInterface;
import ch.chnoch.thesis.renderer.SceneManagerIterator;
import ch.chnoch.thesis.renderer.Shape;
import ch.chnoch.thesis.renderer.VertexBuffers;

public class Util {

	/**
	 * Creates a primitive type float array from a List of reference Float type
	 * values
	 * 
	 * @param list
	 *            The List with the Float values
	 * @return a float array
	 */
	public static float[] floatListToArray(List<Float> list) {
		float[] floatArray = new float[list.size()];
		for (int i = 0; i < list.size(); i++) {
			floatArray[i] = list.get(i);
		}

		return floatArray;
	}
	
	/**
	 * Creates a primitive type float array from a List of reference Float type
	 * values
	 * 
	 * @param list
	 *            The List with the Float values
	 * @return a float array
	 */
	public static int[] intListToArray(List<Integer> list) {
		int[] floatArray = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			floatArray[i] = list.get(i);
		}

		return floatArray;
	}
	
	/**
	 * Creates a new Identity Matrix.
	 * @return an identity Matrix.
	 */
	public static Matrix4f getIdentityMatrix() {
		return new Matrix4f(1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1);
	}
	
	public static Shape loadCube(float scale) {
        VertexBuffers vertexBuffer = new VertexBuffers();
        vertexBuffer.setColorBuffer(colors);
        vertexBuffer.setIndexBuffer(indices);
        
        for (int i=0;i<vertices.length;i++) {
        	vertices[i] *= scale;
        }
        
        vertexBuffer.setVertexBuffer(vertices);

        // Make a shape and add the object
        return new Shape(vertexBuffer);
	}
	
	
	
	
	public static int one = 0x10000;
	static float vertices[] = {
			-one, -one, -one,
			one, -one, -one,
			one,  one, -one,
			-one,  one, -one,
			-one, -one,  one,
			one, -one,  one,
			one,  one,  one,
			-one,  one,  one,
	};
	
	static int colors[] = {
			0,    0,    0,  one,
			one,    0,    0,  one,
			one,  one,    0,  one,
			0,  one,    0,  one,
			0,    0,  one,  one,
			one,    0,  one,  one,
			one,  one,  one,  one,
			0,  one,  one,  one,
	};
	
	static short indices[] = {
			0, 4, 5,    0, 5, 1,
			1, 5, 6,    1, 6, 2,
			2, 6, 7,    2, 7, 3,
			3, 7, 4,    3, 4, 0,
			4, 7, 6,    4, 6, 5,
			3, 0, 1,    3, 1, 2
	};
	
	public static Shape unproject(float x, float y, RenderContext renderer) {
		SceneManagerInterface sceneManager = renderer.getSceneManager();
		Camera camera = sceneManager.getCamera();
		Frustum frustum = sceneManager.getFrustum();
		
		Matrix4f staticMatrix = new Matrix4f(renderer.getViewportMatrix());
		staticMatrix.mul(frustum.getProjectionMatrix());
		staticMatrix.mul(camera.getCameraMatrix());

		SceneManagerIterator it = sceneManager.iterator();

		Matrix4f inverse, transformation;
		RenderItem item;
		while (it.hasNext()) {
			Vector3f w1 = new Vector3f(x, y, -1);
			Vector3f w2 = new Vector3f(x, y, 1);
			inverse = new Matrix4f(staticMatrix);
			item = it.next();
			inverse.mul(item.getT());
			
			transformation = new Matrix4f(inverse);
			
			inverse.invert();
			inverse.transform(w1);
			inverse.transform(w2);
//			Vector4f o1 = new Vector4f(w1);
//			Vector4f o2 = new Vector4f(w2);
//			o1.w = 1;
//			o2.w = 1;
			intersectRay(w1, w2, item.getShape(), transformation);
			if () {
//				item.getShape().simpleUpdate(x, y, mPreviousX, mPreviousY, TOUCH_SCALE_FACTOR);
				return item.getShape();
			}
		}

		return null;
	}
	
	private static boolean intersectRay(Vector3f o1, Vector3f o2, Shape shape, Matrix4f trans) {
		Vector3f hitPoint = new Vector3f();
		Vector3f origin = o1;
		Vector3f direction = new Vector3f(o2);
		direction.sub(o1);
		Ray ray = new Ray(origin, direction);
		BoundingBox box = shape.getBoundingBox().transform(trans);
		box.hitPoint(ray, hitPoint);
		return box.intersect(ray);
	}
}
