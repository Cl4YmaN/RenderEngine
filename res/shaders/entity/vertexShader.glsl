#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec3 normal;
layout(location = 2) in vec2 textureCoordinates;
layout(location = 3) in vec3 emission;
layout(location = 4) in vec3 ambient;
layout(location = 5) in vec3 specular;
layout(location = 6) in float shininess;

out vec3 surfaceNormal;
out vec3 toCameraVector;
out vec3 toLightVector;
out vec2 textureCoords;

out vec3 matEmission;
out vec3 matAmbient;
out vec3 matSpecular;
out float matShininess;

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

uniform vec3 lightPos;

void main(void){
    vec4 worldPosition = modelMatrix * vec4(position, 1.0);
	gl_Position = projectionMatrix * viewMatrix  * worldPosition;
	surfaceNormal = (transpose(inverse(modelMatrix)) * vec4(normal,0.0)).xyz;

    toLightVector = lightPos - worldPosition.xyz;

	toCameraVector = (inverse(viewMatrix) * vec4(0.0,0.0,0.0,1.0)).xyz - worldPosition.xyz;
    
    // Geben Sie die Texturkoordinaten an den Fragmentshader weiter
    textureCoords = textureCoordinates;
    
    matEmission = emission;
	matAmbient = ambient;
	matSpecular = specular;
	matShininess = shininess;
    
}