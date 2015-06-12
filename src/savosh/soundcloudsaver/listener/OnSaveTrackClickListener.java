package savosh.soundcloudsaver.listener;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import savosh.soundcloudsaver.ApplicationContext;
import savosh.soundcloudsaver.SaverFragment;
import savosh.soundcloudsaver.model.Track;

import static savosh.soundcloudsaver.ObjectsLocator.*;

public class OnSaveTrackClickListener implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(SaverFragment.BROADCAST_SAVE_TRACK);
        intent.putExtra(SaverFragment.BROADCAST_KEY_SAVE_TRACK, (Track) v.getTag());

//        context.getmTabHost().setCurrentTabByTag(SaverFragment.TAG);
//        Log.i(getClass().getName(), "Save: " + newTrackForSave.getTitle());
    }
}
