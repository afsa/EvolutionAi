package se.afsa.evolutionai.ui;

import java.awt.Graphics2D;

/**
 * Makes classes drawable.
 */
public interface Drawable {
	/**
	 * Called when the stage calls for a frame update. Should not be called for other reasons.
	 * @param graphics2d - the graphics used to draw shapes.
	 */
	public void draw(Graphics2D graphics2d);
}
