package com.kyzeragon.staffderpsmod;

import java.io.File;
import java.util.List;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.kyzeragon.staffderpsmod.config.StaffDerpsConfig;
import com.mumfrey.liteloader.ChatFilter;
import com.mumfrey.liteloader.OutboundChatListener;
import com.mumfrey.liteloader.Tickable;
import com.mumfrey.liteloader.core.LiteLoader;
import com.mumfrey.liteloader.modconfig.ConfigStrategy;
import com.mumfrey.liteloader.modconfig.ExposableOptions;

/**
 * What more derps do you want?
 *
 * @author Kyzeragon
 */
@ExposableOptions(strategy = ConfigStrategy.Versioned, filename="staffderpsmod.json")
public class LiteModStaffDerps implements Tickable, ChatFilter, OutboundChatListener // TODO: display invis location
{
	///// FIELDS /////
	private static KeyBinding leftBinding;
	private static KeyBinding rightBinding;

	private CompassMath compassMath;
	private SeeInvisible invis;
	private PetOwner owner;

	private boolean showOwner;
	private boolean sentCmd;

	private StaffDerpsConfig config;

	///// METHODS /////
	public LiteModStaffDerps() {}

	@Override
	public String getName() { return "Staff Derps"; }

	@Override
	public String getVersion() { return "0.9.7"; }

	@Override
	public void init(File configPath)
	{
		this.compassMath = new CompassMath(Minecraft.getMinecraft());
		this.invis = new SeeInvisible();
		this.owner = new PetOwner();
		this.config = new StaffDerpsConfig();
		this.showOwner = false;
		this.sentCmd = false;

		leftBinding = new KeyBinding("key.compass.left", -97, "key.categories.litemods");
		rightBinding = new KeyBinding("key.compass.right", -96, "key.categories.litemods");

		LiteLoader.getInput().registerKeyBinding(leftBinding);
		LiteLoader.getInput().registerKeyBinding(rightBinding);
	}

	@Override
	public void upgradeSettings(String version, File configPath, File oldConfigPath) {}

	@Override
	public void onTick(Minecraft minecraft, float partialTicks, boolean inGame, boolean clock)
	{
		if (inGame && minecraft.currentScreen == null && Minecraft.isGuiEnabled())
		{			
			if (LiteModStaffDerps.leftBinding.isPressed())
				this.compassMath.jumpTo();
			else if (LiteModStaffDerps.rightBinding.isPressed())
				this.compassMath.passThrough();

			if (this.config.getSeeInvisibleOn())
			{
				FontRenderer fontRender = minecraft.fontRenderer;
				String invsPlayers = this.invis.getInvsString();
				fontRender.drawStringWithShadow("Hidden players: " 
						+ invsPlayers, 0, 0, 0xFFAA00);
			}

			if (this.config.getSeePetOwnerOn())
			{
				FontRenderer fontRender = minecraft.fontRenderer;
				String dogs = this.owner.getDogOwners();
				String cats = this.owner.getCatOwners();
				fontRender.drawStringWithShadow("Dogs: " 
						+ dogs, 0, 0, 0xFFAA00);
				fontRender.drawStringWithShadow("Cats: " 
						+ cats, 0, 10, 0xFFAA00);
			}
		}
	}

