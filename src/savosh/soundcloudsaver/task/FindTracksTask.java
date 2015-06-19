package savosh.soundcloudsaver.task;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;
import savosh.soundcloudsaver.ApplicationContext;
import savosh.soundcloudsaver.model.Track;
import savosh.soundcloudsaver.service.TrackService;

import java.util.List;


public class FindTracksTask extends AsyncTask<String, Void, List<Track>> {

    public static class Links {
        public final ProgressBar forSearchProgressBar;
        public final ArrayAdapter<Track> searchedItemsArrayAdapter;
        public final List<Track> foundTracks;

        public Links(ProgressBar forSearchProgressBar, ArrayAdapter<Track> searchedItemsArrayAdapter,
                     List<Track> foundTracks) {
            this.forSearchProgressBar = forSearchProgressBar;
            this.searchedItemsArrayAdapter = searchedItemsArrayAdapter;
            this.foundTracks = foundTracks;
        }
    }

    private Links links;

    public FindTracksTask(final String searchText, final ProgressBar forSearchProgressBar,
                          final ArrayAdapter<Track> searchedItemsArrayAdapter, final List<Track> foundTracks) {
        links = new Links(forSearchProgressBar, searchedItemsArrayAdapter, foundTracks);
        this.executeOnExecutor(THREAD_POOL_EXECUTOR, searchText);
    }

    public void unlink(){
        links = null;
    }

    public void link(Links links){
        this.links = links;
    }

    @Override
    protected void onPreExecute() {
        if(links != null) {
            links.searchedItemsArrayAdapter.clear();
            links.searchedItemsArrayAdapter.notifyDataSetChanged();
            links.forSearchProgressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected List<Track> doInBackground(String... params) {
        return TrackService.find(params[0]);
    }

    @Override
    protected void onPostExecute(List<Track> list) {
        Log.i(getClass().getName(), "Tracks: " + list);
        if(links != null) {
            links.forSearchProgressBar.setVisibility(View.GONE);
            if (list != null) {
                links.foundTracks.clear();
                links.foundTracks.addAll(list);
                links.searchedItemsArrayAdapter.clear();
                links.searchedItemsArrayAdapter.addAll(list);
                links.searchedItemsArrayAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(ApplicationContext.instance, "No result", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
