package way.king.com.media01.camera

import android.graphics.Camera
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.*
import android.widget.Button
import android.widget.LinearLayout
import way.king.com.media01.R
import way.king.com.media01.audiosrc.AudioRecorder
import way.king.com.media01.audiosrc.AudioRecorderI
import way.king.com.media01.audiosrc.TouchButton

class CmaraActivity : AppCompatActivity(), SurfaceHolder.Callback {
    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {


    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        camera?.setPreviewDisplay(holder)
        camera?.startPreview()

    }


    var surfaceView: SurfaceView? = null
    var textureView: TextureView? = null
    var camera: android.hardware.Camera? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.camera_activity)
        surfaceView = findViewById(R.id.surface)
        textureView = findViewById(R.id.texture)
        camera = android.hardware.Camera.open()
        camera?.setDisplayOrientation(90)
        val parameters = camera?.parameters
        parameters?.pictureFormat = ImageFormat.NV21
        camera?.parameters = parameters
        camera?.setPreviewCallback(object : android.hardware.Camera.PreviewCallback {
            override fun onPreviewFrame(data: ByteArray?, camera: android.hardware.Camera?) {
                Log.i("vick", data.toString())
            }

        })

//        surfaceView?.holder?.addCallback(this)
        textureView?.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureSizeChanged(
                surface: SurfaceTexture?,
                width: Int,
                height: Int
            ) {

            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
                camera?.release()
                return false
            }

            override fun onSurfaceTextureAvailable(
                surface: SurfaceTexture?,
                width: Int,
                height: Int
            ) {
                camera?.setPreviewTexture(surface)
                camera?.startPreview()
            }

        }
    }


}
