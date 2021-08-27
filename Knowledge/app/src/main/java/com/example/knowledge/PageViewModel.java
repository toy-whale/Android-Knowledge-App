package com.example.knowledge;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class PageViewModel extends ViewModel
{
	private final MutableLiveData<ArrayList<String>> mArrayList = new MutableLiveData<>();

	public void setIndex(int index)
	{
		//  Request content list of the subject here
	}

	public MutableLiveData<ArrayList<String>> getList()
	{
		return mArrayList;
	}
}