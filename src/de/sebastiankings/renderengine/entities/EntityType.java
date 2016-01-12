package de.sebastiankings.renderengine.entities;

public enum EntityType {

	TRASHBIN("res/meshes/trashbin/", new EntityDimensions(10, 10, 10)), GUMBA("res/meshes/gumba/", new EntityDimensions(10, 10, 10)), LASER("res/meshes/laser/", new EntityDimensions(10, 10, 10)), SHIP("res/meshes/ship/", new EntityDimensions(10, 10, 10)), ENEMY("res/meshes/enemy/", new EntityDimensions(10, 10, 10));

	/**
	 * Path to DataFolder
	 */
	private String folderName;
	private EntityDimensions dimensions;

	EntityType(String folderName, EntityDimensions dimensions) {
		this.folderName = folderName;
		this.dimensions = dimensions;
	}

	public String getFolderPath() {
		return folderName;
	}

	public EntityDimensions getDimensions() {
		return dimensions;
	}

}
