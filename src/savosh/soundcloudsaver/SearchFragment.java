package savosh.soundcloudsaver;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import savosh.soundcloudsaver.model.Track;
import savosh.soundcloudsaver.service.TrackService;

import java.util.List;

public class SearchFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.main_search_fragment, container, false);

        ListView listView = (ListView) v.findViewById(R.id.main_search_fragment_list_view);
        final SearchedItemsArrayAdapter adapter = new SearchedItemsArrayAdapter(getActivity());
        listView.setAdapter(adapter);


        View editText = v.findViewById(R.id.main_search_fragment_edit_text);
        final View progressBar = v.findViewById(R.id.main_search_fragment_progress_bar);
        showKeyboard(editText);
        editText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(final View v, int keyCode, KeyEvent event) {
                if (keyCode == 66){
                    if(event.getAction() == KeyEvent.ACTION_UP) {
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
                                return new TrackService().find(searchText);
                            }

                            @Override
                            protected void onPostExecute(List<Track> list) {
                                Log.i(getClass().getName(), "Tracks: " + list);
                                Toast.makeText(getActivity(), "Search text: " + list, Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                adapter.addAll(list);
                                adapter.notifyDataSetChanged();
                            }
                        }.execute();
                    }
                    return true;
                }
                return false;
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
}
