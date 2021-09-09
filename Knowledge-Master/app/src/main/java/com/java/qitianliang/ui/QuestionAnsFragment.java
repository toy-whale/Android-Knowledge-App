package com.java.qitianliang.ui;

import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.drawable.AnimatedStateListDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.java.qitianliang.ConnectChecker;
import com.java.qitianliang.MainActivity;
import com.java.qitianliang.R;
import com.java.qitianliang.question.Question;
import com.java.qitianliang.server.InputQuestion;
import com.java.qitianliang.server.Login;

public class QuestionAnsFragment extends Fragment {

    public static QuestionAnsFragment newInstance() {
        return new QuestionAnsFragment();
    }
    private Button Post;
    private EditText Question_Text;
    private EditText Answer_Text;
    Handler handler= new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 1) { //联网成功
                Bundle bundle = msg.getData();
                String question = bundle.getString("question");
                String answer = bundle.getString("answer");
                Answer_Text.setText(Html.fromHtml("<b>问题：</b>"));
                Answer_Text.append(question);
                Answer_Text.append("\n\n");
                Answer_Text.append(Html.fromHtml("<b>回答：</b>"));
                Answer_Text.append(answer);
                Answer_Text.setVisibility(View.VISIBLE);
            }
            else if(msg.what == 0) { //联网失败
                System.out.println("失败！");
                Toast.makeText(getActivity(), "网络不太好呢~", Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //StrictMode.setThreadPolicy(policy);
        View view = inflater.inflate(R.layout.question_ans_fragment, container, false);
        Post = (Button) view.findViewById(R.id.ask_question);
        Question_Text = (EditText) view.findViewById(R.id.question_Text);
        Answer_Text = (EditText) view.findViewById(R.id.answer_Text);
        //点击提交事件
        Post.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String question = Question_Text.getText().toString();
                if(question.equals("")) {
                    Toast.makeText(getActivity(), "问题不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                new Thread() {
                    @Override
                    public void run() {
                        Message msg=new Message();
                        if(ConnectChecker.check(getActivity()) == false)
                            msg.what = 0;
                        else {
                            msg.what = 1;
                            String ID = "";
                            try {
                                ID = Login.get("14759265980", "Ee123456");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (ID == null || ID.equals(""))
                                msg.what = 0;
                            String answer = null;
                            try {
                                answer = InputQuestion.get(MainActivity.currentSubject, question, ID);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if(answer == null || answer.equals(""))
                                answer = "不好意思，这个问题小E不太懂呢~";
                            Bundle bundle = new Bundle();
                            bundle.putString("question", question);
                            bundle.putString("answer", answer);
                            msg.setData(bundle);
                        }
                        handler.sendMessage(msg);
                    }
                }.start();
            }
        });
        return view;
    }
}