	@Override
	public void onSendChatMessage(C01PacketChatMessage packet, String message)
	{
		String[] tokens = message.split(" ");
		if (tokens[0].equalsIgnoreCase("/staffderps") || tokens[0].equalsIgnoreCase("/sd"))
		{
			this.sentCmd = true;
			if (tokens.length < 2)
			{
				this.logMessage("Staff Derps [v" + this.getVersion() + "] by Kyzeragon");
				this.logMessage("Type /sd help or /staffderps help for commands.");
				return;
			}
			else if (tokens[1].equalsIgnoreCase("invis") || tokens[1].equalsIgnoreCase("invisible"))
			{
				if (tokens.length < 3)
					this.logError("Usage: /staffderps invis <on|off>");
				else if (tokens[2].equalsIgnoreCase("on"))
				{
					this.config.setSeeInvisibleOn(true);
					this.logMessage("See through invisibility: ON");
				}
				else if (tokens[2].equalsIgnoreCase("off"))
				{
					this.config.setSeeInvisibleOn(false);
					this.logMessage("See through invisibility: OFF");
				}
				else
					this.logError("Usage: /staffderps invis <on|off>");
			}
			else if (tokens[1].equalsIgnoreCase("pet"))
			{ // TODO: make it clearer which pet it is
				if (tokens.length < 3)
					this.logError("Usage: /staffderps pet <on|off|copy>");
				else if (tokens[2].equalsIgnoreCase("on")) {
					this.config.setSeePetOwnerOn(true);
					this.logMessage("Displaying pets in 2 block radius");
				}
				else if (tokens[2].equalsIgnoreCase("off")) {
					this.config.setSeePetOwnerOn(false);
					this.logMessage("Pet display: OFF");
				}
				else if (tokens[2].equalsIgnoreCase("copy")) {
					String result = this.owner.getRandomOwner();
					if (result == null || result == "")
						this.logError("No owners for any pets in range!");
					else {
						GuiScreen.setClipboardString(result);
						this.logMessage("Owner UUID copied to clipboard.");
					}
				}
				else
					this.logError("Usage: /staffderps pet <on|off|copy>");
			}
			else if (message.matches(".*(Mecha|mecha).*"))
			{
				message.replaceAll("(Mecha|mecha)", "Mecha ");
				tokens = message.split(" ");
				for (int i = 0; i < tokens.length; i++)
				{
					if (tokens[i].equalsIgnoreCase("mecha"))
					{
						int first = 10000000; 
						int second = 10000000;
						for (int j = i + 1; j < tokens.length; j++)
						{
							if (tokens[j].matches("-*[0-9]*"))
							{
								if (first != 10000000)
								{
									second = Integer.parseInt(tokens[j]);
									Minecraft.getMinecraft().thePlayer.sendChatMessage(
											"/tppos " + first + " 100 " + second);
									return;
								}
								else
									first = Integer.parseInt(tokens[j]);
							}
						}
					}
				}
				this.logError("Usage: /staffderps mecha <x> <y>");
			}
			else if (tokens[1].equalsIgnoreCase("help"))
			{
				String[] commands = {"invis <on|off> - See through invisibility effect.",
						"pet <on|off|copy> - Get pet owner.",
						"mecha <x> <y> - Teleport to chunk coords.",
						"help - This help message."};
				this.logMessage("Staff Derps [v" + this.getVersion() + "] commands (alias /sd)");
				for (String command: commands)
					this.logMessage("/staffderps " + command);
			}
		}
	}


	/**
	 * Stops the Unknown command error from the server from displaying
	 */
	@Override
	public boolean onChat(S02PacketChat chatPacket, IChatComponent chat,
			String message) {
		if (message.matches(".*nknown.*ommand.*") && this.sentCmd)
		{
			this.sentCmd = false;
			return false;
		}
		return true;
	}

	/**
	 * Logs the message to the user
	 * @param message The message to log
	 */
	private void logMessage(String message)
	{
		ChatComponentText displayMessage = new ChatComponentText(message);
		displayMessage.setChatStyle((new ChatStyle()).setColor(EnumChatFormatting.AQUA));
		Minecraft.getMinecraft().thePlayer.addChatComponentMessage(displayMessage);
	}

	/**
	 * Logs the error message to the user
	 * @param message The error message to log
	 */
	private void logError(String message)
	{
		ChatComponentText displayMessage = new ChatComponentText(message);
		displayMessage.setChatStyle((new ChatStyle()).setColor(EnumChatFormatting.RED));
		Minecraft.getMinecraft().thePlayer.addChatComponentMessage(displayMessage);
	}
}
