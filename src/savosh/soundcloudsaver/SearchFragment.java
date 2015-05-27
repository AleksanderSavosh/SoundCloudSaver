package savosh.soundcloudsaver;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import savosh.soundcloudsaver.model.Track;
import savosh.soundcloudsaver.service.TrackService;

import java.io.Serializable;
import java.util.List;

public class SearchFragment extends Fragment {

    public static final String RESULT_KEY = "result";
    private List<Track> tracks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        Log.i(getClass().getName(), "onCreateView");
        View v = inflater.inflate(R.layout.main_search_fragment, container, false);

        ListView listView = (ListView) v.findViewById(R.id.main_search_fragment_list_view);
        EditText editText = (EditText) v.findViewById(R.id.main_search_fragment_edit_text);

        final SearchedItemsArrayAdapter adapter = new SearchedItemsArrayAdapter(getActivity());
        if(savedInstanceState != null && savedInstanceState.containsKey(RESULT_KEY)){
            tracks = (List) savedInstanceState.get(RESULT_KEY);
            adapter.addAll(tracks);
        }
        listView.setAdapter(adapter);

        final View progressBar = v.findViewById(R.id.main_search_fragment_progress_bar);
        showKeyboard(editText);
        editText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(final View v, int keyCode, KeyEvent event) {
                if (keyCode == 66) {
                    if (event.getAction() == KeyEvent.ACTION_UP) {
                        new AsyncTask<Void, Void, List<Track>>() {
                            String searchText;

                            @Override
                            protected void onPreExecute() {
                                hideKeyboard(v);
                                adapter.clear();
                                adapter.notifyDataSetChanged();
                                progressBar.setVisibility(View.VISIBLE);
                                searchText = ((EditText) v).getText().toString();
                                Toast.makeText(getActivity(), "Search text: " + searchText, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            protected List<Track> doInBackground(Void... params) {
                                return TrackService.service().find(searchText);
                            }

                            @Override
                            protected void onPostExecute(List<Track> list) {
                                Log.i(getClass().getName(), "Tracks: " + list);
                                progressBar.setVisibility(View.GONE);
                                if (list != null) {
                                    tracks = list;
                                    adapter.addAll(list);
                                    adapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(getActivity(), "No result", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                    return true;
                }
                return false;
            }
        });

        TrackPlayer.init(getActivity(), v);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(view.getId() == R.id.main_search_fragment_list_item_save){
                    Log.i(getClass().getName(), "Save: " + adapter.getItem(position).getTitle());
                }


                Track track = adapter.getItem(position);
                TrackPlayer.put(track);
                Toast.makeText(getActivity(), "Add to player: " + track.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(tracks != null) {
            outState.putSerializable(RESULT_KEY, (Serializable) tracks);
        }
    }

}
