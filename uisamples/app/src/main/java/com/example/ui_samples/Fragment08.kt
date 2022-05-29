package com.example.ui_samples

import android.opengl.GLES10
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


private const val TAG = "Fragment08"

// GLSurfaceViewサンプルコード。
// 参考：https://qiita.com/cha84rakanal/items/f82691c033679e53684d
class Fragment08 : Fragment() {
    companion object {
        const val title = "GLSurfaceViewサンプルコード"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val activity = activity as MainActivity
        activity.title = Fragment08.title

        val gLView = GLSurfaceView(activity)
        gLView.setEGLContextClientVersion(2)

        val mRenderer = GLRenderer()
        gLView.setRenderer(mRenderer)
        gLView.renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
        return gLView
    }

    class GLRenderer : GLSurfaceView.Renderer {
        private lateinit var vertexBuffer: FloatBuffer
        private lateinit var colorBuffer: FloatBuffer

        private val vertexShaderCode = """
        attribute vec4 vPosition;
        attribute vec4 color;
        varying   vec4 vColor;
        void main() {
            vColor = color;
            gl_Position = vPosition;
        }
        """

        private val fragmentShaderCode = """
        precision mediump float;

        varying vec4 vColor;
        void main() {
            gl_FragColor = vColor;
        }
        """

        private var shaderProgram = 0

        private fun loadShader(type: Int, shaderCode: String): Int {
            val shader = GLES20.glCreateShader(type)
            GLES20.glShaderSource(shader, shaderCode)
            GLES20.glCompileShader(shader)
            return shader
        }

        override fun onSurfaceCreated(gl: GL10, config: EGLConfig?) {
            gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)

            val values = floatArrayOf(
                -0.5f, -0.5f, 0.0f,
                0.5f, -0.5f, 0.0f,
                0.0f, 0.5f, 0.0f
            )

            vertexBuffer =
                ByteBuffer.allocateDirect(values.size * 4)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer().apply {
                        put(values)
                        position(0)
                    }

            val colors = floatArrayOf(
                1.0f, 1.0f, 0.0f, 1.0f,
                0.0f, 1.0f, 1.0f, 1.0f,
                1.0f, 0.0f, 1.0f, 1.0f
            )

            colorBuffer = ByteBuffer.allocateDirect(colors.size * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer().apply {
                    put(colors)
                    position(0)
                }

            val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
            val fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)
            shaderProgram = GLES20.glCreateProgram()
            GLES20.glAttachShader(shaderProgram, vertexShader)
            GLES20.glAttachShader(shaderProgram, fragmentShader)
            GLES20.glLinkProgram(shaderProgram)
        }

        override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        }

        override fun onDrawFrame(gl: GL10) {
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

            GLES20.glUseProgram(shaderProgram)
            val positionAttribute = GLES20.glGetAttribLocation(shaderProgram, "vPosition")
            GLES20.glEnableVertexAttribArray(positionAttribute)

            GLES20.glVertexAttribPointer(
                positionAttribute,
                3,
                GLES20.GL_FLOAT,
                false,
                0,
                vertexBuffer
            )

            val colorAttribute = GLES20.glGetAttribLocation(shaderProgram, "color")
            GLES20.glEnableVertexAttribArray(colorAttribute)

            GLES20.glVertexAttribPointer(
                colorAttribute,
                4,
                GLES20.GL_FLOAT,
                false,
                0,
                colorBuffer
            )

            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3)

            GLES20.glDisableVertexAttribArray(positionAttribute)
            GLES20.glDisableVertexAttribArray(colorAttribute)
        }
    }
}