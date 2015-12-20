package de.sebastiankings.renderengine.entities.types;

import org.joml.Vector3f;

import de.sebastiankings.renderengine.entities.Entity;

public class Enemy {

	private Entity entity;
	private Vector3f relativMovement;
	
	public boolean isAlive = true;

	public Enemy(Entity entity, Vector3f spawn, Vector3f relativMovement) {
		this.entity = entity;
		this.relativMovement = relativMovement;
		this.entity.moveEntityGlobal(spawn);
	}

	public Entity getEntity() {
		return entity;
	}

	public Vector3f getRelativMovement() {
		return relativMovement;
	}
	
	public void invalidate(){
		this.isAlive = false;
		this.entity.setShowEntity(false);
	}

}
