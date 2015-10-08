package com.github.funnygopher.whatsshakin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class EarthquakeFragment extends Fragment {

    private EarthquakeFeedAdapter mAdapter;

    public EarthquakeFragment() {
        mAdapter = new EarthquakeFeedAdapter(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_earthquake, container, false);

        ListView mListView = (ListView) view.findViewById(R.id.listview_earthquake);
        mListView.setAdapter(mAdapter);

        String[] feedUrls = getResources().getStringArray(R.array.earthquake_filter_values);
        mAdapter.fetchEarthquakeFeed(feedUrls[0]);

        return view;
    }

    public void setFilter(String feedUrl) {
        mAdapter.fetchEarthquakeFeed(feedUrl);
    }
}