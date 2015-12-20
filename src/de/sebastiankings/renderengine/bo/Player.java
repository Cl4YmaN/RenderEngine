package de.sebastiankings.renderengine.bo;

import de.sebastiankings.renderengine.entities.Entity;

public class Player {

	private Entity entity;

	public Player(Entity entity) {
		super();
		this.entity = entity;
	}

	public Entity getEntity() {
		return entity;
	}

}
