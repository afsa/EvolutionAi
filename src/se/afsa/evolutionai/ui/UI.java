package se.afsa.evolutionai.ui;

import java.awt.Color;

import javax.swing.JFrame;

import se.afsa.evolutionai.engine.BehaviorData;
import se.afsa.evolutionai.engine.Engine;
import se.afsa.evolutionai.engine.GameMode;
import se.afsa.evolutionai.entities.ComputerPlayer;
import se.afsa.evolutionai.entities.Plant;
import se.afsa.evolutionai.entities.Player;
import se.afsa.evolutionai.stage.GUIStage;


public class UI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6952632673558449187L;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 int
			radiusPlayer = 300,
			radiusPlants = 400,
			numberOfEntities = 10;
		 double
			startSizePlayer = 20,
			startSizePlant = 15;
		
		GUIStage guiStage = new GUIStage();
		guiStage.init();
		
		for (int i = 0; i < numberOfEntities; i++) {
			double x = Math.cos(2*Math.PI*i/numberOfEntities);
			double y = Math.sin(2*Math.PI*i/numberOfEntities);
			if(i == 0) {
				guiStage.addEntity(new Player(startSizePlayer, x * radiusPlayer, y * radiusPlayer, new Color(0, 0, 128)));
			} else {
				guiStage.addEntity(new ComputerPlayer(startSizePlayer, x * radiusPlayer, y * radiusPlayer, new BehaviorData()));
			}
			guiStage.addEntity(new Plant(startSizePlant, x * radiusPlants, y * radiusPlants));
		}
		
		new Engine(guiStage, GameMode.ALL_PLAYERS_DEAD, 60);
	}
}
