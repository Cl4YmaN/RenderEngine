package de.sebastiankings.renderengine;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glEnable;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.system.libffi.Closure;

import de.sebastiankings.renderengine.bo.Game;
import de.sebastiankings.renderengine.bo.Inputs;
import de.sebastiankings.renderengine.engine.DisplayManager;
import de.sebastiankings.renderengine.entities.Camera;
import de.sebastiankings.renderengine.entities.Entity;
import de.sebastiankings.renderengine.entities.EntityFactory;
import de.sebastiankings.renderengine.entities.EntityType;
import de.sebastiankings.renderengine.entities.PointLight;
import de.sebastiankings.renderengine.entities.types.Enemy;
import de.sebastiankings.renderengine.entities.types.Player;
import de.sebastiankings.renderengine.entities.types.Shot;
import de.sebastiankings.renderengine.shaders.EntityShaderProgram;
import de.sebastiankings.renderengine.shaders.TerrainShaderProgramm;
import de.sebastiankings.renderengine.terrain.Terrain;
import de.sebastiankings.renderengine.utils.ServiceFunctions;
import de.sebastiankings.renderengine.utils.TerrainUtils;

public class MainGameLoop {
	private static final Logger LOGGER = Logger.getLogger(MainGameLoop.class);
	@SuppressWarnings("unused")
	private static GLFWErrorCallback errorCallback;
	@SuppressWarnings("unused")
	private static GLFWKeyCallback keyCallback;
	@SuppressWarnings("unused")
	private static Closure debug;

	private static long windowId;

	private static Game game;

	private static Terrain terrain;

	public static void main(String[] args) {

		try {
			// Setup window
			init();
			initEnemyEntites();

			LOGGER.info("Start GameLoop");
			long lastStartTime = System.currentTimeMillis();
			while (glfwWindowShouldClose(windowId) == GL_FALSE) {
				long deltaTime = System.currentTimeMillis() - lastStartTime;
				lastStartTime = System.currentTimeMillis();
				handleInputs(deltaTime);
				doGameLogic(deltaTime, lastStartTime);
				render(deltaTime);
				DisplayManager.updateDisplay();
			}
			LOGGER.info("Ending Gameloop! Cleaning Up");
			cleanUp();
			LOGGER.info("Finished cleaning! Goodbye!");

		} catch (Exception e) {
			LOGGER.error("There was an error!", e);
			e.printStackTrace();
		} finally {

		}
	}

	private static void init() {
		LOGGER.info("Initialize Game");
		// Alle OGL-Settings laden
		windowId = DisplayManager.createDisplay();
		loadOpenGlSettings();
		// Inputhandler Registrieren
		Inputs inputs = new Inputs();
		inputs.registerInputs(DisplayManager.getWindow());
		// PlayerModel laden
		Player player = new Player(EntityFactory.createEntity(EntityType.SHIP));
		player.getEntity().moveEntityGlobal(new Vector3f(0, 1.5f, 0));

		PointLight sun = new PointLight(new Vector3f(1000, 1000, 0), new Vector3f(0.5f, 0.5f, 0.5f), new Vector3f(1.0f, 1.0f, 1.0f), new Vector3f(1.0f, 1.0f, 1.0f));
		game = new Game(player, new Camera(), inputs, sun);
		game.setTerrain(TerrainUtils.generateTerrain(300f, 10000f));
		initShaderProgramms();
	}

	private static void initShaderProgramms() {
		game.setEntityShader(new EntityShaderProgram("res/shaders/entity/vertexShader.glsl", "res/shaders/entity/fragmentShader.glsl"));
		game.setTerrainShader(new TerrainShaderProgramm("res/shaders/terrain/vertexShader.glsl", "res/shaders/terrain/fragmentShader.glsl"));

	}

	private static void initEnemyEntites() {
		List<Enemy> enemies = new ArrayList<>();
		// Level generate Random Enemys
		float enemyDensity = 1 / 20f;
		float enemyStartOffset = 200;
		float enemyEndOffset = 400;
		LOGGER.trace("Calculating enemy Count!");
		float realTerrainLength = game.getTerrain().getLength() - enemyStartOffset - enemyEndOffset;
		LOGGER.trace("Real Terrain Length: " + realTerrainLength);
		float distanceBetweenEnemy = realTerrainLength * enemyDensity;
		LOGGER.trace("Units between enemy: " + distanceBetweenEnemy);
		int enemyCount = (int) (realTerrainLength / distanceBetweenEnemy);

		LOGGER.debug("Enemy Count " + enemyCount);
		for (int i = 0; i < enemyCount; i++) {
			Entity temp = EntityFactory.createEntity(EntityType.GUMBA);
			temp.rotateY(Constants.RAD_90);
			Vector3f spawn = new Vector3f(0, 0, -(enemyStartOffset + i * distanceBetweenEnemy));
			LOGGER.trace("Spawnpoint: " + spawn.toString(Constants.DEFAULT_FLOAT_FORMAT));
			Enemy next = new Enemy(temp, spawn, new Vector3f());
			enemies.add(next);
		}
		// Level generate Special Enemys

		game.setEnemies(enemies);
	}

