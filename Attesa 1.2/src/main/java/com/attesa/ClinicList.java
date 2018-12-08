package com.attesa;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import static com.attesa.R.drawable.round_bg_circlered;

public class ClinicList extends ArrayAdapter<Clinic>
{

    private Activity context;
    private List<Clinic> clinicList;

    public ClinicList(Activity context, List<Clinic> clinicList) {
        super(context, R.layout.clist_layout, clinicList);
        this.context = context;
        this.clinicList = clinicList;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.clist_layout, null, true);

        TextView tvName = (TextView) listViewItem.findViewById(R.id.tvcname);
        TextView tvLive = (TextView) listViewItem.findViewById(R.id.tvLive);
        TextView tvctype = (TextView) listViewItem.findViewById(R.id.tvctype);

        Clinic clinic = clinicList.get(position);
        tvName.setText(clinic.getCname());
        tvLive.setText(clinic.getClive());


        String stat = String.valueOf(clinic.getCstatus());
        if (stat.equals("1"))
        {
            stat = "Open";
        }
        tvctype.setText(clinic.getCtype() + " · " + stat);


        // Parse the string live value into an actual integer.
        int myLiveNum = 0;
        try {
            myLiveNum = Integer.parseInt(tvLive.getText().toString());

            // Set live color to red (aka. long wait!)
            if (myLiveNum >= 8)
            {
                tvLive.setBackground(ContextCompat.getDrawable(context, R.drawable.round_bg_circlered));
            }
            // Set live color to orange (aka. average wait)
            else if (myLiveNum >= 5 && myLiveNum <= 7)
            {
                tvLive.setBackground(ContextCompat.getDrawable(context, R.drawable.round_bg_circleorange));
            }
            // Set live color to blue (aka. short wait)
            else if (myLiveNum >= 1 && myLiveNum <= 4)
            {
                tvLive.setBackground(ContextCompat.getDrawable(context, R.drawable.round_bg_circleblue));
            }
            // Set live color green (aka. no wait; 0 people ahead)
            else
            {
                tvLive.setBackground(ContextCompat.getDrawable(context, R.drawable.round_bg_circlegreen));
            }
        } catch(NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }

        return listViewItem;

    }
}
