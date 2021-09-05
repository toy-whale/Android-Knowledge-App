package com.java.qitianliang.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.java.qitianliang.MainActivity;
import com.java.qitianliang.R;
import com.java.qitianliang.noScrollListview.NoScrollListview;
import com.java.qitianliang.ui.list_instance.Instance_list;
import com.java.qitianliang.ui.list_instance.ListAdapter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class InstanceListFragment extends Fragment {

    ListAdapter instance_adapter;
    NoScrollListview instance_listView;
    TextView page_number;
    TextView list_number;
    Spinner sort_option;
    Spinner display_option;

    private List<Instance_list> InstanceList = new ArrayList<Instance_list>();
    private List<List<Instance_list>> InstancePairList = new ArrayList<>();
    private ArrayList<String> instanceListOfSub;
    private ArrayList<String> instanceListOfSubByChar;
    private ArrayList<String> instanceListOfSubByLength;
    private String currentSub;
    private final int INSTANCE_A_TIME = 20;
    private int current_page = 1;
    private int instance_amount;
    private int page_amount;

    // sort and display
    private final int DEFALUT = 1;
    private final int CHAR = 2;
    private final int LENGTH = 3;
    private final int COLUMN = 4;
    private final int GRID = 5;
    private final int WATER = 6;
    private int currentSort = 1;
    private int currentDis = 4;

    public static InstanceListFragment newInstance() {
        return new InstanceListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.instance_list_fragment, container, false);

        currentSub = MainActivity.currentSubject;
        instanceListOfSub = MainActivity.instanceListOfAll.get(currentSub);
        instanceListOfSubByLength = new ArrayList<String>(instanceListOfSub);
        instanceListOfSubByLength.sort(new Comparator<String>() {
            @Override
            public int compare(String s, String t1) {
                return s.length() - t1.length();
            }
        });
        instanceListOfSubByChar = new ArrayList<String>(instanceListOfSub);
        instanceListOfSubByChar.sort(Comparator.naturalOrder());
        instance_amount = instanceListOfSub.size();

        page_amount = (instance_amount / 20) + 1;
        list_number = (TextView) view.findViewById(R.id.list_number);
        list_number.setText("共计" + instance_amount + "个实体");
        page_number = (TextView) view.findViewById(R.id.pages_list);
        page_number.setText("共" + page_amount + "页,第" + current_page + "页");

        for (int i = 0; i < INSTANCE_A_TIME; i++) {
            InstanceList.add(new Instance_list(instanceListOfSub.get(i)));
        }

        instance_listView = view.findViewById(R.id.list_list_view);
        instance_adapter = new ListAdapter(getActivity(), R.layout.instance_list_item, InstanceList);
        instance_listView.setAdapter(instance_adapter);

        Button goto_last = (Button)  view.findViewById(R.id.last_page);
        goto_last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (current_page >= 2) {
                    int i = (current_page - 2) * INSTANCE_A_TIME;
                    int j = i + INSTANCE_A_TIME;
                    InstanceList.clear();
                    switch (currentSort) {
                        case 1:
                            for (; i < j; i++) {
                                InstanceList.add(new Instance_list(instanceListOfSub.get(i)));
                            }
                            break;
                        case 2:
                            for (; i < j; i++) {
                                InstanceList.add(new Instance_list(instanceListOfSubByChar.get(i)));
                            }
                            break;
                        case 3:
                            for (; i < j; i++) {
                                InstanceList.add(new Instance_list(instanceListOfSubByLength.get(i)));
                            }
                            break;
                        default:
                            break;
                    }
                    current_page--;
                    instance_adapter.notifyDataSetChanged();
                    page_number.setText("共" + page_amount + "页,第" + current_page + "页");
                }
                else
                    Toast.makeText(getContext(), "已经是第一页!", Toast.LENGTH_LONG).show();
            }

        });

        Button goto_next = (Button)  view.findViewById(R.id.next_page);
        goto_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (current_page < page_amount) {
                    int i = (current_page) * INSTANCE_A_TIME;
                    int j = i + INSTANCE_A_TIME;
                    InstanceList.clear();
                    switch (currentSort) {
                        case 1:
                            for (; i < j; i++) {
                                InstanceList.add(new Instance_list(instanceListOfSub.get(i)));
                            }
                            break;
                        case 2:
                            for (; i < j; i++) {
                                InstanceList.add(new Instance_list(instanceListOfSubByChar.get(i)));
                            }
                            break;
                        case 3:
                            for (; i < j; i++) {
                                InstanceList.add(new Instance_list(instanceListOfSubByLength.get(i)));
                            }
                            break;
                        default:
                            break;
                    }
                    current_page++;
                    instance_adapter.notifyDataSetChanged();
                    page_number.setText("共" + page_amount + "页,第" + current_page + "页");
                }
                else
                    Toast.makeText(getContext(), "已经是最后一页!", Toast.LENGTH_LONG).show();
            }
        });

        sort_option = (Spinner) view.findViewById(R.id.sortOptions);
        sort_option.setSelection(0, true);
        sort_option.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // defalut
                if (i == 0) {
                    if (currentSort == 1) return;
                    InstanceList.clear();
                    int j = (current_page - 1) * INSTANCE_A_TIME;
                    int k = j + INSTANCE_A_TIME;
                    for (; j < k; j++) {
                        InstanceList.add(new Instance_list(instanceListOfSub.get(j)));
                    }
                    instance_adapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "默认排序", Toast.LENGTH_SHORT).show();
                    currentSort = 1;
                }
                // char
                else if (i == 1) {
                    if (currentSort == 2) return;
                    InstanceList.clear();
                    int j = (current_page - 1) * INSTANCE_A_TIME;
                    int k = j + INSTANCE_A_TIME;
                    for (; j < k; j++) {
                        InstanceList.add(new Instance_list(instanceListOfSubByChar.get(j)));
                    }
                    instance_adapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "字符排序", Toast.LENGTH_SHORT).show();
                    currentSort = 2;
                }
                // length
                else if (i == 2) {
                    if (currentSort == 3) return;
                    InstanceList.clear();
                    int j = (current_page - 1) * INSTANCE_A_TIME;
                    int k = j + INSTANCE_A_TIME;
                    for (; j < k; j++) {
                        InstanceList.add(new Instance_list(instanceListOfSubByLength.get(j)));
                    }
                    instance_adapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "长度排序", Toast.LENGTH_SHORT).show();
                    currentSort = 3;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        display_option = (Spinner) view.findViewById(R.id.displayOptions);
        display_option.setSelection(0, true);
        display_option.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // column
                if (i == 0) {
                    if (currentDis == 4) return;
                    currentDis = 4;
                }
                // grid
                else if (i == 1) {
                    if (currentDis == 5) return;
                    currentDis = 5;
                }
                // water
                else if (i == 2) {
                    if (currentDis == 6) return;
                    currentDis = 6;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }

    public void goto_last_page() {

    }

}