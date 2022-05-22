package pobj.pinboard.editor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import pobj.pinboard.document.Board;
import pobj.pinboard.document.Clip;

public class Selection {
	private List<Double> left = new ArrayList<>();
	private List<Double> top = new ArrayList<>();
	private List<Double> right = new ArrayList<>();
	private List<Double> bottom = new ArrayList<>();
	private List<Clip> clips = new ArrayList<>();
	
	public void select(Board board, double x, double y) {
		clips.clear(); left.clear(); top.clear(); right.clear(); bottom.clear();
		
		for (Clip clip: board.getContents()) {
			if (clip.isSelected(x,y)) {
				clips.add(clip);
				left.add(clip.getLeft());
				top.add(clip.getTop());
				right.add(clip.getRight());
				bottom.add(clip.getBottom());

				break;
			}
		}
	}

	public void toggleSelect(Board board, double x, double y) {
		for (Clip clip: board.getContents()) {
			if (clip.isSelected(x,y)) {
				if (clips.contains(clip)) {
					clips.remove(clip);
					left.remove(clip.getLeft());
					top.remove(clip.getTop());
					right.remove(clip.getRight());
					bottom.remove(clip.getBottom());
				}
				else {
					clips.add(clip);
					left.add(clip.getLeft());
					top.add(clip.getTop());
					right.add(clip.getRight());
					bottom.add(clip.getBottom());
				}
				break;
			}
		}
	}
	
	public void clear() {
		clips.clear();
	}
	 
	public List<Clip> getContents(){
		return clips;
	}
	
	 public void drawFeedback(GraphicsContext gc) {	
		 double minLeft = Collections.min(left);
		 double minTop = Collections.min(top);
		 double width = Collections.max(right) - minLeft;
		 double heigth = Collections.max(bottom) - minTop; 
			
		 gc.strokeRect(minLeft, minTop, width, heigth);
	 }
	 
	 public double getLeft() {
		 return Collections.min(left);
	 }
	 public double getTop() {
		 return Collections.min(top);
	 }
	 public double getRight() {
		 return Collections.max(right);
	 }
	 public double getBottom() {
		 return Collections.max(bottom);
	 }
	 public void move(double x, double y) {
		 left.add(getLeft() + x);
		 top.add(getTop() + y);
		 right.add(getRight() + x);
		 bottom.add(getBottom() + y);
	 }
		
	 public boolean isSelected(double x, double y) {
		 return (x >= getLeft() && x <= getRight()) && (y >= getTop() && y <= getBottom());
	 }
}
