package com.kyzeragon.staffderpsmod;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class CompassMath {

	private Minecraft minecraft;

	public CompassMath(Minecraft minecraft) 
	{
		this.minecraft = minecraft;
	}

	public void passThrough()
	{
		Vec3 vector = minecraft.thePlayer.getLookVec();
		double prevX = minecraft.thePlayer.posX;
		double prevY = minecraft.thePlayer.posY;
		double prevZ = minecraft.thePlayer.posZ;

		boolean doesWallExist = false;
		ChatStyle style = new ChatStyle();
		ChatComponentText message; 

		for (int i = 0; i < 512; i++)
		{
			prevX += vector.xCoord/8;
			prevY += vector.yCoord/8;
			prevZ += vector.zCoord/8;

			int x = (int) Math.floor(prevX);
			int y = (int) Math.floor(prevY);
			int z = (int) Math.floor(prevZ);

			Block block = minecraft.theWorld.getBlock(x, y, z);

			if (canCollide(x, y, z))
				doesWallExist = true;

			if (doesWallExist && y > 0)
			{
				if (!canCollide(x, y + 1, z) && !canCollide(x, y, z))
				{
					minecraft.thePlayer.sendChatMessage("/tppos " + x + " " + y + " " + z);
					return;
				}
				else if (!canCollide(x, y, z) && !canCollide(x, y - 1, z))
				{
					minecraft.thePlayer.sendChatMessage("/tppos " + x + " " + (y - 1) + " " + z);
					return;
				}
			}
		}

		if (doesWallExist)
			message = new ChatComponentText("Too much wall. You shall not pass!");
		else
			message = new ChatComponentText("Nothing to pass through!");

		style.setColor(EnumChatFormatting.DARK_RED);
		message.setChatStyle(style);
		minecraft.thePlayer.addChatMessage(message);
	}

	public void jumpTo()
	{
		Vec3 vector = minecraft.thePlayer.getLookVec();
		double prevX = minecraft.thePlayer.posX;
		double prevY = minecraft.thePlayer.posY;
		double prevZ = minecraft.thePlayer.posZ;
		ChatStyle style = new ChatStyle();
		ChatComponentText message; 

		for (int i = 0; i < 512; i++)
		{
			prevX += vector.xCoord/8;
			prevY += vector.yCoord/8;
			prevZ += vector.zCoord/8;

			int x = (int) Math.floor(prevX);
			int y = (int) Math.floor(prevY);
			int z = (int) Math.floor(prevZ);

			Block block = minecraft.theWorld.getBlock(x, y, z);

			if (canCollide(x, y, z))
			{
				for (int j = y; j < 256; j++)
				{
					if (!canCollide(x, j + 1, z) && !canCollide(x, j + 2, z))
					{
						minecraft.thePlayer.sendChatMessage("/tppos " + x + " " + (j + 1) + " " + z);
						return;
					}
				}
			}
		}

		message = new ChatComponentText("No block in sight (or too far)!");
		style.setColor(EnumChatFormatting.DARK_RED);
		message.setChatStyle(style);
		minecraft.thePlayer.addChatMessage(message);
	}

	private boolean canCollide(int x, int y, int z)
	{
		Block block = minecraft.theWorld.getBlock(x, y, z);
		if (block.getCollisionBoundingBoxFromPool(minecraft.theWorld, x, y, z) != null)
			return true;
		return false;
	}

}
