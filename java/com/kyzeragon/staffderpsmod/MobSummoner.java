package com.kyzeragon.staffderpsmod;

import java.text.DecimalFormat;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class MobSummoner 
{
	private String command;
	private double scalar;
	private DecimalFormat df;

	
	public MobSummoner()
	{
		this.command = "/summon Pig ~ ~1 ~ {Attributes:[{Name:generic.maxHealth,Base:1}]}";
		this.scalar = 2;
		this.df = new DecimalFormat("0.0");
	}
	
	public void summon()
	{
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		
		double x = player.getLookVec().xCoord * this.scalar;
		double y = player.getLookVec().yCoord * this.scalar;
		double z = player.getLookVec().zCoord * this.scalar;
		
		String result = "Motion:[" + df.format(x) + "," + df.format(y) + "," + df.format(z) + "]";
		if (this.command.contains(":"))
			result = "," + result;
		result = this.command.substring(0, this.command.length() - 1) + result + "}";
		Minecraft.getMinecraft().thePlayer.sendChatMessage(result);
	}
	
	public void setScalar(String message)
	{
		double n = this.scalar;
		try {
			n = Double.parseDouble(message);
		} catch(Exception e) {
			LiteModStaffDerps.logError("Unable to parse scalar! Must be a double.");
			return;
		}
		if (n > 9)
		{
			LiteModStaffDerps.logError("Scalar too large! Maximum 9.");
			return;
		}
		this.scalar = n;
		LiteModStaffDerps.logMessage("Summon vector scalar set to " + this.scalar, true);
	}
	
	public void showScalar()
	{
		LiteModStaffDerps.logMessage("Current summon vector scalar is " + this.scalar, true);
	}
	
	public void setCommand(String message)
	{
		if (!	message.matches("/summon .* .* .* .* \\{.*\\}"))
		{
			LiteModStaffDerps.logError("Summon format incorrect! Needs summon ~x ~y ~z {}");
			return;
		}
		else if (message.length() > 100 - 21)
		{
			LiteModStaffDerps.logError("Command is too long! Must be fewer than 80 characters.");
			return;
		}
		this.command = message;	
		LiteModStaffDerps.logMessage("Summon command set: " + this.command, true);
	}
}
