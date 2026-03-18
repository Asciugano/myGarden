#version 400 core

in vec3 passColor;
in vec3 surfaceNormal;
in vec3 toCameraVector;

out vec4 outColor;

uniform vec3 lightColor;

void main() {
    vec3 N = normalize(surfaceNormal);

    vec3 L = normalize(vec3(-1.0, 1.0, 1.0));

    vec3 V = normalize(toCameraVector);

    float diff = max(dot(N, L), 0.0);     
    vec3 diffuse = diff * lightColor;

    vec3 R = reflect(-L, N);
    float spec = pow(max(dot(V, R), 0.0), 16.0);
    vec3 specular = 0.1 * spec * lightColor;  

    float ambientStrength = 0.2;
    vec3 ambient = ambientStrength * passColor;

    vec3 finalColor = ambient + diffuse * passColor + specular;

    outColor = vec4(finalColor, 1);
}
