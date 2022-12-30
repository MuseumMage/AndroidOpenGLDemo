#version 300 es

precision mediump float;
uniform vec4 ourColor;
out vec4 FragColor;

void main() {
    FragColor = ourColor;
}