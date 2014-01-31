package se.afsa.evolutionai.engine;

import java.awt.Color;
import java.awt.Graphics2D;

import se.afsa.evolutionai.ui.Drawable;

/**
 * Class for calculating and displaying the FPS of the game.
 */
public class FPSCounter implements Drawable {
	private Integer FPS = 0;
	private int counter = 0;
	private long timeElapsed = 0;
	private boolean showFPS;
	private int
			x,
			y;
	
	/**
	 * Create a new FPSCounter.
	 * @param x - x coordinate.
	 * @param y - y coordinate.
	 * @param showFPS - if the counter should be viewed.
	 */
	public FPSCounter(int x, int y, boolean showFPS) {
		this.showFPS = showFPS;
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Call on every frame to be able to detect the FPS.
	 * @param interval - the interval since the last frame.
	 */
	public void countFrame(long interval) {
		timeElapsed += interval;
		counter++;
		
		if(timeElapsed >= 1000000000) {
			FPS = counter;
			counter = 0;
			timeElapsed = 0;
		}
	}
	
	/**
	 * Get the current FPS.
	 * @return The FPS.
	 */
	public int getFPS() {
		return FPS;
	}
	
	/**
	 * Toggle if the FPS should be viewed.
	 */
	public void toogleFPS() {
		showFPS = !showFPS;
	}

	@Override
	public void draw(Graphics2D graphics2d) {
		// TODO Auto-generated method stub
		if(showFPS) {
			graphics2d.setColor(new Color(0, 0, 0));
			graphics2d.drawString("FPS: " + FPS.toString(), x, y);
		}
	}
}
