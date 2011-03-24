package ch.chnoch.thesis.renderer;

import java.nio.IntBuffer;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

public class BoundingBox {

	private Vector3f low, high;

	public BoundingBox(IntBuffer vertices) {
		init(vertices);
	}

	private void init(IntBuffer vertices) {
		int x, y, z, lowX, lowY, lowZ, highX, highY, highZ;
		lowX = Integer.MAX_VALUE;
		lowY = Integer.MAX_VALUE;
		lowZ = Integer.MAX_VALUE;
		highX = Integer.MIN_VALUE;
		highY = Integer.MIN_VALUE;
		highZ = Integer.MIN_VALUE;

		while (vertices.remaining() > 0) {
			x = vertices.get();
			y = vertices.get();
			z = vertices.get();

			if (x < lowX) {
				lowX = x;
			}
			if (x > highX) {
				highX = x;
			}
			if (y < lowY) {
				lowY = y;
			}
			if (y > highY) {
				highY = y;
			}
			if (z < lowZ) {
				lowZ = z;
			}
			if (z > highZ) {
				highZ = z;
			}
		}
		vertices.position(0);
		
		// correction because of 16/16 fixed integer-representation of fp's
		lowX >>= 16;
		lowY >>= 16;
		lowZ >>= 16;
		highX >>= 16;
		highY >>= 16;
		highZ >>= 16;

		low = new Vector3f(lowX, lowY, lowZ);
		high = new Vector3f(highX, highY, highZ);
	}

	public boolean intersect(Ray ray) {
		float tXmin, tXmax, tYmin, tYmax, tZmin, tZmax;
		
		if (ray.getDirection().x >= 0) {
			tXmin = (low.x - ray.getOrigin().x) / ray.getDirection().x;
			tXmax = (high.x - ray.getOrigin().x) / ray.getDirection().x;
		} else {
			tXmin = (high.x - ray.getOrigin().x) / ray.getDirection().x;
			tXmax = (low.x - ray.getOrigin().x) / ray.getDirection().x;
		}
		
		if (ray.getDirection().y >= 0) {
			tYmin = (low.y - ray.getOrigin().y) / ray.getDirection().y;
			tYmax = (high.y - ray.getOrigin().y) / ray.getDirection().y;
		} else {
			tYmin = (high.y - ray.getOrigin().y) / ray.getDirection().y;
			tYmax = (low.y - ray.getOrigin().y) / ray.getDirection().y;
		}
		
		if ((tXmin > tYmax) || (tYmin > tXmax))
			return false;
		if (tYmin > tXmin)
			tXmin = tYmin;
		if (tYmax < tXmax)
			tXmax = tYmax;
		
		if (ray.getDirection().z >= 0) {
			tZmin = (low.z - ray.getOrigin().z) / ray.getDirection().z;
			tZmax = (high.z - ray.getOrigin().z) / ray.getDirection().z;
		} else {
			tZmin = (high.z - ray.getOrigin().z) / ray.getDirection().z;
			tZmax = (low.z - ray.getOrigin().z) / ray.getDirection().z;
		}
		
		if ((tXmin > tZmax) || (tZmin > tXmax))
			return false;

		return true;
	}
	
	public Vector3f getLow() {
		return low;
	}

	public Vector3f getHigh() {
		return high;
	}

	/**
	 * This method is to be used to transform the bounding box together with the shape
	 * @param trans
	 */
	public void transform(Matrix4f trans) {
		trans.transform(low);
		trans.transform(high);
	}
}
