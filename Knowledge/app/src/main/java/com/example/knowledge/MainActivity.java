package com.example.knowledge;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;


import com.example.knowledge.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
	private ActivityMainBinding binding;

	private String loginUsername = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		binding = ActivityMainBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
		ViewPager viewPager = binding.viewPager;
		viewPager.setAdapter(sectionsPagerAdapter);
		TabLayout tabs = binding.tabs;
		tabs.setupWithViewPager(viewPager);
		FloatingActionButton fab = binding.fab;

		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
			}
		});

		// Login Activity
		startActivityForResult(new Intent(getApplicationContext(), ActivityLogin.class), 1);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// Get Login Username for Collecting List and History Record
		switch (requestCode) {
			case 1:
				if (resultCode == RESULT_OK) {
					loginUsername = data.getStringExtra("data_return");
				}
				break;
			default:
		}
	}
}