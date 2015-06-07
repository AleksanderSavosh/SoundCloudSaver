package savosh.soundcloudsaver.task;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import savosh.soundcloudsaver.model.Track;
import savosh.soundcloudsaver.service.TrackService;

import java.util.List;

import static savosh.soundcloudsaver.ObjectsLocator.*;

public class FindTracksTask extends AsyncTask<Void, Void, List<Track>> {

    public FindTracksTask() {
        this.executeOnExecutor(THREAD_POOL_EXECUTOR);
    }

    @Override
    protected void onPreExecute() {
        searchedItemsArrayAdapter.clear();
        searchedItemsArrayAdapter.notifyDataSetChanged();
        forSearchProgressBar.setVisibility(View.VISIBLE);
        Toast.makeText(mainActivity, "Search text: " + searchText, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected List<Track> doInBackground(Void... params) {
        return TrackService.find(searchText);
    }

    @Override
    protected void onPostExecute(List<Track> list) {
        Log.i(getClass().getName(), "Tracks: " + list);
        forSearchProgressBar.setVisibility(View.GONE);
        if (list != null) {
            foundTracks = list;
            searchedItemsArrayAdapter.addAll(list);
            searchedItemsArrayAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(mainActivity, "No result", Toast.LENGTH_SHORT).show();
        }
        findTracksTask = null;
    }
}
