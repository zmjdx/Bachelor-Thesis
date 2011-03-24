package ch.chnoch.thesis.viewer.test;


import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import android.content.Context;
import android.test.AndroidTestCase;
import android.test.mock.MockContext;
import ch.chnoch.thesis.renderer.*;
import ch.chnoch.thesis.renderer.util.Util;

public class PickingTest extends AndroidTestCase {

	GraphSceneManager mSceneManager;
	GLViewer mViewer;
	GLRenderer10 mRenderer;
	Context context;
	
	Matrix4f mIdentity;
	
	Shape shape;
	ShapeNode root;
	
	public PickingTest() {
		context = new MockContext();
		
		mSceneManager = new GraphSceneManager();
		mRenderer = new GLRenderer10(context);
		mRenderer.setSceneManager(mSceneManager);
		mIdentity = Util.getIdentityMatrix();
		
		mSceneManager.getCamera().getCameraMatrix().set(mIdentity);
//		mSceneManager.getFrustum().getProjectionMatrix().set(mIdentity);
		mRenderer.getViewportMatrix().set(mIdentity);
		
		shape = Util.loadCube(1);
		root = new ShapeNode();
		root.setTransformationMatrix(new Matrix4f(mIdentity));
		root.setShape(shape);
		
		mSceneManager.setRoot(root);
	}
	
	public void testSetup() {
		Matrix4f id = new Matrix4f(1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1);
		
//		assertTrue(mSceneManager.getFrustum().getProjectionMatrix().equals(id));
		
		assertTrue(mRenderer.getViewportMatrix().equals(id));
		assertEquals(mSceneManager.getRoot(), root);
		assertTrue(root.getChildren() == null);
		assertEquals(root.getShape(), shape);
	}
	
	public void testProjectionInverse() {
		Matrix4f proj = mSceneManager.getFrustum().getProjectionMatrix();
		Matrix4f proj_inv = new Matrix4f(mIdentity);
		proj.invert(proj_inv);
		
		proj_inv.invert();
		
		Vector3f x = new Vector3f(1,0,0);
		Vector3f x_or = new Vector3f(1,0,0);
		Vector3f y = new Vector3f(0,1,0);
		Vector3f y_or = new Vector3f(0,1,0);
		Vector3f z = new Vector3f(0,0,1);
		Vector3f z_or = new Vector3f(0,0,1);
		
		assertTrue(proj != proj_inv);
		
		proj.transform(x);
		proj.transform(y);
		proj.transform(z);
		
		proj_inv.transform(x);
		proj_inv.transform(y);
		proj_inv.transform(z);
		
		assertEquals(x, x_or);
		assertEquals(y, y_or);
		assertEquals(z, z_or);
		assertEquals(proj, proj_inv);
	}
	
	public void testBoundingBox() {
		Vector3f one = new Vector3f(1, 1, 1);
		Vector3f minusOne = new Vector3f(-1, -1, -1);
		
		assertEquals(shape.getBoundingBox().getHigh(), one);
		assertEquals(shape.getBoundingBox().getLow(), minusOne);
	}
	
	
	public void testPicking() {
		assertEquals(Util.unproject(240, 400, mRenderer), shape);
		assertEquals(Util.unproject(1, 1, mRenderer), shape);
		assertEquals(Util.unproject(-1, -1, mRenderer), shape);
		assertEquals(Util.unproject(10000, 100000, mRenderer), shape);
		
		assertNull(Util.unproject(-1.001f, -1.001f, mRenderer));
		assertNull(Util.unproject(1.001f, 1.001f, mRenderer));
		assertNull(Util.unproject(2, 2, mRenderer));
		assertNull(Util.unproject(-2, -2, mRenderer));
		assertNull(Util.unproject(10, 0, mRenderer));
	}
	
	
}