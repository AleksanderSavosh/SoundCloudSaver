package savosh.soundcloudsaver;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
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
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                onHandleIntent(intent);
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        return super.onStartCommand(intent, flags, startId);
    }

    AudioManager.OnAudioFocusChangeListener afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int focusChange) {
            onAudioFocusChange(focusChange);
        }
    };

    protected void onHandleIntent(Intent intent) {
        Log.d(getClass().getName(), "onHandleIntent");

        if (intent.hasExtra(INTENT_KEY_ADD_TRACK)) {
            if (currentTrack == null) {
                Log.d(getClass().getName(), "currentTrack init");
                currentTrack = (Track) intent.getSerializableExtra(INTENT_KEY_ADD_TRACK);

                Intent broadcastIntent = new Intent(MainActivity.BROADCAST_NAME);
                broadcastIntent.putExtra(MainActivity.BROADCAST_KEY_TITLE, currentTrack.getTitle());
                ApplicationContext.instance.sendBroadcast(broadcastIntent);
            } else {
                Log.d(getClass().getName(), "nextTrack init");
                nextTrack = (Track) intent.getSerializableExtra(INTENT_KEY_ADD_TRACK);

                Intent broadcastIntent = new Intent(MainActivity.BROADCAST_NAME);
                broadcastIntent.putExtra(MainActivity.BROADCAST_KEY_TITLE_NEXT, nextTrack.getTitle());
                ApplicationContext.instance.sendBroadcast(broadcastIntent);
            }

            return;
        }

        Operation operation = (Operation) intent.getSerializableExtra(INTENT_KEY_OPERATION);
        final AudioManager am = (AudioManager) ApplicationContext.instance.getSystemService(Context.AUDIO_SERVICE);
        switch (operation) {
            case PLAY:
                if (currentTrack != null) {

                    // Request audio focus for playback
                    int result = am.requestAudioFocus(afChangeListener,
                            // Use the music stream.
                            AudioManager.STREAM_MUSIC,
                            // Request permanent focus.
                            AudioManager.AUDIOFOCUS_GAIN);


                    if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                        if (mediaPlayer == null) {
                            try {
                                File file = FileUtil.getDownloadMp3File(currentTrack.getTitle());
                                if (file != null) {
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
                }
                break;
            case PAUSE:
                if (mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    }
                }
                if (playerProgressTask != null) {
                    playerProgressTask.setStop(true);
                }
                break;
            case STOP:
                am.abandonAudioFocus(afChangeListener);
                if (nextTrack != null) {
                    currentTrack = nextTrack;
                    nextTrack = null;
                }

                Intent broadcastIntent = new Intent(MainActivity.BROADCAST_NAME);
                broadcastIntent.putExtra(MainActivity.BROADCAST_KEY_PLAYER_PROGRESS, 0);
                broadcastIntent.putExtra(MainActivity.BROADCAST_KEY_PLAYER_TIME_PROGRESS, "00:00");
                broadcastIntent.putExtra(MainActivity.BROADCAST_KEY_TITLE, currentTrack.getTitle());
                sendBroadcast(broadcastIntent);

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

    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                // resume playback
                if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                }
                break;

            case AudioManager.AUDIOFOCUS_LOSS:
                // Lost focus for an unbounded amount of time: stop playback and release media player
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                // Lost focus for a short time, but we have to stop
                // playback. We don't release the media player because playback
                // is likely to resume
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // Lost focus for a short time, but it's ok to keep playing
                // at an attenuated level
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
            Intent intent = new Intent(MainActivity.BROADCAST_NAME);
            intent.putExtra(MainActivity.BROADCAST_KEY_MAX_PLAYER_PROGRESS, 100);
            intent.putExtra(MainActivity.BROADCAST_KEY_PLAYER_PROGRESS, 0);
            intent.putExtra(MainActivity.BROADCAST_KEY_PLAYER_TIME_PROGRESS, "00:00");
            ApplicationContext.instance.sendBroadcast(intent);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            if (mediaPlayer != null) {
                int currPosition = mediaPlayer.getCurrentPosition();
                Log.i(getClass().getName(), "currPosition: " + currPosition);
                int currProgress = (int) (currPosition / (float) mediaPlayer.getDuration() * 100);
                Log.i(getClass().getName(), "currProgress: " + currProgress);

                String seconds = "" + (currPosition / 1000 - currPosition / (60000) * 60);
                String minutes = "" + (currPosition / (60000));

                seconds = seconds.length() == 1 ? "0" + seconds : seconds;
                minutes = minutes.length() == 1 ? "0" + minutes : minutes;

                Intent intent = new Intent(MainActivity.BROADCAST_NAME);
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
