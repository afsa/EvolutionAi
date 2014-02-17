package se.afsa.evolutionai.engine;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Mattias Jönsson
 * A class that converts user input to a vector.
 */
public class Keys {
	
	private HashMap<Integer, Vector2D> keyToVector = new HashMap<>();
	private List<Integer> keysPressed = new ArrayList<>();
	
	/**
	 * Create default keys for movement.
	 */
	public Keys() {
		keyToVector.put(KeyEvent.VK_D, new Vector2D(1, 0));
		keyToVector.put(KeyEvent.VK_W, new Vector2D(0, -1));
		keyToVector.put(KeyEvent.VK_A, new Vector2D(-1, 0));
		keyToVector.put(KeyEvent.VK_S, new Vector2D(0, 1));
	}
	
	/**
	 * Create custom keys for movement.
	 * Four {@link KeyEvent} should be given in a array in the order:
	 * Right, up, left, down.
	 * @param keys - the array of keys.
	 */
	public Keys(int[] keys) {
		if(keys.length != 4) {
			throw new IllegalArgumentException("Argument 'keys' must have length 4.");
		}
		
		for (int i = 0; i < keys.length; i++) {
			keyToVector.put(keys[i], new Vector2D(Math.round(Math.cos(i*Math.PI/2)), -Math.round(Math.sin(i*Math.PI/2))));
		}
	}
	
	/**
	 * Call when key is pressed.
	 * @param key - the key pressed.
	 */
	public void keyPressed(int key) {
		if(keyToVector.containsKey(key) && !keysPressed.contains(key)) {
			keysPressed.add(key);
		}
	}
	
	/**
	 * Call when a key is released.
	 * @param key - the released key.
	 */
	public void keyReleased(int key) {
		if(keysPressed.contains(key)) {
			keysPressed.remove(keysPressed.indexOf(key));
		}
	}
	
	/**
	 * Convert the keys to a vector.
	 * @return The vector.
	 */
	public Vector2D getDirection() {
		Vector2D direction = new Vector2D();
		for (int i = 0; i < keysPressed.size(); i++) {
			direction.add(keyToVector.get(keysPressed.get(i)));
		}
		
		return direction.normalize();
	}
}
