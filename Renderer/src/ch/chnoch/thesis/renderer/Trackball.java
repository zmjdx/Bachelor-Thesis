package ch.chnoch.thesis.renderer;

import javax.vecmath.AxisAngle4f;
import javax.vecmath.Matrix3f;
import javax.vecmath.Vector3f;

public class Trackball {

	private float mWidth = 0, mHeight = 0;
	private Node mNode;

	public Trackball() {
	}

	public Trackball(Node shape) {
		setNode(shape);
	}

	public Trackball(Node shape, int width, int height) {
		mWidth = width;
		mHeight = height;
		setNode(shape);
	}

	public void setNode(Node node) {
		mNode = node;
	}
	
	public Node getNode() {
		return mNode;
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
	
	public Vector3f projectOnTrackball(Vector3f vector) {
		Vector3f point = new Vector3f(vector);
		Vector3f center = mNode.getBoundingBox().getCenter();
		point.sub(center);
		point.normalize();
		point.add(center);
		return point;
	}

	public void update(Vector3f cur, Vector3f prev, float factor) {
		Vector3f axisVector = new Vector3f();
		axisVector.cross(prev, cur);
		// axisVector.normalize();
		// newVector.negate();
		float angle = prev.angle(cur)*factor;

//		Vector4f ax = new Vector4f(axisVector);
//		ax.w = 1;
//		mNode.getTransformationMatrix().transform(ax);
//		axisVector.x = ax.x;
//		axisVector.y = ax.y;
//		axisVector.z = ax.z;
		// AxisAngle:
		AxisAngle4f axisAngle = new AxisAngle4f(axisVector, angle);
//		Quat4f quat = new Quat4f();
//		quat.set(axisAngle);
//		quat.normalize();
//		
//		Quat4f q = new Quat4f();
//		mNode.getTransformationMatrix().get(q);
//		quat.mul(q);
		Matrix3f rot = new Matrix3f();
		mNode.getTransformationMatrix().getRotationScale(rot);
		
//		rot.setRotation(axisAngle);
		mNode.getTransformationMatrix().setRotation(axisAngle);
		
//		mNode.getTransformationMatrix().set(quat);
	}
	
}