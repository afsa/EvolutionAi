package se.afsa.evolutionai.engine;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import se.afsa.evolutionai.event.GameEventHandler;
import se.afsa.evolutionai.event.GameEventType;
import se.afsa.evolutionai.stage.GUIStage;
import se.afsa.evolutionai.stage.Stage;

/**
 * The class that takes care of the game loop and decides when the game should update the stage.
 */
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
	
	/**
	 * Create a new engine.
	 * @param gameStage - the stage that the engine should run.
	 * @param gameMode - the type of game mode.
	 * @param FPS - the target FPS (this is only a target and should not exceed the framerate of the monitor).
	 */
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

	/**
	 * A non-GUI loop.
	 */
	private void gameLoop() {
		while(!isDead) {
			runMechanics(1);
		}
	}
	
	/**
	 * A GUI loop.
	 */
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

	/**
	 * Run the mechanics of the game.
	 * @param movementAmplifier - the time passed divided of the time that should pass.
	 */
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
	
	/**
	 * Toggle game to pause or continue.
	 */
	public void togglePause() {
		isRunning = !isRunning;
		gameEventHandler.fireEvent(isRunning ? GameEventType.CONTINUE : GameEventType.PAUSE, this);
	}
	
	/**
	 * Start the game.
	 */
	public void start() {
		isRunning = true;
		gameEventHandler.fireEvent(GameEventType.START, this);
	}
	
	/**
	 * Stop the game.
	 */
	public void stop() {
		isDead = true;
		gameEventHandler.fireEvent(GameEventType.STOP, this);
	}
	
	/**
	 * Reload the game.
	 */
	public void reload() {
		stage.reloadEntities();
		isDead = false;
		isRunning = false;
	}
	
	/**
	 * Get the stage this engine runs.
	 * @return The stage.
	 */
	public Stage getStage() {
		return stage;
	}
		
	/**
	 * Bind keys to controls.
	 * @param key - the key to be bound.
	 * @param reference - the reference string (what it does).
	 * @param abstractAction - the action.
	 */
	private void addControlButtons(int key, String reference, AbstractAction abstractAction) {
		stage.getInputMap().put(KeyStroke.getKeyStroke(key, 0), reference);
		stage.getActionMap().put(reference, abstractAction);
	}

	/**
	 * Get the target FPS.
	 * @return The FPS.
	 */
	public int getFPS() {
		return FPS;
	}

	/**
	 * Set the target FPS.
	 * @param FPS - the target FPS.
	 */
	public void setFPS(int FPS) {
		this.FPS = FPS;
	}

	/**
	 * Get the game mode.
	 * @return The game mode.
	 */
	public GameMode getGameMode() {
		return gameMode;
	}

	/**
	 * Set the game mode.
	 * Warning! This could bug the game it is done during execution.
	 * @param gameMode - the new game mode.
	 */
	public void setGameMode(GameMode gameMode) {
		this.gameMode = gameMode;
	}
	
	/**
	 * Get the game event handler.
	 * @return The game event handler.
	 */
	public GameEventHandler getGameEventHandler() {
		return gameEventHandler;
	}
}