package se.afsa.evolutionai.stage;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import se.afsa.evolutionai.entities.Entity;

public class Stage extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3562829420792965293L;
	private List<Entity> entities = new ArrayList<>();
	
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
	
	public void setEntities(List<Entity> entityList) {
		entities.addAll(entityList);
	}

}
