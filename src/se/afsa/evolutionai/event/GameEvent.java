package se.afsa.evolutionai.event;

import java.util.EventObject;

import se.afsa.evolutionai.engine.Engine;

/**
 * @author Mattias Jönsson
 * Events that can be called from the game. These events are CONTINUE, PAUSE, START, STOP.
 * @see GameEventType
 */
public class GameEvent extends EventObject {
	
	private GameEventType gameEventType;
	private Engine engine;

	private static final long serialVersionUID = 3344918028742784886L;

	/**
	 * Create a new game event.
	 * @param gameEventType - type of event.
	 * @param engine - the engine that called the event.
	 * @param caller - the class that called the event.
	 * @see GameEventType
	 */
	public GameEvent(GameEventType gameEventType, Engine engine, Object caller) {
		super(caller);
		// TODO Auto-generated constructor stub
		this.gameEventType = gameEventType;
		this.engine = engine;
	}
	
	/**
	 * Get the type of event.
	 * @return The type of event.
	 * @see GameEventType
	 */
	public GameEventType getEventType() {
		return gameEventType;
	}
	
	/**
	 * Get the engine that runs this game.
	 * @return The engine.
	 * @see GameEventType
	 */
	public Engine getEngine() {
		return engine;
	}

}
