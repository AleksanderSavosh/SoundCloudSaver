package savosh.soundcloudsaver;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SaverFragment extends Fragment {
    public static final String TAG = "SaverFragment";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(getClass().getName(), "onCreateView");
        return inflater.inflate(R.layout.main_saver_fragment, container, false);
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
    }

    @Override
    public void onStop() {
        Log.d(getClass().getName(), "onStop");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.d(getClass().getName(), "onDestroyView");
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
