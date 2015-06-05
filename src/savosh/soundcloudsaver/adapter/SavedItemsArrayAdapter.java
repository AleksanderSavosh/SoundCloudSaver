package savosh.soundcloudsaver.adapter;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.squareup.picasso.Picasso;
import savosh.soundcloudsaver.R;
import savosh.soundcloudsaver.model.Track;
import savosh.soundcloudsaver.task.SaveTask;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class SavedItemsArrayAdapter extends ArrayAdapter<Track> {

    public static Map<Track, SaveTask> savingsTrack = new HashMap<>();

    public SavedItemsArrayAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public void add(Track object) {
        super.add(object);
        SaveTask saveTask = new SaveTask();
        saveTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, object);
        savingsTrack.put(object, saveTask);
    }

    private class ViewHolder {
        ImageView image;
        TextView title;
        ProgressBar progress;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if(row == null){
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            row = inflater.inflate(R.layout.main_saver_fragment_list_item, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) row.findViewById(R.id.main_saver_fragment_list_item_image);
            viewHolder.title = (TextView) row.findViewById(R.id.main_saver_fragment_list_item_title);
            viewHolder.progress = (ProgressBar) row.findViewById(R.id.main_saver_fragment_list_item_progress);

            row.setTag(viewHolder);
        }

        final ViewHolder viewHolder = (ViewHolder) row.getTag();
        final Track track = getItem(position);

        if(savingsTrack.containsKey(track)){
            savingsTrack.get(track).setProgressBar(viewHolder.progress);
        } else {
            viewHolder.progress.setMax(100);
            viewHolder.progress.setProgress(100);
        }

        if(track.getArtworkUrl() != null && track.getArtworkUrl().trim().length() > 0) {
            Picasso.with(getContext()).load(track.getArtworkUrl().replace("large", getContext()
                    .getResources().getString(R.string.main_search_fragment_image_size))).into(viewHolder.image);
        }

        if(track.getTitle() != null && track.getTitle().trim().length() > 0){
            viewHolder.title.setText(track.getTitle().toUpperCase());
        } else {
            viewHolder.title.setText("No title".toUpperCase());
        }

        return row;
    }
}
