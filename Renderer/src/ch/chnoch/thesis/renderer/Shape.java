package ch.chnoch.thesis.renderer;

import javax.vecmath.*;

/**
 * Represents a 3D shape. The shape currently just consists of its vertex data.
 * It should later be extended to include material properties, shaders, etc.
 */
public class Shape {

	private Material mMaterial;
	private VertexBuffers mVertexBuffers;
	private Matrix4f t;
	private BoundingBox mBox;

	public Shape(VertexBuffers vertexBuffers) {
		mVertexBuffers = vertexBuffers;
		init();
	}
	
	private void init() {
		mBox = new BoundingBox(mVertexBuffers.getVertexBuffer());
//		mTrackball = new Trackball(this);
		t = new Matrix4f();
		t.setIdentity();
	}

	public VertexBuffers getVertexBuffers() {
		return mVertexBuffers;
	}

	public void setTransformation(Matrix4f t) {
		this.t = t;
	}

	public Matrix4f getTransformation() {
		return t;
	}
	
	public BoundingBox getBoundingBox() {
		return mBox;
	}

	/**
	 * To be implemented in the "Textures and Shading" project.
	 */
	public void setMaterial(Material material) {
		this.mMaterial = material;
	}

	/**
	 * To be implemented in the "Textures and Shading" project.
	 */
	public Material getMaterial() {
		return this.mMaterial;
	}

}