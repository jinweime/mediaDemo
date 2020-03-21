package way.king.com.media01.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MySurfaceView extends SurfaceView implements Runnable, SurfaceHolder.Callback {
    private SurfaceHolder mHolder; // 用于控制SurfaceView
    private Thread t; // 声明一条线程
    private Canvas mCanvas; // 声明一张画布
    private Paint p; // 声明一支画笔

    public MySurfaceView(Context context) {
        this(context, null);
    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHolder = getHolder(); // 获得SurfaceHolder对象
        mHolder.addCallback(this); // 为SurfaceView添加状态监听
        p = new Paint(); // 创建一个画笔对象
        p.setColor(Color.WHITE); // 设置画笔的颜色为白色
        setFocusable(true); // 设置焦点
    }

    /**
     * 当SurfaceView创建的时候，调用此函数
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        t = new Thread(this); // 创建一个线程对象
        t.start(); // 启动线程
    }

    /**
     * 当SurfaceView的视图发生改变的时候，调用此函数
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    /**
     * 当SurfaceView销毁的时候，调用此函数
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mHolder.removeCallback(this);
    }


    @Override
    public void run() {
        Draw(); // 调用自定义画画方法
    }

    /**
     * 自定义一个方法，在画布上画一个圆
     */
    protected void Draw() {
        mCanvas = mHolder.lockCanvas(); // 获得画布对象，开始对画布画画
        if (mCanvas != null) {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.BLUE);
            paint.setStrokeWidth(10);
            paint.setStyle(Style.FILL);
            mCanvas.drawCircle(500, 500, 250, paint);
            mHolder.unlockCanvasAndPost(mCanvas); // 完成画画，把画布显示在屏幕上
        }
    }
}