package se.afsa.evolutionai.entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import se.afsa.evolutionai.engine.BehaviorData;
import se.afsa.evolutionai.engine.Vector2D;


public class ComputerPlayer extends LivingEntity {
	
	private BehaviorData bd;

	/**
	 * Computer players or AIs are the opponents in the game. They are unlike the players controlled by the computer using an AI code.
	 * @param size - the size of the computer player
	 * @param x - the x coordinate
	 * @param y - the y coordinate
	 * @param behaviorData - the data needed for the computer to run the AI
	 */
	public ComputerPlayer(double size, double x, double y, BehaviorData behaviorData) {
		super(size, x, y);
		// TODO Auto-generated constructor stub
		this.bd = behaviorData;
	}

	@Override
	public void shape(Graphics2D graphics2d, int x, int y) {
		// TODO Auto-generated method stub        
		int circleEdge = (int) (2 * getRadius());
        
		graphics2d.setColor(new Color(230, 0, 0));
		graphics2d.fillOval(x-circleEdge/2, y-circleEdge/2, circleEdge, circleEdge);
	}
	
	/**
	 * Get the vector between two entities. 
	 * @param entity - the other entity
	 * @return the vector between the entities, with origin in this entity.
	 */
	private Vector2D vectorToTarget(Entity entity) {
		return getPos().from(entity.getPos());
	}
	
	/**
	 * Get the amplifier based on the distance to the other entity.
	 * @param livingEntity - the other entity
	 * @return a number describing the distance
	 */
	private double distanceAmplifier(Entity entity) {
		double dist = bd.getDistanceConstant() + bd.getDistanceAmplifier() * getDistance(entity) - getRadius() - entity.getRadius();
		return (dist >= 0) ? Math.pow(dist, -bd.getDistancePower()) : 0;
	}

	/**
	 * Get the amplifier based on the size of the other entity.
	 * @param livingEntity - the other entity
	 * @return a number describing the size difference
	 */
	private double targetAmplifier(Entity entity) {
		double targetSize = entity.getSize();
		double size = getSize();
		if(targetSize >= size) {
			return Math.abs(bd.getTargetAmplifier() * targetSize - bd.getTargetSelfAmplifier() * size + bd.getTargetConstant());
		} else {
			return Math.abs(bd.getTargetAmplifier()*targetSize*(targetSize - size * bd.getTargetSelfAmplifier()) + bd.getTargetConstant());
		}
	}
	
	private Vector2D wallDetection() {
		double x = getPos().getX();
		double y = getPos().getY();
		double distX = Math.pow(bd.getWallConstant()*(getPosMaxX() - Math.abs(x) - getRadius()), -bd.getWallPower());
		double distY = Math.pow(bd.getWallConstant()*(getPosMaxY() - Math.abs(y) - getRadius()), -bd.getWallPower());
		Vector2D detectX = new Vector2D(-x, 0).normalize().weight(distX);
		Vector2D detectY = new Vector2D(0, -y).normalize().weight(distY);
		return detectX.add(detectY).weight(bd.getWallAmplifier());
	}

	/**
	 * Makes the AI decide how the entity should move.
	 * @param entities - a list of all entities
	 * @return a vector (dx, dy)
	 */
	public Vector2D movement(List<Entity> entities) {
		Vector2D delta = new Vector2D();
		for (int i = 0; i < entities.size(); i++) {
			
			if(entities.get(i) == this) {
				continue;
			}
			
			Vector2D temp = new Vector2D();
			temp.add(vectorToTarget(entities.get(i)).normalize()).weight(targetAmplifier(entities.get(i))).weight(distanceAmplifier(entities.get(i)));
			
			if(getSize() > entities.get(i).getSize()) {
				delta.add(temp.weight(bd.getOffenseAmplifier()));
			} else {
				delta.subtract(temp.weight(bd.getDefenceAmplifier()));
			}
		}
		
		delta.add(wallDetection());
		
		delta = (delta.length() == 0) ? getRandomDecision() : delta;
		
		return delta.quantize().normalize();
	}
	
	/**
	 * In case of inconclusive AI take a random vector
	 * @return
	 */
	private Vector2D getRandomDecision() {
		return new Vector2D((Math.random() * 2) - 1, (Math.random() * 2) - 1);
	}
	
	public BehaviorData getBehaviorData() {
		return bd;
	}

	@Override
	public void runFrame(List<Entity> entities, double movementAmplifier) {
		// TODO Auto-generated method stub
		move(movement(entities).weight(getSpeed()).weight(movementAmplifier));
	}
}