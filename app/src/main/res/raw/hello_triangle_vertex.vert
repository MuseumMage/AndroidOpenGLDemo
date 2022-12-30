#version 300 es

layout (location = 0) in vec4 aPos;

void main() {
    gl_Position = aPos;
}