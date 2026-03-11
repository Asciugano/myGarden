#version 400 core

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;
in float visibility;

out vec4 outColor;

uniform sampler2D textureSampler;
uniform vec3 lightColor;
uniform vec3 attenuation;
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColor;

void main() {
    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitVectorToCamera = normalize(toCameraVector);

    vec3 totalDiffuse = vec3(0);
    vec3 totalSpecular = vec3(0);

    float distance = length(toLightVector);
    float attFactor = attenuation.x + (attenuation.y * distance) + (attenuation.z * distance * distance);

    vec3 unitLightVector = normalize(toLightVector);
    float nDotl = dot(unitNormal, unitLightVector);
    float brightness = max(nDotl, 0);

    vec3 lightDirection = -unitLightVector;
    vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);

    float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
    specularFactor = max(specularFactor, 0);
    float damperFactor = pow(specularFactor, shineDamper);

    totalDiffuse = totalDiffuse + (brightness * lightColor) / attFactor;
    totalSpecular = totalSpecular + (damperFactor * reflectivity * lightColor) / attFactor;
    totalDiffuse = max(totalDiffuse, 0.2);

    vec4 textureColor = texture(textureSampler, pass_textureCoords);
    if(textureColor.a < 0.5) {
        discard;
    }

    outColor = vec4(totalDiffuse, 1) * textureColor + vec4(totalSpecular, 1);
//    outColor = mix(vec4(skyColor, 1), outColor, visibility);
    //    outColor = vec4(1, 1, 1, 1);
}