package savosh.soundcloudsaver.listener;

import android.util.Log;
import android.view.View;
import savosh.soundcloudsaver.SaverFragment;
import savosh.soundcloudsaver.model.Track;

import static savosh.soundcloudsaver.ObjectsLocator.*;

public class OnSaveTrackClickListener implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        newTrackForSave = (Track) v.getTag();
        mTabHost.setCurrentTabByTag(SaverFragment.TAG);
        Log.i(getClass().getName(), "Save: " + newTrackForSave.getTitle());
    }
}
