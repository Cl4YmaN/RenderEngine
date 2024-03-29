package de.sebastiankings.renderengine;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_C;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F;
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
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

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
import de.sebastiankings.renderengine.entities.Model;
import de.sebastiankings.renderengine.entities.PointLight;
import de.sebastiankings.renderengine.entities.types.Enemy;
import de.sebastiankings.renderengine.entities.types.Player;
import de.sebastiankings.renderengine.entities.types.Shot;
import de.sebastiankings.renderengine.shaders.EntityShaderProgram;
import de.sebastiankings.renderengine.shaders.SkyboxShaderProgramm;
import de.sebastiankings.renderengine.shaders.TerrainShaderProgramm;
import de.sebastiankings.renderengine.utils.LoaderUtils;
import de.sebastiankings.renderengine.utils.ServiceFunctions;
import de.sebastiankings.renderengine.utils.SkyboxUtils;
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

	private static Map<EntityType, List<Entity>> entityMap;

	public static void main(String[] args) {

		try {
			// Setup window
			init();
			// EXPERIMENTAL
			entityMap = new HashMap<EntityType, List<Entity>>();
			initEnemyEntites();

			LOGGER.info("Start GameLoop");
			long lastStartTime = System.currentTimeMillis() - 10;
			while (glfwWindowShouldClose(windowId) == GL_FALSE) {
				long deltaTime = System.currentTimeMillis() - lastStartTime;
				if (game.logFps && deltaTime != 0) {
					LOGGER.info("FPS : " + (1000 / deltaTime));
				}
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

	private static void addEntity(Entity e) {
		EntityType type = e.getType();
		List<Entity> entityList = entityMap.get(type);
		if (entityList != null) {
			entityList.add(e);
		} else {
			ArrayList<Entity> temp = new ArrayList<>();
			temp.add(e);
			entityMap.put(type, temp);
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
		game.setSkybox(SkyboxUtils.loadSkybox("res/skybox/test"));
		initShaderProgramms();
	}

	private static void initShaderProgramms() {
		game.setEntityShader(new EntityShaderProgram("res/shaders/entity/vertexShader.glsl", "res/shaders/entity/fragmentShader.glsl"));
		game.setTerrainShader(new TerrainShaderProgramm("res/shaders/terrain/vertexShader.glsl", "res/shaders/terrain/fragmentShader.glsl"));
		game.setSkyboxShader(new SkyboxShaderProgramm("res/shaders/skybox/vertexShader.glsl", "res/shaders/skybox/fragmentShader.glsl"));

	}

	private static void initEnemyEntites() {
		List<Enemy> enemies = new ArrayList<>();
		// Level generate Random Enemys
		float enemyDensity = 1 / 2000f;
		float enemyStartOffset = 200;
		float enemyEndOffset = 400;
		LOGGER.trace("Calculating enemy Count!");
		float realTerrainLength = game.getTerrain().getLength() - enemyStartOffset - enemyEndOffset;
		LOGGER.trace("Real Terrain Length: " + realTerrainLength);
		float distanceBetweenEnemy = realTerrainLength * enemyDensity;
		LOGGER.trace("Units between enemy: " + distanceBetweenEnemy);
		int enemyCount = (int) (realTerrainLength / distanceBetweenEnemy);

		LOGGER.debug("Enemy Count " + enemyCount);
		Random r = new Random();
		for (int i = 0; i < enemyCount; i++) {
			float xPosition = (r.nextFloat() * (game.getTerrain().getWidth() - 30)) - (game.getTerrain().getWidth() / 2);
			Entity temp = null;
			int enemyType = r.nextInt(2);
			if(enemyType == 0) {
				temp = EntityFactory.createEntity(EntityType.ENEMY);
			} else if(enemyType == 1) {
				temp = EntityFactory.createEntity(EntityType.GUMBA);
				temp.rotateY(-Constants.RAD_90);
			}
			Vector3f spawn = new Vector3f(xPosition, 0, -(enemyStartOffset + i * distanceBetweenEnemy));
			LOGGER.trace("Spawnpoint: " + spawn.toString(Constants.DEFAULT_FLOAT_FORMAT));
			Vector3f enemyRelativMovement = r.nextInt(2) == 1 ? new Vector3f(Constants.ENEMY_MOVEMENT_SPEED,0,0) : new Vector3f(-Constants.ENEMY_MOVEMENT_SPEED,0,0);
			Enemy next = new Enemy(temp, spawn, enemyRelativMovement);
			enemies.add(next);
			addEntity(temp);
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
		if (inputs.keyPressed(GLFW_KEY_ESCAPE)) {
			glfwSetWindowShouldClose(DisplayManager.getWindow(), GL_TRUE);
		}
		if (game.allowShipMovement) {

			if (inputs.keyPressed(GLFW_KEY_A)) {
				playerEntity.getEntityState().setRotationZ((float) Math.toRadians(10.0d));
				Vector3f movement = ServiceFunctions.createMovementVector(-Constants.SHIP_MOVEMENT_SPPED, 0, 0, deltaTime);
				playerEntity.moveEntityRelativ(movement);
			}
			if (inputs.keyPressed(GLFW_KEY_D)) {
				playerEntity.getEntityState().setRotationZ((float) Math.toRadians(-10.0d));
				Vector3f movement = ServiceFunctions.createMovementVector(Constants.SHIP_MOVEMENT_SPPED, 0, 0, deltaTime);
				playerEntity.moveEntityRelativ(movement);
			}
			// CLEAR ROTATION IF NOT LEFT OR RIGHT
			if (!inputs.keyPressed(GLFW_KEY_A) && !inputs.keyPressed(GLFW_KEY_D)) {
				playerEntity.getEntityState().setRotationZ((float) Math.toRadians(0.0d));
			}
			if (inputs.keyPressed(GLFW_KEY_W)) {
				Vector3f movement = ServiceFunctions.createMovementVector(0, 0, -Constants.SHIP_MOVEMENT_SPPED, deltaTime);
				playerEntity.moveEntityRelativ(movement);
			}
			if (inputs.keyPressed(GLFW_KEY_S)) {
				Vector3f movement = ServiceFunctions.createMovementVector(0, 0, Constants.SHIP_MOVEMENT_SPPED, deltaTime);
				playerEntity.moveEntityRelativ(movement);
			}
		}
		// EXPERIMENTAL!!!!
		if (inputs.keyPressed(GLFW_KEY_F)) {
			if (game.allowGlobalMovement) {
				game.allowGlobalMovement = false;
			} else {
				game.allowGlobalMovement = true;
			}
		}

		if (inputs.keyPressed(GLFW_KEY_C)) {
			if (game.showEgo) {
				game.showEgo = false;
				game.getCamera().loadDefaultCamSettings(game.getPlayer().getEntity().getEntityState().getCurrentPosition());
			} else {
				game.showEgo = true;
				game.getCamera().loadAlternativCamSettings(game.getPlayer().getEntity().getEntityState().getCurrentPosition());
			}
		}
		if (inputs.keyPressed(GLFW_KEY_SPACE)) {
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
		// Make near enemys visible;
		float playerCurrentZ = game.getPlayer().getEntity().getEntityState().getCurrentPosition().z;
		for (Enemy enemy : game.getEnemies()) {
			if (enemy.isAlive) {
				float enemyZ = enemy.getEntity().getEntityState().getCurrentPosition().z;
				if (playerCurrentZ + Constants.ENEMY_MAX_DRAW_DISTANCE > enemyZ && playerCurrentZ - Constants.ENEMY_MAX_DRAW_DISTANCE < enemyZ) {
					enemy.getEntity().setShowEntity(true);
				} else {
					enemy.getEntity().setShowEntity(false);
				}
			}

		}
		// Model Movements
		// Move Shots
		// Animate and validate Shot
		List<Shot> invalidShots = new ArrayList<Shot>();
		for (Shot shot : game.getShots()) {
			if (shot.validate(currentTime)) {
				shot.getEntity().moveEntityGlobal(new Vector3f(globalMovement).add(0, 0, -Constants.SHOT_MOVEMENT_SPEED));
			} else {
				shot.invalidate();
				invalidShots.add(shot);
			}
		}
		for (Shot shot : invalidShots) {
			game.getShots().remove(shot);
		}
		// Move Player (global)
		if (game.allowGlobalMovement) {
			game.getPlayer().getEntity().moveEntityGlobal(globalMovement);
			if(game.showEgo){
				Vector3f egoCamPosition = new Vector3f(game.getPlayer().getEntity().getEntityState().getRealPosition()).add(new Vector3f(0, 20, 50));
				game.getCamera().setCameraPosition(egoCamPosition);
			} else {
				game.getCamera().move(globalMovement);				
			}
			game.getSun().getLightPos().add(globalMovement);
		}
		
		//Move Enemys
		for(Enemy enemy: game.getEnemies()){
			enemy.move(deltaTime);
		}

		// Colissions!
		// First: Active Shots and Visible enemys!
		for (Shot shot : game.getShots()) {
			if (shot.isValid) {
				boolean hitDetected = false;
				Vector3f[] boxPoints = generateHitBoxPoints(shot.getEntity());
				for (Vector3f boxPoint : boxPoints) {
					for (Enemy enemy : game.getEnemies()) {
						if (enemy.isAlive) {
							if (pointIsInEntityBox(boxPoint, enemy.getEntity())) {
								hitDetected = true;
								shot.invalidate();
								enemy.invalidate();
								break;
							}
						}
					}
					if (hitDetected) {
						break;
					}
				}
				if (hitDetected) {
					LOGGER.trace("Shot hit enemy");
					hitDetected = false;
				}
			}
		}

		// Colission between Ship and Enemy
		boolean hitDetected = false;
		Vector3f[] boxPoints = generateHitBoxPoints(game.getPlayer().getEntity());
		for (Vector3f boxPoint : boxPoints) {
			for (Enemy enemy : game.getEnemies()) {
				if (enemy.isAlive) {
					if (pointIsInEntityBox(boxPoint, enemy.getEntity())) {
						hitDetected = true;
						enemy.invalidate();
						break;
					}
				}
			}
			if (hitDetected) {
				break;
			}
		}
		if (hitDetected) {
			LOGGER.info("Yout died!");
			game.allowGlobalMovement = false;
			game.allowShipMovement = false;
		}
	}

	private static Vector3f[] generateHitBoxPoints(Entity e) {
		Vector3f[] result = new Vector3f[8];
		Vector3f p = e.getEntityState().getRealPosition();
		float dX = e.getDimensions().getWidth() / 2;
		float dY = e.getDimensions().getHeight() / 2;
		float dZ = e.getDimensions().getLength() / 2;
		result[0] = new Vector3f(p.x - dX, p.y + dY, p.z - dZ);
		result[1] = new Vector3f(p.x + dX, p.y + dY, p.z - dZ);
		result[2] = new Vector3f(p.x - dX, p.y + dY, p.z + dZ);
		result[3] = new Vector3f(p.x + dX, p.y + dY, p.z + dZ);
		result[4] = new Vector3f(p.x - dX, p.y - dY, p.z - dZ);
		result[5] = new Vector3f(p.x + dX, p.y - dY, p.z - dZ);
		result[6] = new Vector3f(p.x - dX, p.y - dY, p.z + dZ);
		result[7] = new Vector3f(p.x + dX, p.y - dY, p.z + dZ);
		return result;

	}

	/**
	 * Checks if an given Point in Worldspace is within an entitys Box
	 */
	private static boolean pointIsInEntityBox(Vector3f point, Entity e) {
		Vector3f p = e.getEntityState().getRealPosition();
		float halfDimensionX = e.getDimensions().getWidth() / 2;
		float halfDimensionY = e.getDimensions().getHeight() / 2;
		float halfDimensionZ = e.getDimensions().getLength() / 2;
		if (((p.x - halfDimensionX) < point.x && (p.x + halfDimensionX) > point.x) && ((p.y - halfDimensionY) < point.y && (p.y + halfDimensionY) > point.y) && ((p.z - halfDimensionZ) < point.z && (p.z + halfDimensionZ) > point.z)) {
			return true;
		}
		return false;
	}

	/**
	 * Optimized Rendering // Improved rendertime per frame
	 * 
	 * @param deltaTime
	 */
	private static void renderOptimized(long deltaTime) {

		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		Camera camera = game.getCamera();
		camera.updateViewMatrix();
		// Render enemies
		// Preparien entityShader
		EntityShaderProgram entityShader = game.getEntityShader();
		entityShader.start();
		entityShader.loadTexture();
		entityShader.loadLight(game.getSun());
		entityShader.loadViewMatrix(camera.getViewMatrix());
		entityShader.loadProjectionMatrix(camera.getProjectionMatrix());
		// Ausführung nur einmal/model nötig
		for (Entry<EntityType, List<Entity>> typeEntry : entityMap.entrySet()) {
			Entity prototype = typeEntry.getValue().get(0);
			Model currentModel = prototype.getModel();
			glBindVertexArray(currentModel.getVaoID());
			glEnableVertexAttribArray(0);
			glEnableVertexAttribArray(1);
			glEnableVertexAttribArray(2);
			glEnableVertexAttribArray(3);
			glEnableVertexAttribArray(4);
			glEnableVertexAttribArray(5);
			glEnableVertexAttribArray(6);
			glBindTexture(GL_TEXTURE_2D, prototype.getTexture().getTextureID());
			// Muss pro entity ausgeführt werden
			for (Entity entity : typeEntry.getValue()) {
				if (entity.showEntity()) {
					entityShader.loadModelMatrix(entity.getModelMatrix());
					glDrawElements(GL_TRIANGLES, entity.getModel().getVertexCount(), GL_UNSIGNED_INT, 0);
				}
			}
			// CLEANUP VERTEX ARRAY ATTRIBUTES
			glDisableVertexAttribArray(0);
			glDisableVertexAttribArray(1);
			glDisableVertexAttribArray(2);
			glDisableVertexAttribArray(3);
			glDisableVertexAttribArray(4);
			glDisableVertexAttribArray(5);
			glDisableVertexAttribArray(6);
		}
		entityShader.stop();
		// Render Player
		game.getPlayer().getEntity().render(deltaTime, entityShader, game.getCamera(), game.getSun());
	}

	private static void render(long deltaTime) {

		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		Camera camera = game.getCamera();
		camera.updateViewMatrix();
		//Render Skybox
		// Render Terrain
		game.getTerrain().render(game.getTerrainShader(), camera, game.getSun());
		// Shading entities
		// Render enemies
		for (Enemy enemy : game.getEnemies()) {
			Entity entity = enemy.getEntity();
			if (entity.showEntity()) {
				entity.render(0.0f, game.getEntityShader(), camera, game.getSun());
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
		game.getSkybox().render(game.getSkyboxShader(), camera);
	}

	private static void shoot() {
		long currentTimeMillis = System.currentTimeMillis();
		long shotDelta = currentTimeMillis - game.lastShotFired;
		if (shotDelta >= Constants.SHOT_COOLDOWN) {
			Entity shotEntity = EntityFactory.createEntity(EntityType.LASER);
			Shot shot = new Shot(shotEntity, currentTimeMillis);
			addEntity(shotEntity);
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
