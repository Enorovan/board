package pobj.pinboard.editor.commands;

import java.util.List;

import pobj.pinboard.document.Clip;
import pobj.pinboard.document.ClipGroup;
import pobj.pinboard.editor.EditorInterface;

public class CommandGroup implements Command {
	private ClipGroup group;
	private EditorInterface editor;
	
	public CommandGroup(EditorInterface editor, List<Clip> clips) {
		this.editor = editor;
		group = new ClipGroup(clips);
	}

	@Override
	public void execute() {
		for (Clip clip: group.getClips()) editor.getBoard().removeClip(clip);
		editor.getBoard().addClip(group);
	}

	@Override
	public void undo() {
		editor.getBoard().removeClip(group);	
		for (Clip clip: group.getClips()) editor.getBoard().addClip(clip);
	}

}
