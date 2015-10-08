package com.github.funnygopher.whatsshakin;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

public class EarthquakeXMLParser {

    private static final String ns = null;
    private static class Origin {
        public final Date date;
        public final String latitude;
        public final String longitude;

        public Origin(Date date, String latitude, String longitude) {
            this.date = date;
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

    private static class Magnitude {
        public final double mag;

        public Magnitude(double mag) {
            this.mag = mag;
        }
    }

    public List parse(InputStream is) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            is.close();
        }
    }

    private List readFeed(XmlPullParser parser) throws IOException, XmlPullParserException {
        List events = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, "q:quakeml");
        parser.next();
        while(parser.next() != XmlPullParser.END_TAG) {
            if(parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();
            if(name.equals("event")) {
                events.add(readEarthquake(parser));
            } else {
                skip(parser);
            }
        }
        return events;
    }

    private Earthquake readEarthquake(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "event");
        String title = null;
        Origin origin = null;
        Magnitude magnitude = null;

        while(parser.next() != XmlPullParser.END_TAG) {
            if(parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if(name.equals("description")) {
                title = readTitle(parser);
            } else if(name.equals("origin")) {
                origin = readOrigin(parser);
            } else if(name.equals("magnitude")) {
                magnitude = readMagnitude(parser);
            } else {
                skip(parser);
            }
        }

        return new Earthquake(title, origin.date, magnitude.mag);
    }

    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "description");
        String title = null;

        while(parser.next() != XmlPullParser.END_TAG) {
            if(parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if(name.equals("text")) {
                title = readText(parser);
            } else {
                skip(parser);
            }
        }
        parser.require(XmlPullParser.END_TAG, ns, "description");
        return title;
    }

    private Origin readOrigin(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "origin");
        Date date = null;

        while(parser.next() != XmlPullParser.END_TAG) {
            if(parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if(name.equals("time")) {
                date = readDate(parser);
            } else {
                skip(parser);
            }
        }
        parser.require(XmlPullParser.END_TAG, ns, "origin");
        return new Origin(date, "", "");
    }

    private Date readDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "time");
        String date = null;

        while(parser.next() != XmlPullParser.END_TAG) {
            if(parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if(name.equals("value")) {
                date = readText(parser);
            } else {
                skip(parser);
            }
        }
        parser.require(XmlPullParser.END_TAG, ns, "time");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date qdate = new GregorianCalendar(0, 0, 0).getTime();
        try {
            qdate = sdf.parse(date);
        } catch (ParseException e) {
            Log.d("EARTHQUAKE_XML_PARSER", "Date parsing exception.", e);
        }

        return qdate;
    }

    private Magnitude readMagnitude(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "magnitude");
        double mag = 0.0;

        while(parser.next() != XmlPullParser.END_TAG) {
            if(parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if(name.equals("mag")) {
                mag = readMag(parser);
            } else {
                skip(parser);
            }
        }
        parser.require(XmlPullParser.END_TAG, ns, "magnitude");
        return new Magnitude(mag);
    }

    private double readMag(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "mag");
        String magnitude = null;

        while(parser.next() != XmlPullParser.END_TAG) {
            if(parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if(name.equals("value")) {
                magnitude = readText(parser);
            } else {
                skip(parser);
            }
        }
        parser.require(XmlPullParser.END_TAG, ns, "mag");

        return Double.parseDouble(magnitude);
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

}
