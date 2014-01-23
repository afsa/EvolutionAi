package se.afsa.evolutionai.event;

import java.util.ArrayList;
import java.util.List;

import se.afsa.evolutionai.engine.Engine;

public class GameEventHandler {
	
	private static List<GameListener> gameListeners = new ArrayList<>();
	
	public synchronized void addGameListener(GameListener gameListener) {
		gameListeners.add(gameListener);
	}
	
	public synchronized void removeGameListener(GameListener gameListener) {
		if(gameListeners.contains(gameListener)) {
			gameListeners.remove(gameListener);
		}
	}
	
	public synchronized void fireEvent(GameEventType gameEventType, Engine engine) {
		GameEvent gameEvent = new GameEvent(gameEventType, engine, this);
		
		for (int i = 0; i < gameListeners.size(); i++) {
			gameListeners.get(i).handleEvent(gameEvent);
		}
	}
}
