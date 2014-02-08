package se.afsa.evolutionai.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import se.afsa.evolutionai.entities.ComputerPlayer;
import se.afsa.evolutionai.event.GameEvent;
import se.afsa.evolutionai.event.GameEventType;
import se.afsa.evolutionai.event.GameListener;
import se.afsa.evolutionai.resource.Config;
import se.afsa.evolutionai.resource.FileHandler;
import se.afsa.evolutionai.ui.UI;

/**
 * Runs the game automatically by extracting and reentering entities into the game.
 * Is used for generating levels.
 */
public class Breeder implements GameListener {
	
	private int 
			turns,
			turnsLeft;
	private UI ui;
	
	private Config config = new Config();

	/**
	 * Set the amount of turns the breeder should run.
	 * @param turns - the amount of turns.
	 * @param ui - the UI in which the game is run.
	 */
	public Breeder(int turns, UI ui) {
		this.turns = turns;
		this.turnsLeft = turns;
		this.ui = ui;
	}
	
	/**
	 * Generate new children data from the survivors from the last run.
	 * @param parents - the entities that survived.
	 * @return A list of behavior data for the new entities.
	 */
	private List<BehaviorData> getChildrenData(List<ComputerPlayer> parents) {
		List<BehaviorData> children = new ArrayList<>();
		int parentLength = parents.size();
		int childPerParent = config.getInt("numberOfEntities", "10")/parentLength;
		for (int i = 0; i < parentLength; i++) {
			for (int j = 0; j < childPerParent; j++) {
				children.add(parents.get(i).getBehaviorData().createChildren(parents.get((i+1)%parentLength).getBehaviorData()));
			}
		}
		return children;
	}

	// Detect when to reenter and extract the entities.
	@Override
	public void handleEvent(GameEvent event) {
		// TODO Auto-generated method stub
		if(event.getEventType() == GameEventType.STOP) {
			List<ComputerPlayer> computerPlayers = event.getEngine().getStage().getEntities(ComputerPlayer.class);
			List<BehaviorData> childrenBehaviorData = getChildrenData(computerPlayers);
			
			if(turnsLeft > 0) {
				turnsLeft--;
				System.out.println(turns-turnsLeft);
				event.getEngine().getStage().addEnities(childrenBehaviorData, 0);
				event.getEngine().reload();
			} else {
				new FileHandler().save(new File("test.bin"), childrenBehaviorData);
				event.getEngine().getGameEventHandler().removeGameListener(this);
				ui.startGUIGame(childrenBehaviorData);
			}
		}
	}

}
