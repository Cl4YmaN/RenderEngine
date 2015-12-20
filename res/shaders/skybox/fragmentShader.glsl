#version 400

in vec3 textureCoords;
out vec4 out_Color;

uniform samplerCube cubeMap;

void main(void){
    out_Color = texture(cubeMap, textureCoords);
    //out_Color = vec4(0.5,0.5,0.5,1.0);
}