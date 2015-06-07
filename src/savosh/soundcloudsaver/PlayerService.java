package savosh.soundcloudsaver;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import savosh.soundcloudsaver.model.Track;
import savosh.soundcloudsaver.task.PlayerProgressTask;
import savosh.soundcloudsaver.util.FileUtil;

import java.io.File;

import static savosh.soundcloudsaver.ObjectsLocator.*;

public class PlayerService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(getClass().getName(), "onStartCommand");
        Track current = currentTrack;
        Context context = mainActivity;

        switch (operation) {
            case PLAY:
                Toast.makeText(context, "Play", Toast.LENGTH_SHORT).show();
                if (current != null) {
                    if (mediaPlayer == null) {
                        try {
                            File file = FileUtil.getDownloadMp3File(current.getTitle());
                            if(file != null) {
                                Log.i(getClass().getName(), "PLay from file: " + file.getAbsolutePath());
                                mediaPlayer = MediaPlayer.create(context, Uri.fromFile(file));
                            } else {
                                Log.i(getClass().getName(), "PLay from url: " + current.getStreamUrl());
                                mediaPlayer = MediaPlayer.create(context, Uri.parse(current.getStreamUrl()));
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
                Toast.makeText(context, "Pause", Toast.LENGTH_SHORT).show();
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
                    if (currentTrackTitle != null && nextTrack != null) {
                        currentTrackTitle.setText(nextTrack.getTitle());
                    }
                    nextTrack = null;
                }
                if(playerProgressBar != null){
                    playerProgressBar.setProgress(0);
                }
                if(timeProgress != null){
                    timeProgress.setText("00:00");
                }
                if(nextTrackTitle != null) {
                    nextTrackTitle.setVisibility(View.GONE);
                }
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

        operation = null;

        return super.onStartCommand(intent, flags, startId);
    }

}
