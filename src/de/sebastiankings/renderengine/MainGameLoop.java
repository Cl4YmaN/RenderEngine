/*
 * Instructions:
 *  - on Mac: VM Options: -Djava.library.path=libs/LWJGL/native -XstartOnFirstThread
 */
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
import java.util.Map;

import org.apache.log4j.Logger;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.system.libffi.Closure;

import de.sebastiankings.renderengine.bo.Inputs;
import de.sebastiankings.renderengine.engine.DisplayManager;
import de.sebastiankings.renderengine.entities.Camera;
import de.sebastiankings.renderengine.entities.Entity;
import de.sebastiankings.renderengine.entities.EntityFactory;
import de.sebastiankings.renderengine.entities.EntityType;
import de.sebastiankings.renderengine.entities.PointLight;
import de.sebastiankings.renderengine.shaders.EntityShaderProgram;
import de.sebastiankings.renderengine.shaders.TerrainShaderProgramm;
import de.sebastiankings.renderengine.terrain.Terrain;
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

	public static void main(String[] args) {

		try {
			// Setup window
			init();

			PointLight light = new PointLight(new Vector3f(1000, 1000, 0), new Vector3f(0.5f, 0.5f, 0.5f), new Vector3f(1.0f, 1.0f, 1.0f), new Vector3f(1.0f, 1.0f, 1.0f));
			
			loadOpenGlSettings();
			inputs = new Inputs();
			inputs.registerInputs(DisplayManager.getWindow());
			ArrayList<Entity> entities = new ArrayList<Entity>();
			Entity ball = EntityFactory.createEntity(EntityType.GUMBA);
			entities.add(ball);

			Terrain terrain = TerrainUtils.generateTerrain(100f, 1000f);
			
			
			Vector3f movement = new Vector3f(0,0,-0.05f);
			
			LOGGER.info("Start GameLoop");
			long lastStartTime = System.currentTimeMillis();
			while (glfwWindowShouldClose(windowId) == GL_FALSE) {
				// Tim
				long deltaTime = System.currentTimeMillis() - lastStartTime;
				lastStartTime = System.currentTimeMillis();
				// Clear framebuffer
				glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
				handleInputs();
				camera.updateViewMatrix();
				//Shading Terrain
				terrain.render(terrainShader, camera, light);
				//Shading entitys
				for (Entity entity : entities) {
					if (entity.showEntity()) {
						entity.render(0.0f, entityShader, camera, light);
					}
					entity.rotateZ((float) (Math.PI / 500));
					entity.moveEntity(movement);
				}
				camera.move(movement);
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

	private static void init(){
		LOGGER.info("Initialize Game");
		windowId = DisplayManager.createDisplay();
		camera = new Camera();
		DisplayManager.setCamera(camera);
		initShaderProgramms();
	}
	
	private static void initShaderProgramms(){
		entityShader = new EntityShaderProgram("res/shaders/entity/vertexShader.glsl", "res/shaders/entity/fragmentShader.glsl");
		terrainShader = new TerrainShaderProgramm("res/shaders/terrain/vertexShader.glsl", "res/shaders/terrain/fragmentShader.glsl");
	}
	private static void handleInputs() {
		if (inputs.keyPresse(GLFW_KEY_ESCAPE)) {
			glfwSetWindowShouldClose(DisplayManager.getWindow(), GL_TRUE);
		}
	}

	private static Map<EntityType, List<Entity>> initEntities() {
		// Map<EntityType,List<Entity>> result = new
		// HashMap<EntityType,List<Entity>>();
		// Entity ball = EntityFactory.createEntity(EntityType.GUMBA);
		// Entity ball2 = EntityFactory.createEntity(EntityType.GUMBA);
		// entities.add(ball);
		// entities.add(ball2);
		return null;
	}

	private static void loadOpenGlSettings() {
		LOGGER.trace("Loading OGL-Settings");
		glClearColor(0.0f, 0.0f, 0.0f, 1);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		glEnable(GL_DEPTH_TEST);
	}

}
