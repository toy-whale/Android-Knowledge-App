package com.java.qitianliang.relative;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.NetworkOnMainThreadException;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import com.java.qitianliang.R;
import com.java.qitianliang.roundBackgroundColorSpan.RoundBackgroundColorSpan;

public class RelativeAdapter extends ArrayAdapter<Relative> {
    private final int resourceId;

    public RelativeAdapter(Context context, int textViewResourceId, List<Relative> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Relative relative = (Relative) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView text = (TextView) view.findViewById(R.id.rtext);
        String Name = relative.getName();
        String Label = relative.getLabel();
        String Object = relative.getObject();
        String Flag = relative.getFlag();
        if(Flag.equals("0")) {
            int lena = Object.length();
            int lenb = Name.length();
            int len = lena + lenb + Label.length() + 1;
            SpannableString spannableString = new SpannableString(Object + Label + " " + Name);
            spannableString.setSpan(new RoundBackgroundColorSpan(Color.parseColor("#00FF00"), Color.parseColor("#545454")), 0, lena, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new RoundBackgroundColorSpan(Color.parseColor("#FF3A36"), Color.parseColor("#FFFFFF")), len - lenb, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            text.setText(spannableString);
        }
        else {
            int lena = Name.length();
            int lenb = Object.length();
            int len = lena + lenb + Label.length() + 1;
            SpannableString spannableString = new SpannableString(Name + Label + " " + Object);
            spannableString.setSpan(new RoundBackgroundColorSpan(Color.parseColor("#FF3A36"), Color.parseColor("#FFFFFF")), 0, lena, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new RoundBackgroundColorSpan(Color.parseColor("#00FF00"), Color.parseColor("#545454")), len - lenb, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            text.setText(spannableString);
        }
        return view;
    }
}
