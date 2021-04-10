package com.example.mdp31stmarch2021;

/// NAME:Connor Frew  ////
/// Student Number: S1705548 ////

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ViewSwitcher avw;
    private Button s1Button;
    private Button s2Button;
    private RadioGroup mapTypeGroup;
    private RadioButton normalViewButton;
    private RadioButton terrainViewButton;
    private RadioButton hybridViewButton;
    private RadioButton satelliteViewButton;
    private CheckBox panZoom;
    private ArrayList<Item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        items = (ArrayList<Item>) getIntent().getExtras().getSerializable("Item");

        mapTypeGroup = (RadioGroup)findViewById(R.id.mapTypeGroup);
        normalViewButton = (RadioButton)findViewById(R.id.normalViewRadio);
        terrainViewButton = (RadioButton)findViewById(R.id.terrainViewRadio);
        hybridViewButton = (RadioButton)findViewById(R.id.hybridViewRadio);
        satelliteViewButton = (RadioButton)findViewById(R.id.satelliteViewRadio);
        panZoom = (CheckBox)findViewById(R.id.panZoom);

        Log.e(getPackageName(), "just before avw");
        avw = (ViewSwitcher) findViewById(R.id.vwSwitch);
        if (avw == null)
        {
            Toast.makeText(getApplicationContext(), "Null ViewSwicther", Toast.LENGTH_LONG);
            Log.e(getPackageName(), "null pointer");
        }
        s1Button = (Button) findViewById(R.id.screen1Button);
        s2Button = (Button) findViewById(R.id.screen2Button);
        s1Button.setOnClickListener(this::onClick);
        s2Button.setOnClickListener(this::onClick);
        normalViewButton.setOnClickListener(this::onClick);
        terrainViewButton.setOnClickListener(this::onClick);
        hybridViewButton.setOnClickListener(this::onClick);
        satelliteViewButton.setOnClickListener(this::onClick);
        normalViewButton.toggle();
        panZoom.setOnClickListener(this::onClick);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng ukLocations = new LatLng(54,2);

        for (int i = 0; i < items.size(); i++)
        {
            String magnitude = items.get(i).getMagnitude().substring(11);
            BitmapDescriptor itemMapMarkerIcon = null;

            if (Float.parseFloat(magnitude) < 1)
            {
                itemMapMarkerIcon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
            }
            else if (Float.parseFloat(magnitude) >=1 && Float.parseFloat(magnitude) < 3)
            {
                itemMapMarkerIcon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
            }
            else if (Float.parseFloat(magnitude) >=3)
            {
                itemMapMarkerIcon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
            }

            mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(items.get(i).getLat()), Double.parseDouble((items.get(i).getLon())))).title(items.get(i).getLocation() + "," + items.get(i).getMagnitude()).icon(itemMapMarkerIcon));
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ukLocations));
    }

   public void onClick(View arg0)
    {
        if (arg0 == s1Button)
        {
            avw.showNext();
        }
        else
        if (arg0 == s2Button)
        {
            avw.showPrevious();
        }
        else
        if (arg0 == normalViewButton)
        {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
        else
        if (arg0 == terrainViewButton)
        {
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        }
        else
        if (arg0 == hybridViewButton)
        {
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        }
        else
        if (arg0 == satelliteViewButton)
        {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }

        if (panZoom.isChecked())
        {
            mMap.getUiSettings().setZoomControlsEnabled(true);
        }
        else
        {
            mMap.getUiSettings().setZoomControlsEnabled(false);
        }
    }
}