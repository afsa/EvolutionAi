package se.afsa.evolutionai.engine;

import java.util.List;

import se.afsa.evolutionai.entities.ComputerPlayer;
import se.afsa.evolutionai.event.GameEvent;
import se.afsa.evolutionai.event.GameEventType;
import se.afsa.evolutionai.event.GameListener;
import se.afsa.evolutionai.ui.UI;

public class Breeder implements GameListener {
	
	private int 
			turns,
			turnsLeft;
	private UI ui;

	public Breeder(int turns, UI ui) {
		this.turns = turns;
		this.turnsLeft = turns;
		this.ui = ui;
	}

	@Override
	public void handleEvent(GameEvent event) {
		// TODO Auto-generated method stub
		if(event.getEventType() == GameEventType.STOP && turnsLeft > 0) {
			turnsLeft--;
			List<ComputerPlayer> computerPlayer = event.getEngine().getStage().getEntities(ComputerPlayer.class);
			
		}
	}

}
