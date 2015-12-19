package de.sebastiankings.renderengine.entities;

import java.text.DecimalFormat;

import org.apache.log4j.Logger;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import de.sebastiankings.renderengine.engine.DisplayManager;

/**
 * Created by bryan on 28.10.2015.
 */
public class Camera {

	
	private static final Logger LOGGER = Logger.getLogger(Camera.class);
	private static final float FOV = 70;
	private static final Vector3f DEFAULT_UP = new Vector3f(0, 1, 0);
	private static final DecimalFormat FORMAT = new DecimalFormat("#0.0000");
	private static final float MOVE_FACTOR = 2f;
	private static final float MIN_THETA = (float) (Math.PI / 2.0) * -1.0f + 0.001f;
	private static final float MAX_THETA = (float) (Math.PI / 2.0) * 1.0f - 0.001f;
	private Matrix4f viewMatrix = new Matrix4f();
	private Matrix4f projectionMatrix = new Matrix4f();
	private Vector3f camPos = new Vector3f();
	private Vector3f lookDir = new Vector3f();

	private float theta = (float) Math.PI * 0.17f;
	private float phi = (float) Math.PI * 0.0f;
	private float camDist = 28.0f;

	public Camera() {
		this.projectionMatrix = createProjectionMatrix(1.0f, 2000.0f);
		this.camPos = new Vector3f(0, 50, 50);
		this.lookDir = new Vector3f(0,-1,0);
		LOGGER.debug("Campos: " + camPos.toString(FORMAT));
		LOGGER.debug("LookDir: " + lookDir.toString(FORMAT));
		LOGGER.trace("");
		this.updateViewMatrix();
	}

	public Matrix4f getProjectionMatrix() {
		return new Matrix4f(this.projectionMatrix);
	}

	public Matrix4f getViewMatrix() {
		return new Matrix4f(this.viewMatrix);
	}

	public void incrementTheta(float dTheta) {
		float newTheta = clamp(MIN_THETA, MAX_THETA, this.theta - dTheta);
		this.theta = newTheta;
	}

	private float clamp(float lower, float upper, float value) {
		if (value < lower) {
			return lower;
		}
		if (value > upper) {
			return upper;
		}
		return value;
	}

	public void incrementPhi(float dPhi) {
		this.phi -= dPhi;
	}

	public void setDist(float dy) {
		camDist = Math.max(camDist + (dy / 5.0f), 3.0f);
	}

	public void moveForward() {
		// Berechnung der Bewegungsrichtung
		Vector3f moveDirection = getCurrentLookDirection();
		moveDirection = moveDirection.normalize();
		moveDirection = moveDirection.mul(MOVE_FACTOR * -1);
		camPos = camPos.add(moveDirection);
		// fix Cameraposition
		
	}

	public void moveBackward() {
		Vector3f moveDirection = getCurrentLookDirection();
		moveDirection = moveDirection.normalize();
		moveDirection = moveDirection.mul(MOVE_FACTOR);
		camPos = camPos.add(moveDirection);
	}

	public void moveLeft() {
		// Berechnung der Bewegungsrichtung
		Vector3f moveDirection = getCurrentLookDirection();
		Vector3f leftDirection = new Vector3f(moveDirection).cross(DEFAULT_UP);
		leftDirection = leftDirection.normalize();
		leftDirection = leftDirection.mul(MOVE_FACTOR);
		camPos = camPos.add(leftDirection);
		// fix Cameraposition
	}

	public void moveRight() {
		Vector3f moveDirection = getCurrentLookDirection();
		Vector3f leftDirection = new Vector3f(moveDirection).cross(DEFAULT_UP);
		Vector3f rightDirection = new Vector3f(leftDirection).negate();
		rightDirection = rightDirection.normalize();
		rightDirection = rightDirection.mul(MOVE_FACTOR);
		camPos = camPos.add(rightDirection);
	}

	public void move(Vector3f movement) {
		camPos.add(movement);
	}
	
	private Vector3f getCurrentLookDirection() {
		return new Vector3f((float) (Math.sin(phi) * Math.cos(theta)), (float) (Math.sin(theta)), (float) (Math.cos(phi) * Math.cos(theta)));
	}

	public void updateViewMatrix() {
//		lookDir = getCurrentLookDirection().negate();
		Vector3f lookAt = new Vector3f(camPos).add(new Vector3f(new Vector3f(lookDir)));
		viewMatrix = new Matrix4f().lookAt(camPos, lookAt, DEFAULT_UP);
	}

	private Matrix4f createProjectionMatrix(float near, float far) {
		float aspectRatio = (float) DisplayManager.getWidth() / (float) DisplayManager.getHeight();
		float y_scale = (float) (1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio;
		float x_scale = y_scale / aspectRatio;
		float frustum_length = far - near;

		Matrix4f m = new Matrix4f();
		m.m00 = x_scale;
		m.m11 = y_scale;
		m.m22 = -((far + near) / frustum_length);
		m.m23 = -1;
		m.m32 = -((2 * near * far) / frustum_length);
		m.m33 = 0;
		return m;
	}
}
