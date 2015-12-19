package de.sebastiankings.renderengine.bo;

import java.util.ArrayList;
import java.util.List;

public class Face {

	private List<FaceEntry> faceData;

	public Face() {
		this.faceData = new ArrayList<FaceEntry>();
	}

	public List<FaceEntry> getFaceData() {
		return faceData;
	}

	public void addFaceDataEntry(FaceEntry entry) {
		faceData.add(entry);
	}
}
