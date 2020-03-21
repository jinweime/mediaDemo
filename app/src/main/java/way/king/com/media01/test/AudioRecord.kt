package way.king.com.media01.test

import android.content.Context
import android.media.MediaRecorder
import android.util.Log
import java.io.File

/*
 * Author: vick
 * Create: 2020-02-20 22:10
 * Description:
 */
class AudioRecord {
    var mediaRecorder: MediaRecorder? = null

    //    MediaRecorder recorder = new MediaRecorder();
//    * recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//    * recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//    * recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//    * recorder.setOutputFile(PATH_NAME);
//    * recorder.prepare();
//    * recorder.start();
    companion object {
        val OPEN = true
    }


    constructor(activity: Context) {
        mediaRecorder = MediaRecorder()
        mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        val path = activity.filesDir.toString() + "/record_mp3/"
        File(path).mkdirs()
        Log.i("vick", path)
        mediaRecorder?.setOutputFile("$path/vick.aac")
        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

    }

    fun start() {
        if (OPEN) {
            mediaRecorder?.prepare()
            mediaRecorder?.start()
        }
    }


    fun stop() {
        mediaRecorder?.stop()
    }

}