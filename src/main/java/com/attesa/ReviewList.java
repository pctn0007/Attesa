package com.attesa;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ReviewList extends ArrayAdapter<Review>
{

    private Activity context;
    private List<Review> reviewList;

    public ReviewList(Activity context, List<Review> reviewList) {
        super(context, R.layout.revlist_layout, reviewList);
        this.context = context;
        this.reviewList = reviewList;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();


        View listViewItem = inflater.inflate(R.layout.revlist_layout, null, true);

        Review review = reviewList.get(position);


        return listViewItem;

    }
}
