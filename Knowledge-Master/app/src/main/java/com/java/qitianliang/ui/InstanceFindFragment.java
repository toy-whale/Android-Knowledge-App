package com.java.qitianliang.ui;

import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.java.qitianliang.R;

import org.w3c.dom.Text;

public class InstanceFindFragment extends Fragment {

    public static InstanceFindFragment newInstance() {
        return new InstanceFindFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.instance_find_fragment, container, false);

        SearchView search = view.findViewById(R.id.searchInstanceByWords);
        Spinner sort = view.findViewById(R.id.sortOptions);
        Spinner filter = view.findViewById(R.id.filterOptions);
        Spinner display = view.findViewById(R.id.displayOptions);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String sort_type = sort.getSelectedItem().toString();
                String filter_type = sort.getSelectedItem().toString();
                String display_type = sort.getSelectedItem().toString();

                // 调用实体查询接口


                // 展示返回数据



                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return view;
    }

}