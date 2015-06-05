package savosh.soundcloudsaver.task;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import savosh.soundcloudsaver.model.Track;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;


public class SaveTask extends AsyncTask<Track, Integer, Track> {
    private static final int MAX_PROGRESS = 100;

    private ProgressBar progressBar;

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
            int lengthOfFile = conexion.getContentLength();
            Log.d("ANDRO_ASYNC", "Lenght of file: " + lengthOfFile);
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
        }
        if(track != null ) {
//                Toast.makeText(,"Finish save: " + aVoid.getTitle(),);
        }
    }
}
