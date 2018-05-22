package com.batman.droidapps.shockearth;

import android.app.Activity;
import android.app.Dialog;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.ArrayList;
import java.util.List;

import static com.batman.droidapps.shockearth.MapActivity.MAP_LAT;
import static com.batman.droidapps.shockearth.MapActivity.MAP_LONG;
import static com.batman.droidapps.shockearth.MapActivity.MAP_MAGNITUDE;

public class EarthShockActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<ShockDataModel>> {


    public static final String LOG_TAG = EarthShockActivity.class.getName();
    private static final String USGS_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";
    private static final int EARTHQUAKE_LOADER_ID = 1;
    private static final int NUMDER_OF_RESULTS = 50;
    private static final int REQUEST_CODE = 85;
    private EarthShockAdapter mAdapter;
    private TextView mEmptyStateTextView;
    private ProgressBar mLoadingSpinner;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earth_shock);

        mContext = this;

        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        mLoadingSpinner = (ProgressBar) findViewById(R.id.loading_spinner);
        earthquakeListView.setEmptyView(mLoadingSpinner);

        mEmptyStateTextView = (TextView) findViewById(R.id.emptyText);
        earthquakeListView.setEmptyView(mEmptyStateTextView);

        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);

        } else {
            View loadingSpinner = findViewById(R.id.loading_spinner);
            loadingSpinner.setVisibility(View.GONE);
            mEmptyStateTextView = (TextView) findViewById(R.id.emptyText);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }


        mAdapter = new EarthShockAdapter(this, new ArrayList<ShockDataModel>());

        earthquakeListView.setAdapter(mAdapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                ShockDataModel currentEarthquake = mAdapter.getItem(position);

                Double latitude = currentEarthquake.getmLatitude();
                Double longitude = currentEarthquake.getmLongitude();
                Double depth = currentEarthquake.getmDepth();
                Double magnitude = currentEarthquake.getMagnitude();

                String loc = latitude + " " + longitude + " " + depth;
                Log.e(LOG_TAG, "loc: " + loc);

                Intent mapIntent = new Intent(mContext, MapActivity.class);
                mapIntent.putExtra(MAP_LAT, latitude);
                mapIntent.putExtra(MAP_LONG, longitude);
                mapIntent.putExtra(MAP_MAGNITUDE, magnitude);
                startActivity(mapIntent);

            }
        });

        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();

        int error = googleApiAvailability.isGooglePlayServicesAvailable(mContext);

        if (error == ConnectionResult.SUCCESS) {

        } else {
            if (googleApiAvailability.isUserResolvableError(error)) {
                Dialog dialog = googleApiAvailability.getErrorDialog((Activity) mContext, error, REQUEST_CODE);
                dialog.show();
            }
        }
    }


    @Override
    public Loader<List<ShockDataModel>> onCreateLoader(int i, Bundle bundle) {
        Log.e(LOG_TAG, "This is onCreat Loader");
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagenitude = sharedPrefs.getString(getString(R.string.min_mag_key), getString(R.string.min_mag_default));

        String orderBy = sharedPrefs.getString(getString(R.string.order_by_key), getString(R.string.order_by_default));

        Uri baseUri = Uri.parse(USGS_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", String.valueOf(NUMDER_OF_RESULTS));
        uriBuilder.appendQueryParameter("minmag", minMagenitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);

        Log.e(LOG_TAG, "Uri: " + uriBuilder.toString());
        return new EarthShockLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<ShockDataModel>> loader, List<ShockDataModel> data) {


        mLoadingSpinner.setVisibility(View.GONE);
        mEmptyStateTextView.setText(R.string.no_earthquake);
        mAdapter.clear();
        if (data != null || !data.isEmpty()) {
            mAdapter.addAll(data);
        }
        Log.e(LOG_TAG, "This is onLoad Finished");
    }

    @Override
    public void onLoaderReset(Loader<List<ShockDataModel>> loader) {
        mAdapter.clear();
        Log.e(LOG_TAG, "This is onLoaderReset");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_setting) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
