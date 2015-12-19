package de.sebastiankings.renderengine.utils;

import org.joml.Vector3f;

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

	public static Vector3f createMovementVector(float xPerMs, float yPerMs, float zPerMs, long deltaTime) {
		Vector3f result = new Vector3f(xPerMs * deltaTime, yPerMs * deltaTime, zPerMs * deltaTime);
		return result;
	}
}
