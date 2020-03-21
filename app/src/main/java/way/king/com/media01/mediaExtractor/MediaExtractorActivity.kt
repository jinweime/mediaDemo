package way.king.com.media01.mediaExtractor

import android.graphics.Camera
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.*
import android.view.View.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import way.king.com.media01.R
import way.king.com.media01.audiosrc.AudioRecorder
import way.king.com.media01.audiosrc.AudioRecorderI
import way.king.com.media01.audiosrc.TouchButton
import java.io.File

class MediaExtractorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.camera_activity)
        val videoPath =
            Environment.getExternalStorageDirectory().toString() + File.separator + "AAAAA/vide21o.mp4"
        val outPath =
            Environment.getExternalStorageDirectory().toString() + File.separator + "AAAAA/vick.mp4"
        val audioOutPath =
            Environment.getExternalStorageDirectory().toString() + File.separator + "AAAAA/vick.aac"
        if (File(videoPath).exists()) {
            Thread() {
                MediaUtils.extractVideo(videoPath, outPath)
                MediaUtils.extractAudio(videoPath, audioOutPath)
            }.start()
        } else {
            Toast.makeText(this, "文件不存在先存储一个文件", Toast.LENGTH_LONG).show()
        }

    }


}
