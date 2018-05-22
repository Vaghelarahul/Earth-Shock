package com.batman.droidapps.shockearth;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;


public class EarthShockLoader extends AsyncTaskLoader<List<ShockDataModel>> {

    private static final String LOG_TAG = EarthShockLoader.class.getName();
    private String mUrl;

    public EarthShockLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        Log.e(LOG_TAG, "This is onStart Loading");
    }

    @Override
    public List<ShockDataModel> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        List<ShockDataModel> earthquakes = ShockHttpJsonUtils.fetchShockData(mUrl);
        Log.e(LOG_TAG, "This is loadInBackground");
        return earthquakes;

    }
}
