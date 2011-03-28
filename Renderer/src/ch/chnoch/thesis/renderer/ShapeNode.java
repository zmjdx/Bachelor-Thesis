package ch.chnoch.thesis.renderer;

import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4f;

import ch.chnoch.thesis.renderer.util.Util;

public class ShapeNode extends Leaf {

    private Shape mShape;
    private BoundingBox mBoundingBox;
    
    public ShapeNode() {
        super();
    }
    
    public void setShape(Shape shape) {
        this.mShape = shape;
        setBoundingBox();
    }
    
    public Shape getShape() {
        return this.mShape;
    }
    
    private void setBoundingBox() {
    	Matrix4f transform = new Matrix4f(transformationMatrix);
    	Matrix4f temp = Util.getIdentityMatrix();
    	
    	Node current = this;
    	while (current.getParent() != null) {
    		parent = current.getParent();
    		temp.set(parent.getTransformationMatrix());
    		temp.mul(transform);
    		transform.set(temp);
    	}
    	transform.setRotationScale(new Matrix3f(1,0,0,0,1,0,0,0,1));
    	mBoundingBox = mShape.getBoundingBox().transform(transform);
    }
    
    public BoundingBox getBoundingBox() {
    	return mBoundingBox;
    }

}