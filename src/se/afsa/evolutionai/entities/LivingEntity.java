package se.afsa.evolutionai.entities;

import java.util.List;

import se.afsa.evolutionai.engine.Vector2D;

public abstract class LivingEntity extends Entity {
	
	private static boolean livingActive = false;
	
	private static double defaultSpeed;
	
	/**
	 * Create a living entity.
	 * Loads the properties file if when first entity is created.
	 * Speed + efficiency = 1 if not they will be scaled so it applies.
	 * @param size - size of the entity
	 * @param x - x coordinate
	 * @param y - y coordinate
	 * @param speed - the speed amplifier
	 * @param efficiency - the efficiency amplifier
	 */
	protected LivingEntity(double size, double x, double y) {
		super(size, x, y);
		if(!livingActive) {
			livingActive = true;
			defaultSpeed = Double.parseDouble(getProperties().getProperty("defaultSpeed", "1"));
		}	
	}
	
	/**
	 * Is run by engine each frame, should not be called for other reasons.
	 * If the entity has an AI, it will respond to the actions of the other entities in the list.
	 * @param entities - a list of entities
	 */
	public abstract void runFrame(List<Entity> entities, double movementAmplifier);
	
	/**
	 * Get speed amplifier
	 * @return speed amplifier
	 */
	public double getSpeed() {
		return defaultSpeed;
	}
	
	/**
	 * Move the entity dx in x and dy in y.
	 * New position (x, y) + (dx, dy).
	 * @param dx - the movement in x
	 * @param dy - the movement i y
	 */
	public void move(double dx, double dy) {
		move(new Vector2D(dx, dy));
	}
	
	/**
	 * Move the entity by adding the vector to its current position
	 * @param vector2d - the movement vector
	 */
	public void move(Vector2D vector2d) {
		setLocation(getPos().add(vector2d));
	}
	
	/**
	 * Increase the size of the entity. The size increase is affected by the efficiency amplifier.
	 * @param size - size to increase
	 */
	public void increaseSize(double size) {
		setSize(getSize() + size);
	}
	
	public void eat(Entity food) {
		food.kill();
		this.increaseSize(food.getSize());
	}
}
