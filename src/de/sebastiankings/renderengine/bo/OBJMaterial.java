package de.sebastiankings.renderengine.bo;

import org.joml.Vector3f;

import de.sebastiankings.renderengine.entities.Material;

public class OBJMaterial {

	private String name;

	private Vector3f ambientColor;
	private Vector3f emission;
	private Vector3f specularColor;
	private float shininess;
	private int lightingMode;
	
	public float[] ambientColorArray;
	public float[] emissionColorArray;
	public float[] specularColorArray;
	public float[] shininessArray;

	
	/**
	 * Later used as Texture
	 */
	private String diffuseMap;

	public OBJMaterial(){
		this.ambientColor = new Vector3f(0.0f,0.0f,0.0f);
		this.emission = new Vector3f(0.1f,0.1f,0.1f);
		this.specularColor = new Vector3f(0.1f,0.1f,0.1f);
		this.shininess = 0.2f;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Vector3f getAmbientColor() {
		return ambientColor;
	}

	public void setAmbientColor(Vector3f ambientColor) {
		this.ambientColor = ambientColor;
	}

	public Vector3f getDiffuseColor() {
		return emission;
	}

	public void setDiffuseColor(Vector3f diffuseColor) {
		this.emission = diffuseColor;
	}

	public Vector3f getSpecularColor() {
		return specularColor;
	}

	public void setSpecularColor(Vector3f specularColor) {
		specularColor = specularColor;
	}

	public float getShininess() {
		return shininess;
	}

	public void setShininess(float shininess) {
		this.shininess = shininess;
	}

	public int getLightingMode() {
		return lightingMode;
	}

	public void setLightingMode(int lightingMode) {
		this.lightingMode = lightingMode;
	}

	public String getDiffuseMap() {
		return diffuseMap;
	}

	public void setDiffuseMap(String diffuseMap) {
		this.diffuseMap = diffuseMap;
	}

	public void convertMaterial(int vertexCount){
		this.ambientColorArray = new float[vertexCount * 3];
		this.emissionColorArray = new float[vertexCount * 3];
		this.specularColorArray = new float[vertexCount * 3];
		this.shininessArray = new float[vertexCount];
		
		for (int i = 0; i < vertexCount; i++) {
			ambientColorArray[i * 3 + 0] = ambientColor.x;
			ambientColorArray[i * 3 + 1] = ambientColor.y;
			ambientColorArray[i * 3 + 2] = ambientColor.z;
		}
		
		for (int i = 0; i < vertexCount; i++) {
			emissionColorArray[i * 3 + 0] = emission.x;
			emissionColorArray[i * 3 + 1] = emission.y;
			emissionColorArray[i * 3 + 2] = emission.z;
		}
		
		for (int i = 0; i < vertexCount; i++) {
			specularColorArray[i * 3 + 0] = specularColor.x;
			specularColorArray[i * 3 + 1] = specularColor.y;
			specularColorArray[i * 3 + 2] = specularColor.z;
		}
		
		for (int i = 0; i < vertexCount; i++) {
			shininessArray[i] = shininess;
		}
	}
	
	public Material generateMaterial(){
		//Material result = new Material(this.name, new Vector3f(0.0f,0.0f,0.0f), this.ambientColor, this.specularColor, this.shininess);
		return null;
	}
}
