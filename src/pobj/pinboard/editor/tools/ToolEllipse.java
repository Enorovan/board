package pobj.pinboard.editor.tools;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import pobj.pinboard.document.ClipEllipse;
import pobj.pinboard.editor.EditorInterface;

public class ToolEllipse implements Tool {
	private ClipEllipse clipEllipse;
	private double x;
	private double y;

	@Override
	public void press(EditorInterface i, MouseEvent e) {
		x = e.getX();
		y = e.getY();
		clipEllipse = new ClipEllipse(x, y, x, y, i.getCurrentColor());
	}

	@Override
	public void drag(EditorInterface i, MouseEvent e) {
		if(e.getX() < x ) clipEllipse.setGeometry(e.getX(), e.getY(), x, y);
		else clipEllipse.setGeometry(x, y, e.getX(), e.getY());
	}

	@Override
	public void release(EditorInterface i, MouseEvent e) {
		if(x < e.getX() && y > e.getY()) clipEllipse.setGeometry(x, e.getY(), e.getX(), y);
		if(e.getX() < x && y <e.getY()) clipEllipse.setGeometry(e.getX(), y, x, e.getY());
		i.getBoard().addClip(clipEllipse);
	}

	@Override
	public void drawFeedback(EditorInterface i, GraphicsContext gc) {
		gc.strokeOval(clipEllipse.getLeft(), clipEllipse.getTop(), clipEllipse.getLargeur(), clipEllipse.getLongueur());
	}

	@Override
	public String getName(EditorInterface editor) {
		return "Ellipse";
	}

}
