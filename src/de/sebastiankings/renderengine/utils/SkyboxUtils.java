package de.sebastiankings.renderengine.utils;

import org.apache.log4j.Logger;

import de.sebastiankings.renderengine.entities.Model;
import de.sebastiankings.renderengine.entities.types.Skybox;
import de.sebastiankings.renderengine.texture.Texture;

public class SkyboxUtils {
	private static final Logger LOGGER = Logger.getLogger(SkyboxUtils.class);
	private static final float SIZE = 512;

	private static final float[] VERTICES = { -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE,

			-SIZE, -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, -SIZE, SIZE,

			SIZE, -SIZE, -SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, -SIZE, SIZE, -SIZE, -SIZE,

			-SIZE, -SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE,

			-SIZE, SIZE, -SIZE, SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, -SIZE, SIZE, SIZE, -SIZE, SIZE, -SIZE,

			-SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, SIZE };
	
	
	public static float[] getSkyboxVertices(){
		return VERTICES;
	}

	public static Skybox loadSkybox(String folderName) {
		LOGGER.debug("Loading Skybox");
		Texture texture = LoaderUtils.loadCubeMapTexture(folderName);
		Model skyboxModel = LoaderUtils.loadSkyboxVAO(SkyboxUtils.getSkyboxVertices());
		Skybox result = new Skybox(texture, skyboxModel);
		return result;

	}
}
