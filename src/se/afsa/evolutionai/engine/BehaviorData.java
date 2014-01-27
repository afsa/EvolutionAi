package se.afsa.evolutionai.engine;

import java.util.HashMap;

public class BehaviorData {
	
	private String[] requiredValues = {"distancePower", "distanceAmplifier", "distanceConstant", "offenseAmplifier", "defenceAmplifier", "targetAmplifier",
			"targetSelfAmplifier", "targetConstant", "wallAmplifier", "wallPower", "wallConstant"}; 
	private HashMap<String, Double> data = new HashMap<>();
	
	public BehaviorData() {
		createData();
	}
	
	public BehaviorData(HashMap<String, Double> data) {
		this.data.putAll(data);
		checkData();
	}
	
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
	
	public double get(String key) {
		if(data.containsKey(key)) {
			return data.get(key);
		}
		
		return 0;
	}
	
	private void createData() {
		for (int i = 0; i < requiredValues.length; i++) {
			double rnd = (Math.random() * 9.99) + 0.01;
			data.put(requiredValues[i], rnd);
		}
	}
	
	private void checkData() {
		for (int i = 0; i < requiredValues.length; i++) {
			if(!data.containsKey(requiredValues[i])) {
				throw new IllegalArgumentException("'" + requiredValues[i] + "' is missing in BehaviorData!");
			}
		}
	}
	
	public BehaviorData createChildren(BehaviorData behaviorData) {
		HashMap<String, Double> child = new HashMap<>();
		for (int i = 0; i < requiredValues.length; i++) {
			String tempKey = requiredValues[i];
			child.put(tempKey, miosis(data.get(tempKey), behaviorData.get(tempKey)));
		}
		
		return new BehaviorData(child);
	}

	private Double miosis(double a, double b) {
		// TODO Auto-generated method stub
		double rnd = Math.random();
		
		if(rnd <= 0.3) {
			return (a+b)/2;
		}
		
		if(rnd <= 0.6) {
			return a;
		}
		
		if(rnd <= 0.8) {
			return b;
		}
		
		return (Math.random() * 9.99) + 0.01;
	}
}