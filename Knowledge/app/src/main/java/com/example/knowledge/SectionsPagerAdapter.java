package com.example.knowledge;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class SectionsPagerAdapter extends FragmentPagerAdapter
{
	private static final ArrayList<String> subjects = new ArrayList<>(Arrays.asList("Math", "Music"));
	private final Context mContext;

	public SectionsPagerAdapter(Context context, FragmentManager fm)
	{
		super(fm);
		mContext = context;
		//  Load subjects here
	}

	@Override
	public Fragment getItem(int position)
	{
		// getItem is called to instantiate the fragment for the given page.
		// Return a PlaceholderFragment (defined as a static inner class below).
		return PlaceholderFragment.newInstance(position + 1);
	}

	@Nullable
	@Override
	public CharSequence getPageTitle(int position)
	{
		return subjects.get(position);
	}

	@Override
	public int getCount()
	{
		return subjects.size();
	}
}