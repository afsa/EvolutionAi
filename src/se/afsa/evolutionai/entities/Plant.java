package se.afsa.evolutionai.entities;

import java.awt.Color;
import java.awt.Graphics2D;


public class Plant extends Entity {

	/**
	 * Create a new plant. Plants can't move and are used so players can grow without eating other players. They should be smaller than the players.
	 * @param size - size of the entity
	 * @param x - x coordinate
	 * @param y - y coordinate
	 */
	public Plant(double size, double x, double y) {
		super(size, x, y);
		// TODO Auto-generated constructor stub
	}
	
	// Plants do not die, they increase their size and respawn.
	@Override
	public void kill() {
		setSize((getSize() < 1000) ? getSize() * (1 + Math.random()) : getSize());
		super.respawn();
	}

	@Override
	public void shape(Graphics2D graphics2d, int x, int y) {
		// TODO Auto-generated method stub
		int circleEdge = (int) (2 * getRadius());
        
        graphics2d.setColor(new Color(0, 128, 0));
        graphics2d.fillOval(x-circleEdge/2, y-circleEdge/2, circleEdge, circleEdge);
	}

}
