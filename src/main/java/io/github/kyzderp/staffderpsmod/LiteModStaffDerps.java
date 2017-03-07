package io.github.kyzderp.staffderpsmod;

import io.github.kyzderp.staffderpsmod.config.StaffDerpsConfig;
import io.github.kyzderp.staffderpsmod.subfunctions.CompassMath;
import io.github.kyzderp.staffderpsmod.subfunctions.LBFilter;
import io.github.kyzderp.staffderpsmod.subfunctions.MobSummoner;
import io.github.kyzderp.staffderpsmod.subfunctions.PetOwner;
import io.github.kyzderp.staffderpsmod.subfunctions.SeeInvisible;

import java.io.File;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.mumfrey.liteloader.ChatFilter;
import com.mumfrey.liteloader.OutboundChatFilter;
import com.mumfrey.liteloader.PostRenderListener;
import com.mumfrey.liteloader.Tickable;
import com.mumfrey.liteloader.core.LiteLoader;
import com.mumfrey.liteloader.core.LiteLoaderEventBroker.ReturnValue;
import com.mumfrey.liteloader.gl.GL;
import com.mumfrey.liteloader.modconfig.ConfigStrategy;
import com.mumfrey.liteloader.modconfig.ExposableOptions;

/**
 * What more derps do you want?
 *
 * @author Kyzeragon
 */
@ExposableOptions(strategy = ConfigStrategy.Versioned, filename="staffderpsmod.json")
public class LiteModStaffDerps implements Tickable, ChatFilter, OutboundChatFilter, PostRenderListener
{
	///// FIELDS /////
	private static KeyBinding leftBinding;
	private static KeyBinding rightBinding;
	private static KeyBinding summonBinding;

	private final StaffDerpsConfig config = new StaffDerpsConfig();
	private final CompassMath compassMath = new CompassMath(Minecraft.getMinecraft());
	private final SeeInvisible invis = new SeeInvisible();
	private final PetOwner owner = new PetOwner();
	private final LBFilter lbfilter = new LBFilter();
	private final MobSummoner summoner = new MobSummoner(this.getConfig());
	private final Commands commands = new Commands(this);


	///// METHODS /////
	public LiteModStaffDerps() {}

	@Override
	public String getName() { return "Staff Derps"; }

	@Override
	public String getVersion() { return "1.3.1"; }

	@Override
	public void init(File configPath)
	{
		LiteModStaffDerps.leftBinding = new KeyBinding("key.compass.left", -97, "key.categories.litemods");
		LiteModStaffDerps.rightBinding = new KeyBinding("key.compass.right", -96, "key.categories.litemods");
		LiteModStaffDerps.summonBinding = new KeyBinding("key.summon", Keyboard.CHAR_NONE, "key.categories.litemods");

		LiteLoader.getInput().registerKeyBinding(leftBinding);
		LiteLoader.getInput().registerKeyBinding(rightBinding);
		LiteLoader.getInput().registerKeyBinding(LiteModStaffDerps.summonBinding);
	}

	@Override
	public void upgradeSettings(String version, File configPath, File oldConfigPath) {}

	@Override
	public void onTick(Minecraft minecraft, float partialTicks, boolean inGame, boolean clock)
	{
		if (inGame && minecraft.currentScreen == null && Minecraft.isGuiEnabled())
		{
			if (LiteModStaffDerps.summonBinding.isPressed())
			{
				this.getSummoner().summon();
			}

			if (LiteModStaffDerps.leftBinding.isPressed())
				this.compassMath.jumpTo();
			else if (LiteModStaffDerps.rightBinding.isPressed())
				this.compassMath.passThrough();

			if (this.getConfig().getSeeInvisibleOn())
			{
				FontRenderer fontRender = minecraft.fontRendererObj;
				String invsPlayers = this.invis.getInvsString();
				fontRender.drawStringWithShadow("Hidden players: " 
						+ invsPlayers, 0, 0, 0xFFAA00);
			}

			if (this.getConfig().getSeePetOwnerOn())
			{
				FontRenderer fontRender = minecraft.fontRendererObj;
				String dogs = this.getOwner().getDogOwners();
				String cats = this.getOwner().getCatOwners();
				fontRender.drawStringWithShadow("Dogs: " 
						+ dogs, 0, 0, 0xFFAA00);
				fontRender.drawStringWithShadow("Cats: " 
						+ cats, 0, 10, 0xFFAA00);
			}
		}
	}

	@Override
	public boolean onSendChatMessage(String message)
	{
		while (message.matches(".*  .*"))
			message = message.replaceAll("  ", " ");
		String[] tokens = message.split(" ");

		if (tokens[0].equalsIgnoreCase("/staffderps") || tokens[0].equalsIgnoreCase("/sd"))
		{
			this.commands.handleCommand(message);
			return false;
		}
		return true;
	}


