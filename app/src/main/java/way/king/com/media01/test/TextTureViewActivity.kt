package way.king.com.media01.test

import android.content.pm.PackageManager
import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.TextureView
import android.view.TextureView.*
import android.view.View
import android.widget.VideoView
import java.util.jar.Manifest

class TextTureViewActivity : AppCompatActivity(), SurfaceTextureListener {
    var textTure: TextureView? = null
    var camera: Camera? = null
    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
        camera?.stopPreview()
        camera?.release()
        return true
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
        camera = Camera.open()
        camera?.setPreviewTexture(surface)
        camera?.startPreview()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(android.Manifest.permission.CAMERA), 1)
        }
        bindView()

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        textTure = TextureView(this)
        textTure?.surfaceTextureListener = this
        setContentView(textTure)

    }

    override fun onPause() {
        super.onPause()
    }


    private fun bindView() {

    }


    override fun onDestroy() {
        super.onDestroy()
    }
}
