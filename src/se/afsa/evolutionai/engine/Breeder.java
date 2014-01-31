package se.afsa.evolutionai.engine;

import java.util.ArrayList;
import java.util.List;

import se.afsa.evolutionai.entities.ComputerPlayer;
import se.afsa.evolutionai.event.GameEvent;
import se.afsa.evolutionai.event.GameEventType;
import se.afsa.evolutionai.event.GameListener;
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
		for (int i = 0; i < parentLength; i++) {
			for (int j = 0; j < 10; j++) {
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
				event.getEngine().getGameEventHandler().removeGameListener(this);
				ui.startGUIGame(childrenBehaviorData);
			}
		}
	}

}
