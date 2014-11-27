package com.example.tyson.transguard;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tyson on 26/11/2014.
 */
public class XMLParser {
    private static final String ns = null;

    public static class Entry {
        public final String date;
        public final String name;
        public final String amount;

        private Entry(String date, String name, String amount) {
            this.date = date;
            this.name = name;
            this.amount = amount;
        }
    }

    private Entry readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "BLN-GRP-TAG2-ROW");
        String date = null;
        String amount = null;
        String name = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String text = parser.getName();
            if (text.equals("TRANS-DATE")) {
                date = readDate(parser);
            } else if (text.equals("BKI-TXN-DESC")) {
                name = readName(parser);
            } else if (text.equals("BKI-TXN-AMT")) {
                amount = readAmount(parser);
            } else {
                skip(parser);
            }
        }
        return new Entry(date, amount, name);
    }

    // Processes title tags in the feed.
    private String readDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "TRANS-DATE");
        String date = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "TRANS-DATE");
        return date;
    }

    // Processes link tags in the feed.
    private String readName(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "BKI-TXN-DESC");
        String name = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "BKI-TXN-DESC");
        return name;
    }

    // Processes summary tags in the feed.
    private String readAmount(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "BKI-TXN-AMT");
        String amount = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "BKI-TXN-AMT");
        return amount;
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

    // For the tags title and summary, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }


    public List parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List entries = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, "SCSMSG");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("BLN-GRP-TAG2-ROW")) {
                entries.add(readEntry(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }

}
