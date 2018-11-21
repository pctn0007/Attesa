//Anthony Pacitto [pctn0007] (Team Leader)
//Dariusz Kulpinski [n01164025]
//Winson Vuong [n01104944]
package com.attesa;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ClinicListView extends ArrayAdapter<String> {

    private String[] cname;
    private String[] ctype;
    private Integer[] clive;
    private Activity context;

    // Pass above arrays into constructor below.
    public ClinicListView(Activity context, String[] cname, String[] ctype, Integer[] clive) {
        super(context, R.layout.clinic_layout, cname);

        this.context = context;
        this.cname = cname;
        this.ctype = ctype;
        this.clive = clive;

    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View r = convertView;
        ViewHolder viewHolder = null;
        if (r == null)
        {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.clinic_layout, null, true);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) r.getTag();
        }

        viewHolder._cname.setText(cname[position]);
        viewHolder._ctype.setText(ctype[position]);
        viewHolder._clive.setText(String.valueOf(clive[position]));

        return r;

    }

    class ViewHolder
    {
        TextView _cname;
        TextView _ctype;
        TextView _clive;

        ViewHolder(View v)
        {
            _cname = v.findViewById(R.id.tvcname);
            _ctype = v.findViewById(R.id.tvctype);
           _clive = v.findViewById(R.id.tvLive);

        }


    }
}
