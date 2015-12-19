package de.sebastiankings.renderengine.utils;

import org.apache.log4j.Logger;

import de.sebastiankings.renderengine.entities.Model;
import de.sebastiankings.renderengine.terrain.Terrain;
import de.sebastiankings.renderengine.texture.Texture;

public class TerrainUtils {

	private static final Logger LOGGER = Logger.getLogger(TerrainUtils.class);

	public static Terrain generateTerrain(float width, float length) {
		LOGGER.debug("Generating Terrain");
		float halfWidth = width / 2;
//		float[] vertices = {
//				// frontline
//				-halfWidth, 0.0f, 0.0f, halfWidth, 0.0f, 0.0f,
//				// horizontLine
//				-halfWidth, 0.0f, -length, halfWidth, 0.0f, -length, };
		
		float[] vertices = {
				// frontline
				-halfWidth, 0.0f, 0.0f,
				halfWidth, 0.0f, 0.0f,
				// horizontLine
				-halfWidth, 0.0f, -length,
				halfWidth, 0.0f, -length };

		float[] textures = { 0.0f, 0.0f, 0.0f, 1.0f, length / halfWidth, 0.0f, length / halfWidth, 1.0f, };

		float[] normals = { 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f };

		int[] indices = { 0, 1, 3, 0, 3, 2 };
		Model terrainModel = LoaderUtils.loadTerrainVAO(vertices, textures, normals, indices);
		Texture terrainTexture = LoaderUtils.loadTexture("res/terrain/stars.png");
		Terrain result = new Terrain(terrainModel, terrainTexture);
		LOGGER.debug("Terrain generated");
		return result;
	}

}
