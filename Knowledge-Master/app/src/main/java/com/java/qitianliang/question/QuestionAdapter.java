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

public class QuestionAdapter extends ArrayAdapter<Question> {
    private final int resourceId;

    public QuestionAdapter(Context context, int textViewResourceId, List<Question> objects) {
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
        Abtn.setText("A. " + question.getA());
        RadioButton Bbtn = (RadioButton) view.findViewById(R.id.B);
        Bbtn.setText("B. " + question.getB());
        RadioButton Cbtn = (RadioButton) view.findViewById(R.id.C);
        Cbtn.setText("C. " + question.getC());
        RadioButton Dbtn = (RadioButton) view.findViewById(R.id.D);
        Dbtn.setText("D. " + question.getD());
        //提交按钮
        Button Post = (Button) view.findViewById(R.id.post);
        //结果
        Button Share = (Button) view.findViewById(R.id.post_share);
        TextView result = (TextView) view.findViewById(R.id.result);
        //点击提交事件
        Post.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
                int id = radioGroup.getCheckedRadioButtonId();
                String answer = null;
                if(id == R.id.A)
                    answer = "A";
                if(id == R.id.B)
                    answer = "B";
                if(id == R.id.C)
                    answer = "C";
                if(id == R.id.D)
                    answer = "D";
                String qAnswer = question.getAnswer();
                if(answer.equals(qAnswer)) {
                    result.setText("回答正确！");
                    result.setTextColor(Color.parseColor("#008000"));
                }
                else {
                    result.setText("回答错误！正确答案是" + qAnswer + "！");
                    result.setTextColor(Color.parseColor("#FF0000"));
                }
                result.setVisibility(View.VISIBLE);
                Post.setEnabled(false);
            }
        });
        Share.setOnClickListener(new View.OnClickListener() { //分享试题
            public void onClick(View v) {
                String Text;
                Text = "你的答案是什么呢？一起来回答看看吧！\n" +
                        "题目：" + '\n' +
                        question.getBody().substring(2) + '\n' +
                        "A." + question.getA() + '\n' +
                        "B." + question.getB() + '\n' +
                        "C." + question.getC() + '\n' +
                        "D." + question.getD();
                ShareUtil.shareText(getContext(), Text,"分享习题");
            }
        });
        return view;
    }
}
