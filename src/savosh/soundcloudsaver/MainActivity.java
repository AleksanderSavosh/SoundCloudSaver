package savosh.soundcloudsaver;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import savosh.soundcloudsaver.cache.Cache;
import savosh.soundcloudsaver.listener.OnPlayerButtonClickListener;

import static savosh.soundcloudsaver.ObjectsLocator.*;

public class MainActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        Log.d(getClass().getName(), "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        if(!isRotateScreenEvent) {
            Picasso.with(this).setIndicatorsEnabled(true);
            Cache.init(this);
        }
        mainActivity = this;

        OnPlayerButtonClickListener onPlayerButtonClickListener = new OnPlayerButtonClickListener();

        findViewById(R.id.main_activity_play).setOnClickListener(onPlayerButtonClickListener);
        findViewById(R.id.main_activity_pause).setOnClickListener(onPlayerButtonClickListener);
        findViewById(R.id.main_activity_stop).setOnClickListener(onPlayerButtonClickListener);

        playerProgressBar = (ProgressBar) findViewById(R.id.main_activity_progress_play);
        timeProgress = (TextView) findViewById(R.id.main_activity_time);
        currentTrackTitle = (TextView) findViewById(R.id.main_activity_title);
        nextTrackTitle = (TextView) findViewById(R.id.main_activity_next);

        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        mTabHost.addTab(mTabHost.newTabSpec(SearchFragment.TAG).setIndicator("Search"),
                SearchFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(SaverFragment.TAG).setIndicator("Save"),
                SaverFragment.class, null);

        setTabsBackground(mTabHost);
    }

    private void setTabsBackground(TabHost tabhost) {
        TabWidget widget = tabhost.getTabWidget();
        for(int i = 0; i < widget.getChildCount(); i++) {
            View v = widget.getChildAt(i);
            // Look for the title view to ensure this is an indicator and not a divider.
            TextView tv = (TextView)v.findViewById(android.R.id.title);
            if(tv == null) {
                continue;
            }
            tv.setTextColor(getResources().getColor(R.color.main_toolbar));
            v.setBackgroundResource(R.drawable.main_activity_tabs);
        }
    }

    @Override
    protected void onDestroy() {
        Log.d(getClass().getName(), "onDestroy");
        super.onDestroy();
        destroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(getClass().getName(), "onSaveInstanceState");
        isRotateScreenEvent = true;
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(getClass().getName(), "onRestoreInstanceState");
        isRotateScreenEvent = false;
        super.onRestoreInstanceState(savedInstanceState);
    }
}
