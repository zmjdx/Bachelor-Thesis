package ch.chnoch.thesis.renderer;

import javax.vecmath.*;

import ch.chnoch.thesis.renderer.interfaces.SceneManagerInterface;

import ch.chnoch.thesis.renderer.interfaces.SceneManagerInterface;

import ch.chnoch.thesis.renderer.interfaces.SceneManagerInterface;

/**
 * Stores the specification of a virtual camera. You will extend this class to
 * construct a 4x4 camera matrix, i.e., the world-to- camera transform from
 * intuitive parameters.
 * 
 * A scene manager (see {@link SceneManagerInterface},
 * {@link SimpleSceneManager}) stores a camera.
 */
public class Camera {

	private Matrix4f cameraMatrix;
	private Vector3f centerOfProjection, lookAtPoint, upVector;

	/**
	 * Construct a camera with a default camera matrix. The camera matrix
	 * corresponds to the world-to-camera transform. This default matrix places
	 * the camera at (0,0,10) in world space, facing towards the origin (0,0,0)
	 * of world space, i.e., towards the negative z-axis.
	 */
	public Camera() {
		centerOfProjection = new Vector3f(0, 0, 15);
		lookAtPoint = new Vector3f(0, 0, 0);
		upVector = new Vector3f(0, 1, 0);
		cameraMatrix = new Matrix4f();
		this.update();
	}

	/**
	 * Return the camera matrix, i.e., the world-to-camera transform. For
	 * example, this is used by the renderer.
	 * 
	 * @return the 4x4 world-to-camera transform matrix
	 */
	public Matrix4f getCameraMatrix() {
		return cameraMatrix;
	}

	public Vector3f getCenterOfProjection() {
		return centerOfProjection;
	}

	public void setCenterOfProjection(Vector3f centerOfProjection) {
		this.centerOfProjection = centerOfProjection;
		this.update();
	}

	public Vector3f getLookAtPoint() {
		return lookAtPoint;
	}

	public void setLookAtPoint(Vector3f lookAtPoint) {
		this.lookAtPoint = lookAtPoint;
		this.update();
	}

	public Vector3f getUpVector() {
		return upVector;
	}

	public void setUpVector(Vector3f upVector) {
		this.upVector = upVector;
		this.update();
	}

	public void update() {
		updateCamera();
	}

	private void updateCamera() {
		Vector3f x = new Vector3f();
		Vector3f y = new Vector3f();
		Vector3f z = new Vector3f();
		Vector3f temp = new Vector3f(this.centerOfProjection);

		temp.sub(this.lookAtPoint);
		temp.normalize();
		z.set(temp);

		temp.set(this.upVector);
		temp.cross(temp, z);
		temp.normalize();
		x.set(temp);

		y.cross(z, x);

		Matrix4f newMatrix = new Matrix4f();
		newMatrix.setColumn(0, x.getX(), x.getY(), x.getZ(), 0);
		newMatrix.setColumn(1, y.getX(), y.getY(), y.getZ(), 0);
		newMatrix.setColumn(2, z.getX(), z.getY(), z.getZ(), 0);
		newMatrix.setColumn(3, centerOfProjection.getX(),
				centerOfProjection.getY(), centerOfProjection.getZ(), 1);
		newMatrix.invert();
		this.cameraMatrix.set(newMatrix);
	}

	private void updateCamera2() {
		float forwardx, forwardy, forwardz, invMag;
		float upx, upy, upz;
		float sidex, sidey, sidez;

		forwardx = centerOfProjection.x - lookAtPoint.x;
		forwardy = centerOfProjection.y - lookAtPoint.y;
		forwardz = centerOfProjection.z - lookAtPoint.z;

		invMag = (float) (1.0 / Math.sqrt(forwardx * forwardx + forwardy * forwardy
				+ forwardz * forwardz));
		forwardx = forwardx * invMag;
		forwardy = forwardy * invMag;
		forwardz = forwardz * invMag;

		invMag = (float) (1.0 / Math.sqrt(upVector.x * upVector.x + upVector.y
				* upVector.y + upVector.z * upVector.z));
		upx = upVector.x * invMag;
		upy = upVector.y * invMag;
		upz = upVector.z * invMag;

		// side = up cross forward
		sidex = upy * forwardz - forwardy * upz;
		sidey = upz * forwardx - upx * forwardz;
		sidez = upx * forwardy - upy * forwardx;

		invMag = (float) (1.0 / Math.sqrt(sidex * sidex + sidey * sidey + sidez * sidez));
		sidex *= invMag;
		sidey *= invMag;
		sidez *= invMag;

		// recompute up = forward cross side

		upx = forwardy * sidez - sidey * forwardz;
		upy = forwardz * sidex - forwardx * sidez;
		upz = forwardx * sidey - forwardy * sidex;

		float[] mat = new float[16];
		mat[0] = sidex;
		mat[1] = sidey;
		mat[2] = sidez;

		mat[4] = upx;
		mat[5] = upy;
		mat[6] = upz;

		mat[8] = forwardx;
		mat[9] = forwardy;
		mat[10] = forwardz;

		mat[3] = -centerOfProjection.x * mat[0] + -centerOfProjection.y * mat[1] + -centerOfProjection.z * mat[2];
		mat[7] = -centerOfProjection.x * mat[4] + -centerOfProjection.y * mat[5] + -centerOfProjection.z * mat[6];
		mat[11] = -centerOfProjection.x * mat[8] + -centerOfProjection.y * mat[9] + -centerOfProjection.z * mat[10];

		mat[12] = mat[13] = mat[14] = 0;
		mat[15] = 1;
		
		cameraMatrix.set(mat);
	}
}
