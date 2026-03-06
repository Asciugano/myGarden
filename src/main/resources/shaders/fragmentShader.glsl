#version 400 core

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector;

out vec4 outColor;

uniform sampler2D textureSampler;
uniform vec3 lightColor;

void main() {
    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitLightVector = normalize(toLightVector);
    float nDotl = dot(unitNormal, unitLightVector);
    float brightness = max(nDotl, 0);
    vec3 diffuse = brightness * lightColor;
//    outColor = vec4(1, 1, 1, 1);
    outColor = vec4(diffuse, 1) * texture(textureSampler, pass_textureCoords);
}