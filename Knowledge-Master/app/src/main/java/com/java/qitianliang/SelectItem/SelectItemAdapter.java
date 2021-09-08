package com.java.qitianliang.SelectItem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import com.java.qitianliang.R;

public class SelectItemAdapter extends RecyclerView.Adapter<SelectItemAdapter.ItemHolder> {

    private ArrayList<String> hotSearchTestList;

    public SelectItemAdapter(ArrayList<String> hotSearchTestList) {
        this.hotSearchTestList = hotSearchTestList;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.select_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.mTvWord.setText(hotSearchTestList.get(position));
    }

    @Override
    public int getItemCount() {
        return hotSearchTestList.size();
    }

    static class ItemHolder extends RecyclerView.ViewHolder {
        TextView mTvWord;

        public ItemHolder(View itemView) {
            super(itemView);
            mTvWord = itemView.findViewById(R.id.tv_hotword);
        }
    }
}