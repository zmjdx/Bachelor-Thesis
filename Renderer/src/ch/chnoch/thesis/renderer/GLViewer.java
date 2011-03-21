package ch.chnoch.thesis.renderer;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class GLViewer extends GLSurfaceView {
	
	private GLRenderer10 mRenderer;
	
	private float mPreviousX;
    private float mPreviousY;
    
    private int mWidth, mHeight;
    
    private final float TOUCH_SCALE_FACTOR = 0.01f;
    private final float TRACKBALL_SCALE_FACTOR = 1;
    
    private Trackball mTrackball;
	
	public GLViewer(Context context) {
		super(context);
	}
	
	public GLViewer(Context context, RenderContext renderer, Trackball trackball) {
		super(context);
		mRenderer = (GLRenderer10) renderer;
		mRenderer.setViewer(this);
		
		setRenderer(mRenderer);
		setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		
		mTrackball = trackball;
	}
	
	public void setTrackball(Trackball trackball) {
		mTrackball = trackball;
	}
	
	/*
	 * Leave Trackball-Control for now...
	@Override
	public boolean onTrackballEvent(MotionEvent e) {

		float angleX = e.getX() * TRACKBALL_SCALE_FACTOR;
		float angleY = e.getY() * TRACKBALL_SCALE_FACTOR;

		mTrackball.simpleUpdate(angleX, angleY);

		requestRender();
		return true;
	}
	*/

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		float x = e.getX();
		float y = e.getY();

		switch (e.getAction()) {
		case MotionEvent.ACTION_MOVE:
			float dx = (x - mPreviousX) * TOUCH_SCALE_FACTOR;
			float dy = (y - mPreviousY) * TOUCH_SCALE_FACTOR;
			Vector3f cur = unproject(x, y);
			Vector3f prev = unproject(mPreviousX, mPreviousY);
			mTrackball.update(cur, prev, TOUCH_SCALE_FACTOR);
//			mTrackball.simpleUpdate(x, y, mPreviousX, mPreviousY, TOUCH_SCALE_FACTOR);
//			mRenderer.pick(x,y);
			requestRender();
		}
		mPreviousX = x;
		mPreviousY = y;
		return true;
	}
	

	public Vector3f unproject(float x, float y) {
		SceneManagerInterface sceneManager = mRenderer.getSceneManager();
		Camera camera = sceneManager.getCamera();
		Frustum frustum = sceneManager.getFrustum();
		
		Matrix4f staticMatrix = new Matrix4f(mRenderer.getViewportMatrix());
		staticMatrix.mul(frustum.getProjectionMatrix());
		staticMatrix.mul(camera.getCameraMatrix());

		SceneManagerIterator it = sceneManager.iterator();

		Matrix4f inverse;
		RenderItem item;
		while (it.hasNext()) {
			Vector3f w1 = new Vector3f(x, y, -10);
			Vector3f w2 = new Vector3f(x, y, 10);
			inverse = new Matrix4f(staticMatrix);
			item = it.next();
			inverse.mul(item.getT());
			inverse.invert();
			inverse.transform(w1);
			inverse.transform(w2);
//			Vector4f o1 = new Vector4f(w1);
//			Vector4f o2 = new Vector4f(w2);
//			o1.w = 1;
//			o2.w = 1;
			if (intersectRay(w1, w2, item.getShape())) {
//				item.getShape().simpleUpdate(x, y, mPreviousX, mPreviousY, TOUCH_SCALE_FACTOR);
				item.getShape().toString();
			}
		}

		return null;
	}
	
	private boolean intersectRay(Vector3f o1, Vector3f o2, Shape shape) {
		Vector3f origin = o1;
		Vector3f direction = new Vector3f(o2); 
		direction.sub(o1);
		Ray ray = new Ray(origin, direction);
		return shape.getBoundingBox().intersect(ray);
	}
	
	
 
	public void surfaceHasChanged(int width, int height) {
		mWidth = width;
		mHeight = height;
		mTrackball.setSize(width, height);
	}
}
