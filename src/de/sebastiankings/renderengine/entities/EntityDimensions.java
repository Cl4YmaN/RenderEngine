package de.sebastiankings.renderengine.entities;

public class EntityDimensions {

	private float length;
	private float width;
	private float heigt;

	public EntityDimensions(float length, float width, float heigt) {
		super();
		this.length = length;
		this.width = width;
		this.heigt = heigt;
	}

	public float getLength() {
		return length;
	}

	public float getWidth() {
		return width;
	}

	public float getHeigt() {
		return heigt;
	}

}
