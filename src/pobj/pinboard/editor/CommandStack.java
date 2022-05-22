package pobj.pinboard.editor;

import java.util.ArrayDeque;
import java.util.Deque;

import pobj.pinboard.editor.commands.Command;

public class CommandStack {
	private Deque<Command> undo = new ArrayDeque<Command>();
	private Deque<Command> redo = new ArrayDeque<Command>();
	
	public void addCommand(Command commande) {
		undo.push(commande);
		redo.clear();
	}
	
	public void undo() {
		Command commande = undo.pop();
		commande.undo();
		redo.push(commande);
	}

	public void redo() {
		Command commande = redo.pop();
		commande.execute();
		undo.push(commande);
	}	
	
	public boolean isUndoEmpty() {
		return undo.isEmpty();
	}
	
	public boolean isRedoEmpty() {
		return redo.isEmpty();
	}
}
