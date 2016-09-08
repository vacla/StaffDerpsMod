package io.github.kyzderp.staffderpsmod;

import net.minecraft.client.Minecraft;

public class Commands 
{
	private LiteModStaffDerps main;
	
	public Commands(LiteModStaffDerps main)
	{
		this.main = main;
	}
	
	
	public void handleCommand(String message)
	{
		String[] tokens = message.split(" ");
		
		if (tokens.length < 2)
		{
			LiteModStaffDerps.logMessage("Staff Derps \u00A78[\u00A7av" + this.main.getVersion() 
					+ "\u00A78] by \u00A72Kyzeragon", false);
			LiteModStaffDerps.logMessage("Type \u00A72/sd help \u00A7aor "
					+ "\u00A72/staffderps help \u00A7afor commands.", false);
		}
		else if (tokens[1].equalsIgnoreCase("invis") || tokens[1].equalsIgnoreCase("invisible"))
		{
			invisCommand(tokens);
		}
		else if (tokens[1].equalsIgnoreCase("pet"))
		{ 
			petCommand(tokens);
		}
		else if (tokens[1].equalsIgnoreCase("chunk") || tokens[1].equalsIgnoreCase("c"))
		{
			chunkTPCommand(tokens);
		}
		else if (tokens[1].equalsIgnoreCase("tp"))
		{
			tpposCommand(message, tokens);
		}
		else if (tokens[1].equalsIgnoreCase("lbf"))
		{
			this.main.getLbfilter().handleCommand(message);
		}
		else if (tokens[1].equalsIgnoreCase("summon"))
		{
			this.main.getSummoner().setCommand(message.replaceAll("sd |staffderps ", ""));
		}
		else if (tokens[1].equalsIgnoreCase("scalar"))
		{
			if (tokens.length == 2)
				this.main.getSummoner().showScalar();
			else
				this.main.getSummoner().setScalar(tokens[2]);
		}
		else if (tokens[1].equalsIgnoreCase("help"))
		{
			this.helpCommand();
		}
		else
		{
			LiteModStaffDerps.logMessage("Staff Derps \u00A78[\u00A7av" + this.main.getVersion() 
					+ "\u00A78] by \u00A72Kyzeragon", false);
			LiteModStaffDerps.logMessage("Type \u00A72/sd help \u00A7aor "
					+ "\u00A72/staffderps help \u00A7afor commands.", false);
		}
	}


	/**
	 * 
	 */
	private void helpCommand() 
	{
		String[] commands = {"invis <on|off> \u00A77- See through invisibility effect.",
				"pet <on|off|copy> \u00A77- Get pet owner.",
				"chunk <x> <y> \u00A77- Teleport to chunk coords.",
				"tp <coordinates> \u00A77- Attempts to TP to poorly formatted coords.",
				"lbf y <minY> <maxY> \u00A77- Shows only lb entries within specified Y.",
				"summon <mob> ~x ~y ~z {[data]} \u00A77- Pew pew!",
				"scalar <double> \u00A77- Set the scalar for shooting the mob",
		"help - This help message."};
		LiteModStaffDerps.logMessage("Staff Derps [v" + this.main.getVersion() + "] commands (alias /staffderps)", false);
		for (String command: commands)
			LiteModStaffDerps.logMessage("/sd " + command, false);
		// TODO: add wiki link
	}


