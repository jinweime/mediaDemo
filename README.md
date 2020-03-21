# Android音视频入门(一)：音频的录制和播放


一、前言

当我们使用各种播放器，系统API来完成音视频播放和录制的时候，其实底层已经帮我做了很多看不到的工作。比如如何进行采集、编解码、合成、压缩等一系列工作，各种库其实都已经帮我实现了，我们只需要按规范调用API来完成我们想要的功能。但是在未来的学习过程中我们会一一的深入内部去探讨各个知识点。

下面是整理的关于android音视频开发的学习脑图，分别把采集、渲染、编码、传输相关的知识点拆分了下，通过这个图可以大概的了解目前的学习位置。

![知识大概](https://tva1.sinaimg.cn/large/00831rSTgy1gd01dughk7j31h20u00v0.jpg)

![](https://tva1.sinaimg.cn/large/00831rSTgy1gd16qw6bicj30wo0ek0t4.jpg)
二、使用系统API完成音视频采集和播放

2.1 播放音视频文件

在android开发中系统的api已经提供了一个叫MediaRecorder的类给开发者来完成音视频的播放，底层的一系列细节都被封装了起来，所以我们现在使用api完成体验一下成功的感觉。

2.1.1 MediaPlayer 概览


[MediaPlayer官方介绍](https://developer.android.com/guide/topics/media/mediaplayer?hl=zh-cn)

Android 多媒体框架支持播放各种常见媒体类型，以便您轻松地将音频、视频和图片集成到应用中。您可以使用 MediaPlayer API，播放存储在应用资源（原始资源）内的媒体文件、文件系统中的独立文件或者通过网络连接获得的数据流中的音频或视频。



下面我们使用MediaPlayer来完成音视频的播放，播放源支持网络文件和本地文件。

配置清单文件的权限

```
    <uses-permission android:name="android.permission.INTERNET" />
    
    //唤醒锁定权限 - 如果播放器应用需要防止屏幕变暗或处理器进入休眠状态，或者要使用 MediaPlayer.setScreenOnWhilePlaying() 或 MediaPlayer.setWakeMode() 方法，则您必须申请此权限。
    <uses-permission android:name="android.permission.WAKE_LOCK" />
    
    
```


部分核心代码

```
 //播放视频
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

	//播放音频
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

```

2.1.2 AudioTrack 概览

AudioTrack只支持wav格式的音频文件，因为wav格式的音频文件大部分都是PCM流。AudioTrack不创建解码器，所以只能播放不需要解码的wav文件。

MediaPlayer在framework层还是会创建AudioTrack，把解码后的PCM数流传递给AudioTrack，AudioTrack再传递给AudioFlinger进行混音，然后才传递给硬件播放,所以是MediaPlayer包含了AudioTrack。



部分核心代码


```
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
```

2.2 采集音视频

Android提供了两套音频采集的API，MediaRecorder 和 AudioRecord前者是一个更加上层一点的API。


2.2.1 MediaRecorder

MediaRecorder 概览

MediaRecorder类似于mediaPlayer是一个偏向上层的api，支持音频的采集和视频的采集。

[MediaRecorder官方介绍](https://developer.android.com/guide/topics/media/mediarecorder?hl=zh-cn)


部分核心代码

```
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
```

2.2.2 AudioRecord采集



部分核心代码


```
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
        //获得最小缓冲区大小，用于记录音频所用。
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
```

2.2.3 采集相机数据

SurfaceView或TextureView采集相机数据

```
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

```


三、总结

**播放**

目前知道系统提供给开发者的api有MediaPlayer一个高度封装的类，可以播放器本地音视频文件或者网络文件，使用AudioTrack可以播放原始的音频文件。

**采集**

MediaRecorder和AudioRecord可以采集音频文件，MediaRecorder 底层封装了AudioRecord，比如直播需要传输的话就需要用它来做。
