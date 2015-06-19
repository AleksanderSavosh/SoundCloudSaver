package savosh.soundcloudsaver.task;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import savosh.soundcloudsaver.ApplicationContext;
import savosh.soundcloudsaver.model.Track;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;


public class SaveTask extends AsyncTask<Track, Integer, Track> {
    private static final int MAX_PROGRESS = 100;
    final List<Track> savedTracks;
    final Map<Track, SaveTask> savingsTrack;

    public SaveTask(Track track, final List<Track> savedTracks, final Map<Track, SaveTask> savingsTrack) {
        this.savedTracks = savedTracks;
        this.savingsTrack = savingsTrack;
        executeOnExecutor(THREAD_POOL_EXECUTOR, track);
    }

    private ProgressBar progressBar;

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
        this.progressBar.setMax(MAX_PROGRESS);
        this.progressBar.setProgress(0);
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
            URLConnection connection = url.openConnection();
            connection.connect();
            int lengthOfFile = connection.getContentLength();
            Log.d(getClass().getName(), "Length of file: " + lengthOfFile);
            InputStream input = new BufferedInputStream(url.openStream());
            OutputStream output = new FileOutputStream(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                            + "/" + params[0].getTitle() + ".mp3");
            byte data[] = new byte[1024];
            long total = 0;
            while ((count = input.read(data)) != -1) {
                total += count;
                publishProgress((int)((total*100)/lengthOfFile));
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
    protected void onPostExecute(Track track) {
        if(progressBar != null){
            progressBar.setVisibility(View.GONE);
            progressBar = null;
        }
        savingsTrack.remove(track);
        if(!savedTracks.contains(track)) {
            savedTracks.add(track);
        }
    }
}
