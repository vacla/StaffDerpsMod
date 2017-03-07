package io.github.kyzderp.staffderpsmod.subfunctions;

import io.github.kyzderp.staffderpsmod.LiteModStaffDerps;
import io.github.kyzderp.staffderpsmod.config.StaffDerpsConfig;

import java.text.DecimalFormat;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class MobSummoner 
{
	private StaffDerpsConfig config;
	private String command;
	private double scalar;
	private DecimalFormat df;

	
	public MobSummoner(StaffDerpsConfig config)
	{
		this.config = config;
		this.command = config.getSummon();
		this.scalar = config.getScalar();
		this.df = new DecimalFormat("0.0");
	}
	
	public void summon()
	{
		EntityPlayer player = Minecraft.getMinecraft().player;
		
		double x = player.getLookVec().xCoord * this.scalar;
		double y = player.getLookVec().yCoord * this.scalar;
		double z = player.getLookVec().zCoord * this.scalar;
		
		String result = "Motion:[" + df.format(x) + "," + df.format(y) + "," + df.format(z) + "]";
		if (this.command.contains(":"))
			result = "," + result;
		result = this.command.substring(0, this.command.length() - 1) + result + "}";
		Minecraft.getMinecraft().player.sendChatMessage(result);
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
		this.config.setScalar(this.scalar);
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
		this.config.setSummon(this.command);
		LiteModStaffDerps.logMessage("Summon command set: " + this.command, true);
	}
}
