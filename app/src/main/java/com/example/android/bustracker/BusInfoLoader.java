package com.example.android.bustracker;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by yishuyan on 5/1/17.
 */

public class BusInfoLoader extends AsyncTaskLoader<List<BusInfo>> {
    /** Tag for log messages */
    private static final String LOG_TAG = BusInfoLoader.class.getSimpleName();
    /** Query URL */
    private String mUrl;
    /**
     * Constructs a new {@link BusInfoLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public BusInfoLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }
    @Override
    protected void onStartLoading(){
        forceLoad();
    }
    /**
     * This is on a background thread.
     */


    @Override
    public List<BusInfo> loadInBackground() {
        if(mUrl == null){
            return null;
        }
        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<BusInfo> busList = QueryUtils.extractFeatureFromJson(mUrl);
        return busList;
    }
}
