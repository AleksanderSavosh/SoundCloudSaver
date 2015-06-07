package savosh.soundcloudsaver.listener;


import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import savosh.soundcloudsaver.ObjectsLocator;
import savosh.soundcloudsaver.model.Track;

public class OnPlayerAddItemClickListener implements AdapterView.OnItemClickListener {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Track track = null;

        if(ObjectsLocator.searchFragment.isVisible()){
            track = ObjectsLocator.searchedItemsArrayAdapter.getItem(position);
        } else if(ObjectsLocator.saverFragment.isVisible()) {
            track = ObjectsLocator.savedItemsArrayAdapter.getItem(position);
        } else {
            return;
        }

        if (ObjectsLocator.currentTrack == null) {
            ObjectsLocator.currentTrackTitle.setText(track.getTitle());
            ObjectsLocator.currentTrack = track;
        } else {
            ObjectsLocator.nextTrackTitle.setVisibility(View.VISIBLE);
            ObjectsLocator.nextTrackTitle.setText(track.getTitle());
            ObjectsLocator.nextTrack = track;
        }

        Toast.makeText(ObjectsLocator.mainActivity, "Add to player: " + track.getTitle(), Toast.LENGTH_SHORT).show();
    }
}