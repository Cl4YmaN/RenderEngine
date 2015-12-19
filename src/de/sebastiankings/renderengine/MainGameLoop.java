/*
 * Instructions:
 *  - on Mac: VM Options: -Djava.library.path=libs/LWJGL/native -XstartOnFirstThread
 */
package de.sebastiankings.renderengine;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
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
import java.util.Map;

import org.apache.log4j.Logger;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.system.libffi.Closure;

import de.sebastiankings.renderengine.bo.Inputs;
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
	private static Inputs inputs;
	private static Camera camera;

	private static EntityShaderProgram entityShader;
	private static TerrainShaderProgramm terrainShader;

	private static Entity player;

	private static PointLight light;
	private static Terrain terrain;
	private static List<Shot> shots = new ArrayList<>();
	private static long lastShot;
	private static ArrayList<Entity> entities;

	public static void main(String[] args) {

		try {
			// Setup window
			init();

			light = new PointLight(new Vector3f(1000, 1000, 0), new Vector3f(0.5f, 0.5f, 0.5f), new Vector3f(1.0f, 1.0f, 1.0f), new Vector3f(1.0f, 1.0f, 1.0f));

			loadOpenGlSettings();
			inputs = new Inputs();
			inputs.registerInputs(DisplayManager.getWindow());
			entities = new ArrayList<Entity>();
			player = EntityFactory.createEntity(EntityType.SHIP);
			player.moveEntityGlobal(new Vector3f(0, 1.5f, 0));
			entities.add(player);

			terrain = TerrainUtils.generateTerrain(300f, 10000f);

			

			LOGGER.info("Start GameLoop");
			long lastStartTime = System.currentTimeMillis();
			while (glfwWindowShouldClose(windowId) == GL_FALSE) {
				long deltaTime = System.currentTimeMillis() - lastStartTime;
				lastStartTime = System.currentTimeMillis();
				handleInputs(deltaTime);
				doGameLogic(deltaTime,lastStartTime);
				render(deltaTime);				
				DisplayManager.updateDisplay();
			}
			LOGGER.info("Ending Gameloop! Cleaning Up");
			entityShader.cleanUp();
			terrainShader.cleanUp();
			DisplayManager.closeDisplay();
			LOGGER.info("Finished cleaning! Goodbye!");

		} catch (Exception e) {
			LOGGER.error("There was an error!", e);
			e.printStackTrace();
		} finally {

		}
	}

	private static void init() {
		LOGGER.info("Initialize Game");
		windowId = DisplayManager.createDisplay();
		camera = new Camera();
		DisplayManager.setCamera(camera);
		initShaderProgramms();
	}

	private static void initShaderProgramms() {
		entityShader = new EntityShaderProgram("res/shaders/entity/vertexShader.glsl", "res/shaders/entity/fragmentShader.glsl");
		terrainShader = new TerrainShaderProgramm("res/shaders/terrain/vertexShader.glsl", "res/shaders/terrain/fragmentShader.glsl");
	}

	private static void handleInputs(long deltaTime) {
		if (inputs.keyPresse(GLFW_KEY_ESCAPE)) {
			glfwSetWindowShouldClose(DisplayManager.getWindow(), GL_TRUE);
		}
		if (inputs.keyPresse(GLFW_KEY_A)) {
			player.getEntityState().setRotationZ((float) Math.toRadians(10.0d));
			Vector3f movement = ServiceFunctions.createMovementVector(-Constants.SHIP_MOVEMENT_SPPED, 0, 0, deltaTime);
			player.moveEntityRelativ(movement);
		}
		if (inputs.keyPresse(GLFW_KEY_D)) {
			player.getEntityState().setRotationZ((float) Math.toRadians(-10.0d));
			Vector3f movement = ServiceFunctions.createMovementVector(Constants.SHIP_MOVEMENT_SPPED, 0, 0, deltaTime);
			player.moveEntityRelativ(movement);
		}
		// CLEAR ROTATION IF NOT LEFT OR RIGHT
		if (!inputs.keyPresse(GLFW_KEY_A) && !inputs.keyPresse(GLFW_KEY_D)) {
			player.getEntityState().setRotationZ((float) Math.toRadians(0.0d));
		}
		if (inputs.keyPresse(GLFW_KEY_W)) {
			Vector3f movement = ServiceFunctions.createMovementVector(0, 0, -Constants.SHIP_MOVEMENT_SPPED, deltaTime);
			player.moveEntityRelativ(movement);
		}
		if (inputs.keyPresse(GLFW_KEY_S)) {
			Vector3f movement = ServiceFunctions.createMovementVector(0, 0, Constants.SHIP_MOVEMENT_SPPED, deltaTime);
			player.moveEntityRelativ(movement);
		}

		if (inputs.keyPresse(GLFW_KEY_SPACE)) {
			shoot();

		}
	}

	private static void doGameLogic(long deltaTime, long currentTime){
		//Animate and validate Shot
		List<Shot> invalidShots = new ArrayList<Shot>();
		for (Shot shot : shots) {
			if(shot.validate(currentTime)){
				shot.getEntity().moveEntityGlobal(new Vector3f(0,0,-Constants.SHOT_MOVEMENT_SPEED));
			} else {
				shot.getEntity().setShowEntity(false);
				invalidShots.add(shot);
			}
		}
		for (Shot shot : invalidShots) {
			shots.remove(shot);
		}
		
	}
	
	private static void render(long deltaTime){
		Vector3f movement = ServiceFunctions.createMovementVector(0, 0, -Constants.LEVEL_MOVEMENT_SPEED, deltaTime);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		camera.updateViewMatrix();
		// Shading Terrain
		terrain.render(terrainShader, camera, light);
		// Shading entitys
		for (Entity entity : entities) {
			if (entity.showEntity()) {
				entity.render(0.0f, entityShader, camera, light);
			}
			entity.moveEntityGlobal(movement);
		}
		camera.move(movement);
		light.getLightPos().add(movement);
	}
	

	private static void shoot() {
		long currentTimeMillis = System.currentTimeMillis();
		long shotDelta = currentTimeMillis - lastShot;
		if (shotDelta >= Constants.SHOT_COOLDOWN) {
			Entity shotEntity = EntityFactory.createEntity(EntityType.LASER);
			Shot shot = new Shot(shotEntity, currentTimeMillis);
			shot.getEntity().moveEntityGlobal(new Vector3f(player.getEntityState().getRealPosition()).add(Constants.SHOT_OFFSET));
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

}
