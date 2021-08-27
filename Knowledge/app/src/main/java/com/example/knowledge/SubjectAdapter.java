package com.example.knowledge;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectViewHolder>
{
	private final ArrayList<Item> items;

	SubjectAdapter(ArrayList<Item> items)
	{
		this.items = new ArrayList<>(items);
	}

	@NotNull
	@Override
	public SubjectViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType)
	{
		return new SubjectViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item, parent, false));
	}

	@Override
	public void onBindViewHolder(@NonNull @NotNull SubjectViewHolder holder, int position)
	{
		Item item = items.get(position);
		holder.setContent(item.getTitle(), item.getDescription());
		holder.itemView.setOnClickListener(v ->
		{

		});
	}

	@Override
	public int getItemCount()
	{
		return items.size();
	}
}