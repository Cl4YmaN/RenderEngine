package de.sebastiankings.renderengine.entities;

public enum EntityType {

	TRASHBIN("res/meshes/trashbin/"),
	FLOOR("res/meshes/floor/"),
	GUMBA("res/meshes/gumba/");

	/**
	 * Path to DataFolder 
	 */
	private String folderName;

	EntityType(String folderName) {
		this.folderName = folderName;
	}

	public String getFolderPath() {
		return folderName;
	}

}
