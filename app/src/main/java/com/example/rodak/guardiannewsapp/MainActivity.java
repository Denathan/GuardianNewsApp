package com.example.rodak.guardiannewsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsDto>> {

    private List<NewsDto> news = new ArrayList<>();
    List<NewsDto> savedNewsList = new ArrayList<>();
    private NewsAdapter newsAdapter;
    private TextView emptyListTextView;
    private static final int NEWS_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConnectivityManager connectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();

        ListView newsListView = (ListView) findViewById(R.id.list);
        newsAdapter = new NewsAdapter(MainActivity.this, 0, news);
        newsListView.setAdapter(newsAdapter);
        emptyListTextView = (TextView) findViewById(R.id.empty_list);

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            emptyListTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<NewsDto>> onCreateLoader(int id, Bundle args) {
        String REQUEST_URL = "http://content.guardianapis.com/search";
        Uri baseUri = Uri.parse(REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("show-fields", "byline");
        uriBuilder.appendQueryParameter("pageSize", "50");
        uriBuilder.appendQueryParameter("q", "Poland");
        uriBuilder.appendQueryParameter("api-key", "5336517f-3d76-40df-a72c-015f57961863");
        return new DataLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<NewsDto>> loader, List<NewsDto> data) {
        newsAdapter.setNewsInfoList(null);
        if (data != null && !data.isEmpty()) {
            newsAdapter.setNewsInfoList(data);
            newsAdapter.notifyDataSetChanged();
            savedNewsList = new ArrayList<>(data);
            newsAdapter.addAll(savedNewsList);
        } else {
            emptyListTextView.setText(R.string.empty_list);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsDto>> loader) {
        newsAdapter.setNewsInfoList(null);
    }
}
