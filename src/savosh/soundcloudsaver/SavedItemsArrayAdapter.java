package savosh.soundcloudsaver;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.squareup.picasso.Picasso;
import savosh.soundcloudsaver.model.Track;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class SavedItemsArrayAdapter extends ArrayAdapter<Track> {

    public static Map<Track, SaveTask> savingsTrack = new HashMap<>();

    public static class SaveTask extends AsyncTask<Track, Integer, Track> {
        private ProgressBar progressBar;
        private static final int MAX_PROGRESS = 100;
        public void setProgressBar(ProgressBar progressBar) {
            this.progressBar = progressBar;
            this.progressBar.setProgress(MAX_PROGRESS);
            this.progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            if(progressBar != null){
                progressBar.setProgress(values[0]);
            }
        }

        @Override
        protected Track doInBackground(Track... params) {
            int count;
            try {
                URL url = new URL(params[0].getStreamUrl());
                URLConnection conexion = url.openConnection();
                conexion.connect();
                int lenghtOfFile = conexion.getContentLength();
                Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);
                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                                + "/" + params[0].getTitle() + ".mp3");
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress((int)((total*100)/lenghtOfFile));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
                return params[0];
            } catch (Exception e) {}
            return null;
        }

        @Override
        protected void onPostExecute(Track aVoid) {
            if(progressBar != null){
                progressBar.setVisibility(View.GONE);
            }
            if(aVoid != null ) {
//                Toast.makeText(,"Finish save: " + aVoid.getTitle(),);
            }
        }
    }

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
