package savosh.soundcloudsaver;

import android.app.Application;
import com.squareup.picasso.Picasso;
import savosh.soundcloudsaver.cache.Cache;

/**
 * Created by student on 08.06.2015.
 */
public class ApplicationContext extends Application {

    public static ApplicationContext instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Picasso.with(this).setIndicatorsEnabled(true);
        Cache.init(this);
    }

}
