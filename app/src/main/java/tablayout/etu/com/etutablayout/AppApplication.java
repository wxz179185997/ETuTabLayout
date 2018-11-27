package tablayout.etu.com.etutablayout;

import android.app.Application;

public class AppApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        init(this);
    }

    private static Application application;


    public static void init(Application app) {
        if (null == application) {
            application = app;
            DeviceUtils.init(application);
        }
    }

    public static Application getApplication() {
        return application;
    }
}
