package se.afsa.evolutionai.event;

import java.util.ArrayList;
import java.util.List;

import se.afsa.evolutionai.engine.Engine;

/**
 * A class that handles all the game events. All listeners used in this class is accessed a static variable.
 * Therefore listeners can be added from different handlers with the same result.
 */
public class GameEventHandler {
	
	private static List<GameListener> gameListeners = new ArrayList<>();
	
	/**
	 * Add a game listener.
	 * @param gameListener - the listener to be added.
	 */
	public synchronized void addGameListener(GameListener gameListener) {
		gameListeners.add(gameListener);
	}
	
	/**
	 * Remove a game listener.
	 * @param gameListener - the listener to be removed.
	 */
	public synchronized void removeGameListener(GameListener gameListener) {
		if(gameListeners.contains(gameListener)) {
			gameListeners.remove(gameListener);
		}
	}
	
	/**
	 * Fire an event.
	 * @param gameEventType - the type of event to fire. The types are CONTINUE, PAUSE, START, STOP.
	 * @param engine - the engine that fires this event.
	 */
	public synchronized void fireEvent(GameEventType gameEventType, Engine engine) {
		GameEvent gameEvent = new GameEvent(gameEventType, engine, this);
		
		for (int i = 0; i < gameListeners.size(); i++) {
			gameListeners.get(i).handleEvent(gameEvent);
		}
	}
}
