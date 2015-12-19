package de.sebastiankings.renderengine.entities;

import org.joml.Vector3f;

public class EntityState {

	private float roationZ;
	
	private Vector3f currentPosition;

	public EntityState(){
		//SET DEFAULT VALUES;
		this.setRoationZ(0.0f);
		this.currentPosition = new Vector3f();
	}
	
	public Vector3f getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(Vector3f currentPosition) {
		this.currentPosition = currentPosition;
	}

	public float getRoationZ() {
		return roationZ;
	}

	public void setRoationZ(float roationZ) {
		this.roationZ = roationZ;
	}
	
	public void incrementRotationZ(float rotationZ){
		this.roationZ += rotationZ;
	}

}
