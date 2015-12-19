package de.sebastiankings.renderengine.entities;

import org.joml.Vector3f;

import de.sebastiankings.renderengine.Constants;
import de.sebastiankings.renderengine.utils.ServiceFunctions;

public class EntityState {

	private float rotationX;
	private float rotationY;
	private float rotationZ;

	private Vector3f currentPosition;

	private Vector3f relativePosition;

	public EntityState() {
		// SET DEFAULT VALUES;
		this.setRotationX(0.0f);
		this.setRotationY(0.0f);
		this.rotationZ = 0.0f;
		this.currentPosition = new Vector3f();
		this.relativePosition = new Vector3f();
	}

	public Vector3f getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(Vector3f currentPosition) {
		this.currentPosition = currentPosition;
	}

	public float getRotationZ() {
		return rotationZ;
	}

	public void setRotationZ(float roationZ) {
		this.rotationZ = roationZ;
	}

	public void incrementRotationX(float rotationX) {
		this.setRotationX(this.getRotationX() + rotationX);
	}

	public void incrementRotationY(float rotationY) {
		this.setRotationY(this.getRotationY() + rotationY);
	}

	public void incrementRotationZ(float rotationZ) {
		this.rotationZ += rotationZ;
	}

	public float getRotationX() {
		return rotationX;
	}

	public void setRotationX(float rotationX) {
		this.rotationX = rotationX;
	}

	public float getRotationY() {
		return rotationY;
	}

	public void setRotationY(float rotationY) {
		this.rotationY = rotationY;
	}

	public void changeRelativPosition(Vector3f movement) {
		this.relativePosition.add(movement);
		validateRelativPosition();

	}

	private void validateRelativPosition() {
		// ValidateX
		this.relativePosition.x = ServiceFunctions.clamp(-Constants.PLAYER_MAX_RELATIV_DISTANCE_X, Constants.PLAYER_MAX_RELATIV_DISTANCE_X, this.relativePosition.x);
		// ValidateY
		this.relativePosition.y = ServiceFunctions.clamp(-Constants.PLAYER_MAX_RELATIV_DISTANCE_Y, Constants.PLAYER_MAX_RELATIV_DISTANCE_Y, this.relativePosition.y);
		// ValidateZ
		this.relativePosition.z = ServiceFunctions.clamp(-Constants.PLAYER_MAX_RELATIV_DISTANCE_Z, 0, this.relativePosition.z);
	}

	public Vector3f getRelativePosition() {
		return relativePosition;
	}

	public void setRelativePosition(Vector3f relativePosition) {
		this.relativePosition = relativePosition;
		validateRelativPosition();
	}

}
