package savosh.soundcloudsaver.task;

import android.os.AsyncTask;
import android.util.Log;
import savosh.soundcloudsaver.ObjectsLocator;

public class PlayerProgressTask extends AsyncTask<Void, Void, Void> {

    public PlayerProgressTask() {
        executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private boolean stop = false;

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    @Override
    protected void onPreExecute() {
        if(ObjectsLocator.playerProgressBar != null) {
            ObjectsLocator.playerProgressBar.setMax(100);
            ObjectsLocator.playerProgressBar.setProgress(0);
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        if(ObjectsLocator.mediaPlayer != null) {
            int currPosition = ObjectsLocator.mediaPlayer.getCurrentPosition();
            Log.i(getClass().getName(), "currPosition: " + currPosition);
            int currProgress = (int) (currPosition / (float) ObjectsLocator.mediaPlayer.getDuration() * 100);
            Log.i(getClass().getName(), "currProgress: " + currProgress);

            if(ObjectsLocator.playerProgressBar != null) {
                ObjectsLocator.playerProgressBar.setProgress(currProgress);
            }
            if(ObjectsLocator.timeProgress != null) {
                String seconds = "" + (currPosition / 1000 - currPosition / (60000) * 60);
                String minutes = "" + (currPosition / (60000));

                seconds = seconds.length() == 1? "0" + seconds : seconds;
                minutes = minutes.length() == 1? "0" + minutes : minutes;

                ObjectsLocator.timeProgress.setText(minutes + ":" + seconds);
            }
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        while (true) {
            if (ObjectsLocator.mediaPlayer == null) {
                return null;
            }
            if (stop) {
                return null;
            }
            if (!ObjectsLocator.mediaPlayer.isPlaying()) {
                return null;
            }
            publishProgress();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Log.e(getClass().getName(), "Error in update media player progress block code: "
                        + e.getMessage(), e);
                return null;
            }
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        ObjectsLocator.playerProgressTask = null;
    }
}
