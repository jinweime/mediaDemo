package way.king.com.media01.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceView
import way.king.com.media01.R

class SurfaceActivity : AppCompatActivity() {
    private var surface: SurfaceView? = null
    private var holder: SurfaceHolder? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_surface)
        bindView()

    }

    override fun onPause() {
        super.onPause()
    }


    private fun bindView() {
        holder = surface?.holder
        holder?.addCallback(MyHolder())
    }

    inner class MyHolder : SurfaceHolder.Callback {
        override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {

        }

        override fun surfaceDestroyed(holder: SurfaceHolder?) {
        }

        override fun surfaceCreated(holder: SurfaceHolder?) {
        }

    }


    override fun onDestroy() {
        super.onDestroy()
    }
}
