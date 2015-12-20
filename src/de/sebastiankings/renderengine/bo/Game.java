package de.sebastiankings.renderengine.bo;

import java.util.ArrayList;
import java.util.List;

import de.sebastiankings.renderengine.entities.Camera;
import de.sebastiankings.renderengine.entities.PointLight;
import de.sebastiankings.renderengine.entities.types.Enemy;
import de.sebastiankings.renderengine.entities.types.Player;
import de.sebastiankings.renderengine.entities.types.Shot;
import de.sebastiankings.renderengine.entities.types.Skybox;
import de.sebastiankings.renderengine.shaders.EntityShaderProgram;
import de.sebastiankings.renderengine.shaders.SkyboxShaderProgramm;
import de.sebastiankings.renderengine.shaders.TerrainShaderProgramm;
import de.sebastiankings.renderengine.terrain.Terrain;

public class Game {
	
	// Special Entitys
	private Player player;
	private Terrain terrain;
	private Skybox skybox;
	private List<Shot> shots;
	
	private List<Enemy> enemies;

	private PointLight sun;
	
	private Camera camera;
	private Inputs inputs;

	// public flags
	public boolean allowShipMovement = true;
	public boolean allowEnemyMovement = true;
	public boolean allowGlobalMovement = true;
	public boolean showEgo = false;
	public boolean gamePaused = false;
	public boolean doGameLogics = true;
	public boolean logFps = false;
	// public timestamps
	public long lastShotFired;

	// Shaderprogramms
	// Entity
	private EntityShaderProgram entityShader;
	// Terrain
	private TerrainShaderProgramm terrainShader;
	// Skybox
	private SkyboxShaderProgramm skyboxShader;

	public Game(Player player, Camera camera, Inputs inputs, PointLight sun) {
		this.setPlayer(player);
		this.setCamera(camera);
		this.setInputs(inputs);
		this.setSun(sun);
		this.shots = new ArrayList<>();
		this.setEnemies(new ArrayList<>());
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

	public List<Enemy> getEnemies() {
		return enemies;
	}

	public void setEnemies(List<Enemy> enemies) {
		this.enemies = enemies;
	}

	public SkyboxShaderProgramm getSkyboxShader() {
		return skyboxShader;
	}

	public void setSkyboxShader(SkyboxShaderProgramm skyboxShader) {
		this.skyboxShader = skyboxShader;
	}

	public Skybox getSkybox() {
		return skybox;
	}

	public void setSkybox(Skybox skybox) {
		this.skybox = skybox;
	}

}
