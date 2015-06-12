package savosh.soundcloudsaver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import savosh.soundcloudsaver.adapter.SavedItemsArrayAdapter;
import savosh.soundcloudsaver.listener.OnPlayerAddItemClickListener;
import savosh.soundcloudsaver.model.Track;
import savosh.soundcloudsaver.service.TrackService;
import savosh.soundcloudsaver.task.SaveTask;

import java.util.HashMap;
import java.util.Map;

import static savosh.soundcloudsaver.ObjectsLocator.*;

public class SaverFragment extends Fragment {
    public static final String TAG = SaverFragment.class.getName();

    public static final String BROADCAST_SAVE_TRACK = "savosh.soundcloudsaver.SaverFragment.saveTrack";
    public static final String BROADCAST_KEY_SAVE_TRACK = "trackForSave";
    private BroadcastReceiver broadcastSaveTrack = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Track track = (Track) intent.getSerializableExtra(BROADCAST_KEY_SAVE_TRACK);
            if(!savedTracks.contains(track) && (savingsTrack == null || savingsTrack != null && !savingsTrack.containsKey(track))) {
                (savingsTrack == null ? savingsTrack = new HashMap<Track, SaveTask>() : savingsTrack).put(track, new SaveTask(track));
                savedItemsArrayAdapter.add(track);
                savedItemsArrayAdapter.notifyDataSetChanged();
            } else {
                Log.d(getClass().getName(), "Track has already been saved");
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(getClass().getName(), "onCreateView");
        View v = inflater.inflate(R.layout.main_saver_fragment, container, false);
//        saverFragment = this;

        if(savedItemsArrayAdapter == null) {
            savedItemsArrayAdapter = new SavedItemsArrayAdapter(getActivity());
            savedTracks = TrackService.read();
            savedItemsArrayAdapter.addAll(savedTracks);
        }


        ((ListView) v.findViewById(R.id.main_saver_fragment_list_view))
                .setAdapter(savedItemsArrayAdapter);

        if(onPlayerAddItemClickListener == null) {
            onPlayerAddItemClickListener = new OnPlayerAddItemClickListener();
        }
        ((ListView) v.findViewById(R.id.main_saver_fragment_list_view))
                .setOnItemClickListener(onPlayerAddItemClickListener);

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        Log.d(getClass().getName(), "onAttach");
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(getClass().getName(), "onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.d(getClass().getName(), "onViewCreated");
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.d(getClass().getName(), "onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d(getClass().getName(), "onResume");
        super.onResume();
        getActivity().registerReceiver(broadcastSaveTrack, new IntentFilter(BROADCAST_SAVE_TRACK));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(getClass().getName(), "onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        Log.d(getClass().getName(), "onPause");
        super.onPause();
        getActivity().unregisterReceiver(broadcastSaveTrack);

    }

    @Override
    public void onStop() {
        Log.d(getClass().getName(), "onStop");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.d(getClass().getName(), "onDestroyView");
        TrackService.save(savedTracks);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d(getClass().getName(), "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.d(getClass().getName(), "onDetach");
        super.onDetach();
    }
}
