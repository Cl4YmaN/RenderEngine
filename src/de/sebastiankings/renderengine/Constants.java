package de.sebastiankings.renderengine;

import java.text.DecimalFormat;

import org.joml.Vector3f;

public class Constants {

	
	//MATH
	public static final float RAD_90 = (float) Math.toRadians(90); // PER MS
	public static final float RAD_180 = (float) Math.toRadians(180); // PER MS
	
	//MOVEMENTS
	public static final float LEVEL_MOVEMENT_SPEED = 0.05f; // PER MS
	public static final float SHIP_MOVEMENT_SPPED = 0.07f; // per MS
	public static final float SHOT_MOVEMENT_SPEED = 0.7f; // per MS
	
	//COOLDOWNS
	public static final int SHOT_COOLDOWN = 250; //4 Shots / second
	
	//MAX/MIN VALUES
	public static final int MAX_SHOOTS_FIRED = 1;
	public static final float PLAYER_MAX_RELATIV_DISTANCE_X = 100f;
	public static final float PLAYER_MAX_RELATIV_DISTANCE_Y = 0;
	public static final float PLAYER_MAX_RELATIV_DISTANCE_Z = 150;
	public static final int SHOT_MAX_LIFE_TIME = 1000; //ms
	public static final int LEVEL_RANDOM_ENEMY_COUNT = 50; //ms
	
	//ETC
	public static final Vector3f SHOT_OFFSET = new Vector3f(0,0,-10);
	public static final DecimalFormat DEFAULT_FLOAT_FORMAT = new DecimalFormat("#0.0000");
	
	//CAMERA
	public static final Vector3f CAMERA_DEFAULT_POSITION = new Vector3f(0, 150, 50);
	public static final float CAMERA_DEFAULT_PHI = (float) Math.PI * 0.0f;
	public static final float CAMERA_DEFAULT_THETA = (float) Math.PI * 0.3f;
	public static final float CAMERA_MIN_THETA = (float) (Math.PI / 2.0) * -1.0f + 0.001f;
	public static final float CAMERA_MAX_THETA = (float) (Math.PI / 2.0) * 1.0f - 0.001f;
	
	
}
