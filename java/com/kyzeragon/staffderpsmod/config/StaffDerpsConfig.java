package com.kyzeragon.staffderpsmod.config;

import java.io.File;

public class StaffDerpsConfig 
{
	private boolean seeInvisibleEnabled;
	private boolean seePetOwnerEnabled;
	
	public StaffDerpsConfig()
	{
		this.seeInvisibleEnabled = false;
		this.seePetOwnerEnabled = false;
	}
	
	public void readConfig()
	{
		File configFile = new File("");
		return;
	}
	
	///// Getters /////
	public boolean getSeeInvisibleOn() { return this.seeInvisibleEnabled; }	
	public boolean getSeePetOwnerOn() { return this.seePetOwnerEnabled; }
	
	///// Setters /////
	public void setSeeInvisibleOn(boolean enabled) { this.seeInvisibleEnabled = enabled; }
	public void setSeePetOwnerOn(boolean enabled) { this.seePetOwnerEnabled = enabled; }
}
