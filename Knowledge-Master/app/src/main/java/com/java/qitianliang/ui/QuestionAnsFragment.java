package com.java.qitianliang.ui;

import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.drawable.AnimatedStateListDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.java.qitianliang.R;
import com.java.qitianliang.question.Question;
import com.java.qitianliang.server.InputQuestion;
import com.java.qitianliang.server.Login;

public class QuestionAnsFragment extends Fragment {

    public static QuestionAnsFragment newInstance() {
        return new QuestionAnsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        View view = inflater.inflate(R.layout.question_ans_fragment, container, false);
        Button Post = (Button) view.findViewById(R.id.ask_question);
        EditText Question_Text = (EditText) view.findViewById(R.id.question_Text);
        EditText Answer_Text = (EditText) view.findViewById(R.id.answer_Text);
        //点击提交事件
        Post.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String question = Question_Text.getText().toString();
                if(question.equals("")) {
                    Toast.makeText(getActivity(), "问题不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                String ID = "";
                try {
                    ID = Login.get("14759265980", "Ee123456");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //默认为语文，需要传入学科信息
                String answer = null;
                try {
                    answer = InputQuestion.get("chinese", question, ID);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(answer == null || answer.equals("")) {
                    answer = "不好意思，这个问题小E不太懂呢~";
                }
                Answer_Text.setText(Html.fromHtml("<b>问题：</b>"));
                Answer_Text.append(question);
                Answer_Text.append("\n\n");
                Answer_Text.append(Html.fromHtml("<b>回答：</b>"));
                Answer_Text.append(answer);
                Answer_Text.setVisibility(View.VISIBLE);
            }
        });
        return view;
    }
}