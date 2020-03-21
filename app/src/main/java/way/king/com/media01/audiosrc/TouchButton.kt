package way.king.com.media01.audiosrc

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.Button

/*
 * Author: VICK
 * Create: 2020-02-21 17:45
 * Description:
 */
class TouchButton : Button {
    var buttonLongClickListener: ButtonLongClickListener? = null

    var mIsDown = false
    var mHandler: Handler = Handler()
    var mRunnable: Runnable = Runnable {
        mIsDown = true
        buttonLongClickListener?.down()
    }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val action = event?.action
        if (action == MotionEvent.ACTION_DOWN) {
            mHandler.postDelayed(mRunnable, 1000)
        } else if (action == MotionEvent.ACTION_UP) {
            mHandler.removeCallbacks(mRunnable)
            if (mIsDown) {
                mIsDown = false
                //长按抬起
                buttonLongClickListener?.up()
            }
        }
        //处理事件
        return true
    }


    interface ButtonLongClickListener {
        fun down()
        fun up()
    }
}