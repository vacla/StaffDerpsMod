package io.github.kyzderp.staffderpsmod.subfunctions;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;

public class SeeInvisible {
	
	private AxisAlignedBB bb;

	public SeeInvisible(){}
	
	private List<EntityPlayer> getPlayers()
	{
		EntityPlayer player = Minecraft.getMinecraft().player;
		this.bb = new AxisAlignedBB(player.posX - 32, player.posY - 32, player.posZ - 32, 
				player.posX + 32, player.posY + 32, player.posZ + 32);
		return Minecraft.getMinecraft().world.getEntitiesWithinAABB(EntityPlayer.class, this.bb);
	}
	
	public List<EntityPlayer> getInvsPlayers()
	{
		List<EntityPlayer> playerList = new ArrayList<EntityPlayer>();
		
		for (Object player: this.getPlayers())
		{
			if (((EntityPlayer) player).isInvisible())
			{
				playerList.add((EntityPlayer) player);
			}
		}
		return playerList;
	}

	public String getInvsString() 
	{
		EntityPlayer player = Minecraft.getMinecraft().player;
		List<EntityPlayer> invsPlayers = this.getInvsPlayers();
		if (invsPlayers.contains(player))
			invsPlayers.remove(player);
		int n = invsPlayers.size();
		String result = "";
		
		for (int j = 0; j < n; j++)
		{
			int currentDist = 100;
			Entity currentPlayer = null;
			//get closest
			for (int i = 0; i < invsPlayers.size(); i++)
			{
				int dist = this.distanceToMob((Entity) invsPlayers.get(i), player);
				if ( dist < currentDist)
				{
					currentDist = dist;
					currentPlayer = (Entity)invsPlayers.get(i);
				}
			}
			result += currentPlayer.getName() + "(" + currentDist + "m) ";
			invsPlayers.remove(currentPlayer);
		}
		return result;
	}
	
	private int distanceToMob(Entity e1, Entity e2)
	{
		return (int) Math.sqrt(Math.pow((e1.posX - e2.posX), 2)
				+ Math.pow((e1.posY - e2.posY), 2)
				+ Math.pow((e1.posZ - e2.posZ), 2));
	}
}
