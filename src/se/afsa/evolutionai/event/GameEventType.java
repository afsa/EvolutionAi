package se.afsa.evolutionai.event;

/**
 * The different types of events that can be called by the engine.
 * CONTINUE - When the game is continued after a pause.
 * PAUSE - When the game is paused.
 * START - When the game is initially started.
 * STOP - When the game ends.
 */
public enum GameEventType {
	START, STOP, PAUSE, CONTINUE;
}
