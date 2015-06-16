package savosh.soundcloudsaver;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import savosh.soundcloudsaver.adapter.SearchedItemsArrayAdapter;
import savosh.soundcloudsaver.listener.OnPlayerAddItemClickListener;
import savosh.soundcloudsaver.model.Track;
import savosh.soundcloudsaver.task.FindTracksTask;


import java.util.ArrayList;
import java.util.List;

import static savosh.soundcloudsaver.ObjectsLocator.*;

public class SearchFragment extends Fragment {
    public static final String TAG = SearchFragment.class.getName();

    final List<Track> foundTracks = new ArrayList<>();
    FindTracksTask findTracksTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        Log.i(getClass().getName(), "onCreateView");
        View v = inflater.inflate(R.layout.main_search_fragment, container, false);

        ListView listView = (ListView) v.findViewById(R.id.main_search_fragment_list_view);
        EditText editText = (EditText) v.findViewById(R.id.main_search_fragment_edit_text);

//        searchFragment = this;

        final SearchedItemsArrayAdapter searchedItemsArrayAdapter = new SearchedItemsArrayAdapter(getActivity());
        searchedItemsArrayAdapter.addAll(foundTracks);
        listView.setAdapter(searchedItemsArrayAdapter);


        final ProgressBar forSearchProgressBar = (ProgressBar) v.findViewById(R.id.main_search_fragment_progress_bar);
        showKeyboard(editText);
        editText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(final View v, int keyCode, KeyEvent event) {
                if (keyCode == 66) {
                    if (event.getAction() == KeyEvent.ACTION_UP) {
                        if(findTracksTask == null || findTracksTask.getStatus() == AsyncTask.Status.FINISHED){
                            hideKeyboard(v);
                            findTracksTask = new FindTracksTask(((EditText) v).getText().toString(),
                                    forSearchProgressBar, searchedItemsArrayAdapter, foundTracks);
                        }
                    }
                    return true;
                }
                return false;
            }
        });


//        if (currentTrack != null) {
//            Intent intent = new Intent(MainActivity.BROADCAST_SET_CURRENT_TRACK_TITLE);
//            intent.putExtra(MainActivity.BROADCAST_KEY_TITLE, currentTrack.getTitle());
//            getActivity().sendBroadcast(intent);
//        }
//
//        if (nextTrack != null) {
//            Intent intent = new Intent(MainActivity.BROADCAST_SET_NEXT_TRACK_TITLE);
//            intent.putExtra(MainActivity.BROADCAST_KEY_TITLE, nextTrack.getTitle());
//            getActivity().sendBroadcast(intent);
//        }

        listView.setOnItemClickListener(new OnPlayerAddItemClickListener());

        return v;
    }

    private void showKeyboard(View view){
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    private void hideKeyboard(View view) {
        InputMethodManager manager = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager != null) {
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        Log.i(getClass().getName(), "onAttach");
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        Log.i(getClass().getName(), "onDetach");
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        Log.i(getClass().getName(), "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(getClass().getName(), "onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        Log.i(getClass().getName(), "onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onStart() {
        Log.i(getClass().getName(), "onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.i(getClass().getName(), "onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.i(getClass().getName(), "onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.i(getClass().getName(), "onStop");
        super.onStop();
    }
}
