#version 330 core
layout (location = 0) in vec3 lPos;
layout (location = 1) in vec3 lNormal;
layout (location = 2) in vec2 lTexCoords;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

out vec3 fragPos;
out vec3 normal;
out vec2 texCoords;

void main() {
    gl_Position = projection * view * model * vec4(lPos, 1.0);
    fragPos = vec3(model * vec4(lPos, 1.0));
    normal = lNormal;
    texCoords = lTexCoords;
}