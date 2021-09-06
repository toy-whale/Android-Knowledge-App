package com.java.qitianliang.ui;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.java.qitianliang.MainActivity;
import com.java.qitianliang.R;
import com.java.qitianliang.SQLite.Entity;
import com.java.qitianliang.SQLite.EntityDBManager;
import com.java.qitianliang.SQLite.Title;
import com.java.qitianliang.SQLite.TitleDBManager;

import java.util.List;

public class CollectingHisFragment extends Fragment {

    public static CollectingHisFragment newInstance() {
        return new CollectingHisFragment();
    }
    private String username = MainActivity.loginUsername;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if(username != null)
            PrintAll();
        return inflater.inflate(R.layout.collecting_his_fragment, container, false);
    }
    void PrintAll() {
        TitleDBManager u = TitleDBManager.getInstance(getActivity(), username);
        List<Title> v = u.getAllTitle();
        for (int i = 0; i < v.size(); i++) {
            Title y = v.get(i);
            System.out.println(i + 1 + ": ");
            System.out.println(y.getTitle());
        }
        System.out.println("收藏记录输出结束!\n");
    }


}