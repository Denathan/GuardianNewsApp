package com.example.rodak.guardiannewsapp;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsDto>> {

    private List<NewsDto> news = new ArrayList<>();
    private List<NewsDto> savedNewsList = new ArrayList<>();
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

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));

        String section = sharedPrefs.getString(
                getString(R.string.settings_section_by_key),
                getString(R.string.settings_section_by_default));

        Uri baseUri = Uri.parse(REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("order-by", orderBy);
        uriBuilder.appendQueryParameter("show-fields", "byline");
        uriBuilder.appendQueryParameter("pageSize", "50");
        uriBuilder.appendQueryParameter("q", section);
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
        ListView newsListView = (ListView) findViewById(R.id.list);
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsDto currentNews = savedNewsList.get(position);
                Uri newsUri = Uri.parse(currentNews.getUrl());
                Intent openWebsite = new Intent(Intent.ACTION_VIEW, newsUri);
                startActivity(openWebsite);
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<List<NewsDto>> loader) {
        newsAdapter.setNewsInfoList(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