	/**
	 * Method to Handle all user Inputs,
	 * 
	 * @param deltaTime
	 *            Timedifference to lastFrame
	 */
	private static void handleInputs(long deltaTime) {
		Inputs inputs = game.getInputs();
		Entity playerEntity = game.getPlayer().getEntity();
		if (inputs.keyPresse(GLFW_KEY_ESCAPE)) {
			glfwSetWindowShouldClose(DisplayManager.getWindow(), GL_TRUE);
		}
		if (inputs.keyPresse(GLFW_KEY_A)) {
			playerEntity.getEntityState().setRotationZ((float) Math.toRadians(10.0d));
			Vector3f movement = ServiceFunctions.createMovementVector(-Constants.SHIP_MOVEMENT_SPPED, 0, 0, deltaTime);
			playerEntity.moveEntityRelativ(movement);
		}
		if (inputs.keyPresse(GLFW_KEY_D)) {
			playerEntity.getEntityState().setRotationZ((float) Math.toRadians(-10.0d));
			Vector3f movement = ServiceFunctions.createMovementVector(Constants.SHIP_MOVEMENT_SPPED, 0, 0, deltaTime);
			playerEntity.moveEntityRelativ(movement);
		}
		// CLEAR ROTATION IF NOT LEFT OR RIGHT
		if (!inputs.keyPresse(GLFW_KEY_A) && !inputs.keyPresse(GLFW_KEY_D)) {
			playerEntity.getEntityState().setRotationZ((float) Math.toRadians(0.0d));
		}
		if (inputs.keyPresse(GLFW_KEY_W)) {
			Vector3f movement = ServiceFunctions.createMovementVector(0, 0, -Constants.SHIP_MOVEMENT_SPPED, deltaTime);
			playerEntity.moveEntityRelativ(movement);
		}
		if (inputs.keyPresse(GLFW_KEY_S)) {
			Vector3f movement = ServiceFunctions.createMovementVector(0, 0, Constants.SHIP_MOVEMENT_SPPED, deltaTime);
			playerEntity.moveEntityRelativ(movement);
		}
		// EXPERIMENTAL!!!!
		if (inputs.keyPresse(GLFW_KEY_F)) {
			if (game.allowGlobalMovement) {
				game.allowGlobalMovement = false;
			} else {
				game.allowGlobalMovement = true;
			}
		}

		if (inputs.keyPresse(GLFW_KEY_C)) {
			if (game.showEgo) {
				game.showEgo = false;
				game.getCamera().loadDefaultCamSettings(game.getPlayer().getEntity().getEntityState().getCurrentPosition());
			} else {
				game.showEgo = true;
				game.getCamera().loadAlternativCamSettings(game.getPlayer().getEntity().getEntityState().getCurrentPosition());
			}
		}
		if (inputs.keyPresse(GLFW_KEY_SPACE)) {
			shoot();

		}
	}

	/**
	 * Method for global Movement, Shot (Movement and Validation), collision
	 * Detections
	 * 
	 * @param deltaTime
	 *            Time since last Frame
	 * @param currentTime
	 *            Current System Time at FrameStart
	 */
	private static void doGameLogic(long deltaTime, long currentTime) {
		Vector3f globalMovement = ServiceFunctions.createMovementVector(0, 0, -Constants.LEVEL_MOVEMENT_SPEED, deltaTime);
		// Model Movements
		// Move Shots
		// Animate and validate Shot
		List<Shot> invalidShots = new ArrayList<Shot>();
		for (Shot shot : game.getShots()) {
			if (shot.validate(currentTime)) {
				shot.getEntity().moveEntityGlobal(new Vector3f(globalMovement).add(0, 0, -Constants.SHOT_MOVEMENT_SPEED));
			} else {
				shot.getEntity().setShowEntity(false);
				invalidShots.add(shot);
			}
		}
		for (Shot shot : invalidShots) {
			game.getShots().remove(shot);
		}
		// Move Player (global)
		if (game.allowGlobalMovement) {
			game.getCamera().move(globalMovement);
			game.getSun().getLightPos().add(globalMovement);
			game.getPlayer().getEntity().moveEntityGlobal(globalMovement);
		}

	}

	private static void render(long deltaTime) {

		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		Camera camera = game.getCamera();
		camera.updateViewMatrix();
		// Shading Terrain
		game.getTerrain().render(game.getTerrainShader(), camera, game.getSun());
		// Shading entities
		// Render enemies
		for (Enemy enemy : game.getEnemies()) {
			Entity entity = enemy.getEntity();
			if (entity.showEntity()) {
				entity.render(0.0f, game.getEntityShader(), camera, game.getSun());
			}
			if (game.allowEnemyMovement) {
				// Moving
			}
		}
		// Render Shots
		for (Shot shot : game.getShots()) {
			Entity entity = shot.getEntity();
			if (entity.showEntity()) {
				entity.render(0.0f, game.getEntityShader(), camera, game.getSun());
			}
		}
		// Render Player
		game.getPlayer().getEntity().render(deltaTime, game.getEntityShader(), game.getCamera(), game.getSun());
	}

	private static void shoot() {
		long currentTimeMillis = System.currentTimeMillis();
		long shotDelta = currentTimeMillis - game.lastShotFired;
		if (shotDelta >= Constants.SHOT_COOLDOWN) {
			Entity shotEntity = EntityFactory.createEntity(EntityType.LASER);
			Shot shot = new Shot(shotEntity, currentTimeMillis);
			shot.getEntity().moveEntityGlobal(new Vector3f(game.getPlayer().getEntity().getEntityState().getRealPosition()).add(Constants.SHOT_OFFSET));
			game.getShots().add(shot);
			game.lastShotFired = currentTimeMillis;
			LOGGER.trace("Shot fired");
		}
	}

	private static void loadOpenGlSettings() {
		LOGGER.trace("Loading OGL-Settings");
		glClearColor(0.0f, 0.0f, 0.0f, 1);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		glEnable(GL_DEPTH_TEST);
	}

	private static void cleanUp() {
		game.getEntityShader().cleanUp();
		game.getTerrainShader().cleanUp();
		DisplayManager.closeDisplay();
	}

}
