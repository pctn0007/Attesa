package com.attesa;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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


        ImageView imgVote = (ImageView) listViewItem.findViewById(R.id.imgRVote);
        TextView tvcname = (TextView) listViewItem.findViewById(R.id.tvRCname);
        TextView tvcomment = (TextView) listViewItem.findViewById(R.id.tvRComment);

        tvcname.setText(review.getCid());
        tvcomment.setText(review.getVcomment());
        String rvote = review.getUvote();

        if (tvcomment.equals(""))
        {
            tvcomment.setText(R.string.str_nocomment);
        }
        
        // Set vote image to represent if it was a good or bad review.
        if (rvote.equals(context.getString(R.string.str_good)))
        {
            imgVote.setImageResource(R.drawable.tup);
        }
        else if(rvote.equals(context.getString(R.string.str_bad)))
        {
            imgVote.setImageResource(R.drawable.tdown);
        }
        else
        {
            imgVote.setImageResource(R.drawable.tup);
        }



        return listViewItem;

    }
}
