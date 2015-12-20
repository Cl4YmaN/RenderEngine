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
import de.sebastiankings.renderengine.bo.Player;
import de.sebastiankings.renderengine.bo.Shot;
import de.sebastiankings.renderengine.engine.DisplayManager;
import de.sebastiankings.renderengine.entities.Camera;
import de.sebastiankings.renderengine.entities.Entity;
import de.sebastiankings.renderengine.entities.EntityFactory;
import de.sebastiankings.renderengine.entities.EntityType;
import de.sebastiankings.renderengine.entities.PointLight;
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
	private static List<Shot> shots = new ArrayList<>();
	private static long lastShot;
	private static ArrayList<Entity> entities;

	public static void main(String[] args) {

		try {
			// Setup window
			init();
			entities = new ArrayList<Entity>();

			entities.add(game.getPlayer().getEntity());

			terrain = TerrainUtils.generateTerrain(300f, 10000f);

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
		Player player = new Player(EntityFactory.createEntity(EntityType.GUMBA));
		player.getEntity().moveEntityGlobal(new Vector3f(0, 1.5f, 0));

		PointLight sun = new PointLight(new Vector3f(1000, 1000, 0), new Vector3f(0.5f, 0.5f, 0.5f), new Vector3f(1.0f, 1.0f, 1.0f), new Vector3f(1.0f, 1.0f, 1.0f));
		game = new Game(player, new Camera(), inputs, sun);
		initShaderProgramms();
	}

	private static void initShaderProgramms() {
		game.setEntityShader(new EntityShaderProgram("res/shaders/entity/vertexShader.glsl", "res/shaders/entity/fragmentShader.glsl"));
		game.setTerrainShader(new TerrainShaderProgramm("res/shaders/terrain/vertexShader.glsl", "res/shaders/terrain/fragmentShader.glsl"));

	}

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
		if (inputs.keyPresse(GLFW_KEY_F)) {
			if(game.allowGlobalMovement){
				game.allowGlobalMovement = false;				
			}else {
				game.allowGlobalMovement = true;
			}
		}
		if (inputs.keyPresse(GLFW_KEY_SPACE)) {
			shoot();

		}
	}

	private static void doGameLogic(long deltaTime, long currentTime) {
		// Animate and validate Shot
		List<Shot> invalidShots = new ArrayList<Shot>();
		for (Shot shot : shots) {
			if (shot.validate(currentTime)) {
				shot.getEntity().moveEntityGlobal(new Vector3f(0, 0, -Constants.SHOT_MOVEMENT_SPEED));
			} else {
				shot.getEntity().setShowEntity(false);
				invalidShots.add(shot);
			}
		}
		for (Shot shot : invalidShots) {
			shots.remove(shot);
		}

	}

	private static void render(long deltaTime) {
		Vector3f movement = ServiceFunctions.createMovementVector(0, 0, -Constants.LEVEL_MOVEMENT_SPEED, deltaTime);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		Camera camera = game.getCamera();
		camera.updateViewMatrix();
		// Shading Terrain
		terrain.render(game.getTerrainShader(), camera, game.getSun());
		// Shading entitys
		for (Entity entity : entities) {
			if (entity.showEntity()) {
				entity.render(0.0f, game.getEntityShader(), camera, game.getSun());
			}
			if (game.allowGlobalMovement) {
				entity.moveEntityGlobal(movement);
			}
		}
		if (game.allowGlobalMovement) {
			camera.move(movement);
			game.getSun().getLightPos().add(movement);
		}
	}

	private static void shoot() {
		long currentTimeMillis = System.currentTimeMillis();
		long shotDelta = currentTimeMillis - lastShot;
		if (shotDelta >= Constants.SHOT_COOLDOWN) {
			Entity shotEntity = EntityFactory.createEntity(EntityType.TRASHBIN);
			Shot shot = new Shot(shotEntity, currentTimeMillis);
			shot.getEntity().moveEntityGlobal(new Vector3f(game.getPlayer().getEntity().getEntityState().getRealPosition()).add(Constants.SHOT_OFFSET));
			shots.add(shot);
			entities.add(shotEntity);
			lastShot = currentTimeMillis;
			LOGGER.trace("Shot fired");
		} else {
			LOGGER.trace("Cooldown");
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
