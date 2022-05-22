package pobj.pinboard.editor.commands;

import pobj.pinboard.document.Clip;
import pobj.pinboard.document.ClipGroup;
import pobj.pinboard.editor.EditorInterface;

public class CommandUngroup implements Command {
	private ClipGroup group;
	private EditorInterface editor;
	
	public CommandUngroup(EditorInterface editor, ClipGroup group) {
		this.editor = editor;
		this.group = group;
	}
	
	@Override
	public void execute() {
		editor.getBoard().removeClip(group);
		for (Clip clip: group.getClips()) editor.getBoard().addClip(clip);
	}

	@Override
	public void undo() {
		for (Clip clip: group.getClips()) editor.getBoard().removeClip(clip);
		editor.getBoard().addClip(group);		
	}

}
