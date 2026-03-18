#version 400 core

in vec3 passColor;
in vec3 surfaceNormal;
in vec3 worldPosition;
in vec3 toCameraVector;
in vec3 toLightVector;

out vec4 outColor;

uniform vec3 lightColor;

void main() {
  vec3 unitNormal = normalize(surfaceNormal);
  vec3 lightDir = normalize(toLightVector);
  vec3 viewDir = normalize(toCameraVector);

  // Diffuse
  float diff = max(dot(unitNormal, lightDir), 0.2);
  vec3 diffuse = diff * lightColor;

  // Specular
  vec3 reflectDir = reflect(-lightDir, unitNormal);
  float spec = pow(max(dot(viewDir, reflectDir), 0.0), 16);
  vec3 specular = spec * lightColor;

  // Ambient
  float ambientStrength = 0.4;
  vec3 ambient = ambientStrength * lightColor;

  vec3 finalColor = (ambient + diffuse + specular) * passColor;

  outColor = vec4(finalColor, 1.0);
}
