package com.kyzeragon.staffderpsmod;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;

public class PetOwner {

	AxisAlignedBB bb;
	String randomOwner;

	public PetOwner(){}
	
	private List getDog()
	{
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		this.bb = AxisAlignedBB.getBoundingBox(player.posX - 4, player.posY - 4, player.posZ - 4, 
				player.posX + 4, player.posY + 4, player.posZ + 4);
		return Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(EntityWolf.class, this.bb);
	}
	
	public String getDogOwners()
	{
		String result = "";
		for (Object dog: this.getDog())
		{
			this.randomOwner = ((EntityWolf)dog).getOwnerName();
			result += ((EntityWolf)dog).getCommandSenderName() + " ";			
		}
		return result;
	}
	
	private List getCat()
	{
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		this.bb = AxisAlignedBB.getBoundingBox(player.posX - 2, player.posY - 2, player.posZ - 2, 
				player.posX + 2, player.posY + 2, player.posZ + 2);
		return Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(EntityOcelot.class, this.bb);
	}
	
	public String getCatOwners()
	{
		String result = "";
		for (Object cat: this.getCat())
		{
			this.randomOwner = ((EntityOcelot)cat).getOwnerName();
			result += ((EntityOcelot)cat).getCommandSenderName() + " ";			
		}
		return result;
	}
	
	/**
	 * copy UUID of in-range pet to clipboard
	 */
	public void getRandomOwner()
	{
		GuiScreen.setClipboardString(this.randomOwner);
	}
}
