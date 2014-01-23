package se.afsa.evolutionai.engine;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import se.afsa.evolutionai.entities.ComputerPlayer;
import se.afsa.evolutionai.entities.Entity;
import se.afsa.evolutionai.entities.LivingEntity;
import se.afsa.evolutionai.entities.Player;
import se.afsa.evolutionai.stage.GUIStage;
import se.afsa.evolutionai.stage.Stage;

public class Engine {
	
	private int FPS;
	private GameMode gameMode;
	private Thread loop;
	private boolean isRunning = false;
	private static Stage stage;
	private List<Player> players;
	private List<ComputerPlayer> computerPlayers;
	private List<Entity> entities;
	private int totalPlayed;
	private String toggleFPS = "ActionToggleFPS";
	private String pauseGame = "ActionPauseGame";
	private FPSCounter FPScounter = new FPSCounter(20, 20, false);
	
	public Engine(Stage gameStage, GameMode gameMode, int FPS) {
		stage = gameStage;
		setFPS(FPS);
		setGameMode(gameMode);
		if(stage instanceof GUIStage) {
			((GUIStage) stage).addGraphicalItem(FPScounter);
			
			addControlButtons(KeyEvent.VK_F1, toggleFPS, new AbstractAction() {
				private static final long serialVersionUID = -6274004520654911374L;

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					FPScounter.toogleFPS();
					stage.repaint();
				}
			});
			addControlButtons(KeyEvent.VK_SPACE, pauseGame, new AbstractAction() {
				private static final long serialVersionUID = -5363193342610765472L;

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					togglePause();
				}
			});
		}
		
		loop = new Thread() {
			public void run() {
				reloadEntities();
				
				if(stage instanceof GUIStage) {
					guiGameLoop();
				} else {
					gameLoop();
				}
			}
		};
		
		loop.start();
	}

	private void gameLoop() {
		while(isRunning) {
			runMechanics(1);
		}
	}
	
	private void guiGameLoop() {
		long intervalTargetLength = 1000000000 / FPS;
		long lastFrame = System.nanoTime();
		
		while(true) {
			if(!isRunning) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				lastFrame = System.nanoTime();
				continue;
			}
			
			long now = System.nanoTime();
			long intervalLength = now - lastFrame;
			lastFrame = now;
			
			double movementAmplifier = 60*(double)intervalLength/1000000000;
			runMechanics(movementAmplifier);
			
			FPScounter.countFrame(intervalLength);
			
			((GUIStage) stage).repaint();
			
			try {
				Thread.sleep((intervalTargetLength - (System.nanoTime() - now))/1000000);
			} catch (InterruptedException e ) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO: handle exception
				// Game is lagging and the time to sleep is negative.
				// Ignore exception and continue running.
			}
		}
	}

	private void runMechanics(double movementAmplifier) {
		for (int i = 0; i < players.size(); i++) {
			LivingEntity tempEntity = players.get(i);
			if(tempEntity.isAlive()) {
				tempEntity.runFrame(entities, movementAmplifier);
			} else {
				players.remove(tempEntity);
				entities.remove(tempEntity);
			}
		}
		
		for (int i = 0; i < computerPlayers.size(); i++) {
			LivingEntity tempEntity = computerPlayers.get(i);
			if(tempEntity.isAlive()) {
				tempEntity.runFrame(entities, movementAmplifier);
			} else {
				computerPlayers.remove(tempEntity);
				entities.remove(tempEntity);
			}
		}
		
		collisionDetector(entities);
		
		if(gameMode.isFinished(players.size(), computerPlayers.size(), totalPlayed)) {
			stop();
		}
	}
	
	public void togglePause() {
		isRunning = !isRunning;
	}
	
	public void start() {
		isRunning = true;
	}
	
	public void stop() {
		isRunning = false;
	}
	
	private void collisionDetector(List<Entity> entities) {
		int n = 1;
		int entitySize = entities.size();
		for (int i = 0; i < entitySize; i++) {
			for (int j = n; j < entitySize; j++) {
				Entity entity1 = entities.get(i);
				Entity entity2 = entities.get(j);
				if(entity1.getDistance(entity2) < entity1.getRadius() + entity2.getRadius()) {
					handleCollision(entity1, entity2);
				}
			}
			n++;
		}
	}

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
	
	private void checkEat(Entity eater, Entity food) {
		if(eater instanceof LivingEntity) {
			((LivingEntity) eater).eat(food);
		} else {
			food.kill();
		}
	}
	
	private void addControlButtons(int key, String reference, AbstractAction abstractAction) {
		stage.getInputMap().put(KeyStroke.getKeyStroke(key, 0), reference);
		stage.getActionMap().put(reference, abstractAction);
	}

	public int getFPS() {
		return FPS;
	}

	public void setFPS(int FPS) {
		this.FPS = FPS;
	}

	public GameMode getGameMode() {
		return gameMode;
	}

	public void setGameMode(GameMode gameMode) {
		this.gameMode = gameMode;
	}
	
	public void reloadEntities() {
		entities = stage.getEntities();
		players = stage.getEntities(Player.class);
		computerPlayers = stage.getEntities(ComputerPlayer.class);
		totalPlayed = players.size() + computerPlayers.size();
	}
}