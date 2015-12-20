package de.sebastiankings.renderengine.texture;

import java.nio.ByteBuffer;

public class PNGData {

	private int width;
	private int height;
	private ByteBuffer pictureData;

	public PNGData(int width, int height, ByteBuffer pictureData) {
		this.width = width;
		this.height = height;
		this.pictureData = pictureData;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public ByteBuffer getPictureData() {
		return pictureData;
	}

}
