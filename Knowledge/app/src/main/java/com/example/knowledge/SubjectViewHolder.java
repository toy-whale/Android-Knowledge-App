package com.example.knowledge;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class SubjectViewHolder extends RecyclerView.ViewHolder
{
	private final TextView titleTV;
	private final TextView descriptionTV;

	public SubjectViewHolder(View itemView)
	{
		super(itemView);

		titleTV = itemView.findViewById(R.id.title);
		descriptionTV = itemView.findViewById(R.id.title);
	}

	void setContent(String title, String description)
	{
		this.titleTV.setText(title);
		this.descriptionTV.setText(description);
	}
}
