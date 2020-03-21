package way.king.com.media01.audiosrc

import android.content.Context
import androidx.annotation.NonNull

/*
 * Author: Vick
 * Create: 2020-02-21 17:38
 * Description:
 */
interface AudioRecorderI {


    fun initData(@NonNull context: Context)
    fun start()
    fun pause()
    fun stop()
    fun destroy()
    fun getFileName():String
    fun getFilePath():String
}