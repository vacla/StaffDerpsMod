package io.github.kyzderp.staffderpsmod.subfunctions;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

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
		command = command.replaceAll("\u00A7.", "");
		String[] tokens = command.split(" ");
		if (tokens.length < 3)
			return this.logError("\u00A78[\u00A74!\u00A78] \u00A7cNot enough arguments! Usage: /sd lbf <y|regex> \u00A78[\u00A74!\u00A78]");
		else if (tokens[2].equalsIgnoreCase("y"))
			return this.handleYCommand(command);
		else if (tokens[2].equalsIgnoreCase("regex"))
			return this.handleRegexCommand(command);
		else
			return this.logError("\u00A78[\u00A74!\u00A78] \u00A7cInvalid arguments! Usage: /sd lbf <y|regex> \u00A78[\u00A74!\u00A78]");
	}
	
	private String handleRegexCommand(String command)
	{
		return "";
	}
	
	private String handleYCommand(String command)
	{
		String[] tokens = command.split(" ");

		if (tokens.length > 5)
			return this.logError("\u00A78[\u00A74!\u00A78] \u00A7cToo many arguments! Usage: /sd lbf y <minY> <maxY> \u00A77or \u00A7c/sd lbf y clear \u00A78[\u00A74!\u00A78]");
		
		else if (tokens.length == 3)
			return this.logMessage("\u00A7bCurrently showing lb entries with Y coords from "
						+ this.showMinY + " to " + this.showMaxY + " (inclusive).");
		
		else if (tokens.length == 4)
		{
			if (tokens[3].equalsIgnoreCase("clear"))
			{
				this.showMinY = 0;
				this.showMaxY = 256;
				return this.logMessage("\u00A7bNow showing all y coordinates for logblock entries.");
			}
			return this.logError("\u00A78[\u00A74!\u00A78] \u00A7cInvalid arguments! Usage: /sd lbf y <minY> <maxY> \u00A77or \u00A7c/sd lbf y clear \u00A78[\u00A74!\u00A78]");
		}
		else if (tokens.length == 5) // correct usage for y coord
		{
			if (!tokens[3].matches("[0-9]+"))
				return this.logError("\u00A78[\u00A74!\u00A78] \u00A7c" + tokens[3] + " is not a valid positive integer! \u00A78[\u00A74!\u00A78]");

			else if (!tokens[4].matches("[0-9]+"))
				return this.logError("\u00A78[\u00A74!\u00A78] \u00A7c" + tokens[4] + " is not a valid positive integer! \u00A78[\u00A74!\u00A78]");
			else {
				this.showMinY = Integer.parseInt(tokens[3]);
				this.showMaxY = Integer.parseInt(tokens[4]);
				return this.logMessage("\u00A7bNow showing only lb entries with Y coords between "
						+ this.showMinY + " to " + this.showMaxY + " (inclusive).");
			}
		}
		else
			return this.logError("\u00A78[\u00A74\u00A7lERROR\u00A78] \u00A7cThe code should never reach this point! Kyzer needs to fix :O");
	}
	
	public int getMinY() { return this.showMinY; }
	
	public int getMaxY() { return this.showMaxY; }
	
	/**
	 * Logs the message to the user
	 * @param message The message to log
	 */
	private String logMessage(String message)
	{
		TextComponentString displayMessage = new TextComponentString(message);
		displayMessage.setStyle((new Style()).setColor(TextFormatting.AQUA));
		Minecraft.getMinecraft().player.sendMessage(displayMessage);
		return message;
	}

	/**
	 * Logs the error message to the user
	 * @param message The error message to log
	 */
	private String logError(String message)
	{
		TextComponentString displayMessage = new TextComponentString(message);
		displayMessage.setStyle((new Style()).setColor(TextFormatting.RED));
		Minecraft.getMinecraft().player.sendMessage(displayMessage);
		return message;
	}
}
