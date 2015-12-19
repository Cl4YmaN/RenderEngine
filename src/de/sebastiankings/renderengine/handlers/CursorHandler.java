package de.sebastiankings.renderengine.handlers;

import org.apache.log4j.Logger;
import org.lwjgl.glfw.GLFWCursorPosCallback;

public class CursorHandler extends GLFWCursorPosCallback {

	private double x;
	private double y;

	private double deltaX;
	private double deltaY;

	@Override
	public void invoke(long window, double xpos, double ypos) {
		deltaX = x - xpos;
		deltaY = y - ypos;
		x = xpos;
		y = ypos;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public float getDeltaX() {
		return (float) deltaX;
	}

	public float getDeltaY() {
		return (float) deltaY;
	}

}
