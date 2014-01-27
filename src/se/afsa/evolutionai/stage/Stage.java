package se.afsa.evolutionai.stage;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import se.afsa.evolutionai.engine.BehaviorData;
import se.afsa.evolutionai.entities.ComputerPlayer;
import se.afsa.evolutionai.entities.Entity;
import se.afsa.evolutionai.entities.Plant;
import se.afsa.evolutionai.entities.Player;

public class Stage extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3562829420792965293L;
	private List<Entity> entities = new ArrayList<>();
	
	private int
			radiusPlayer = 300,
			radiusPlants = 400,
			numberOfEntities = 10;
	private double
			startSizePlayer = 20,
			startSizePlant = 15;
	
	public List<Entity> getEntities() {
		return entities;
	}
	
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
	
	public Entity getEntity(int i) {
		return entities.get(i);
	}
	
	public void addEntity(Entity entity) {
		entities.add(entity);
	}
	
	public void addEnities(List<BehaviorData> behaviorData, boolean player) {
		entities = new ArrayList<>();
		for (int i = 0; i < numberOfEntities; i++) {
			double x = Math.cos(2*Math.PI*i/numberOfEntities);
			double y = Math.sin(2*Math.PI*i/numberOfEntities);
			if(i == 0 && player) {
				addEntity(new Player(startSizePlayer, x * radiusPlayer, y * radiusPlayer, new Color(0, 0, 128)));
			} else {
				addEntity(new ComputerPlayer(startSizePlayer, x * radiusPlayer, y * radiusPlayer, (behaviorData != null && behaviorData.size() > i) ? behaviorData.get(i) : new BehaviorData()));
			}
			addEntity(new Plant(startSizePlant, x * radiusPlants, y * radiusPlants));
		}
	}
	
	public void setEntities(List<Entity> entityList) {
		entities.addAll(entityList);
	}

}
