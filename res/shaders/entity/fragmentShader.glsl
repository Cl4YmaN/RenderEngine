#version 330 core

in vec3 surfaceNormal;
in vec3 toCameraVector;

in vec3 toLightVector;
in vec2 textureCoords;

in vec3 matEmission;
in vec3 matAmbient;
in vec3 matSpecular;
in float matShininess;

out vec4 out_Color;

uniform sampler2D texture1;

uniform vec3 lightColAmbient;
uniform vec3 lightColDiffuse;
uniform vec3 lightColSpecular;
uniform vec4 attenuation;




vec4 shade(vec3 toLightVector, vec3 unitNormal, vec3 unitToCameraVector,vec3 matDiffuse, vec3 lightColAmbient, vec3 lightColDiffuse, vec3 lightColSpecular)
{
	float distance = length(toLightVector);
	float attFactor = attenuation.x + (attenuation.y * (distance) + attenuation.z * distance * distance);
    vec3 unitLightVector = normalize(toLightVector);
    vec3 lightDirection = -unitLightVector;
    vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);

    float specularFactor = dot(reflectedLightDirection, unitToCameraVector);
    specularFactor = max(specularFactor, 0.0);
    float dampedFactor = pow (specularFactor, matShininess);
    vec3 finalSpecular = dampedFactor * lightColSpecular;

    float nDotl = dot(unitNormal, unitLightVector);
    float brightness = max(nDotl, 0.2);
    vec3 diffuse = brightness * lightColDiffuse;

	return vec4(matEmission + lightColAmbient * matAmbient  + matDiffuse * diffuse + matSpecular * finalSpecular, 1.0);
}

void main(void){

    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitToCameraVector = normalize(toCameraVector);
    
    // TODO: Ersetzen Sie die folgende Zeile, um die Texturfarbe für diesen Texel abzufragen und als diffuse Materialfarbe zu verwenden
    vec3 diffuse = texture(texture1,textureCoords).xyz;
    
    
    out_Color = shade(toLightVector, unitNormal, unitToCameraVector, diffuse, lightColAmbient, lightColDiffuse, lightColSpecular);
}