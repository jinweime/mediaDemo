package way.king.com.media01.test

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.w3c.dom.Text
import way.king.com.media01.R
import way.king.com.media01.audiosrc.AudioActivity
import way.king.com.media01.audiosrc.AudioRecorder
import way.king.com.media01.audiosrc.AudioRecorderI
import way.king.com.media01.audiosrc.TouchButton
import way.king.com.media01.audiosrc.demo.DemoMainActivity
import way.king.com.media01.camera.CmaraActivity
import way.king.com.media01.mediaExtractor.MediaExtractorActivity
import way.king.com.media01.opengl.OpenGl1Activity
import way.king.com.media01.util.SpannableStringUtils
import way.king.com.media01.util.SpannableStringUtils.*
import java.util.ArrayList

class SimpleMainActivity : AppCompatActivity() {
    private val TAG = "VICK"
    private val MY_PERMISSIONS_REQUEST = 1001
    /**
     * 需要申请的运行时权限
     */
    private val permissions =
        arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private val mPermissionList = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermissions()
        setContentView(R.layout.demo_main_activity)
        findViewById<Button>(R.id.but_audio).setOnClickListener {
            startActivity(Intent(this, DemoMainActivity::class.java))
        }
        findViewById<Button>(R.id.but_audio2).setOnClickListener {
            startActivity(Intent(this, AudioActivity::class.java))
        }
        findViewById<Button>(R.id.but_camera).setOnClickListener {
            startActivity(Intent(this, CmaraActivity::class.java))
        }
        findViewById<Button>(R.id.but_ext).setOnClickListener {
            startActivity(Intent(this, MediaExtractorActivity::class.java))
        }
        findViewById<Button>(R.id.but_open_gl1).setOnClickListener {
            startActivity(Intent(this, OpenGl1Activity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == MY_PERMISSIONS_REQUEST) {
            for (i in grantResults.indices) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, permissions[i] + " 权限被用户禁止！")
                }
            }
            // 运行时权限的申请不是本demo的重点，所以不再做更多的处理，请同意权限申请。
        }
    }

    private fun checkPermissions() {
        // Marshmallow开始才用申请运行时权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (i in permissions.indices) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        permissions[i]
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    mPermissionList.add(permissions[i])
                }
            }
            if (!mPermissionList.isEmpty()) {
                val permissions = mPermissionList.toTypedArray()
                ActivityCompat.requestPermissions(this, permissions, MY_PERMISSIONS_REQUEST)
            }
        }
    }

    public fun show(v: View) {
        val p = BottomPopu(this)
        p.show(v)
    }


}
