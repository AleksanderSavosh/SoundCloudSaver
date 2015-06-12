package savosh.soundcloudsaver;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import savosh.soundcloudsaver.model.Track;
import savosh.soundcloudsaver.util.FileUtil;

import java.io.File;

public class PlayerService extends Service {

    public static final String INTENT_KEY_OPERATION = "Operation";
    public static final String INTENT_KEY_ADD_TRACK = "AddTrack";

    private Track currentTrack;
    private Track nextTrack;
    private PlayerProgressTask playerProgressTask;
    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        Log.d(getClass().getName(), "onCreate");
        super.onCreate();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                onHandleIntent(intent);
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        return super.onStartCommand(intent, flags, startId);
    }

    protected void onHandleIntent(Intent intent) {
        Log.d(getClass().getName(), "onHandleIntent");

        if(intent.hasExtra(INTENT_KEY_ADD_TRACK)) {
            if(currentTrack == null){
                Log.d(getClass().getName(), "currentTrack init");
                currentTrack = (Track) intent.getSerializableExtra(INTENT_KEY_ADD_TRACK);
                Intent intentSetCurrentTrackTitle = new Intent(MainActivity.BROADCAST_SET_CURRENT_TRACK_TITLE);
                intentSetCurrentTrackTitle.putExtra(MainActivity.BROADCAST_KEY_TITLE, currentTrack.getTitle());
                ApplicationContext.instance.sendBroadcast(intentSetCurrentTrackTitle);
            } else {
                Log.d(getClass().getName(), "nextTrack init");
                nextTrack = (Track) intent.getSerializableExtra(INTENT_KEY_ADD_TRACK);
                Intent intentSetNextTrackTitle = new Intent(MainActivity.BROADCAST_SET_NEXT_TRACK_TITLE);
                intentSetNextTrackTitle.putExtra(MainActivity.BROADCAST_KEY_TITLE, nextTrack.getTitle());
                ApplicationContext.instance.sendBroadcast(intentSetNextTrackTitle);
            }

            return;
        }

        Operation operation = (Operation) intent.getSerializableExtra(INTENT_KEY_OPERATION);

        switch (operation) {
            case PLAY:
                if (currentTrack != null) {
                    if (mediaPlayer == null) {
                        try {
                            File file = FileUtil.getDownloadMp3File(currentTrack.getTitle());
                            if(file != null) {
                                Log.i(getClass().getName(), "PLay from file: " + file.getAbsolutePath());
                                mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.fromFile(file));
                            } else {
                                Log.i(getClass().getName(), "PLay from url: " + currentTrack.getStreamUrl());
                                mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(currentTrack.getStreamUrl()));
                            }
                            mediaPlayer.start();
                            playerProgressTask = new PlayerProgressTask();
                        } catch (Exception e) {
                            Log.e(getClass().getName(), "Error in block start play track: " + e.getMessage(), e);
                        }
                    } else {
                        mediaPlayer.start();
                        playerProgressTask = new PlayerProgressTask();
                    }
                }
                break;
            case PAUSE:
                if (mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    }
                }
                if(playerProgressTask != null){
                    playerProgressTask.setStop(true);
                }
                break;
            case STOP:
                if(nextTrack != null) {
                    currentTrack = nextTrack;
                    nextTrack = null;
                }

                Intent updateProgressIntent = new Intent(MainActivity.BROADCAST_UPDATE_PROGRESS);
                updateProgressIntent.putExtra(MainActivity.BROADCAST_KEY_PLAYER_PROGRESS, 0);
                updateProgressIntent.putExtra(MainActivity.BROADCAST_KEY_PLAYER_TIME_PROGRESS, "00:00");
                sendBroadcast(updateProgressIntent);

                Intent setCurrentTrackTitleIntent = new Intent(MainActivity.BROADCAST_SET_CURRENT_TRACK_TITLE);
                setCurrentTrackTitleIntent.putExtra(MainActivity.BROADCAST_KEY_TITLE, currentTrack.getTitle());
                sendBroadcast(setCurrentTrackTitleIntent);

                Intent setNextTrackTitleIntent = new Intent(MainActivity.BROADCAST_SET_NEXT_TRACK_TITLE);
                setNextTrackTitleIntent.putExtra(MainActivity.BROADCAST_KEY_VISIBLE, false);
                sendBroadcast(setNextTrackTitleIntent);

                if (mediaPlayer != null) {
                    try {
                        mediaPlayer.release();
                        mediaPlayer = null;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (playerProgressTask != null) {
                    playerProgressTask.setStop(true);
                }
                break;
        }

    }

    @Override
    public void onDestroy() {
        Log.d(getClass().getName(), "onDestroy");
        super.onDestroy();
    }

    class PlayerProgressTask extends AsyncTask<Void, Void, Void> {

        public PlayerProgressTask() {
            executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }

        private boolean stop = false;

        public void setStop(boolean stop) {
            this.stop = stop;
        }

        @Override
        protected void onPreExecute() {
            Intent intent = new Intent(MainActivity.BROADCAST_UPDATE_PROGRESS);
            intent.putExtra(MainActivity.BROADCAST_KEY_MAX_PLAYER_PROGRESS, 100);
            intent.putExtra(MainActivity.BROADCAST_KEY_PLAYER_PROGRESS, 0);
            intent.putExtra(MainActivity.BROADCAST_KEY_PLAYER_TIME_PROGRESS, "00:00");
            ApplicationContext.instance.sendBroadcast(intent);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            if(mediaPlayer != null) {
                int currPosition = mediaPlayer.getCurrentPosition();
                Log.i(getClass().getName(), "currPosition: " + currPosition);
                int currProgress = (int) (currPosition / (float) mediaPlayer.getDuration() * 100);
                Log.i(getClass().getName(), "currProgress: " + currProgress);

                String seconds = "" + (currPosition / 1000 - currPosition / (60000) * 60);
                String minutes = "" + (currPosition / (60000));

                seconds = seconds.length() == 1? "0" + seconds : seconds;
                minutes = minutes.length() == 1? "0" + minutes : minutes;

                Intent intent = new Intent(MainActivity.BROADCAST_UPDATE_PROGRESS);
                intent.putExtra(MainActivity.BROADCAST_KEY_PLAYER_PROGRESS, currProgress);
                intent.putExtra(MainActivity.BROADCAST_KEY_PLAYER_TIME_PROGRESS, minutes + ":" + seconds);
                ApplicationContext.instance.sendBroadcast(intent);

            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            while (true) {
                if (stop) {
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
            playerProgressTask = null;
        }
    }


}
