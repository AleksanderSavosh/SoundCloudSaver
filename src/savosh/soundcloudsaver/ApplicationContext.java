package savosh.soundcloudsaver;

import android.app.Application;
import com.squareup.picasso.Picasso;
import savosh.soundcloudsaver.cache.Cache;
import savosh.soundcloudsaver.model.Track;

/**
 * Created by student on 08.06.2015.
 */
public class ApplicationContext extends Application {

    public static ApplicationContext instance;

    public static ApplicationContext getSelf(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Picasso.with(this).setIndicatorsEnabled(true);
        Cache.init(this);
    }

    private MainActivity mainActivity;
    private Track trackForSave;

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void setTrackForSave(Track trackForSave) {
        this.trackForSave = trackForSave;
    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public Track getTrackForSave() {
        return trackForSave;
    }

    public void removeLinkToMainActivity() {
        this.mainActivity = null;
    }

    public void removeLinkToTrackForSave() {
        this.trackForSave = null;
    }

    public boolean isLinkToMainActivityNull(){
        return mainActivity == null;
    }

    public boolean isLinkToTrackForSaveNull() {
        return this.trackForSave == null;
    }
}
