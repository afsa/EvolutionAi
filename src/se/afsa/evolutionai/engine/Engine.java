package se.afsa.evolutionai.engine;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import se.afsa.evolutionai.event.GameEventHandler;
import se.afsa.evolutionai.event.GameEventType;
import se.afsa.evolutionai.stage.GUIStage;
import se.afsa.evolutionai.stage.Stage;

public class Engine {
	
	private int FPS;
	private GameMode gameMode;
	private Thread loop;
	private boolean 
			isRunning = false,
			isDead = false;
	private static Stage stage;
	private String toggleFPS = "ActionToggleFPS";
	private String pauseGame = "ActionPauseGame";
	private FPSCounter FPScounter = new FPSCounter(20, 20, false);
	private GameEventHandler gameEventHandler = new GameEventHandler();
	
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
				stage.reloadEntities();
				
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
		while(!isDead) {
			runMechanics(1);
		}
	}
	
	private void guiGameLoop() {
		long intervalTargetLength = 1000000000 / FPS;
		long lastFrame = System.nanoTime();
		
		while(!isDead) {
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
		stage.runMechanics(movementAmplifier);
		int players = stage.getPlayerCount(),
			computer = stage.getComputerPlayerCount(),
			totalPlayed = stage.getTotalPlayed();
		stage.collisionDetector();
		
		if(gameMode.isFinished(players, computer, totalPlayed)) {
			stop();
		}
	}
	
	public void togglePause() {
		isRunning = !isRunning;
		gameEventHandler.fireEvent(isRunning ? GameEventType.CONTINUE : GameEventType.PAUSE, this);
	}
	
	public void start() {
		isRunning = true;
		gameEventHandler.fireEvent(GameEventType.START, this);
	}
	
	public void stop() {
		isDead = true;
		gameEventHandler.fireEvent(GameEventType.STOP, this);
	}
	
	public void reload() {
		stage.reloadEntities();
		isDead = false;
		isRunning = false;
	}
	
	public Stage getStage() {
		return stage;
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
	
	public GameEventHandler getGameEventHandler() {
		return gameEventHandler;
	}
}