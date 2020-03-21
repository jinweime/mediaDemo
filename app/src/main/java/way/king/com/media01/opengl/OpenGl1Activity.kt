package way.king.com.media01.opengl

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import way.king.com.media01.R
import way.king.com.media01.audiosrc.AudioRecorder
import way.king.com.media01.audiosrc.AudioRecorderI
import way.king.com.media01.audiosrc.TouchButton
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class OpenGl1Activity : AppCompatActivity() {

    var lgSurfaceView: GLSurfaceView? = null
    var myGLRenderer: MyGLRenderer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lgSurfaceView = MyGLSurace(this)
        setContentView(lgSurfaceView)

    }


    inner class MyGLSurace(context: Context?) : GLSurfaceView(context) {
        init {
            setEGLContextClientVersion(2)
            myGLRenderer = MyGLRenderer()
            setRenderer(myGLRenderer)
        }
    }

    class MyGLRenderer : GLSurfaceView.Renderer {
        override fun onDrawFrame(gl: GL10?) {
            // Redraw background color
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
            GLES20.glViewport(0, 0, width, height)

        }

        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
            //Set the background frame color
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        }
    }


}
