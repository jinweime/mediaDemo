package way.king.com.media01.audiosrc

import android.content.Context
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.AudioRecord.getMinBufferSize
import android.media.MediaRecorder
import android.os.Environment
import way.king.com.media01.audiosrc.demo.GlobalConfig.*
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception


/*
 * Author: Vick
 * Create: 2020-02-21 17:40
 * Description:
 */
class AudioRecorder : AudioRecorderI {


    /**
     * 采样率，现在能够保证在所有设备上使用的采样率是44100Hz, 但是其他的采样率（22050, 16000, 11025）在一些设备上也可以使用。
     */
    val SAMPLE_RATE_INHZ = 44100

    /**
     * 声道数。CHANNEL_IN_MONO and CHANNEL_IN_STEREO. 其中CHANNEL_IN_MONO是可以保证在所有设备能够使用的。
     */
    val CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO
    /**
     * 返回的音频数据的格式。 ENCODING_PCM_8BIT, ENCODING_PCM_16BIT, and ENCODING_PCM_FLOAT.
     */
    val AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT

    var isRecord = false
    var mContext: Context? = null
    override fun getFilePath(): String {
        return mFilePath
    }

    var mFileName = ""
    var mFilePath = ""
    override fun getFileName(): String {
        return mFileName
    }

    override fun initData(context: Context) {
        mContext = context
    }

    var audioRecord: AudioRecord? = null
    override fun start() {
        //获得最小缓冲区大小，用于记录音频所用。它表明一个AudioRecord对象还没有被读取（同步）声音数据前能录多长的音(即一次可以录制的声音容量)。声音数据从音频硬件中被读出，数据大小不超过整个录音数据的大小（可以分多次读出），即每次读取初始化buffer容量的数据。
        val minBufferSize = getMinBufferSize(
            44100,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )
        //构建录音对象
        audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC, 44100, AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT, minBufferSize
        )
        mFileName = System.currentTimeMillis().toString() + ".acc"
        val byteArray = ByteArray(minBufferSize)
        val file = File(mContext?.getExternalFilesDir("media"), mFileName)
        mFilePath = file.toString()
        audioRecord?.startRecording()
        isRecord = true
        //开启线程写入录音数据
        Thread {
            var outputStream: FileOutputStream? = null
            try {
                outputStream = FileOutputStream(file)
                while (isRecord) {
                    //audioRecord通过  read（）   方法来读取数据
                    val read = audioRecord?.read(byteArray, 0, minBufferSize)
                    //不出错就写入文件
                    if (AudioRecord.ERROR_INVALID_OPERATION != read) {
                        outputStream.write(byteArray)
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                outputStream?.close()
            }


        }.start()
    }

    override fun pause() {
    }

    override fun stop() {
        isRecord = false
        audioRecord?.stop()
        audioRecord?.release()
        audioRecord = null
    }

    override fun destroy() {
    }

}