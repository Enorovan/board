package pobj.pinboard.editor.commands;

import pobj.pinboard.document.ClipRect;
import pobj.pinboard.editor.EditorInterface;

public class CommandMove implements Command {
	private ClipRect rectangle;
	private EditorInterface editor;
	private double h;
	private double l;
	
	public CommandMove(EditorInterface editor, ClipRect rectangle, double h, double l) {
		this.rectangle = rectangle;
		this.editor = editor;
		this.h = h;
		this.l = l;
	}

	@Override
	public void execute() {
		editor.getBoard().removeClip(rectangle);
		rectangle.move(h,l);
		editor.getBoard().addClip(rectangle);
	}

	@Override
	public void undo() {
		rectangle.move(-1 * h,-1 * l);
		editor.getBoard().removeClip(rectangle);
		editor.getBoard().addClip(rectangle);
	}

}
