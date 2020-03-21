package way.king.com.media01.test;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import way.king.com.media01.R;

/*
 * Author: VICK
 * Create: 2020-03-14
 * Description:
 */
public class BottomPopu extends PopupWindow {

    private PopupWindow mPopu;

    public BottomPopu(Context context) {
        View view = View.inflate(context, R.layout.view_more_child_video, null);
//        TextView tv = view.findViewById(R.id.tv_text);
        mPopu = new PopupWindow(view, 400, 160, true);
        mPopu.getContentView().getRootView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick();
                }
            }
        });
    }

    public void show(View v) {
        mPopu.showAsDropDown(v,-mPopu.getWidth(),0);
    }

    public OnPopuClickListener listener;

    public void setListener(OnPopuClickListener listener) {
        this.listener = listener;
    }

    public interface OnPopuClickListener {

        void onItemClick();

    }
}
