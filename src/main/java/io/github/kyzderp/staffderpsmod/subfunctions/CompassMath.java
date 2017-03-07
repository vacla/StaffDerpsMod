package io.github.kyzderp.staffderpsmod.subfunctions;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class CompassMath {

	private Minecraft minecraft;

	public CompassMath(Minecraft minecraft) 
	{
		this.minecraft = minecraft;
	}

	public void passThrough()
	{
		Vec3d vector = minecraft.player.getLookVec();
		double prevX = minecraft.player.posX;
		double prevY = minecraft.player.posY + 1.62;
		double prevZ = minecraft.player.posZ;

		boolean doesWallExist = false;
		Style style = new Style();
		TextComponentString message; 

		for (int i = 0; i < 512; i++)
		{
			prevX += vector.xCoord/8;
			prevY += vector.yCoord/8;
			prevZ += vector.zCoord/8;

			int x = (int) Math.floor(prevX);
			int y = (int) Math.floor(prevY);
			int z = (int) Math.floor(prevZ);

//			Block block = minecraft.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();

			if (canCollide(x, y, z))
				doesWallExist = true;

			if (doesWallExist && y > 0)
			{
				if (!canCollide(x, y + 1, z) && !canCollide(x, y, z))
				{
					minecraft.player.sendChatMessage("/tppos " + x + " " + y + " " + z);
					return;
				}
				else if (!canCollide(x, y, z) && !canCollide(x, y - 1, z))
				{
					minecraft.player.sendChatMessage("/tppos " + x + " " + (y - 1) + " " + z);
					return;
				}
			}
		}

		if (doesWallExist)
			message = new TextComponentString("Too much wall. You shall not pass!");
		else
			message = new TextComponentString("Nothing to pass through!");

		style.setColor(TextFormatting.DARK_RED);
		message.setStyle(style);
		minecraft.player.sendMessage(message);
	}

	public void jumpTo()
	{
		Vec3d vector = minecraft.player.getLookVec();
		double prevX = minecraft.player.posX;
		double prevY = minecraft.player.posY + 1.62;
		double prevZ = minecraft.player.posZ;
		Style style = new Style();
		TextComponentString message; 

		for (int i = 0; i < 512; i++)
		{
			prevX += vector.xCoord/8;
			prevY += vector.yCoord/8;
			prevZ += vector.zCoord/8;

			int x = (int) Math.floor(prevX);
			int y = (int) Math.floor(prevY);
			int z = (int) Math.floor(prevZ);

//			Block block = minecraft.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();

			if (canCollide(x, y, z))
			{
				for (int j = y; j < 256; j++)
				{
					if (!canCollide(x, j + 1, z) && !canCollide(x, j + 2, z))
					{
						minecraft.player.sendChatMessage("/tppos " + x + " " + (j + 1) + " " + z);
						return;
					}
				}
			}
		}

		message = new TextComponentString("No block in sight (or too far)!");
		style.setColor(TextFormatting.DARK_RED);
		message.setStyle(style);
		minecraft.player.sendMessage(message);
	}

	@SuppressWarnings("deprecation")
	private boolean canCollide(int x, int y, int z)
	{
		BlockPos pos = new BlockPos(x, y, z);
		IBlockState state = minecraft.world.getBlockState(pos);
		Block block = state.getBlock();
		if (block.getCollisionBoundingBox(state, minecraft.world, pos) != null)
			return true;
		return false;
	}

}
