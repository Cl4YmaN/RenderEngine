package de.sebastiankings.renderengine.bo;

public class FaceEntry {

	private int vertexId;
	private int textureId;
	private int normalId;

	public FaceEntry(int vertexId, int textureId, int normalId) {
		this.vertexId = vertexId;
		this.textureId = textureId;
		this.normalId = normalId;
	}

	public int getVertexId() {
		return vertexId;
	}

	public int getTextureId() {
		return textureId;
	}

	public int getNormalId() {
		return normalId;
	}

}
