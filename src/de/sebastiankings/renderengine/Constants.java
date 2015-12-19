package de.sebastiankings.renderengine;

import org.joml.Vector3f;

public class Constants {

	
	//MOVEMENTS
	public static final float LEVEL_MOVEMENT_SPEED = 0.05f; // PER MS
	public static final float SHIP_MOVEMENT_SPPED = 0.07f; // per MS
	public static final float SHOT_MOVEMENT_SPEED = 0.4f; // per MS
	
	//COOLDOWNS
	public static final int SHOT_COOLDOWN = 250; //4 Shots / second
	
	//MAX/MIN VALUES
	public static final int MAX_SHOOTS_FIRED = 1;
	public static final float PLAYER_MAX_RELATIV_DISTANCE_X = 40f;
	public static final float PLAYER_MAX_RELATIV_DISTANCE_Y = 0;
	public static final float PLAYER_MAX_RELATIV_DISTANCE_Z = 80f;
	public static final int SHOT_MAX_LIFE_TIME = 1000; //ms
	
	//ETC
	public static final Vector3f SHOT_OFFSET = new Vector3f(0,0,-10);
	
}
