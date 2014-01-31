package se.afsa.evolutionai.entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import se.afsa.evolutionai.engine.Keys;


/**
 * An entity controlled by the player.
 * @see LivingEntity
 */
public class Player extends LivingEntity implements KeyListener {

	private Color color;
	private Keys controls ;

	/**
	 * Create a player. Players are controlled by the user. Multiple players are possible if they are set with different keys. Default keys are W,A,S,D.
	 * @param size - size of the entity
	 * @param x - x coordinate
	 * @param y - y coordinate
	 * @param speed - the speed amplifier
	 * @param efficiency - the efficiency amplifier
	 * @param color - color of the players entity
	 */
	public Player(double size, double x, double y, Color color) {
		super(size, x, y);
		// TODO Auto-generated constructor stub
		this.color = color;
		controls = new Keys();
	}
	
	/**
	 * Create a player. Players are controlled by the user. Multiple players are possible if they are set with different keys.
	 * @param size - size of the entity
	 * @param x - x coordinate
	 * @param y - y coordinate
	 * @param speed - the speed amplifier
	 * @param efficiency - the efficiency amplifier
	 * @param color - color of the players entity
	 * @param keys - set keys in order right, up, left, down (use KeyEvent)
	 */
	public Player(double size, double x, double y, Color color, int[] keys) {
		super(size, x, y);
		// TODO Auto-generated constructor stub
		
		this.color = color;
		controls = new Keys(keys);
	}

	@Override
	public void shape(Graphics2D graphics2d, int x, int y) {
		// TODO Auto-generated method stub        
        int circleEdge = (int) (2 * getRadius());
        
        graphics2d.setColor(color);
        graphics2d.fillOval(x-circleEdge/2, y-circleEdge/2, circleEdge, circleEdge);
	}

	@Override
	public void runFrame(List<Entity> entities, double movementAmplifier) {
		// TODO Auto-generated method stub
		move(controls.getDirection().weight(getSpeed()).weight(movementAmplifier));
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		controls.keyPressed(e.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		controls.keyReleased(e.getKeyCode());
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}
}
