package com.java.qitianliang;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java.qitianliang.noScrollListview.NoScrollListview;
import com.java.qitianliang.question.Question;
import com.java.qitianliang.question.QuestionAdapter;
import com.java.qitianliang.question.TestAdapter;
import com.java.qitianliang.server.Login;
import com.java.qitianliang.server.TestQuestion;

import java.util.ArrayList;
import java.util.List;

public class ActivityTest extends AppCompatActivity {
    private List<Question> QuestionList = new ArrayList<Question>();
    private JSONArray y = new JSONArray();
    private NoScrollListview test_listview;
    private Button POST;
    private boolean is_finish = false;
    private int number = 10;
    private int right_number = 0;
    private static int grade = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.show();
        Intent intent = getIntent();
        y = JSONArray.parseArray(intent.getStringExtra("question"));
        test_listview = findViewById(R.id.test_list_view);
        POST = findViewById(R.id.post_test);
        initQuestion(y);
        number = y.size();
        POST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                is_finish = true;
                POST.setEnabled(false);
                right_number = 0;
                for(int i = 0; i < number;i++) { //判断题目对错
                    View v = findView(i, test_listview);
                    RadioGroup radioGroup = (RadioGroup) v.findViewById(R.id.radioGroup);
                    TextView result = (TextView) v.findViewById(R.id.test_result);
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
                    String qAnswer = QuestionList.get(i).getAnswer();
                    if(answer.equals(qAnswer)) {
                        right_number += 1;
                        result.setText("回答正确！");
                        result.setTextColor(Color.parseColor("#008000"));
                    }
                    else {
                        result.setText("回答错误！正确答案是" + qAnswer + "！");
                        result.setTextColor(Color.parseColor("#FF0000"));
                    }
                    result.setVisibility(View.VISIBLE);
                }
                grade = (int)(100.0 * (double) right_number / (double) number);
                AlertDialog.Builder result = new AlertDialog.Builder(ActivityTest.this);
                result.setTitle("测试结果");
                result.setMessage("恭喜您完成测试!您答对" + right_number + "题，答错" + (number - right_number)
                        + "题，您的成绩为" + grade + "。请继续努力！");
                result.setPositiveButton("确认",null);
                result.show();
            }
        });
    }
    private void initQuestion(JSONArray data) {
        QuestionList = new ArrayList<Question>();
        for (int i = 0; i < data.size(); i++) {
            JSONObject y = data.getJSONObject(i);
            String Body = y.getString("Body");
            Body = (i + 1) + "." + Body;
            y.put("Body", Body);
            Question u = new Question(y);
            QuestionList.add(u);
        }
        TestAdapter question_adapter = new TestAdapter(this, R.layout.test_question, QuestionList);
        test_listview.setAdapter(question_adapter);
    }

    private View findView(int position, NoScrollListview listView) {
        int firstListItemPosition = listView.getFirstVisiblePosition();
        int lastListItemPosition = firstListItemPosition
                + listView.getChildCount() - 1;
        if (position < firstListItemPosition || position > lastListItemPosition) {
            return listView.getAdapter().getView(position, null, listView);
        } else {
            final int childIndex = position - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                if(is_finish == true)
                    finish();
                else {
                    AlertDialog.Builder notice = new AlertDialog.Builder(ActivityTest.this);
                    notice.setTitle("提示");
                    notice.setMessage("测试正在进行中，确认要退出么？");
                    notice.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            dialog.dismiss();
                            finish();
                        }
                    });
                    notice.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            dialog.dismiss();
                        }
                    });
                    notice.show();
                }
                break;
            default:
                break;
        }
        return true;
    }
}
