#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec3 normal;
layout(location = 2) in vec2 textureCoordinates;

out vec3 surfaceNormal;
out vec3 toCameraVector;
out vec3 toLightVector;
out vec2 textureCoords;

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
     
}