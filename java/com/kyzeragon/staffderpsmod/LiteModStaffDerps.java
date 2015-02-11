package com.kyzeragon.staffderpsmod;

import java.io.File;
import java.util.List;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.kyzeragon.staffderpsmod.config.StaffDerpsConfig;
import com.kyzeragon.staffderpsmod.config.StaffDerpsConfigScreen;
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
public class LiteModStaffDerps implements Tickable
{
	private static KeyBinding leftBinding;
	private static KeyBinding rightBinding;
	private static KeyBinding ownerBinding;
	private static KeyBinding copyUUIDBinding;
	private static KeyBinding configBinding;

	private CompassMath compassMath;
	private SeeInvisible invis;
	private PetOwner owner;
	
	private int canCompass;
	private boolean showOwner = false;

	///// Config /////
	private StaffDerpsConfigScreen configScreen;
	private StaffDerpsConfig config;
	
	public LiteModStaffDerps() {}

	@Override
	public String getName()
	{
		return "Staff Derps";
	}

	@Override
	public String getVersion()
	{
		return "0.9.7";
	}

	@Override
	public void init(File configPath)
	{
		this.canCompass = 0; 
		this.compassMath = new CompassMath(Minecraft.getMinecraft());
		this.invis = new SeeInvisible();
		this.owner = new PetOwner();
		this.config = new StaffDerpsConfig();
		this.configScreen = new StaffDerpsConfigScreen(this.config);
		
		leftBinding = new KeyBinding("key.compass.left", -97, "key.categories.litemods");
		rightBinding = new KeyBinding("key.compass.right", -96, "key.categories.litemods");
		ownerBinding = new KeyBinding("key.owner.show", Keyboard.KEY_SEMICOLON, "key.categories.litemods");
		copyUUIDBinding = new KeyBinding("key.owner.copy", Keyboard.KEY_L, "key.categories.litemods");
		configBinding = new KeyBinding("key.staffderps", Keyboard.KEY_HOME, "key.categories.litemods");
	
		
		LiteLoader.getInput().registerKeyBinding(leftBinding);
		LiteLoader.getInput().registerKeyBinding(rightBinding);
		LiteLoader.getInput().registerKeyBinding(ownerBinding);
		LiteLoader.getInput().registerKeyBinding(copyUUIDBinding);
		LiteLoader.getInput().registerKeyBinding(configBinding);
	}

	@Override
	public void upgradeSettings(String version, File configPath, File oldConfigPath) {}

	@Override
	public void onTick(Minecraft minecraft, float partialTicks, boolean inGame, boolean clock)
	{
		if (inGame && minecraft.currentScreen == null && Minecraft.isGuiEnabled())
		{			
			if (LiteModStaffDerps.leftBinding.isPressed())
				if (this.config.getCompassEnabled())
					this.compassMath.jumpTo();
			else if (LiteModStaffDerps.rightBinding.isPressed())
				if (this.config.getCompassEnabled())
					this.compassMath.passThrough();
			else if (LiteModStaffDerps.ownerBinding.isPressed())
				this.showOwner = !this.showOwner;
			else if (LiteModStaffDerps.copyUUIDBinding.isPressed()) 
				this.owner.getRandomOwner();
			else if (LiteModStaffDerps.configBinding.isPressed())
			{
				System.out.println("trying to display config screen");
				minecraft.displayGuiScreen(this.configScreen);
			}
			
			if (this.config.getSeeInvisibleOn())
			{
				FontRenderer fontRender = minecraft.fontRenderer;
				String invsPlayers = this.invis.getInvsString();
				fontRender.drawStringWithShadow("Hidden players: " 
						+ invsPlayers, 0, 0, 0xFFAA00);
			}
			
			if (this.showOwner)
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
}