	/**
	 * Stops the Unknown command error from the server from displaying,
	 * and also prevents displaying of lb entries that should be filtered out
	 */
	@Override
	public boolean onChat(ITextComponent chat, String message, 
			ReturnValue<ITextComponent> newMessage) {
		if (message.matches("\u00A7r\u00A76\\([0-9]+\\).*at .*:.*:.*"))
			return this.getLbfilter().handleEntry(message);
		return true;
	}

	/**
	 * Display location of invisible players if display is on.
	 */
	@Override
	public void onPostRenderEntities(float partialTicks) 
	{
		if (!this.getConfig().getSeeInvisibleOn())
			return;
		// TODO: display player name too
		RenderHelper.disableStandardItemLighting();
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);

		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.disableLighting();
		GlStateManager.depthMask(false);
		GlStateManager.disableDepth();

		boolean foggy = GL11.glIsEnabled(GL11.GL_FOG);
		GlStateManager.disableFog();
		GlStateManager.pushMatrix();

		EntityPlayer player = Minecraft.getMinecraft().player;
		GlStateManager.translate(-(player.prevPosX + (player.posX - player.prevPosX) * partialTicks),
				-(player.prevPosY + (player.posY - player.prevPosY) * partialTicks),
				-(player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks));

		Tessellator tess = Tessellator.getInstance();
		List<EntityPlayer> players = this.invis.getInvsPlayers();
		for (EntityPlayer currentPlayer: players)
		{
			double x = currentPlayer.posX - 0.3;
			double y = currentPlayer.posY;
			double z = currentPlayer.posZ - 0.3;
			GL.glLineWidth(3.0f);
			GL.glColor4f(1F, 0F, 0F, 0.8F);
			VertexBuffer vbuf = tess.getBuffer();

			vbuf.begin(GL.GL_LINE_LOOP, GL.VF_POSITION);
			vbuf.pos(x, y, z).endVertex();
			vbuf.pos(x + 0.6, y, z).endVertex();
			vbuf.pos(x + 0.6, y, z + 0.6).endVertex();
			vbuf.pos(x, y, z + 0.6).endVertex();
			tess.draw();

			vbuf.begin(GL.GL_LINE_LOOP, GL.VF_POSITION);
			vbuf.pos(x, y + 1.8, z).endVertex();
			vbuf.pos(x + 0.6, y + 1.8, z).endVertex();
			vbuf.pos(x + 0.6, y + 1.8, z + 0.6).endVertex();
			vbuf.pos(x, y + 1.8, z + 0.6).endVertex();
			tess.draw();

			vbuf.begin(GL.GL_LINE_LOOP, GL.VF_POSITION);
			vbuf.pos(x, y, z).endVertex();
			vbuf.pos(x, y + 1.8, z).endVertex();

			vbuf.pos(x + 0.6, y, z).endVertex();
			vbuf.pos(x + 0.6, y + 1.8, z).endVertex();

			vbuf.pos(x + 0.6, y, z + 0.6).endVertex();
			vbuf.pos(x + 0.6, y + 1.8, z + 0.6).endVertex();

			vbuf.pos(x, y, z + 0.6).endVertex();
			vbuf.pos(x, y + 1.8, z + 0.6).endVertex();
			tess.draw();
		}

		GlStateManager.popMatrix();

		// Only re-enable fog if it was enabled before we messed with it.
		// Or else, fog is *all* you'll see with Optifine.
		if (foggy)
		{
			GlStateManager.enableFog();
		}
		GlStateManager.enableDepth();
		GlStateManager.depthMask(true);
		GlStateManager.enableLighting();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
		RenderHelper.enableStandardItemLighting();
	}

	/**
	 * Logs the message to the user
	 * @param message The message to log
	 */
	public static void logMessage(String message, boolean addPrefix)
	{
		if (addPrefix)
			message = "\u00A78[\u00A72StaffDerps\u00A79] \u00A7a" + message;
		TextComponentString displayMessage = new TextComponentString(message);
		displayMessage.setStyle((new Style()).setColor(TextFormatting.GREEN));
		Minecraft.getMinecraft().player.sendMessage(displayMessage);
	}

	/**
	 * Logs the error message to the user
	 * @param message The error message to log
	 */
	public static void logError(String message)
	{
		TextComponentString displayMessage = new TextComponentString("\u00A78[\u00A7cStaffDerps\u00A78] \u00A7c" + message);
		displayMessage.setStyle((new Style()).setColor(TextFormatting.RED));
		Minecraft.getMinecraft().player.sendMessage(displayMessage);
	}

	@Override
	public void onPostRender(float partialTicks) {}

	public MobSummoner getSummoner() { return summoner; }
	public StaffDerpsConfig getConfig() { return config; }
	public LBFilter getLbfilter() { return lbfilter; }
	public PetOwner getOwner() { return owner; }
}
