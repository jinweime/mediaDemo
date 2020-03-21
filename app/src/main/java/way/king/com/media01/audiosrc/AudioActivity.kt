package way.king.com.media01.audiosrc

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

class AudioActivity : AppCompatActivity() {

    var audioRecord: AudioRecorderI = AudioRecorder()
    var playerAudioI: PlayerAudioI = MeAudioTrack()
    var view: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.audiao_recorder)
        audioRecord.initData(this)
        playerAudioI.initData()
        view = findViewById(R.id.ll_content)
        val touchButton = findViewById<TouchButton>(R.id.but_recorder)
        touchButton.buttonLongClickListener = object :
            TouchButton.ButtonLongClickListener {
            override fun down() {
                Log.i("VICK", "长按")
                touchButton.text = "正在录音中..."
                audioRecord.start()
            }

            override fun up() {
                touchButton.text = "长按录音"
                Log.i("VICK", "长按 up")
                audioRecord.stop()
                addAudioView(audioRecord.getFileName(), audioRecord.getFilePath())
            }

        }
        findAllAudio()
    }

    fun addAudioView(name: String, path: String) {
        val button = createView(name, path)
        view?.addView(button)
    }


    private fun createView(name: String, path: String): View {
        val viewGroup: ViewGroup = inflate(this, R.layout.audio_button, null) as ViewGroup
        val button = viewGroup.findViewById<Button>(R.id.v_but)
        button.text = name
        button.setOnClickListener {
            playerAudioI.start(path)
        }
        return viewGroup
    }

    private fun findAllAudio() {
        view?.removeAllViews()
        val files = getExternalFilesDir("media")?.listFiles()
        for (i in 0..files?.size!!) {
            Log.i("VICK", i.toString())
        }
        Log.i("VICK", "分割线")
        for (i in 0 until files.size) {
            Log.i("VICK", i.toString())
            val file = files[i]
            addAudioView(file.name, file.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        audioRecord.destroy()
    }


}
