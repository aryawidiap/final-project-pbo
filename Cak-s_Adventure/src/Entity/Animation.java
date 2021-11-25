package Entity;

import java.awt.image.BufferedImage;

public class Animation {
	private BufferedImage[] frames; // hold all the frames
	private int currentFrame; // keep track the current frame
	
	private long startTime; // timer between the frames
	private long delay; // how long between frames
	
	private boolean playedOnce;
	
	private void Animation() {
		playedOnce = false;
	}
	
	public void setFrames(BufferedImage[] frames) {
		this.frames = frames;
		currentFrame = 0; // reset to zero
		startTime = System.nanoTime();
		playedOnce = false;
	}
	
	public void setDelay(long delay) {
		this.delay = delay;
	}
	public void setFrame(int i) {
		currentFrame = i;
	}
	public void update() {
		if(delay == -1) // no animation
			return;
		long elapsed = (System.nanoTime() - startTime) / 1000000;
		if(elapsed > delay) {
			currentFrame++;
			startTime = System.nanoTime();
		}
		if(currentFrame == frames.length) {
			currentFrame = 0;
			playedOnce = true;
		}
	}
	
	public int getFrame() {
		return currentFrame;
	}
	public BufferedImage getImage() {
		return frames[currentFrame];
	}
	public boolean hasPlayedOnce() {
		return playedOnce;
	}
}








