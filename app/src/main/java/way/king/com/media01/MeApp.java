package way.king.com.media01;

import android.app.Application;

/*
 * Author: VICK
 * Create: 2020-03-02
 * Description:
 */
public class MeApp extends Application {

    public static MeApp INSTANCE = null;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
    }
}
