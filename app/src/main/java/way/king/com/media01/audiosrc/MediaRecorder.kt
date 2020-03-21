package way.king.com.media01.audiosrc

import android.content.Context
import android.media.AudioFormat
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.util.Log
import java.io.File

/*
 * Author: Vick
 * Create: 2020-02-21 17:40
 * Description:
 */
class MediaRecorder : AudioRecorderI {


    override fun getFilePath(): String {
        return mFilePath
    }

    var mFileName = ""
    var mFilePath = ""

    var mContext: Context? = null
    override fun getFileName(): String {
        return mFileName
    }

    override fun initData(context: Context) {
        mContext = context
    }

    private var mediaRecorder: MediaRecorder? = null
    override fun start() {
        mediaRecorder = MediaRecorder()
        //输出目录
        mediaRecorder?.setOutputFile(getPath())
        //设置采集源
        mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        //设置输出格式 3GPP media file format
        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        //编解码的格式 AMR (Narrowband) audio codec
        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        mediaRecorder?.prepare()
        mediaRecorder?.start()
    }

    override fun stop() {
        mediaRecorder?.stop()
        mediaRecorder?.release()
        mediaRecorder = null

    }

    private fun getPath(): String {
        val path =
            mContext?.getExternalFilesDir("media").toString() + File.separator
        mFileName = System.currentTimeMillis().toString() + ".aac"
        Log.i("VICK", path + mFileName)
        mFilePath = path + mFileName
        return mFilePath
    }

    override fun pause() {
    }


    override fun destroy() {
    }

}