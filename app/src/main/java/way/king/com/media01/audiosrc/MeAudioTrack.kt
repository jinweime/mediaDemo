package way.king.com.media01.audiosrc

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import way.king.com.media01.util.FileUtils
import java.lang.Exception
import android.media.AudioAttributes
import android.R
import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Handler
import java.io.*
import android.os.Environment.DIRECTORY_MUSIC
import way.king.com.media01.audiosrc.demo.GlobalConfig.AUDIO_FORMAT
import way.king.com.media01.audiosrc.demo.GlobalConfig.SAMPLE_RATE_INHZ




/*
 * Author: VICK
 * Create: 2020-02-23 11:13
 * Description:
 */
class MeAudioTrack : PlayerAudioI {
    private var audioData: ByteArray? = null
    var audioTrack: AudioTrack? = null
    override fun initData() {
        //通过 write 写文件

    }

    override fun start(filePath: String) {
        //读文件
        stop()
//        play1(filePath)
        playInModeStream(filePath)


    }

    var mHandler: Handler = Handler()
    var mRunnable: Runnable = Runnable {
        playInModeStatic()
    }

    /**
     * 播放，使用stream模式
     */
    private fun playInModeStream(path: String) {
        /*
        * SAMPLE_RATE_INHZ 对应pcm音频的采样率
        * channelConfig 对应pcm音频的声道
        * AUDIO_FORMAT 对应pcm音频的格式
        * */
        val channelConfig = AudioFormat.CHANNEL_OUT_MONO
        val minBufferSize =
            AudioTrack.getMinBufferSize(SAMPLE_RATE_INHZ, channelConfig, AUDIO_FORMAT)
        audioTrack = AudioTrack(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build(),
            AudioFormat.Builder().setSampleRate(SAMPLE_RATE_INHZ)
                .setEncoding(AUDIO_FORMAT)
                .setChannelMask(channelConfig)
                .build(),
            minBufferSize,
            AudioTrack.MODE_STREAM,
            AudioManager.AUDIO_SESSION_ID_GENERATE
        )
        audioTrack?.play()

        val file = File(path)
        try {
            val fileInputStream = FileInputStream(file)
            Thread(Runnable {
                try {
                    val tempBuffer = ByteArray(minBufferSize)
                    while (fileInputStream.available() > 0) {
                        val readCount = fileInputStream.read(tempBuffer)
                        if (readCount == AudioTrack.ERROR_INVALID_OPERATION || readCount == AudioTrack.ERROR_BAD_VALUE) {
                            continue
                        }
                        if (readCount != 0 && readCount != -1) {
                            audioTrack?.write(tempBuffer, 0, readCount)
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }).start()

        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    private fun playInModeStatic() {
        audioTrack = AudioTrack(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build(),
            AudioFormat.Builder().setSampleRate(22050)
                .setEncoding(AudioFormat.ENCODING_PCM_8BIT)
                .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                .build(),
            audioData?.size!!,
            AudioTrack.MODE_STATIC,
            AudioManager.AUDIO_SESSION_ID_GENERATE
        )
        audioTrack?.write(audioData, 0, audioData?.size!!)
        audioTrack?.play()
    }

    private fun play1(path: String) {
        // static模式，需要将音频数据一次性write到AudioTrack的内部缓冲区
        Thread {
            audioData = FileUtils.readFile2Bytes(path)
            mHandler.post(mRunnable)
        }.run { start() }

    }

    override fun pause() {
    }

    override fun stop() {
        audioTrack?.stop()
        audioTrack?.release()
    }

}