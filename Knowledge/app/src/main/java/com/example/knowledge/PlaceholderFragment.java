package com.example.knowledge;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.knowledge.databinding.FragmentMainBinding;

import java.util.ArrayList;

public class PlaceholderFragment extends Fragment
{

	private static final String ARG_SECTION_NUMBER = "section_number";

	private PageViewModel pageViewModel;
	private FragmentMainBinding binding;

	public static PlaceholderFragment newInstance(int index)
	{
		PlaceholderFragment fragment = new PlaceholderFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(ARG_SECTION_NUMBER, index);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
		int index = 1;
		if (getArguments() != null)
		{
			index = getArguments().getInt(ARG_SECTION_NUMBER);
		}
		pageViewModel.setIndex(index);
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{

		binding = FragmentMainBinding.inflate(inflater, container, false);
		View root = binding.getRoot();

		final RecyclerView listView = binding.contentList;
		pageViewModel.getList().observe(getViewLifecycleOwner(), new Observer<ArrayList<String>>()
		{
			@Override
			public void onChanged(ArrayList<String> strings)
			{
				//  Update UI (the ListView here)
			}
		});
		return root;
	}

	@Override
	public void onDestroyView()
	{
		super.onDestroyView();
		binding = null;
	}
}