package com.example.knowledge;

import java.io.Serializable;

public class Item implements Serializable
{
	private String title;
	private String description;

	Item(String title, String description)
	{
		this.title = title;
		this.description = description;
	}

	String getTitle()
	{
		return title;
	}

	String getDescription()
	{
		return description;
	}
}