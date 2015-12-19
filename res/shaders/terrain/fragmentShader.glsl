#version 330 core

in vec3 surfaceNormal;
in vec3 toCameraVector;

in vec3 toLightVector;
in vec2 textureCoords;

out vec4 out_Color;

uniform sampler2D texture1;

uniform vec3 lightColAmbient;
uniform vec3 lightColDiffuse;
uniform vec3 lightColSpecular;

void main(void){

    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitToCameraVector = normalize(toCameraVector);
    
    // TODO: Ersetzen Sie die folgende Zeile, um die Texturfarbe für diesen Texel abzufragen und als diffuse Materialfarbe zu verwenden
    vec3 diffuse = texture(texture1,textureCoords).xyz;
    
    
    out_Color = vec4(diffuse,1);
}