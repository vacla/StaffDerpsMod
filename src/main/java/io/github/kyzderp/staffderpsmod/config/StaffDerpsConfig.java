package io.github.kyzderp.staffderpsmod.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import com.mumfrey.liteloader.util.log.LiteLoaderLogger;

import net.minecraft.client.Minecraft;

public class StaffDerpsConfig 
{
	private boolean seeInvisibleEnabled;
	private boolean seePetOwnerEnabled;
	private double scalar;
	private String summonCommand;
	
	private final File dirs = new File(Minecraft.getMinecraft().mcDataDir, "liteconfig" + File.separator 
			+ "config.1.10.2" + File.separator + "StaffDerps");
	private final File path = new File(dirs.getPath() + File.separator + "staffderpsconfig.txt");
	
	public StaffDerpsConfig()
	{
		this.seeInvisibleEnabled = false;
		this.seePetOwnerEnabled = false;
		if (!path.exists())
		{
			this.dirs.mkdirs();
			if (!this.writeFile())
				LiteLoaderLogger.warning("Cannot write to StaffDerps file!");
			else
				LiteLoaderLogger.info("Created new StaffDerps configuration file.");
		}
		if (!this.loadFile())
			LiteLoaderLogger.warning("Cannot read from StaffDerps file!");
		else
			LiteLoaderLogger.info("StaffDerps configuration loaded.");
	}
	
	private boolean writeFile()
	{
		System.out.println("Writing to file...");
		System.out.println(this.scalar + this.summonCommand);
		PrintWriter writer;
		try {
			writer = new PrintWriter(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		writer.println(this.scalar + this.summonCommand);
		writer.close();
		return true;
	}

	private boolean loadFile()
	{
		if (!path.exists())
			return false;
		Scanner scan;
		try {
			scan = new Scanner(path);
		} catch (FileNotFoundException e) {
			return false;
		}
		if (scan.hasNext())
		{
			String line = scan.nextLine();
			try {
				String[] parts = line.split("/");
				this.scalar = Double.parseDouble(parts[0]);
				this.summonCommand = "/" + parts[1]; 
			} catch (Exception e) {
				e.printStackTrace();
				this.scalar = 2;
				this.summonCommand = "/summon Pig ~ ~1 ~ {Attributes:[{Name:generic.maxHealth,Base:1}],Age:-99}";
				this.writeFile();
			}
		}
		scan.close();
		return true;
	}
	
	///// Getters /////
	public boolean getSeeInvisibleOn() { return this.seeInvisibleEnabled; }	
	public boolean getSeePetOwnerOn() { return this.seePetOwnerEnabled; }
	public double getScalar() { return this.scalar; }
	public String getSummon() { return this.summonCommand; }
	
	///// Setters /////
	public void setSeeInvisibleOn(boolean enabled) { this.seeInvisibleEnabled = enabled; }
	public void setSeePetOwnerOn(boolean enabled) { this.seePetOwnerEnabled = enabled; }
	public void setScalar(double scalar) { this.scalar = scalar; this.writeFile(); }
	public void setSummon(String command) { this.summonCommand = command; this.writeFile(); }
}
