package com.mffa.dev.issbid.Model;

import com.google.gson.annotations.SerializedName;

public class Coord {

	@SerializedName("lon")
	private double lon;

	@SerializedName("lat")
	private double lat;

	public void setLon(double lon){
		this.lon = lon;
	}

	public double getLon(){
		return lon;
	}

	public void setLat(double lat){
		this.lat = lat;
	}

	public double getLat(){
		return lat;
	}

	@Override
 	public String toString(){
		return new StringBuilder("[").append(this.lon).append(",").append(this.lat).append("]").toString();
		}
}