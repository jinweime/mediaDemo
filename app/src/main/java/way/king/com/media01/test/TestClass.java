package way.king.com.media01.test;

import android.os.Handler;
import android.os.Message;

/*
 * Author: jw
 * Create: 2020-02-05 14:52
 * Description:
 */
public class TestClass {
    void  te(){
        for(int i=0;i<100;i++){

        }
    }
    Handler handler = new Handler(){
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
        }
    };
}