	/**
	 * @param message
	 * @param tokens
	 */
	private void tpposCommand(String message, String[] tokens) 
	{
		message = message.replaceAll("\\.[0-9]*", ""); // get rid of decimals
		if (tokens.length == 3 && tokens[2].contains("/")) // try to split with / . ,
			message = message.replaceAll("/", " ");
		else if (tokens.length == 3 && tokens[2].contains("."))
			message = message.replaceAll(".", " ");
		else if (tokens.length == 3 && tokens[2].contains(","))
			message = message.replaceAll(",", " ");

		String result = "";
		char prevChar = 'a';
		for (int i = 7; i < message.length(); i++) // keep only -, numbers, and spaces
		{
			char c = message.charAt(i);
			if (!(prevChar == '-' && c == ' '))
				if ("-0123456789 ".contains("" + c))
					result += c;
		}
		while (result.matches(".*  .*")) // Only 1 space pl0x
			result = result.replaceAll("  ", " ");
		String[] coords = result.trim().split(" ");
		if (coords.length != 3)
		{
			for (String coord: coords)
				System.out.println("\"" + coord + "\"");
			LiteModStaffDerps.logError("Invalid format: " + result);
			return;
		}
		coords[1] = coords[1].replaceAll("-", "");

		// Sometimes people like to switch the y and the z... annoying.
		int y = Integer.parseInt(coords[1]);
		int z = Integer.parseInt(coords[2]);
		if (y > 255 && (z >= 0 && z < 256))
			result = coords[0] + " " + z + " " + y;
		else if (y > 255)
		{
			String sub = coords[1].substring(0, 3);
			if (Integer.parseInt(sub) > 255)
				sub = sub.substring(0, 2);
			result = coords[0] + " " + sub + " " + coords[2];
		}
		else
			result = coords[0] + " " + y + " " + coords[2];
		LiteModStaffDerps.logMessage("Running /tppos " + result, true);
		Minecraft.getMinecraft().thePlayer.sendChatMessage("/tppos " + result);
	}


	/**
	 * @param tokens
	 */
	private void chunkTPCommand(String[] tokens) 
	{
		int first = 10000000;
		int second = 10000000;
		for (int i = 0; i < tokens.length; i++)
		{
			if (tokens[i].matches("-?[0-9]*"))
			{
				if (first != 10000000)
				{
					second = Integer.parseInt(tokens[i]) * 16 + 8;
					first = first * 16 + 8;
					Minecraft.getMinecraft().thePlayer.sendChatMessage(
							"/tppos " + first + " 100 " + second);
					return;
				}
				else
					first = Integer.parseInt(tokens[i]);
			}
		}
		LiteModStaffDerps.logError("Usage: /sd chunk <x> <y>");
	}


	/**
	 * @param tokens
	 */
	private void petCommand(String[] tokens) 
	{
		// TODO: make it clearer which pet it is
		if (tokens.length < 3)
			LiteModStaffDerps.logError("Usage: /sd pet <on|off|copy>");
		else if (tokens[2].equalsIgnoreCase("on")) {
			this.main.getConfig().setSeePetOwnerOn(true);
			LiteModStaffDerps.logMessage("Displaying pets in 2 block radius", true);
		}
		else if (tokens[2].equalsIgnoreCase("off")) {
			this.main.getConfig().setSeePetOwnerOn(false);
			LiteModStaffDerps.logMessage("Pet display: OFF", true);
		}
		else if (tokens[2].equalsIgnoreCase("copy")) {
			String result = this.main.getOwner().getRandomOwner();
			if (result == null || result == "")
				LiteModStaffDerps.logError("No owners for any pets in range!");
			else {
				LiteModStaffDerps.logMessage("Owner name is " + result, true);
			}
		}
		else
			LiteModStaffDerps.logError("Usage: /sd pet <on|off|copy>");
	}


	/**
	 * @param tokens
	 */
	private void invisCommand(String[] tokens) 
	{
		if (tokens.length < 3)
			LiteModStaffDerps.logError("Usage: /sd invis <on|off>");
		else if (tokens[2].equalsIgnoreCase("on"))
		{
			this.main.getConfig().setSeeInvisibleOn(true);
			LiteModStaffDerps.logMessage("See through invisibility: ON", true);
		}
		else if (tokens[2].equalsIgnoreCase("off"))
		{
			this.main.getConfig().setSeeInvisibleOn(false);
			LiteModStaffDerps.logMessage("See through invisibility: OFF", true);
		}
		else
			LiteModStaffDerps.logError("Usage: /sd invis <on|off>");
	}
}
