package pobj.pinboard.editor.tools;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import pobj.pinboard.document.ClipRect;
import pobj.pinboard.editor.EditorInterface;

public class ToolRect implements Tool {
	private ClipRect clipRect;
	private double x;
	private double y;

	@Override
	public void press(EditorInterface i, MouseEvent e) {
		x = e.getX();
		y = e.getY();
		clipRect = new ClipRect(x, y, x, y, i.getCurrentColor());
	}

	@Override
	public void drag(EditorInterface i, MouseEvent e) {
		if (y <= e.getX() && y <= e.getY()) clipRect.setGeometry(x, y, e.getX(), e.getY());
		if (x <= e.getX() && y >= e.getY()) clipRect.setGeometry(x, e.getY(), e.getX(), y);
		if (x >= e.getX() && y <= e.getY()) clipRect.setGeometry(e.getX(), y, x, e.getY());
		if (y >= e.getX() && y >= e.getY()) clipRect.setGeometry(e.getX(), e.getY(), x, y);
	}

	@Override
	public void release(EditorInterface i, MouseEvent e) {
		if (x < e.getX() && y > e.getY()) clipRect.setGeometry(x, e.getY(), e.getX(), y);
		if (e.getX() < x && y < e.getY()) clipRect.setGeometry(e.getX(), y, x, e.getY());
		i.getBoard().addClip(clipRect);
	}

	@Override
	public void drawFeedback(EditorInterface i, GraphicsContext gc) {
		gc.strokeRect(clipRect.getLeft(), clipRect.getTop(),clipRect.getLargeur(),clipRect.getLongueur());
	}

	@Override
	public String getName(EditorInterface editor) {
		return "Rectangle";
	}
}
