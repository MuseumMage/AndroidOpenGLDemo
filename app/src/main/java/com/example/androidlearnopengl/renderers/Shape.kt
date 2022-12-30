package com.example.androidlearnopengl.renderers

import android.opengl.GLES30
import android.util.Log
import com.example.androidlearnopengl.R
import com.example.androidlearnopengl.utils.Utils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

const val COORDS_PER_VERTEX = 3

var triangleCoords = floatArrayOf(     // in counterclockwise order:
    0.0f, 0.622008459f, 0.0f,      // top
    -0.5f, -0.311004243f, 0.0f,    // bottom left
    0.5f, -0.311004243f, 0.0f      // bottom right
)

var squareCoords = floatArrayOf(
    -0.5f, 0.5f, 0.0f,      // top left
    -0.5f, -0.5f, 0.0f,      // bottom left
    0.5f, -0.5f, 0.0f,      // bottom right
    0.5f, 0.5f, 0.0f       // top right
)

class Shape {
    // number of coordinates per vertex in this array

    class Triangle {

        private var mProgram: Int

        private var positionHandle: Int = 0
        private var mColorHandle: Int = 0

        private val vertexCount: Int = triangleCoords.size / COORDS_PER_VERTEX
        private val vertexStride: Int = COORDS_PER_VERTEX * 4 // 4 bytes per vertex

        private val vertexShaderCode =
            Utils.readStringFromRaw(R.raw.android_doc_vertex_shader)
        private val fragmentShaderCode =
            Utils.readStringFromRaw(R.raw.android_doc_frag_shader)

        private var vPMatrixHandle: Int = 0

        // Set color with red, green, blue and alpha (opacity) values
        val color = floatArrayOf(0.63671875f, 0.76953125f, 0.22265625f, 1.0f)

        init {

            val vertexShader: Int = loadShader(GLES30.GL_VERTEX_SHADER, vertexShaderCode)
            val fragmentShader: Int = loadShader(GLES30.GL_FRAGMENT_SHADER, fragmentShaderCode)

            // create empty OpenGL ES Program
            mProgram = GLES30.glCreateProgram().also {

                // add the vertex shader to program
                GLES30.glAttachShader(it, vertexShader)

                // add the fragment shader to program
                GLES30.glAttachShader(it, fragmentShader)

                // creates OpenGL ES program executables
                GLES30.glLinkProgram(it)
            }

            val linkStatus = IntArray(1)
            GLES30.glGetProgramiv(mProgram, GLES30.GL_LINK_STATUS, linkStatus, 0)
            if (linkStatus[0] == 0) {
                Log.d("zhangbo", "link went wrong ")
                GLES30.glDeleteProgram(mProgram)
            }
        }

        private var vertexBuffer: FloatBuffer =
            // (number of coordinate values * 4 bytes per float)
            ByteBuffer.allocateDirect(triangleCoords.size * 4).run {
                // use the device hardware's native byte order
                order(ByteOrder.nativeOrder())

                // create a floating point buffer from the ByteBuffer
                asFloatBuffer().apply {
                    // add the coordinates to the FloatBuffer
                    put(triangleCoords)
                    // set the buffer to read the first coordinate
                    position(0)
                }
            }

        fun draw(mvpMatrix: FloatArray) {
            // Add program to OpenGL ES environment
            GLES30.glUseProgram(mProgram)

            // get handle to vertex shader's vPosition member
            positionHandle = GLES30.glGetAttribLocation(mProgram, "vPosition").also {

                // Enable a handle to the triangle vertices
                GLES30.glEnableVertexAttribArray(it)

                // Prepare the triangle coordinate data
                GLES30.glVertexAttribPointer(
                    it,
                    COORDS_PER_VERTEX,
                    GLES30.GL_FLOAT,
                    false,
                    vertexStride,
                    vertexBuffer
                )

                // get handle to shape's transformation matrix
                vPMatrixHandle =
                    GLES30.glGetUniformLocation(mProgram, "uMVPMatrix").also { matrixHandle ->

                        // Pass the projection and view transformation to the shader
                        GLES30.glUniformMatrix4fv(it, 1, false, mvpMatrix, 0)
                    }

                // get handle to fragment shader's vColor member
                mColorHandle = GLES30.glGetUniformLocation(mProgram, "vColor").also { colorHandle ->

                    // Set color for drawing the triangle
                    GLES30.glUniform4fv(colorHandle, 1, color, 0)
                }

                // Draw the triangle
                GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vertexCount)

                // Disable vertex array
                GLES30.glDisableVertexAttribArray(it)
            }
        }
    }

    class Square {

        private val drawOrder = shortArrayOf(0, 1, 2, 0, 2, 3) // order to draw vertices

        // initialize vertex byte buffer for shape coordinates
        private val vertexBuffer: FloatBuffer =
            // (# of coordinate values * 4 bytes per float)
            ByteBuffer.allocateDirect(squareCoords.size * 4).run {
                order(ByteOrder.nativeOrder())
                asFloatBuffer().apply {
                    put(squareCoords)
                    position(0)
                }
            }

        // initialize byte buffer for the draw list
        private val drawListBuffer: ShortBuffer =
            // (# of coordinate values * 2 bytes per short)
            ByteBuffer.allocateDirect(drawOrder.size * 2).run {
                order(ByteOrder.nativeOrder())
                asShortBuffer().apply {
                    put(drawOrder)
                    position(0)
                }
            }
    }

    companion object {
        fun loadShader(type: Int, shaderCode: String): Int {

            // create a vertex shader type (GLES30.GL_VERTEX_SHADER)
            // or a fragment shader type (GLES30.GL_FRAGMENT_SHADER)
            return GLES30.glCreateShader(type).also { shader ->

                // add the source code to the shader and compile it
                GLES30.glShaderSource(shader, shaderCode)
                GLES30.glCompileShader(shader)
            }
        }
    }
}