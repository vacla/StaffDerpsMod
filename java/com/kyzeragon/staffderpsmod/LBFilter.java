package com.kyzeragon.staffderpsmod;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

public class LBFilter 
{
	private int showMinY;
	private int showMaxY;
	
	public LBFilter()
	{
		this.showMinY = 0;
		this.showMaxY = 256;
	}
	
	public boolean handleEntry(String message)
	{
		String[] afterSplit = message.split(":");
		int y = Integer.parseInt(afterSplit[afterSplit.length - 2]);
		if (y > this.showMaxY || y < this.showMinY)
			return false;
		return true;
	}
	
	public String handleCommand(String command)
	{
		command = command.replaceAll("§.", "");
		String[] tokens = command.split(" ");
		if (tokens.length < 3)
			return this.logError("§8[§4!§8] §cNot enough arguments! Usage: /sd lbf <y|regex> §8[§4!§8]");
		else if (tokens[2].equalsIgnoreCase("y"))
			return this.handleYCommand(command);
		else if (tokens[2].equalsIgnoreCase("regex"))
			return this.handleRegexCommand(command);
		else
			return this.logError("§8[§4!§8] §cInvalid arguments! Usage: /sd lbf <y|regex> §8[§4!§8]");
	}
	
	private String handleRegexCommand(String command)
	{
		return "";
	}
	
	private String handleYCommand(String command)
	{
		String[] tokens = command.split(" ");

		if (tokens.length > 5)
			return this.logError("§8[§4!§8] §cToo many arguments! Usage: /sd lbf y <minY> <maxY> §7or §c/sd lbf y clear §8[§4!§8]");
		
		else if (tokens.length == 3)
			return this.logMessage("§bCurrently showing lb entries with Y coords from "
						+ this.showMinY + " to " + this.showMaxY + " (inclusive).");
		
		else if (tokens.length == 4)
		{
			if (tokens[3].equalsIgnoreCase("clear"))
			{
				this.showMinY = 0;
				this.showMaxY = 256;
				return this.logMessage("§bNow showing all y coordinates for logblock entries.");
			}
			return this.logError("§8[§4!§8] §cInvalid arguments! Usage: /sd lbf y <minY> <maxY> §7or §c/sd lbf y clear §8[§4!§8]");
		}
		else if (tokens.length == 5) // correct usage for y coord
		{
			if (!tokens[3].matches("[0-9]+"))
				return this.logError("§8[§4!§8] §c" + tokens[3] + " is not a valid positive integer! §8[§4!§8]");

			else if (!tokens[4].matches("[0-9]+"))
				return this.logError("§8[§4!§8] §c" + tokens[4] + " is not a valid positive integer! §8[§4!§8]");
			else {
				this.showMinY = Integer.parseInt(tokens[3]);
				this.showMaxY = Integer.parseInt(tokens[4]);
				return this.logMessage("§bNow showing only lb entries with Y coords between "
						+ this.showMinY + " to " + this.showMaxY + " (inclusive).");
			}
		}
		else
			return this.logError("§8[§4§lERROR§8] §cThe code should never reach this point! Kyzer needs to fix :O");
	}
	
	public int getMinY() { return this.showMinY; }
	
	public int getMaxY() { return this.showMaxY; }
	
	/**
	 * Logs the message to the user
	 * @param message The message to log
	 */
	private String logMessage(String message)
	{
		ChatComponentText displayMessage = new ChatComponentText(message);
		displayMessage.setChatStyle((new ChatStyle()).setColor(EnumChatFormatting.AQUA));
		Minecraft.getMinecraft().thePlayer.addChatComponentMessage(displayMessage);
		return message;
	}

	/**
	 * Logs the error message to the user
	 * @param message The error message to log
	 */
	private String logError(String message)
	{
		ChatComponentText displayMessage = new ChatComponentText(message);
		displayMessage.setChatStyle((new ChatStyle()).setColor(EnumChatFormatting.RED));
		Minecraft.getMinecraft().thePlayer.addChatComponentMessage(displayMessage);
		return message;
	}
}
