package com.example.rodak.guardiannewsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class DataLoader extends AsyncTaskLoader<List<NewsDto>> {
    private String url;


    DataLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<NewsDto> loadInBackground() {
        if (url == null) {
            return null;
        }

        return HttpRequest.fetchNewsData(url);
    }
}
