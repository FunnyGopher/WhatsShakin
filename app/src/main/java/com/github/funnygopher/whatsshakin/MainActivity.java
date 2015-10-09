package com.github.funnygopher.whatsshakin;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    private EarthquakeFragment mEarthquakeFragment;

    private Toolbar mToolbar;
    private Spinner mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        mSpinner = (Spinner) findViewById(R.id.spinner_earthquake_filter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] feedUrls = getResources().getStringArray(R.array.earthquake_filter_values);
                String feedUrl = feedUrls[position];
                mEarthquakeFragment.setFilter(feedUrl);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        setEarthquakeFragment();
    }

    private void setEarthquakeFragment() {
        mToolbar.setTitle(getResources().getString(R.string.app_name));
        mToolbar.setBackgroundResource(R.color.action_bar);

        mSpinner.setVisibility(View.VISIBLE);

        mEarthquakeFragment = new EarthquakeFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, mEarthquakeFragment);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
