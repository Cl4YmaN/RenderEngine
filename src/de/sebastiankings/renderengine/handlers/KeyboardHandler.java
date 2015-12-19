package de.sebastiankings.renderengine.handlers;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

public class KeyboardHandler extends GLFWKeyCallback{

	private boolean[] keys = new boolean[65536];
	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		keys[key] = action != GLFW.GLFW_RELEASE;	
	}
	
	public boolean iskeyPressed(int keycode){
		return keys[keycode];
	}

}
