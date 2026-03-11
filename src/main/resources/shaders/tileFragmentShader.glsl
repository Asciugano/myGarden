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

    float brightness = max(dot(unitNormal, unitLightVector), 0.2);

    vec4 textureColor = texture(textureSampler, pass_textureCoords);

    outColor = vec4(brightness * lightColor, 1.0) * textureColor;
}