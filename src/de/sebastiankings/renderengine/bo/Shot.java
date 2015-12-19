package de.sebastiankings.renderengine.bo;

import de.sebastiankings.renderengine.Constants;
import de.sebastiankings.renderengine.entities.Entity;

public class Shot {
	
	private Entity entity;
	private long fired;
	
	public Shot(Entity entity,long fired){
		this.entity = entity;
		this.fired = fired;
	}
	
	
	public boolean validate(long currentTime){
		if(currentTime - fired > Constants.SHOT_MAX_LIFE_TIME){
			return false;			
		}
		return true;
	}


	public Entity getEntity() {
		return entity;
	}


	public long getFired() {
		return fired;
	}

	
}
