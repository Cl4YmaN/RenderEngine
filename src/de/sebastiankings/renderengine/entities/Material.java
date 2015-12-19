package de.sebastiankings.renderengine.entities;

import org.joml.Vector3f;

/**
 * Created by bryan on 11.11.2015.
 */
public class Material {
    private Vector3f emission = new Vector3f(0, 0, 0);
    private Vector3f ambient = new Vector3f(1, 1, 1);
    private Vector3f specular = new Vector3f(1, 1, 1);

    private float shininess = 1.0f;

    public Material(Vector3f emission, Vector3f ambient,  Vector3f specular, float shininess) {
        this.emission = emission;
        this.ambient = ambient;
        this.specular = specular;
        this.shininess = shininess;
    }

    public Vector3f getEmission() {
        return emission;
    }

    public Vector3f getAmbient() {
        return ambient;
    }

    public Vector3f getSpecular() {
        return specular;
    }

    public float getShininess() {
        return shininess;
    }

}
