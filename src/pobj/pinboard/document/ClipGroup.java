package pobj.pinboard.document;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ClipGroup implements Composite {
	private List<Clip> clips;
	private double top; 
	private double left; 
	private double bottom; 
	private double right;
	private Color color = Color.GREEN;	
	
	public ClipGroup(List<Clip> clips) {
		this.clips = new ArrayList<>();
		this.clips.addAll(clips);
		findPosition();
	}
	public ClipGroup() {clips = new ArrayList<>();}
	
	@Override
	public void draw(GraphicsContext ctx) {
		for (Clip clip: clips) clip.draw(ctx);
	}

	@Override
	public double getTop() {
		return top;
	}

	@Override
	public double getLeft() {
		return left;
	}

	@Override
	public double getBottom() {
		return bottom;
	}

	@Override
	public double getRight() {
		return right;
	}

	@Override
	public void setGeometry(double left, double top, double right, double bottom) {
		move(right - left,bottom - top);
	}

	@Override
	public void move(double x, double y) {
		for(Clip clip: clips) clip.move(x,y);
		findPosition();
		
	}

	@Override
	public boolean isSelected(double x, double y) {
		for(Clip clip: clips) if (clip.isSelected(x,y)) return true;
		return false;
	}

	@Override
	public void setColor(Color c) {
		for(Clip clip: clips) clip.setColor(c);
	}

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public Clip copy() {
		ClipGroup copy = new ClipGroup();
		for (Clip clip: clips) copy.clips.add(clip.copy());
		return copy;
	}

	@Override
	public List<Clip> getClips() {
		return clips;
	}

	@Override
	public void addClip(Clip toAdd) {
		clips.add(toAdd);
		findPosition();
	}

	@Override
	public void removeClip(Clip toRemove) {
		clips.remove(toRemove);
		findPosition();
	}
	
	private void findPosition() {
		top = 2147483647;
		left = 2147483647;
		bottom = 0;
		right = 0;
		for(Clip clip: clips) {
			if(top > clip.getTop()) top = clip.getTop();
			if(left > clip.getLeft()) left = clip.getLeft();
			if(bottom < clip.getBottom()) bottom = clip.getBottom();
			if(right < clip.getRight()) right = clip.getRight();
		}
	}
	
	public boolean contains(Clip o) {
		return clips.contains(o);
	}
}
