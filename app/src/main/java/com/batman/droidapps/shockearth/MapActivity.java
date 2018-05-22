package com.batman.droidapps.shockearth;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String MAP_LAT = "mapLat";
    public static final String MAP_LONG = "mapLong";
    public static final String MAP_MAGNITUDE = "magnitude";
    private GoogleMap mGoogleMap;
    private CameraPosition mCameraPosition;
    private double mLatitude;
    private double mLongitude;
    private double mEarthQuakeMag;

    private LatLng mLatLng;

    private CircleOptions mCircleOptions;
    private String mPlace = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_fragment);

        Intent intent = getIntent();
        mLatitude = intent.getDoubleExtra(MAP_LAT, 28.623282);
        mLongitude = intent.getDoubleExtra(MAP_LONG, 77.149406);
        mEarthQuakeMag = intent.getDoubleExtra(MAP_MAGNITUDE, 0);

        mLatLng = new LatLng(mLatitude, mLongitude);


        Geocoder mGeocoder = new Geocoder(this, Locale.getDefault());
        List<Address> address = null;
        try {
          address =  mGeocoder.getFromLocation(mLatitude, mLongitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (address != null && address.size() != 0){

            String featureName = address.get(0).getFeatureName();
            String CountryName = address.get(0).getCountryName();

            if (featureName != null){
                mPlace = mPlace + featureName;

                if (CountryName != null && !CountryName.equals("")) {
                    mPlace = mPlace + ", " + CountryName;
                }

            }else {

                if (CountryName != null && !CountryName.equals("")) {
                    mPlace = CountryName;
                }
            }

        }

        Log.e("TAG", "mPlace: " + mPlace);

        mCameraPosition = new CameraPosition.Builder()
                .tilt(90)
                .bearing(25)  // degree orientation from North
                .zoom(8)
                .target(mLatLng)
                .build();

        mCircleOptions = new CircleOptions()
                .fillColor(getResources().getColor(R.color.EarthquakePointerCircleStrokeColor))
                .center(mLatLng)
                .strokeColor(getResources().getColor(R.color.EarthquakePointerCircle))
                .radius(10000);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_layout);
        mapFragment.getMapAsync(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        mGoogleMap.addMarker(new MarkerOptions()
                .position(mLatLng)
                .title(mPlace + ", Mag: " + mEarthQuakeMag));

        mGoogleMap.addCircle(mCircleOptions);

        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.menu_map_normal:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;

            case R.id.menu_map_hybrid:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;

            case R.id.menu_map_settalite:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;

            case R.id.menu_map_terrain:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;

            case R.id.home:
                NavUtils.navigateUpFromSameTask(this);

        }

        return super.onOptionsItemSelected(item);
    }
}
