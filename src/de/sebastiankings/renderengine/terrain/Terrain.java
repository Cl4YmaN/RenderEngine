package de.sebastiankings.renderengine.terrain;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import org.joml.Matrix4f;

import de.sebastiankings.renderengine.entities.Camera;
import de.sebastiankings.renderengine.entities.Model;
import de.sebastiankings.renderengine.entities.PointLight;
import de.sebastiankings.renderengine.shaders.TerrainShaderProgramm;
import de.sebastiankings.renderengine.texture.Texture;

public class Terrain {
	private Model model;
	private Texture texture;

	public Terrain(Model model, Texture texture) {
		this.model = model;
		this.texture = texture;
	}

	public Model getModel() {
		return model;
	}

	public Texture getTexture() {
		return texture;
	}

	public void render(TerrainShaderProgramm shader, Camera camera, PointLight light) {
		shader.start();
		shader.loadTexture();
		shader.loadLight(light);
		shader.loadViewMatrix(camera.getViewMatrix());

		// bind VAO and activate VBOs //
		Model model = this.getModel();
		glBindVertexArray(model.getVaoID());
		// Activate VAO Data
		// VERTICES
		glEnableVertexAttribArray(0);
		// NORMALS
		glEnableVertexAttribArray(1);
		// DIFFUSEMAP
		glEnableVertexAttribArray(2);

		glBindTexture(GL_TEXTURE_2D, this.getTexture().getTextureID());
		shader.loadModelMatrix(new Matrix4f());
		shader.loadProjectionMatrix(camera.getProjectionMatrix());

		glDrawElements(GL_TRIANGLES, model.getVertexCount(), GL_UNSIGNED_INT, 0);

		// CLEANUP VERTEX ARRAY ATTRIBUTES
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);

		// CLEANUP VERTEX ARRAY
		glBindVertexArray(0);

		shader.stop();
	}

}
