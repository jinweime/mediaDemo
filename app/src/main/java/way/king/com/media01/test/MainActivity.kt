package way.king.com.media01.test

import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import way.king.com.media01.R

class MainActivity : AppCompatActivity() {

    private var surface: SurfaceView? = null
    private var holder: SurfaceHolder? = null
    var record: AudioRecord? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.RECORD_AUDIO
                ), 1
            )
        }
        //录音
        record = AudioRecord(this)
        //播放视频
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setOnPreparedListener {

        }
        setContentView(R.layout.activity_main)
        bindView()

    }

    override fun onPause() {
        super.onPause()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        record?.start()

    }


    private fun bindView() {
        findViewById<View>(R.id.start).setOnClickListener {
            //                        start()

            record?.start()
//            startActivity(Intent(this, TextTureViewActivity::class.java))
        }
        findViewById<View>(R.id.pause).setOnClickListener {
            //            pause()
        }
        findViewById<View>(R.id.reset).setOnClickListener {

            //            reset()
            record?.stop()

        }

        findViewById<View>(R.id.startAudio).setOnClickListener {
            startAudio()

        }
        surface = findViewById(R.id.surface)
        holder = surface?.holder
        holder?.addCallback(MyHolder())
    }

    inner class MyHolder : SurfaceHolder.Callback {
        override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {

        }

        override fun surfaceDestroyed(holder: SurfaceHolder?) {
        }

        override fun surfaceCreated(holder: SurfaceHolder?) {
            mediaPlayer?.setDisplay(holder)
        }

    }


    private fun start() {
        startMedia()
    }

    private fun pause() {
        if (mediaPlayer?.isPlaying!!) {
            mediaPlayer?.pause()
        }
    }

    var mediaPlayer: MediaPlayer? = MediaPlayer()
    private fun startMedia() {
        if (!mediaPlayer?.isPlaying!!) {
            //设置播放源 支持本地文件和网络文件
            mediaPlayer?.setDataSource("https://rbv01.ku6.com/wifi/o_1dv2knrei18ju12l7l2vgj210oe11kvs")
            //开始异步准备
            mediaPlayer?.prepareAsync()
            //设置监听
            mediaPlayer?.setOnPreparedListener {
                //开始播放
                mediaPlayer?.start()
            }
        }
    }

    private fun startAudio() {
        if (!mediaPlayer?.isPlaying!!) {
            val path = "$filesDir/record_mp3/vick.aac"
            mediaPlayer?.setDataSource(this, Uri.parse(path))
            mediaPlayer?.prepareAsync()
            mediaPlayer?.setOnPreparedListener {
                mediaPlayer?.start()
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
