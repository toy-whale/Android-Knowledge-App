package com.java.qitianliang.TreeElement;
import java.util.ArrayList;
import com.java.qitianliang.R;

import android.graphics.Color;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
public class TreeViewAdapter extends BaseAdapter {
    private ArrayList<Element> elementsData;
    private ArrayList<Element> elements;
    private LayoutInflater inflater;
    private int indentionBase;

    public TreeViewAdapter(ArrayList<Element> elements, ArrayList<Element> elementsData, LayoutInflater inflater) {
        this.elements = elements;
        this.elementsData = elementsData;
        this.inflater = inflater;
        indentionBase = 80;
    }

    public ArrayList<Element> getElements() {
        return elements;
    }

    public ArrayList<Element> getElementsData() {
        return elementsData;
    }

    @Override
    public int getCount() {
        return elements.size();
    }

    @Override
    public Object getItem(int position) {
        return elements.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.tree_view, null);
            holder.disclosureImg = (ImageView) convertView.findViewById(R.id.disclosureImg);
            holder.contentText = (TextView) convertView.findViewById(R.id.contentText);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Element element = elements.get(position);
        int level = element.getLevel();
        if(level == 0) {
            holder.contentText.setTextSize(20);
            TextPaint tp = holder.contentText.getPaint();
            tp.setFakeBoldText(true);
            holder.contentText.setTextColor(Color.BLACK);
        }
        else if(element.isHasChildren()) {
            holder.contentText.setTextSize(18);
            TextPaint tp = holder.contentText.getPaint();
            tp.setFakeBoldText(true);
            holder.contentText.setTextColor(Color.BLACK);
        }
        else {
            holder.contentText.setTextSize(16);
            TextPaint tp = holder.contentText.getPaint();
            tp.setFakeBoldText(false);
            holder.contentText.setTextColor(Color.RED);
        }
        if(level != 2) {
            holder.disclosureImg.setPadding(
                    indentionBase * (level) + 40,
                    holder.disclosureImg.getPaddingTop(),
                    holder.disclosureImg.getPaddingRight(),
                    holder.disclosureImg.getPaddingBottom());
        }
        else {
            holder.disclosureImg.setPadding(
                    indentionBase * (level - 1) + 40,
                    holder.disclosureImg.getPaddingTop(),
                    holder.disclosureImg.getPaddingRight(),
                    holder.disclosureImg.getPaddingBottom());
        }
        holder.contentText.setText(element.getContentText());
        if (element.isHasChildren() && !element.isExpanded()) {
            holder.disclosureImg.setImageResource(R.drawable.close);
            holder.disclosureImg.setVisibility(View.VISIBLE);
        } else if (element.isHasChildren() && element.isExpanded()) {
            holder.disclosureImg.setImageResource(R.drawable.open);
            holder.disclosureImg.setVisibility(View.VISIBLE);
        } else if (!element.isHasChildren()) {
            holder.disclosureImg.setImageResource(R.drawable.close);
            holder.disclosureImg.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    static class ViewHolder{
        ImageView disclosureImg;
        TextView contentText;
    }
}
