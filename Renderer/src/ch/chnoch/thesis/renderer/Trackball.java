package ch.chnoch.thesis.renderer;

import javax.vecmath.AxisAngle4d;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

public class Trackball {

	private float mWidth = 0, mHeight = 0;
	private Node mShape;
	private final Vector3f mIdentTrans = new Vector3f(0,0,0);

	public Trackball() {
	}

	public Trackball(Node shape) {
		init(shape);
	}

	public Trackball(Node shape, int width, int height) {
		mWidth = width;
		mHeight = height;
		init(shape);
	}

	private void init(Node shape) {
		mShape = shape;
	}

	public void setSize(int width, int height) {
		mWidth = width;
		mHeight = height;
	}

	private Vector3f trackBallMapping(float px, float py) {
		float x = (px) / (mWidth/2)-1;
		// Flip so +y is up instead of down (correct?)
		float y = (py) / (mHeight/2)-1;
		float z2 = (1-x * x + y * y);
		float z = (float) (z2 > 0 ? Math.sqrt(z2) : 0);
		Vector3f mapped = new Vector3f(x, y, z);
		mapped.normalize();
		return mapped;
	}

	public Quat4f createQuaternion(float x, float y, float oldX, float oldY, final float factor) {

		Vector3f oldVector = this.trackBallMapping(oldX, oldY);

		Vector3f newVector = this.trackBallMapping(x, y);

		Vector3f axisVector = new Vector3f();
		axisVector.cross(oldVector, newVector);
		// axisVector.normalize();
		// newVector.negate();
		float angle = oldVector.angle(newVector)*factor;

		// AxisAngle:
		Quat4f axisAngle = new Quat4f(axisVector.x, axisVector.y,
				axisVector.z, angle);
		// Quaternions:

//		 return new Quat4f(axisVector.x,s * axisVector.y, s *
//		 axisVector.z,
//		 (float) Math.cos(omega));
//		return new Quat4f(axisVector.x,axisVector.y,axisVector.z, angle);
		return axisAngle;
		//		return axisAngle;
	}

	public void simpleUpdate(float x, float y, float oldX, float oldY, final float factor) {

		 Quat4f quat = createQuaternion(x, y, oldX, oldY, factor);
//		AxisAngle4d axisAngle = createQuaternion(x, y, oldX, oldY);
//		Matrix4f trans = new Matrix4f();
//		trans.set(axisAngle);
//		 trans.set(quat);
//		mShape.getTransformation().mul(trans);
//		AxisAngle4f trans = new AxisAngle4f();
//		trans.set(mShape.getTransformation());
		Quat4f q = new Quat4f();
		mShape.getTransformationMatrix().get(q);
		quat.mul(q);
		mShape.getTransformationMatrix().set(quat);
	}
	
}