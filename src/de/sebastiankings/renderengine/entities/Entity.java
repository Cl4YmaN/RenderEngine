package de.sebastiankings.renderengine.entities;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import de.sebastiankings.renderengine.shaders.EntityShaderProgram;
import de.sebastiankings.renderengine.texture.Texture;

public class Entity extends BaseEntity {

	private Texture diffuseTexture;

	public Entity(EntityType type, Model model) {
		super(type, model, new Matrix4f());
	}

	public Entity(EntityType type, Model model, Matrix4f modelMatrix) {
		super(type, model, modelMatrix);
	}

	public Texture getTexture() {
		return diffuseTexture;
	}

	public void setTexture(Texture texture) {
		this.diffuseTexture = texture;
	}

	@Override
	public void render(float deltaTime, EntityShaderProgram shader, Camera camera, PointLight light) {
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
		// EMISSION
		glEnableVertexAttribArray(3);
		// AMBIENT
		glEnableVertexAttribArray(4);
		// SPECULAR
		glEnableVertexAttribArray(5);
		// SHININESS
		glEnableVertexAttribArray(6);

		glBindTexture(GL_TEXTURE_2D, this.getTexture().getTextureID());
		shader.loadModelMatrix(this.getModelMatrix());
		shader.loadProjectionMatrix(camera.getProjectionMatrix());

		glDrawElements(GL_TRIANGLES, model.getVertexCount(), GL_UNSIGNED_INT, 0);

		// CLEANUP VERTEX ARRAY ATTRIBUTES
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glDisableVertexAttribArray(3);
		glDisableVertexAttribArray(4);
		glDisableVertexAttribArray(5);
		glDisableVertexAttribArray(6);
		// CLEANUP VERTEX ARRAY
		glBindVertexArray(0);
		shader.stop();
	}

	public void moveEntity(Vector3f move) {
		this.entityState.getCurrentPosition().add(move);
		updateModelMatrix();
	}

	public void rotateZ(float roation) {
		this.entityState.incrementRotationZ(roation);
		updateModelMatrix();
	}
	
	private void updateModelMatrix(){
		Matrix4f mm = new Matrix4f();
		mm.rotateZ(entityState.getRoationZ());
		mm.translate(entityState.getCurrentPosition());
		this.modelMatrix = mm;
	}

}
