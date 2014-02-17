package se.afsa.evolutionai.engine;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Stores behavior data for the computer players. Includes methods for creating children for the parents.
 */
public class BehaviorData implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3193537697152626031L;
	private String[] requiredValues = {"distancePower", "distanceAmplifier", "distanceConstant", "offenseAmplifier", "defenceAmplifier", "targetAmplifier",
			"targetSelfAmplifier", "targetConstant", "wallAmplifier", "wallPower", "wallConstant"}; 
	private HashMap<String, Double> data = new HashMap<>();
	
	/**
	 * Create a random behavior data class.
	 */
	public BehaviorData() {
		createData();
	}
	
	/**
	 * Create behavior data from hash map.
	 * @param data - the hash map
	 */
	public BehaviorData(HashMap<String, Double> data) {
		this.data.putAll(data);
		checkData();
	}
	
	/**
	 * Create behavior data from variables. The the constants should be between 0.01 and 10.
	 * @param distancePower
	 * @param distanceAmplifier
	 * @param distanceConstant
	 * @param offenseAmplifier
	 * @param defenceAmplifier
	 * @param targetAmplifier
	 * @param targetSelfAmplifier
	 * @param targetConstant
	 * @param wallAmplifier
	 * @param wallPower
	 * @param wallConstant
	 */
	public BehaviorData(double distancePower, double distanceAmplifier, double distanceConstant, double offenseAmplifier, double defenceAmplifier,
			double targetAmplifier, double targetSelfAmplifier, double targetConstant, double wallAmplifier,
			double wallPower, double wallConstant) {
		data.put("distancePower", distancePower);
		data.put("distanceAmplifier", distanceAmplifier);
		data.put("distanceConstant", distanceConstant);
		
		data.put("offenseAmplifier", offenseAmplifier);
		data.put("defenceAmplifier", defenceAmplifier);
		
		data.put("targetAmplifier", targetAmplifier);
		data.put("targetSelfAmplifier", targetSelfAmplifier);
		data.put("targetConstant", targetConstant);
		
		data.put("wallAmplifier", wallAmplifier);
		data.put("wallPower", wallPower);
		data.put("wallConstant", wallConstant);
	}
	
	public double getDistancePower() {
		return data.get("distancePower");
	}
	
	public double getDistanceAmplifier() {
		return data.get("distanceAmplifier");
	}
	
	public double getDistanceConstant() {
		return data.get("distanceConstant");
	}
	
	public double getOffenseAmplifier() {
		return data.get("offenseAmplifier");
	}
	
	public double getDefenceAmplifier() {
		return data.get("defenceAmplifier");
	}
	
	public double getTargetAmplifier() {
		return data.get("targetAmplifier");
	}
	
	public double getTargetSelfAmplifier() {
		return data.get("targetSelfAmplifier");
	}
	
	public double getTargetConstant() {
		return data.get("targetConstant");
	}
	
	public double getWallAmplifier() {
		return data.get("wallAmplifier");
	}
	
	public double getWallPower() {
		return data.get("wallPower");
	}
	
	public double getWallConstant() {
		return data.get("wallConstant");
	}
	
	/**
	 * Get data from string name.
	 * @param key - the string key name.
	 * @return The data.
	 */
	public double get(String key) {
		if(data.containsKey(key)) {
			return data.get(key);
		}
		
		return 0;
	}
	
	/**
	 * Create random data.
	 */
	private void createData() {
		for (int i = 0; i < requiredValues.length; i++) {
			double rnd = (Math.random() * 9.99) + 0.01;
			data.put(requiredValues[i], rnd);
		}
	}
	
	/**
	 * Check if data is valid. If not throw invalid argument exception.
	 */
	private void checkData() {
		for (int i = 0; i < requiredValues.length; i++) {
			if(!data.containsKey(requiredValues[i])) {
				throw new IllegalArgumentException("'" + requiredValues[i] + "' is missing in BehaviorData!");
			}
		}
	}
	
	/**
	 * Create children behavior data.
	 * @param behaviorData - the other parent.
	 * @return The new child behavior data.
	 */
	public BehaviorData createChildren(BehaviorData behaviorData) {
		HashMap<String, Double> child = new HashMap<>();
		for (int i = 0; i < requiredValues.length; i++) {
			String tempKey = requiredValues[i];
			child.put(tempKey, meiosis(data.get(tempKey), behaviorData.get(tempKey)));
		}
		
		return new BehaviorData(child);
	}

	/**
	 * Perform meiosis.
	 * @param a - the first data
	 * @param b - the second data
	 * @return The child data.
	 */
	private Double meiosis(double a, double b) {
		// TODO Auto-generated method stub
		double rnd = (Math.random()*2)-1;
		
		if(rnd <= 0.33) {
			return inBounds(a*(1+rnd/5));
		}
		
		if(rnd <= 0.66) {
			return inBounds(b*(1+rnd/5));
		}
		
		
		return inBounds((a+b)/2*(1+rnd/5));
	}
	
	private double inBounds(double input) {
		return (input >= 0.01) ? ((input <= 10) ? input : 10) : 0.01;
	}
}