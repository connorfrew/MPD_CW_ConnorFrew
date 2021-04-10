package com.example.mdp31stmarch2021;
/// NAME:Connor Frew  ////
/// Student Number: S1705548 ////

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.style.BackgroundColorSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Earthquakes extends AppCompatActivity implements Serializable {

    ListView lvRss;
    ArrayList mLink;
    ArrayList mTitle;
    ArrayList mGeolat;
    ArrayList mGeolong;
    ArrayList mDescription;
    ArrayList mPubdate;
    ArrayList mCategory;
    ArrayList mLocation;
    ArrayList mMagnitude;
    ArrayList items;

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquakes);

        button = (Button) findViewById(R.id.back_mainActivity);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });

        button = (Button) findViewById(R.id.button_map);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            Bundle bundle =  new Bundle();
            bundle.putSerializable("Item", items);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        lvRss = (ListView) findViewById(R.id.lvRss);

        mTitle = new ArrayList();
        mDescription = new ArrayList();
        mLink = new ArrayList();
        mGeolat = new ArrayList();
        mGeolong = new ArrayList();
        mPubdate = new ArrayList();
        mCategory = new ArrayList();
        mLocation = new ArrayList();
        items = new ArrayList();
        mMagnitude = new ArrayList();

        lvRss.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Item item = (Item) parent.getItemAtPosition(position);
                Uri uri = Uri.parse(item.getLink());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        new Earthquakes.ProcessBackground().execute();
    }

    public void openMainActivity(){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public InputStream getInputStream(URL url) {
        try {
            return url.openConnection().getInputStream();
        } catch (IOException e) {
            return null;
        }
    };

    public class ProcessBackground extends AsyncTask<Object, Void, Exception> {

        Exception exception = null;

        @Override
        protected Exception doInBackground(Object[] params) {

            try {
                URL url = new URL("http://quakes.bgs.ac.uk/feeds/MhSeismology.xml");
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(getInputStream(url), "UTF_8");
                boolean insideItem = false;
                int eventType = xpp.getEventType();

                Item item = null;

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        if (xpp.getName().equalsIgnoreCase("item")) {
                            insideItem = true;
                            item = new Item();
                        } else if (xpp.getName().equalsIgnoreCase("title")) {
                            if (insideItem) {
                                item.setTitle(xpp.nextText());
                            }
                        } else if (xpp.getName().equalsIgnoreCase("description")) {
                            if (insideItem) {
                                item.setDescription(xpp.nextText());
                            }
                        } else if (xpp.getName().equalsIgnoreCase("link")) {
                            if (insideItem) {
                                item.setLink(xpp.nextText());
                            }
                        } else if (xpp.getName().equalsIgnoreCase("pubdate")) {
                            if (insideItem) {
                                item.setPubDate(xpp.nextText());
                            }
                        } else if (xpp.getName().equalsIgnoreCase("category")) {
                            if (insideItem) {
                                item.setCategory(xpp.nextText());
                            }
                        } else if (xpp.getName().equalsIgnoreCase("geo:lat")) {
                            if (insideItem) {
                                item.setLat(xpp.nextText());
                            }
                        } else if (xpp.getName().equalsIgnoreCase("geo:long")) {
                            if (insideItem) {
                                item.setLon(xpp.nextText());
                            }
                        }
                    } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")) {
                        insideItem = false;
                        items.add(item);
                    }
                    eventType = xpp.next();
                }
            } catch (MalformedURLException e) {
                exception = e;
            } catch (IOException e) {
                exception = e;
            } catch (XmlPullParserException e) {
                exception = e;
            }
            return exception;
        }

        @Override
        protected void onPostExecute(Exception s) {
            super.onPostExecute(s);

            List<Item> list = new ArrayList<>(items);

            ArrayAdapter adapter = new ArrayAdapter(Earthquakes.this, android.R.layout.simple_list_item_1, list);
            lvRss.setAdapter(adapter);

            Collections.sort(list);
        }
    }

}