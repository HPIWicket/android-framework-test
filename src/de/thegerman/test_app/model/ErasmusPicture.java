package de.thegerman.test_app.model;

import com.google.gson.annotations.SerializedName;

public class ErasmusPicture {
	@SerializedName("path")
	public String path;
	
	@SerializedName("alt")
	public String alt;
	
	@SerializedName("ID")
	public Integer id;
}
