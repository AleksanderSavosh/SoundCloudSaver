package savosh.soundcloudsaver;

import android.media.MediaPlayer;
import android.support.v4.app.FragmentTabHost;
import android.widget.ProgressBar;
import android.widget.TextView;
import savosh.soundcloudsaver.adapter.SavedItemsArrayAdapter;
import savosh.soundcloudsaver.adapter.SearchedItemsArrayAdapter;
import savosh.soundcloudsaver.listener.OnPlayerAddItemClickListener;
import savosh.soundcloudsaver.listener.OnSaveTrackClickListener;
import savosh.soundcloudsaver.model.Track;
import savosh.soundcloudsaver.task.FindTracksTask;
//import savosh.soundcloudsaver.task.PlayerProgressTask;
import savosh.soundcloudsaver.task.SaveTask;

import java.util.List;
import java.util.Map;

public class ObjectsLocator {
    public static Boolean isRotateScreenEvent = false;
//    public static FragmentTabHost mTabHost;

//    public static SearchFragment searchFragment;
//    public static SaverFragment saverFragment;

//    public static SearchedItemsArrayAdapter searchedItemsArrayAdapter;
//    public static SavedItemsArrayAdapter savedItemsArrayAdapter;

//    public static OnPlayerAddItemClickListener onPlayerAddItemClickListener;

//    public static List<Track> foundTracks;
//    public static String searchText;
//    public static ProgressBar forSearchProgressBar;
//    public static FindTracksTask findTracksTask;

//    public static Track newTrackForSave;
//    public static OnSaveTrackClickListener onSaveTrackClickListener;
//    public static Map<Track, SaveTask> savingsTrack;
//    public static List<Track> savedTracks;

//    public static MediaPlayer mediaPlayer;
//    public static Track currentTrack;
//    public static Track nextTrack;
//    public static Operation operation;

//    public static ProgressBar playerProgressBar;
//    public static TextView timeProgress;
//    public static TextView currentTrackTitle;
//    public static TextView nextTrackTitle;

//    public static PlayerProgressTask playerProgressTask;

    public static void destroy(){
//        saverFragment = null;
//        searchFragment = null;
//        if (ObjectsLocator.mediaPlayer != null && !isRotateScreenEvent) {
//            try {
//                ObjectsLocator.mediaPlayer.release();
//                ObjectsLocator.mediaPlayer = null;
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        currentTrack = null;
//        nextTrack = null;
//        operation = null;
//        playerProgressBar = null;
//        timeProgress = null;
//        currentTrackTitle = null;
//        nextTrackTitle = null;

//        if (ObjectsLocator.playerProgressTask != null) {
//            ObjectsLocator.playerProgressTask.setStop(true);
//            ObjectsLocator.playerProgressTask = null;
//        }
    }
}
