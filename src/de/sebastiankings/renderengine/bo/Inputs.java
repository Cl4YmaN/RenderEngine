package de.sebastiankings.renderengine.bo;

import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;

import org.apache.log4j.Logger;

import de.sebastiankings.renderengine.handlers.CursorHandler;
import de.sebastiankings.renderengine.handlers.KeyboardHandler;

public class Inputs {

	private static final Logger LOGGER = Logger.getLogger(Inputs.class);

	private KeyboardHandler keyboard;
	private CursorHandler mouse;

	public Inputs() {
		this.keyboard = new KeyboardHandler();
		this.mouse = new CursorHandler();
	}
	
	public boolean keyPresse(int key){
		return keyboard.iskeyPressed(key);
	}

	public boolean registerInputs(long windowId) {
		LOGGER.debug("Registering Inputs");
		try {
			glfwSetKeyCallback(windowId, keyboard);
			glfwSetCursorPosCallback(windowId, mouse);
			LOGGER.debug("Done");
		} catch (Exception e) {
			LOGGER.error("Cant register inputhandlers", e);
			return false;
		}

		return true;
	}
	
	public void cleanUp(){
		keyboard.release();
		mouse.release();
	}

}
