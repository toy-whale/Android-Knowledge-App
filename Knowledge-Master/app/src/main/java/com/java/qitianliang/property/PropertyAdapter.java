package com.java.qitianliang.property;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import com.java.qitianliang.R;

import com.java.qitianliang.roundBackgroundColorSpan.*;
public class PropertyAdapter extends ArrayAdapter<Property> {
    private final int resourceId;

    public PropertyAdapter(Context context, int textViewResourceId, List<Property> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Property property = (Property) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        //题目
        TextView text = (TextView) view.findViewById(R.id.ptext);
        String Label = property.getLabel();
        String Object = property.getObject();
        int len = Label.length();
        SpannableString spannableString=new SpannableString(Label + Object);
        spannableString.setSpan(new RoundBackgroundColorSpan(Color.parseColor("#FF3A36"),Color.parseColor("#FFFFFF")), 0, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setText(spannableString);
        return view;
    }
}
