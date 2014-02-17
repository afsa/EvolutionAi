package se.afsa.evolutionai.entities;

import java.util.List;

import se.afsa.evolutionai.engine.Vector2D;

/**
 * @author Mattias Jönsson
 * An entity that can move. This, however, needs either an AI or a player to be able to move.
 */
public abstract class LivingEntity extends Entity {
	
	private static boolean livingActive = false;
	
	private static int
			defaultSpeed,
			defaultEnergy;
	
	private double energy;
	
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
			defaultSpeed = getConfig().getInt("defaultSpeed", "1");
			defaultEnergy = getConfig().getInt("defaultEnergy", "1920");
		}
		
		resetEnergy();
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
	
	/**
	 * Eat another entity.
	 * @param food - the entity to be eaten.
	 */
	public void eat(Entity food) {
		food.kill();
		this.increaseSize(food.getSize());
		resetEnergy();
	}
	
	protected void useEnergy(double energyUsed) {
		energy -= energyUsed;
		
		if(energy < 0) {
			kill();
		}
	}
	
	private void resetEnergy() {
		energy = defaultEnergy;
	}
	
	@Override
	public void reload() {
		super.reload();
		resetEnergy();
	}
}