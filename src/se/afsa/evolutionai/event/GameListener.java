package se.afsa.evolutionai.event;

/**
 * @author Mattias Jönsson
 * Make an object being able to listen to events.
 */
public interface GameListener {
	public void handleEvent(GameEvent event);

}
