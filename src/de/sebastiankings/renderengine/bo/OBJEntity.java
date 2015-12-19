package de.sebastiankings.renderengine.bo;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * Rerpäsentiert die Daten eines Objekts aus einer OBJ Datei
 * 
 * @author SKI
 *
 */
public class OBJEntity {

	private String objectDescription;
	private String usedMaterial;

	private List<Vector3f> vertices = new ArrayList<Vector3f>();
	private List<Vector3f> normals = new ArrayList<Vector3f>();
	private List<Vector2f> texturesCoords = new ArrayList<Vector2f>();
	private List<Integer> indices = new ArrayList<Integer>();
	private List<Face> faces = new ArrayList<Face>();
	
	public float[] verticesArray;
	public float[] normalsArray;
	public float[] texturesArray;
	public int[] indicesArray;
	

	public OBJEntity() {
		this.vertices = new ArrayList<Vector3f>();
		this.normals = new ArrayList<Vector3f>();
		this.texturesCoords = new ArrayList<Vector2f>();
		this.indices = new ArrayList<Integer>();
		this.faces = new ArrayList<Face>();
	}

	public void addVertex(Vector3f vertex) {
		this.vertices.add(vertex);
	}

	public void addNormal(Vector3f normal) {
		this.normals.add(normal);
	}

	public void addTextureCoord(Vector2f textureCoord) {
		this.texturesCoords.add(textureCoord);
	}

	public void addIndex(Integer index) {
		this.indices.add(index);
	}

	public void addFace(Face face) {
		this.faces.add(face);
	}

	public String getObjectDescription() {
		return objectDescription;
	}

	public void setObjectDescription(String objectDescription) {
		this.objectDescription = objectDescription;
	}

	public String getUsedMaterial() {
		return usedMaterial;
	}

	public void setUsedMaterial(String usedMaterial) {
		this.usedMaterial = usedMaterial;
	}

	public void  convertModelData() {
		verticesArray = new float[vertices.size() * 3];
		normalsArray = new float[vertices.size() * 3];
		texturesArray = new float[vertices.size() * 2];
		createVertexArray();
		for (Face f : faces) {
			for (FaceEntry faceEntry : f.getFaceData()) {
				processVertex(faceEntry);
			}
		}
		indicesArray = new int[indices.size()];
		for (int i = 0; i < indices.size(); i++) {
			indicesArray[i] = indices.get(i);
		}
	}

	private void createVertexArray() {
		for (int i = 0; i < vertices.size(); i++) {
			Vector3f currentVertex = vertices.get(i);
			verticesArray[i * 3 + 0] = currentVertex.x;
			verticesArray[i * 3 + 1] = currentVertex.y;
			verticesArray[i * 3 + 2] = currentVertex.z;
		}
	}

	// FacesEntry,
	private void processVertex(FaceEntry entry) {
		int currentVertexPointer = entry.getVertexId() - 1;
		indices.add(currentVertexPointer);

		Vector2f currentTex = texturesCoords.get(entry.getTextureId()-1);
		texturesArray[currentVertexPointer * 2] = currentTex.x;
		texturesArray[currentVertexPointer * 2 + 1] = 1 - currentTex.y;

		Vector3f currentNorm = normals.get(entry.getNormalId() - 1);
		normalsArray[currentVertexPointer * 3] = currentNorm.x;
		normalsArray[currentVertexPointer * 3 + 1] = currentNorm.y;
		normalsArray[currentVertexPointer * 3 + 2] = currentNorm.z;
	}

	@Override
	public String toString() {
		return "OBJEntity [objectDescription=" + objectDescription + ", usedMaterial=" + usedMaterial + ", verticesCount=" + vertices.size() + ", normalCount=" + normals.size() + ", texturesCoordsCount=" + texturesCoords.size() + ", indicesCount=" + indices.size() + ", facesCount=" + faces.size() + "]";
	}

}
