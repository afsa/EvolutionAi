package se.afsa.evolutionai.entities;

import java.awt.Graphics2D;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import se.afsa.evolutionai.engine.Vector2D;
import se.afsa.evolutionai.ui.Drawable;

/**
 * Entity superclass, all entities extends from this. The entities have position, size, and color.
 */
public abstract class Entity implements Drawable {
	
	private final static String propertiesLocation = "se/afsa/evolutionai/resource/data/config.properties";
	private static Properties properties = new Properties();
	private static boolean active = false;
	
	private static int 
			posMaxX,
			posMaxY;
	
	private Vector2D pos = new Vector2D();
	private double size;
	
	private final Vector2D startPos;
	private final double startSize;
	
	private boolean isAlive = true;
	
	/**
	 * Create an entity.
	 * Loads the properties file if when first entity is created.
	 * @param size - size of the entity
	 * @param x - x coordinate
	 * @param y - y coordinate
	 */
	protected Entity(double size, double x, double y) {
		if(!active) {
			active = true;
			try {
				InputStream is = getClass().getClassLoader().getResourceAsStream(propertiesLocation);
				properties.load(is);
				is.close();
				
				//Load default data
				posMaxX = Integer.parseInt(properties.getProperty("posMaxX", "0"));
				posMaxY = Integer.parseInt(properties.getProperty("posMaxY", "0"));
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				// Throw error event!!
				e.printStackTrace();
			}
		}
		
		setSize(size);
		setLocation(x, y);
		
		startSize = size;
		startPos = new Vector2D(x, y);
	}
	
	
	/**
	 * Defines how the entity will be drawn. Called by stage each frame, should not be called.
	 * @param graphics - the stage graphics
	 */
	public void draw(Graphics2D graphics2d) {		
		int x = (int) (getPos().getX() + getPosMaxX());
		int y = (int) (getPos().getY() + getPosMaxY());
		
		shape(graphics2d, x, y);
	}
	
	/**
	 * Defines the shape and color for the entity. 
	 * Is run by stage each frame, should not be called for other reasons.
	 * @param graphics2d - Graphics used for drawing the shape
	 * @param x - x coordinate
	 * @param y - y coordinate
	 */
	public abstract void shape(Graphics2D graphics2d, int x, int y);
	
	
	/**
	 * Get the distance between two entities
	 * @param entity - the other entity
	 * @return the distance between the two entities
	 */
	public double getDistance(Entity entity) {
		return pos.distanceTo(entity.getPos());
	}
	
	/**
	 * Sets the location of the entity to (x, y). (Hint: the center of the screen is defined as (0, 0))
	 * @param x - x coordinate
	 * @param y - y coordinate
	 */
	public void setLocation(double x, double y) {
		if(!(x <= posMaxX - getRadius() && x >= -posMaxX + getRadius() && y <= posMaxY - getRadius() && y >= -posMaxY + getRadius())) {
			kill();
		}
		pos.setLocation(x, y);
	}
	
	/**
	 * Set the location of the entity to (x, y). (Hint: the center of the screen is defined as (0, 0))
	 * @param vector2d - the coordinate vector
	 */
	public void setLocation(Vector2D vector2d) {
		setLocation(vector2d.getX(), vector2d.getY());
	}
	
	/**
	 * Get the location of the entity.
	 * @return the coordinate vector
	 */
	public Vector2D getPos() {
		return pos;
	}
	
	/**
	 * Sets entity size
	 * @param size - the new size of the entity
	 */
	public void setSize(double size) {
		this.size = size;
	}
	
	/**
	 * Get the size of the entity
	 * @return the size of the entity
	 */
	public double getSize() {
		return size;
	}
	
	/**
	 * Get the radius of the entity.
	 * @return The radius.
	 */
	public double getRadius() {
		return Math.sqrt(getSize()) * 3/2;
	}
	
	/**
	 * Check if the entity is alive.
	 * If the entity is dead it will be removed from screen if it's a living entity, non-living entities respawn.
	 * @return true if alive, false if dead
	 */
	public boolean isAlive() {
		return isAlive;
	}
	
	/**
	 * Mark the entity as dead.
	 * If the entity is dead it will be removed from screen if it's a living entity, non-living entities respawn.
	 */
	public void kill() {
		isAlive = false;
	}
	
	/**
	 * Make an entity respawn
	 */
	public void respawn() {
		setLocation(new Vector2D((getPosMaxX()-100)*(2*Math.random()-1), (getPosMaxY()-100)*(2*Math.random()-1)));
		isAlive = true;
	}
	
	/**
	 * Get properties object
	 * @return properties object
	 * @see Properties
	 */
	public Properties getProperties() {
		return properties;
	}
	
	/**
	 * Get the largest allowed absolute x coordinate value
	 * @return maximum allowed absolute x coordinate value
	 */
	public int getPosMaxX() {
		return posMaxX;
	}
	
	/**
	 * Get the largest allowed absolute y coordinate value
	 * @return largest allowed absolute y coordinate value
	 */
	public int getPosMaxY() {
		return posMaxY;
	}
	
	/**
	 * Reset the entity to the initial state it was in at game start.
	 */
	public void reload() {
		setSize(startSize);
		setLocation(startPos);
		isAlive = true;
	}
}
