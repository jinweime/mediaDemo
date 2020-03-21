package way.king.com.media01.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.VideoView
import way.king.com.media01.R

class VideoViewActivity : AppCompatActivity() {
    private var videoView: VideoView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_videoview)
        bindView()

    }

    override fun onPause() {
        super.onPause()
    }


    private fun bindView() {
        findViewById<View>(R.id.start).setOnClickListener { videoView?.start() }
        findViewById<View>(R.id.pause).setOnClickListener { videoView?.pause() }
        findViewById<View>(R.id.reset).setOnClickListener { videoView?.stopPlayback() }
        videoView = findViewById(R.id.surface)
        videoView?.setVideoPath("https://rbv01.ku6.com/wifi/o_1dv2knrei18ju12l7l2vgj210oe11kvs")
    }


    override fun onDestroy() {
        super.onDestroy()
        videoView?.stopPlayback()
    }
}
