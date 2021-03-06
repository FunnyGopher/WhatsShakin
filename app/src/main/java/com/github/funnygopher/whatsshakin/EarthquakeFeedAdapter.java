package com.github.funnygopher.whatsshakin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.funnygopher.whatsshakin.util.HttpRequest;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EarthquakeFeedAdapter extends BaseAdapter {

    private Context mContext;
    private GetEarthquakeFeedTask earthquakeFeedTask;
    private List<Earthquake> mEarthquakes;

    public EarthquakeFeedAdapter(Context context) {
        mContext = context;
        mEarthquakes = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mEarthquakes.size();
    }

    @Override
    public Earthquake getItem(int position) {
        return mEarthquakes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.earthquake_feed_list_item, parent, false);
        }

        TextView title_view = (TextView) convertView.findViewById(R.id.earthquake_title);
        TextView date_view = (TextView) convertView.findViewById(R.id.earthquake_date);
        TextView magnitude_view = (TextView) convertView.findViewById(R.id.earthquake_magnitude);

        Earthquake earthquake = getItem(position);
        title_view.setText(earthquake.getTitle());

        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.US);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date date = earthquake.getDate();
        date_view.setText(dateFormat.format(date) + " @ " + timeFormat.format(date));

        magnitude_view.setText("Magnitude: " + Double.toString(earthquake.getMagnitude()));
        return convertView;
    }

    public void fetchEarthquakeFeed(String feedUrl) {
        earthquakeFeedTask = new GetEarthquakeFeedTask(this, feedUrl);
        earthquakeFeedTask.execute();
    }

    public class GetEarthquakeFeedTask extends AsyncTask<Void, Void, List<Earthquake>> {

        private ProgressDialog mProgressDialog;
        private EarthquakeFeedAdapter mAdapter;
        private String mFeedUrl;

        public GetEarthquakeFeedTask(EarthquakeFeedAdapter adapter, String feedUrl) {
            this.mAdapter = adapter;
            this.mFeedUrl = feedUrl;
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setCancelable(true);
            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    earthquakeFeedTask.cancel(true);
                }
            });
        }

        @Override
        protected void onPreExecute() {
            mProgressDialog.setMessage("Sensing trembles...");
            mProgressDialog.show();
        }

        @Override
        protected List<Earthquake> doInBackground(Void... params) {
            try {
                return getEarthquakes(mFeedUrl);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new ArrayList<Earthquake>();
        }

        @Override
        protected void onPostExecute(List<Earthquake> earthquakeList) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }

            mEarthquakes.clear();
            mEarthquakes.addAll(earthquakeList);
            mAdapter.notifyDataSetChanged();
        }

        private List<Earthquake> getEarthquakes(String url) throws IOException, XmlPullParserException {
            List<Earthquake> earthquakes = null;
            InputStream inputStream = null;
            EarthquakeXMLParser parser = new EarthquakeXMLParser();

            try {
                inputStream = getXMLStream(url);
                earthquakes = parser.parse(inputStream);
            } finally {
                if(inputStream != null) {
                    inputStream.close();
                }
            }
            return earthquakes;
        }

        private InputStream getXMLStream(String url) {
            try {
                HttpRequest req = new HttpRequest(HttpRequest.GET, url);
                String response = req.sendAndGetResponse();
                return new ByteArrayInputStream(response.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
