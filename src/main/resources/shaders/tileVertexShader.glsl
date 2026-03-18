#version 400 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec3 color;
layout(location = 2) in vec3 normal;

out vec3 passColor;
out vec3 surfaceNormal;
out vec3 worldPosition;
out vec3 toLightVector;
out vec3 toCameraVector;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition;

void main() {
    vec4 worldPos = transformationMatrix * vec4(position, 1.0);
    gl_Position = projectionMatrix * viewMatrix * worldPos;

    passColor = color;
    worldPosition = worldPos.xyz;

    surfaceNormal = normalize((transformationMatrix * vec4(normal, 0)).xyz);

    toLightVector = lightPosition - worldPosition.xyz;

    toCameraVector = (inverse(viewMatrix) * vec4(0, 0, 0, 1)).xyz - worldPosition.xyz;
}
