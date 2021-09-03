package com.java.qitianliang.ui;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.java.qitianliang.R;

public class KnowledgeSumFragment extends Fragment {

    public static KnowledgeSumFragment newInstance() {
        return new KnowledgeSumFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.knowledge_sum_fragment, container, false);
    }


}