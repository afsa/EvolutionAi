package se.afsa.evolutionai.stage;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import se.afsa.evolutionai.engine.BehaviorData;
import se.afsa.evolutionai.entities.ComputerPlayer;
import se.afsa.evolutionai.entities.Entity;
import se.afsa.evolutionai.entities.LivingEntity;
import se.afsa.evolutionai.entities.Plant;
import se.afsa.evolutionai.entities.Player;

public class Stage extends JPanel {
	/**
	 *  A class that takes care of the entities in the game. This does not support GUI.
	 *  @see GUIStage
	 */
	private static final long serialVersionUID = 3562829420792965293L;
	private List<Entity> entities = new ArrayList<>();
	private List<Entity> aliveEntities = new ArrayList<>();
	private List<LivingEntity> aliveLivingEntities = new ArrayList<>();
	private int totalPlayed;
	
	private int 
			numberOfPlayers = 0,
			numberOfComputerPlayers = 0;
	
	private int
			radiusPlayer = 300,
			radiusPlants = 400,
			numberOfEntities = 10;
	private double
			startSizePlayer = 20,
			startSizePlant = 15;
	
	/**
	 * Get all entities in the game, also the dead.
	 * @return A list of the entities.
	 */
	public List<Entity> getEntities() {
		return entities;
	}
	
	/**
	 * Get all entities in the game with class T.
	 * @param entityClass - classType, example LivingEnitiy.class
	 * @return A list of the entities.
	 */
	@SuppressWarnings("unchecked")
	public <T extends Entity> List<T> getEntities(Class<T> entityClass) {
		List<T> temp = new ArrayList<>();
		
		for (int i = 0; i < entities.size(); i++) {
			Entity tempEntity = entities.get(i);
			if(entityClass.isInstance(tempEntity)) {
				temp.add((T) tempEntity);
			}
		}
		
		return temp;
	}
	
	/**
	 * Get a entity with index i, if out of bound null is returned.
	 * @param i - index
	 * @return The entity, if out of bounds null is returned.
	 */
	public Entity getEntity(int i) {
		if(entities.size() > i) {
			return entities.get(i);
		}
		return null;
	}
	
	/**
	 * Add an entity to all entities in the game. Method reloadEntities must be called for the changes to apply.
	 * @param entity - the new entity
	 */
	public void addEntity(Entity entity) {
		entities.add(entity);
	}
	
	/**
	 * Add entities to the game, this will overwrite previous entities and automatically reload the entities.
	 * @param behaviorData - generated data for computer players
	 * @param player - the number of players
	 */
	public void addEnities(List<BehaviorData> behaviorData, int player) {
		entities = new ArrayList<>();
		for (int i = 0; i < numberOfEntities; i++) {
			double x = Math.cos(2*Math.PI*i/numberOfEntities);
			double y = Math.sin(2*Math.PI*i/numberOfEntities);
			if(player > i) {
				addEntity(new Player(startSizePlayer, x * radiusPlayer, y * radiusPlayer, new Color(0, 0, 128)));
			} else {
				addEntity(new ComputerPlayer(startSizePlayer, x * radiusPlayer, y * radiusPlayer, (behaviorData != null && behaviorData.size() > i) ? behaviorData.get(i) : new BehaviorData()));
			}
			addEntity(new Plant(startSizePlant, x * radiusPlants, y * radiusPlants));
		}
		
		reloadEntities();
	}
	
	/**
	 * Add a list of entities to all entities in the game.  Method reloadEntities must be called for the changes to apply.
	 * @param entityList - the list of entities.
	 */
	public void setEntities(List<Entity> entityList) {
		entities.addAll(entityList);
	}
	
	/**
	 * Reload the entities that are in the game. All entities will respawn and move to their start position.
	 */
	public void reloadEntities() {
		aliveLivingEntities.clear();
		aliveEntities.clear();
		numberOfComputerPlayers = 0;
		numberOfPlayers = 0;
		
		totalPlayed = 0;
		
		for (int i = 0; i < entities.size(); i++) {
			Entity temp = entities.get(i);
			temp.reload();
			
			aliveEntities.add(temp);
			if(temp instanceof LivingEntity) {
				totalPlayed++;
				aliveLivingEntities.add((LivingEntity) temp);
				numberOfPlayers += (temp instanceof Player) ? 1 : 0;
				numberOfComputerPlayers += (temp instanceof ComputerPlayer) ? 1 : 0;
			}
		}
	}
	
