package de.sebastiankings.renderengine.bo;

import java.util.List;

import de.sebastiankings.renderengine.entities.Camera;
import de.sebastiankings.renderengine.entities.Entity;
import de.sebastiankings.renderengine.entities.PointLight;
import de.sebastiankings.renderengine.shaders.EntityShaderProgram;
import de.sebastiankings.renderengine.shaders.TerrainShaderProgramm;
import de.sebastiankings.renderengine.terrain.Terrain;

public class Game {

	// Special Entitys
	private Player player;
	private Terrain terrain;
	private List<Shot> shots;
	private List<Entity> enemies;

	private PointLight sun;
	
	private Camera camera;
	private Inputs inputs;

	// public flags
	public boolean allowShipMovement = true;
	public boolean allowEnemyMovement = true;
	public boolean allowGlobalMovement = true;
	public boolean gamePaused = false;
	public boolean doGameLogics = true;

	// public timestamps
	public boolean lastShotFired;

	// Shaderprogramms
	// Entity
	private EntityShaderProgram entityShader;
	// Terrain
	private TerrainShaderProgramm terrainShader;
	// Water(?)

	public Game(Player player, Camera camera, Inputs inputs, PointLight sun) {
		this.setPlayer(player);
		this.setCamera(camera);
		this.setInputs(inputs);
		this.setSun(sun);
	}

	public EntityShaderProgram getEntityShader() {
		return entityShader;
	}

	public void setEntityShader(EntityShaderProgram entityShader) {
		this.entityShader = entityShader;
	}

	public TerrainShaderProgramm getTerrainShader() {
		return terrainShader;
	}

	public void setTerrainShader(TerrainShaderProgramm terrainShader) {
		this.terrainShader = terrainShader;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public List<Shot> getShots() {
		return shots;
	}

	public void setShots(List<Shot> shots) {
		this.shots = shots;
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	public Inputs getInputs() {
		return inputs;
	}

	public void setInputs(Inputs inputs) {
		this.inputs = inputs;
	}

	public Terrain getTerrain() {
		return terrain;
	}

	public void setTerrain(Terrain terrain) {
		this.terrain = terrain;
	}

	public PointLight getSun() {
		return sun;
	}

	public void setSun(PointLight sun) {
		this.sun = sun;
	}

}
