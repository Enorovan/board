package pobj.pinboard.document;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ClipEllipse extends AbstractClip {

	public ClipEllipse(double left, double top, double right, double bottom, Color color) {
		super(left,top,right,bottom,color);
	}
	
	@Override
	public void draw(GraphicsContext ctx) {
		ctx.setFill(getColor());
		ctx.fillOval(getLeft(),getTop(),Math.abs(getRight()-getLeft()),Math.abs(getBottom()-getTop()));	
	}

	@Override
	public Clip copy() {
		return new ClipEllipse(getLeft(),getTop(),getRight(),getBottom(),getColor());
	}
	
	public boolean isSelected(double x, double y) {
		double cx = (getLeft()+getRight())/2;
		double cy = (getBottom()+getTop())/2;
		double rx = (getLeft()-getRight())/2;
		double ry = (getBottom()-getTop())/2;
		return (Math.pow((x-cx)/rx,2)+Math.pow((y-cy)/ry,2)) <= 1;
	}
}
