package de.sebastiankings.renderengine.entities.types;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import de.sebastiankings.renderengine.entities.Camera;
import de.sebastiankings.renderengine.entities.Model;
import de.sebastiankings.renderengine.shaders.SkyboxShaderProgramm;
import de.sebastiankings.renderengine.texture.Texture;

public class Skybox {

	private Texture texture;
	private Model model;

	public Skybox(Texture texture, Model model) {
		super();
		this.texture = texture;
		this.model = model;
	}

	public Texture getTexture() {
		return texture;
	}

	public Model getModel() {
		return model;
	}
	
	public void render(SkyboxShaderProgramm shader, Camera camera){
		shader.start();
		shader.loadProjectionMatrix(camera.getProjectionMatrix());
		shader.loadViewMatrix(camera);
		glBindVertexArray(model.getVaoID());
		glEnableVertexAttribArray(0);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, this.getTexture().getTextureID());
		GL11.glDrawArrays(GL_TRIANGLES,0, model.getVertexCount());
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		shader.stop();
	}

}
