package com.example.androidlearnopengl.utils

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.GLES30
import android.opengl.GLUtils
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import com.example.androidlearnopengl.OpenGLApplication.Companion.context
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


class Utils {
    companion object {

        fun loadShader(type: Int, shaderCode: String): Int {

            // create a vertex shader type (GLES30.GL_VERTEX_SHADER)
            // or a fragment shader type (GLES30.GL_FRAGMENT_SHADER)
            return GLES30.glCreateShader(type).also { shader ->

                // add the source code to the shader and compile it
                GLES30.glShaderSource(shader, shaderCode)
                GLES30.glCompileShader(shader)

                // check shader program
                val compileStatus = IntArray(1)
                GLES30.glGetShaderiv(shader, GLES30.GL_COMPILE_STATUS, compileStatus, 0)

                if (compileStatus[0] == 0) {
                    Log.e("zhangbo", "loadShader $type Error")
                    Log.e("zhangbo", GLES30.glGetShaderInfoLog(shader))
                    GLES30.glDeleteShader(shader)
                }
            }
        }

        fun readStringFromRaw(@RawRes resId: Int): String {
            return runCatching {
                val builder = StringBuilder()
                val reader =
                    BufferedReader(InputStreamReader(context.resources.openRawResource(resId)))//InputStreamReader它使用指定的字符集读取字节并将它们解码为字符//InputStream openRawResource(int id) 获取资源的数据流，读取资源数据
                var nextLine: String? = reader.readLine()
                while (nextLine != null) {
                    builder.append(nextLine).append("\n")
                    nextLine = reader.readLine()
                }
                reader.close()
                builder.toString()

            }.onFailure {
                when (it) {
                    is IOException -> {
                        throw RuntimeException("Could not open resource: $resId", it)
                    }
                    is Resources.NotFoundException -> {
                        throw RuntimeException("Resource not found: $resId", it)
                    }
                    else -> {}
                }
            }.getOrThrow()//如果此实例表示成功，则返回封装的值;如果实例失败，则引发封装的 Throwable 异常

        }

        //将纹理加载到OpenGL中
        fun loadTexture(@DrawableRes resourceId: Int): Int {

            val textureObjectIds = IntArray(1)
            GLES20.glGenTextures(1, textureObjectIds, 0)//生成一个纹理对象,将生成的ID存储在textureObjectIds中,1要生成的纹理ID的数量
            if (textureObjectIds[0] == 0) {
                Log.w("zhangbo", "Could not generate a new OpenGL textture object.")
                return 0
            }
            //OpenGL需要未压缩的原始数据，因此我们需要使用Android内置的BitmapFactory创建位图，将图像文件解压缩为OpenGL能够理解的格式
            val option = BitmapFactory.Options().apply {   //BitmapFactory类里面的所有方法都是用来解码创建一个Bitmap，其中有一个重要的类是Options，此类用于解码Bitmap时的各种参数控制
                inScaled = false//设置这个Bitmap是否可以被缩放，默认值是true，表示可以被缩放,这告诉Android提供原始图像数据，而不是数据的缩放版本
            }
            val bitmap: Bitmap? = BitmapFactory.decodeResource(
                context.resources,
                resourceId,
                option
            )//第一个参数是包含你要加载的位图资源文件的对象（一般写成 getResources（）就ok了）；第二个时你需要加载的位图资源的Id,第三个参数应该是对你要加载的位图是否需要完整显示
            if (bitmap == null) {
                Log.w("zhangbo", "Resource ID $resourceId could not be decode.")
                GLES20.glDeleteTextures(1, textureObjectIds, 0)//删除OpenGL纹理对象
                return 0
            }
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureObjectIds[0])//第一个参数GL_TEXTURE_2D告诉OpenGL应该将其视为二维纹理，第二个参数告诉OpenGL要绑定到哪个纹理对象ID。
            // 设置默认纹理过滤参数
            //调用glTexParameteri()来设置每个过滤器：GL_TEXTURE_MIN_FILTER表示缩小的情况，而GL_TEXTURE_MAG_FILTER表示放大的情况
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR)
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)

            //将纹理加载到OpenGL并返回ID
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)//将位图数据加载到opengl,这个调用告诉OpenGL读入位图数据，并将其复制到当前绑定的纹理对象中
            bitmap.recycle() //回收位图
            GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D) //生成MIP映射需要的纹理
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)//解除纹理绑定，传入的纹理ID为0
            return textureObjectIds[0]
        }
    }
}