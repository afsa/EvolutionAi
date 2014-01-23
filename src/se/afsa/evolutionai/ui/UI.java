package se.afsa.evolutionai.ui;

import java.awt.Color;

import se.afsa.evolutionai.engine.BehaviorData;
import se.afsa.evolutionai.engine.Breeder;
import se.afsa.evolutionai.engine.Engine;
import se.afsa.evolutionai.engine.GameMode;
import se.afsa.evolutionai.entities.ComputerPlayer;
import se.afsa.evolutionai.entities.Plant;
import se.afsa.evolutionai.entities.Player;
import se.afsa.evolutionai.event.GameEventHandler;
import se.afsa.evolutionai.stage.GUIStage;
import se.afsa.evolutionai.stage.Stage;


public class UI {
	
	private GameEventHandler gameEventHandler = new GameEventHandler();
	private Breeder breeder;
	private Engine engine;
	
	private int
			radiusPlayer = 300,
			radiusPlants = 400,
			numberOfEntities = 10;
	private double
			startSizePlayer = 20,
			startSizePlant = 15;

	public void startGUIGame() {
		// TODO Auto-generated method stub
		
		GUIStage guiStage = new GUIStage();
		guiStage.init();
		
		addEnities(guiStage, true);
		
		engine = new Engine(guiStage, GameMode.ALL_PLAYERS_DEAD, 60);
	}
	
	public void startNonGUIGame(int turns) {
		Stage stage = new Stage();
		addEnities(stage, false);
		engine = new Engine(stage, GameMode.HALF_ALIVE, 0);
		breeder = new Breeder(100, this);
		gameEventHandler.addGameListener(breeder);
	}
	
	private void addEnities(Stage stage, boolean player) {
		for (int i = 0; i < numberOfEntities; i++) {
			double x = Math.cos(2*Math.PI*i/numberOfEntities);
			double y = Math.sin(2*Math.PI*i/numberOfEntities);
			if(i == 0 && player) {
				stage.addEntity(new Player(startSizePlayer, x * radiusPlayer, y * radiusPlayer, new Color(0, 0, 128)));
			} else {
				stage.addEntity(new ComputerPlayer(startSizePlayer, x * radiusPlayer, y * radiusPlayer, new BehaviorData()));
			}
			stage.addEntity(new Plant(startSizePlant, x * radiusPlants, y * radiusPlants));
		}
	}
}
