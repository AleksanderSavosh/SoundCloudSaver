package savosh.soundcloudsaver.listener;

import android.util.Log;
import android.view.View;
import savosh.soundcloudsaver.ApplicationContext;
import savosh.soundcloudsaver.SaverFragment;
import savosh.soundcloudsaver.model.Track;


public class OnSaveTrackClickListener implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        Log.d(getClass().getName(), "Save: " + ((Track) v.getTag()).getTitle());
        if(!ApplicationContext.getSelf().isLinkToMainActivityNull()){
            ApplicationContext.getSelf().getMainActivity().getFragmentTabHost().setCurrentTabByTag(SaverFragment.TAG);
            ApplicationContext.getSelf().setTrackForSave((Track) v.getTag());
        }
    }
}
