package savosh.soundcloudsaver;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

public class MainActivity extends FragmentActivity {
    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Picasso.with(this).setIndicatorsEnabled(true);

        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("search").setIndicator("Search"), SearchFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("save").setIndicator("Save"), SaverFragment.class, null);

        setTabsBackground(mTabHost);
    }

    public void setTabsBackground(TabHost tabhost) {
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

}
