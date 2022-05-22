package pobj.pinboard.document;

import java.io.File;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class ClipImage extends AbstractClip {
	private Image image;
	private File file;

	public ClipImage(double left, double top, File file) {
		super(left, top, 0, 0, null);
		image = new Image(file.toURI().toString());
		double right = image.getWidth();
		double bottom = image.getHeight();
		setGeometry(left, top, right, bottom);
	}

	@Override
	public void draw(GraphicsContext ctx) {
		ctx.drawImage(image, getLeft(), getTop());
	}

	@Override
	public Clip copy() {
		return new ClipImage(getLeft(), getTop(), file);
	}
	
	@Override
	public boolean isSelected(double x, double y) {
		return (x < this.getRight() && x > this.getLeft() && y > this.getTop() && y < this.getBottom());
	}
	
	public Image getImage() {
		return image;
	}
}