	/**
	 * Get a list of entities that are alive in the game. Calls with Entity.class and LivingEntity.class is O(1) and other is O(n).
	 * @param entityClass - the class of the entities
	 * @return A list of the entities
	 */
	@SuppressWarnings("unchecked")
	public <T extends Entity> List<T> getAliveEntities(Class<T> entityClass) {
		if(entityClass == Entity.class) {
			return (List<T>) aliveEntities;
		}
		
		if(entityClass == LivingEntity.class) {
			return (List<T>) aliveLivingEntities;
		}
		
		List<T> temp = new ArrayList<>();
		for (int i = 0; i < aliveEntities.size(); i++) {
			Entity tempEntity = aliveEntities.get(i);
			if(entityClass.isInstance(tempEntity)) {
				temp.add((T) tempEntity);
			}
		}
		
		return temp;
	}
	
	/**
	 * Kills and removes an entity. The entities will still be in the game (use getEntities() method).
	 * @param entity - 
	 */
	public void removeDeadEntity(Entity entity) {
		entity.kill();
		
		if(entity instanceof Plant) {
			entity.respawn();
		} else {
			aliveLivingEntities.remove((LivingEntity) entity);
			aliveEntities.remove(entity);
		}
	}
	
	/**
	 * Get the number of living entities in the game.
	 * @return
	 */
	public int getTotalPlayed() {
		return totalPlayed;
	}
	
	/**
	 * Checks game for colliding entities. If collision is found the game handles it.
	 */
	public void collisionDetector() {
		int n = 1;
		int entitySize = aliveEntities.size();
		for (int i = 0; i < entitySize; i++) {
			for (int j = n; j < entitySize; j++) {
				Entity entity1 = aliveEntities.get(i);
				Entity entity2 = aliveEntities.get(j);
				if(entity1.getDistance(entity2) < entity1.getRadius() + entity2.getRadius()) {
					handleCollision(entity1, entity2);
				}
			}
			n++;
		}
	}

	/**
	 * Make sure which entity is eater and which is food.
	 * @param entity1 - the first entity
	 * @param entity2 - the second entity
	 */
	private void handleCollision(Entity entity1, Entity entity2) {
		// TODO Auto-generated method stub
		switch ((int) Math.signum(entity1.getSize()-entity2.getSize())) {
		case 1:
			checkEat(entity1, entity2);
			break;
			
		case -1:
			checkEat(entity2, entity1);
			break;

		default:
			entity1.kill();
			entity2.kill();
			break;
		}
	}
	
	/**
	 * Check if the eater is an living entity. If so perform eat, else just kill the food.
	 * @param eater
	 * @param food
	 */
	private void checkEat(Entity eater, Entity food) {
		if(eater instanceof LivingEntity) {
			((LivingEntity) eater).eat(food);
		} else {
			food.kill();
		}
	}
	
	/**
	 * Run the game mechanics. Is run by the engine every frame, should not be called for other reasons.
	 * @param movementAmplifier - the amplifier describing if the game is in phase or if it need to speed up/slow down.
	 */
	public void runMechanics(double movementAmplifier) {
		numberOfPlayers = 0;
		numberOfComputerPlayers = 0;
		
		for (int i = 0; i < aliveLivingEntities.size(); i++) {
			LivingEntity tempEntity = aliveLivingEntities.get(i);
			if(tempEntity.isAlive()) {
				numberOfPlayers += (tempEntity instanceof Player) ? 1 : 0;
				numberOfComputerPlayers += (tempEntity instanceof ComputerPlayer) ? 1 : 0;
				tempEntity.runFrame(entities, movementAmplifier);
			} else {
				removeDeadEntity(tempEntity);
			}
		}
	}
	
	/**
	 * Get the amount of players alive.
	 * @return The number of players alive.
	 */
	public int getPlayerCount() {
		return numberOfPlayers;
	}
	
	/**
	 * Get the amount of computer players alive.
	 * @return The number of computer players alive.
	 */
	public int getComputerPlayerCount() {
		return numberOfComputerPlayers;
	}

}
