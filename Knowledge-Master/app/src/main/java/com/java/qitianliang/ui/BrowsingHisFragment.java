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

public class BrowsingHisFragment extends Fragment {

    public static BrowsingHisFragment newInstance() {
        return new BrowsingHisFragment();
    }
    private String username = MainActivity.loginUsername;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if(username != null)
            PrintAll();
        return inflater.inflate(R.layout.browsing_his_fragment, container, false);
    }


    void PrintAll() {
        EntityDBManager manager = EntityDBManager.getInstance(getActivity(), username);
        List<Entity> e = manager.getAllEntity();
        System.out.println();
        for (int i = 0; i < e.size(); i++) {
            Entity y = e.get(i);
            System.out.println(i + 1 + ":");
            System.out.println(y.getName());
            System.out.println(y.getSubject());
            System.out.println(y.getDescription());
            System.out.println(y.getProperty());
            System.out.println(y.getRelative());
            System.out.println(y.getQuestion());
        }
        System.out.println("浏览记录输出结束!");
    }

}