package se.afsa.evolutionai.ui;

import java.util.List;

import se.afsa.evolutionai.engine.BehaviorData;
import se.afsa.evolutionai.engine.Breeder;
import se.afsa.evolutionai.engine.Engine;
import se.afsa.evolutionai.engine.GameMode;
import se.afsa.evolutionai.event.GameEventHandler;
import se.afsa.evolutionai.stage.GUIStage;
import se.afsa.evolutionai.stage.Stage;


public class UI {
	
	private GameEventHandler gameEventHandler = new GameEventHandler();
	private Breeder breeder;
	private Engine engine;
	
	public void startGUIGame() {
		startGUIGame(null);
	}

	public void startGUIGame(List<BehaviorData> behaviorData) {
		// TODO Auto-generated method stub
		
		GUIStage guiStage = new GUIStage();
		guiStage.init();
		
		guiStage.addEnities(behaviorData, true);
		
		engine = new Engine(guiStage, GameMode.ALL_PLAYERS_DEAD, 60);
	}
	
	public void startNonGUIGame(int turns) {
		Stage stage = new Stage();
		stage.addEnities(null, true);
		breeder = new Breeder(turns, this);
		gameEventHandler.addGameListener(breeder);
		engine = new Engine(stage, GameMode.HALF_ALIVE, 0);
	}
}
