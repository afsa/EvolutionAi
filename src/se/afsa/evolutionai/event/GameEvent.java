package se.afsa.evolutionai.event;

import java.util.EventObject;

import se.afsa.evolutionai.engine.Engine;

public class GameEvent extends EventObject {
	
	private GameEventType gameEventType;
	private Engine engine;

	/**
	 * 
	 */
	private static final long serialVersionUID = 3344918028742784886L;

	public GameEvent(GameEventType gameEventType, Engine engine, Object arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
		this.gameEventType = gameEventType;
		this.engine = engine;
	}
	
	public GameEventType getEventType() {
		return gameEventType;
	}
	
	public Engine getEngine() {
		return engine;
	}

}
