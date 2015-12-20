package de.sebastiankings.renderengine.entities;

import org.joml.Vector3f;


public class PointLight {
    private Vector3f lightPos;

    private Vector3f lightColAmbient;
    private Vector3f lightColDiffuse;
    private Vector3f lightColSpecular;
    
    private Vector3f attenuation = new Vector3f(1,0,0);

    public PointLight(Vector3f lightPos, Vector3f lightColAmbient, Vector3f lightColDiffuse,Vector3f lightColSpecular) {

        this.lightPos = lightPos;
        this.lightColAmbient = lightColAmbient;
        this.lightColDiffuse = lightColDiffuse;
        this.lightColSpecular = lightColSpecular;
    }

    public PointLight(Vector3f lightPos, Vector3f lightColAmbient, Vector3f lightColDiffuse,Vector3f lightColSpecular, Vector3f attenuation) {

        this.lightPos = lightPos;
        this.lightColAmbient = lightColAmbient;
        this.lightColDiffuse = lightColDiffuse;
        this.lightColSpecular = lightColSpecular;
        this.attenuation = attenuation;
    }

    public Vector3f getLightPos() {
        return lightPos;
    }


    public Vector3f getLightColAmbient() {
        return lightColAmbient;
    }

    public Vector3f getLightColDiffuse() {
        return lightColDiffuse;
    }

    public Vector3f getLightColSpecular() {
        return lightColSpecular;
    }

	public Vector3f getAttenuation() {
		return attenuation;
	}


}
