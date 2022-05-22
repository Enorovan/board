package pobj.pinboard.editor;

import java.util.ArrayList;
import java.util.List;

import pobj.pinboard.document.Clip;

public class Clipboard {
	private static Clipboard CLIPBOARD = new Clipboard();
	private List<Clip> clips;
	private List<ClipboardListener> listeners;
	
	private Clipboard() {
		clips = new ArrayList<>();
		listeners = new ArrayList<>();
	}
	
	public void copyToClipboard(List<Clip> clips) {
		for(Clip clip : clips)
			this.clips.add(clip.copy());
		notifyListeners();
	}
	
	public List<Clip> copyFromClipboard(){
		List<Clip> listClips = new ArrayList<>();
		for(Clip clip : clips)
			listClips.add(clip.copy());
		notifyListeners();
		return listClips;
	}
	
	public void clear() {
		clips.clear();
		notifyListeners();
	}
	
	public boolean isEmpty() {
		return clips.isEmpty();
	}

	public static Clipboard getInstance() {
		return CLIPBOARD;
	}
	
	public void addListener(ClipboardListener listener) {
		listeners.add(listener);
		notifyListeners();
	}
	
	public void removeListener(ClipboardListener listener) {
		listeners.remove(listener);
	}
	
	public void notifyListeners() {
		for(ClipboardListener clipboardListener : listeners)
			 clipboardListener.clipboardChanged();
	}

}
