package com.java.qitianliang.question;

import android.content.Context;
import android.graphics.Color;
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

import com.java.qitianliang.DetailsActivity;
import com.java.qitianliang.R;
import com.java.qitianliang.ShareUtil;

public class TestAdapter extends ArrayAdapter<Question> {
    private final int resourceId;

    public TestAdapter(Context context, int textViewResourceId, List<Question> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Question question = (Question) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        //题目
        TextView title = (TextView) view.findViewById(R.id.question);
        title.setText(question.getBody());
        //选项
        RadioButton Abtn = (RadioButton) view.findViewById(R.id.A);
        Abtn.setText(question.getA());
        RadioButton Bbtn = (RadioButton) view.findViewById(R.id.B);
        Bbtn.setText(question.getB());
        RadioButton Cbtn = (RadioButton) view.findViewById(R.id.C);
        Cbtn.setText(question.getC());
        RadioButton Dbtn = (RadioButton) view.findViewById(R.id.D);
        Dbtn.setText(question.getD());
        //TextView result = (TextView) view.findViewById(R.id.result);
        return view;
    }
}
