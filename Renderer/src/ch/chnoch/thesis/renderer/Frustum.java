package ch.chnoch.thesis.renderer;

import javax.vecmath.Matrix4f;

import ch.chnoch.thesis.renderer.interfaces.SceneManagerInterface;
import ch.chnoch.thesis.renderer.interfaces.SceneManagerInterface;
import ch.chnoch.thesis.renderer.interfaces.SceneManagerInterface;
import ch.chnoch.thesis.renderer.util.Util;

/**
 * Stores the specification of a viewing frustum, or a viewing volume. The
 * viewing frustum is represented by a 4x4 projection matrix. You will extend
 * this class to construct the projection matrix from intuitive parameters.
 * <p>
 * A scene manager (see {@link SceneManagerInterface},
 * {@link SimpleSceneManager}) stores a frustum.
 */
public class Frustum {

	private Matrix4f projectionMatrix;
	private float nearPlane, farPlane, aspectRatio, vertFOV;

	/**
	 * Construct a default viewing frustum. The frustum is given by a default
	 * 4x4 projection matrix.
	 */
	public Frustum() {
		projectionMatrix = new Matrix4f();
		// Aspect Ratio is 1 on init
		this.aspectRatio = 1;
		this.vertFOV = 60;
		this.nearPlane = 1;
		this.farPlane = 50;
		this.updateFrustum();
		
//		projectionMatrix = Util.getIdentityMatrix();
	}

	/**
	 * Return the 4x4 projection matrix, which is used for example by the
	 * renderer.
	 * 
	 * @return the 4x4 projection matrix
	 */
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

	public float getNearPlane() {
		return nearPlane;
	}

	public void setNearPlane(float nearPlane) {
		this.nearPlane = nearPlane;
		this.updateFrustum();
	}

	public float getFarPlane() {
		return farPlane;
	}

	public void setFarPlane(float farPlane) {
		this.farPlane = farPlane;
		this.updateFrustum();
	}

	public float getAspectRatio() {
		return aspectRatio;
	}

	public void setAspectRatio(float aspectRatio) {
		this.aspectRatio = aspectRatio;
		updateFrustum();
	}

	public float getVertFOV() {
		return vertFOV;
	}

	public void setVertFOV(float vertFOV) {
		this.vertFOV = vertFOV;
		updateFrustum();
	}

	private void updateFrustum() {
		final float DEG2RAD = 3.14159265f / 180;

		float halfFov = vertFOV * 0.5f * DEG2RAD;
		float deltaZ = farPlane - nearPlane;
		float sine = (float)Math.sin(halfFov);
		float cotangent = (float) Math.cos(halfFov) / sine;
		
//		float temp = (float) (1 / (aspectRatio * Math.tan(vertFOV * DEG2RAD / 2)));
		this.projectionMatrix.setM00(cotangent);

//		temp = (float) (1 / Math.tan(vertFOV * DEG2RAD / 2));
		this.projectionMatrix.setM11(cotangent * aspectRatio);

//		temp = (nearPlane + farPlane) / (nearPlane - farPlane);
		this.projectionMatrix.setM22((farPlane + nearPlane)/deltaZ);

//		temp = (2 * nearPlane * farPlane) / (nearPlane - farPlane);
		this.projectionMatrix.setM23(2 * nearPlane * farPlane / deltaZ);
		
		this.projectionMatrix.setM32(-1);
	}

}
