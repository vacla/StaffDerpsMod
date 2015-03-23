package com.kyzeragon.staffderpsmod;

import java.io.File;
import java.util.List;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
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
import org.lwjgl.opengl.GL11;

import com.kyzeragon.staffderpsmod.config.StaffDerpsConfig;
import com.mumfrey.liteloader.ChatFilter;
import com.mumfrey.liteloader.OutboundChatListener;
import com.mumfrey.liteloader.PostRenderListener;
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
public class LiteModStaffDerps implements Tickable, ChatFilter, OutboundChatListener, PostRenderListener
{
	///// FIELDS /////
	private static KeyBinding leftBinding;
	private static KeyBinding rightBinding;

	private CompassMath compassMath;
	private SeeInvisible invis;
	private PetOwner owner;
	private LBFilter lbfilter;
	private ChestSorter chestSorter;

	private boolean showOwner;
	private boolean sentCmd;
	private int grabCooldown;

	private StaffDerpsConfig config;

	///// METHODS /////
	public LiteModStaffDerps() {}

	@Override
	public String getName() { return "Staff Derps"; }

	@Override
	public String getVersion() { return "1.1.2"; }

	@Override
	public void init(File configPath)
	{
		this.compassMath = new CompassMath(Minecraft.getMinecraft());
		this.invis = new SeeInvisible();
		this.owner = new PetOwner();
		this.config = new StaffDerpsConfig();
		this.lbfilter = new LBFilter();

		this.showOwner = false;
		this.sentCmd = false;
		this.chestSorter = new ChestSorter();
		this.grabCooldown = 5;

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
		if (inGame && minecraft.thePlayer.openContainer != null
				&& !minecraft.thePlayer.openContainer.equals(minecraft.thePlayer.inventoryContainer))
		{
			if (this.grabCooldown < 5)
				this.grabCooldown++;
			if (Keyboard.isKeyDown(Keyboard.KEY_TAB) && this.grabCooldown == 5)
			{
				this.chestSorter.grab(minecraft.thePlayer.openContainer);
				this.grabCooldown = 0;
			}
		}

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
		String[] tokens = message.trim().split(" ");
		if (tokens[0].equalsIgnoreCase("/staffderps") || tokens[0].equalsIgnoreCase("/sd"))
		{
			while (message.matches(".*  .*"))
				message = message.replaceAll("  ", " ");
			tokens = message.split(" ");
			this.sentCmd = true;
			if (tokens.length < 2)
			{
				this.logMessage("Staff Derps [v" + this.getVersion() + "] by Kyzeragon");
				this.logMessage("Type /sd help or /staffderps help for commands.");
				return;
			}
			else if (tokens[1].equalsIgnoreCase("grab"))
			{
				this.chestSorter.handleCommand(message);
			}
			else if (tokens[1].equalsIgnoreCase("invis") || tokens[1].equalsIgnoreCase("invisible"))
			{
				if (tokens.length < 3)
					this.logError("Usage: /sd invis <on|off>");
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
					this.logError("Usage: /sd invis <on|off>");
			}
			else if (tokens[1].equalsIgnoreCase("pet"))
			{ // TODO: make it clearer which pet it is
				if (tokens.length < 3)
					this.logError("Usage: /sd pet <on|off|copy>");
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
					this.logError("Usage: /sd pet <on|off|copy>");
			}
			else if (tokens[1].equalsIgnoreCase("chunk") || tokens[1].equalsIgnoreCase("c"))
			{
				int first = 10000000;
				int second = 10000000;
				for (int i = 0; i < tokens.length; i++)
				{
					if (tokens[i].matches("-?[0-9]*"))
					{
						if (first != 10000000)
						{
							second = Integer.parseInt(tokens[i]) * 16;
							first *= 16;
							Minecraft.getMinecraft().thePlayer.sendChatMessage(
									"/tppos " + first + " 100 " + second);
							return;
						}
						else
							first = Integer.parseInt(tokens[i]);
					}
				}
				this.logError("Usage: /sd chunk <x> <y>");
			}
			else if (tokens[1].equalsIgnoreCase("tp"))
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
				while (result.matches(".*  .*")) // fix the y coord if too high
					result = result.replaceAll("  ", " ");
				String[] coords = result.trim().split(" ");
				if (coords.length != 3)
				{
					for (String coord: coords)
						System.out.println("\"" + coord + "\"");
					this.logError("Invalid format: " + result);
					return;
				}
				String y = coords[1];
				if (Integer.parseInt(y) > 260)
				{
					y = y.substring(0, 3);
					if (Integer.parseInt(y) > 260)
						y = y.substring(0, 2);
				}
				result = coords[0] + " " + y + " " + coords[2];
				this.logMessage("Running /tppos " + result);
				Minecraft.getMinecraft().thePlayer.sendChatMessage("/tppos " + result);
			}
			else if (tokens[1].equalsIgnoreCase("lbf"))
			{
				this.lbfilter.handleCommand(message);
			}
			else if (tokens[1].equalsIgnoreCase("help"))
			{
				String[] commands = {"invis <on|off> - See through invisibility effect.",
						"pet <on|off|copy> - Get pet owner.",
						"chunk <x> <y> - Teleport to chunk coords.",
						"tp <coordinates> - Attempts to TP to poorly formatted coords.",
						"lbf y <minY> <maxY> - Shows only lb entries within specified Y.",
						"grab <item[,item2]> - Specify items to grab from container when pressing TAB.",
				"help - This help message."};
				this.logMessage("Staff Derps [v" + this.getVersion() + "] commands (alias /staffderps)");
				for (String command: commands)
					this.logMessage("/sd " + command);
			}
			else
			{
				this.logMessage("Staff Derps [v" + this.getVersion() + "] by Kyzeragon");
				this.logMessage("Type /sd help or /staffderps help for commands.");
				return;
			}
		}
	}


	/**
	 * Stops the Unknown command error from the server from displaying,
	 * and also prevents displaying of lb entries that should be filtered out
	 */
	@Override
	public boolean onChat(S02PacketChat chatPacket, IChatComponent chat,
			String message) {
		System.out.println(message);
		if (message.matches(".*nknown.*ommand.*") && this.sentCmd)
		{
			this.sentCmd = false;
			return false;
		}
		else if (message.matches("§r§6\\([0-9]+\\).*at .*:.*:.*"))
			return this.lbfilter.handleEntry(message);
		return true;
	}

	/**
	 * Display location of invisible players if display is on.
	 */
	@Override
	public void onPostRenderEntities(float partialTicks) {
		if (!this.config.getSeeInvisibleOn())
			return;
		// TODO: display player name too
		RenderHelper.disableStandardItemLighting();
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);

		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDepthMask(false);
		GL11.glDisable(GL11.GL_DEPTH_TEST);

		boolean foggy = GL11.glIsEnabled(GL11.GL_FOG);
		GL11.glDisable(GL11.GL_FOG);

		GL11.glPushMatrix();

		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		GL11.glTranslated(-(player.prevPosX + (player.posX - player.prevPosX) * partialTicks),
				-(player.prevPosY + (player.posY - player.prevPosY) * partialTicks),
				-(player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks));

		Tessellator tess = Tessellator.instance;
		List<EntityPlayer> players = this.invis.getInvsPlayers();
		for (EntityPlayer currentPlayer: players)
		{
			double x = currentPlayer.posX - 0.3;
			double y = currentPlayer.posY;
			double z = currentPlayer.posZ - 0.3;
			GL11.glLineWidth(3.0f);
			tess.startDrawing(GL11.GL_LINE_LOOP);
			tess.setColorRGBA(255, 0, 0, 200);
			tess.addVertex(x, y, z);
			tess.addVertex(x + 0.6, y, z);
			tess.addVertex(x + 0.6, y, z + 0.6);
			tess.addVertex(x, y, z + 0.6);
			tess.draw();

			tess.startDrawing(GL11.GL_LINE_LOOP);
			tess.setColorRGBA(255, 0, 0, 200);
			tess.addVertex(x, y + 1.8, z);
			tess.addVertex(x + 0.6, y + 1.8, z);
			tess.addVertex(x + 0.6, y + 1.8, z + 0.6);
			tess.addVertex(x, y + 1.8, z + 0.6);
			tess.draw();

			tess.startDrawing(GL11.GL_LINES);
			tess.setColorRGBA(255, 0, 0, 200);
			tess.addVertex(x, y, z);
			tess.addVertex(x, y + 1.8, z);

			tess.addVertex(x + 0.6, y, z);
			tess.addVertex(x + 0.6, y + 1.8, z);

			tess.addVertex(x + 0.6, y, z + 0.6);
			tess.addVertex(x + 0.6, y + 1.8, z + 0.6);

			tess.addVertex(x, y, z + 0.6);
			tess.addVertex(x, y + 1.8, z + 0.6);
			tess.draw();
		}

		GL11.glPopMatrix();

		// Only re-enable fog if it was enabled before we messed with it.
		// Or else, fog is *all* you'll see with Optifine.
		if (foggy)
			GL11.glEnable(GL11.GL_FOG);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);

		RenderHelper.enableStandardItemLighting();
	}

	/**
	 * Logs the message to the user
	 * @param message The message to log
	 */
	public static void logMessage(String message)
	{
		ChatComponentText displayMessage = new ChatComponentText(message);
		displayMessage.setChatStyle((new ChatStyle()).setColor(EnumChatFormatting.GREEN));
		Minecraft.getMinecraft().thePlayer.addChatComponentMessage(displayMessage);
	}

	/**
	 * Logs the error message to the user
	 * @param message The error message to log
	 */
	public static void logError(String message)
	{
		ChatComponentText displayMessage = new ChatComponentText("§8[§4!§8] §c" + message + " §8[§4!§8]");
		displayMessage.setChatStyle((new ChatStyle()).setColor(EnumChatFormatting.RED));
		Minecraft.getMinecraft().thePlayer.addChatComponentMessage(displayMessage);
	}

	@Override
	public void onPostRender(float partialTicks) {}
}
