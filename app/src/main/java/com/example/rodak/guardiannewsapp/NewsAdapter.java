package com.example.rodak.guardiannewsapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<NewsDto> {
    private List<NewsDto> newsDto;
    private int layoutResourceId;

    public NewsAdapter(Activity context, int textViewResourceId, List<NewsDto> newsList) {
        super(context, textViewResourceId, newsList);
        layoutResourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View newsItemView = convertView;
        NewsDto newsRecord = getItem(position);
        if (newsItemView == null) {
            newsItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_news, parent, false);
        }

        TextView titleTextView = (TextView) newsItemView.findViewById(R.id.news_title);
        titleTextView.setText(newsRecord.getTitle());
        TextView descrbTextView = (TextView) newsItemView.findViewById(R.id.section_name);
        descrbTextView.setText(newsRecord.getSectionName());
        TextView openingTextView = (TextView) newsItemView.findViewById(R.id.author_name);
        openingTextView.setText(newsRecord.getAuthor());
        TextView ratingTextView = (TextView) newsItemView.findViewById(R.id.publication_date);
        ratingTextView.setText(newsRecord.getWebPublicationDate());
        return newsItemView;
    }

    public void setNewsInfoList(List<NewsDto> newsList) {
        newsDto = newsList;
    }
}
