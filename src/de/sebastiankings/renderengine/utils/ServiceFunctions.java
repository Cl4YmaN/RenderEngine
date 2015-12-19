package de.sebastiankings.renderengine.utils;

public class ServiceFunctions {

	public static float clamp(float lower, float upper, float value) {
		if (value < lower) {
			return lower;
		}
		if (value > upper) {
			return upper;
		}
		return value;
	}
	
}
