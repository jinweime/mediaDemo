package way.king.com.media01.audiosrc

/*
 * Author: VICK
 * Create: 2020-02-22 21:44
 * Description:
 */
interface PlayerAudioI {


    fun initData()
    fun start(filePath: String)
    fun pause()
    fun stop()
}