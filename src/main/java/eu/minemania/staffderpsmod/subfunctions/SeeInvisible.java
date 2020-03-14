package eu.minemania.staffderpsmod.subfunctions;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;

public class SeeInvisible
{
    private Box bb;

    public SeeInvisible(){}

    private List<PlayerEntity> getPlayers()
    {
        PlayerEntity player = MinecraftClient.getInstance().player;
        this.bb = new Box(player.getX() - 32, player.getY() - 32, player.getZ() - 32, player.getX() + 32, player.getY() + 32, player.getZ() + 32);
        return MinecraftClient.getInstance().world.getNonSpectatingEntities(PlayerEntity.class, this.bb);
    }

    public List<PlayerEntity> getInvsPlayers()
    {
        List<PlayerEntity> playerList = new ArrayList<PlayerEntity>();

        for (Object player: this.getPlayers())
        {
            if (((PlayerEntity) player).isInvisible())
            {
                playerList.add((PlayerEntity) player);
            }
        }
        return playerList;
    }

    public String getInvsString()
    {
        PlayerEntity player = MinecraftClient.getInstance().player;
        List<PlayerEntity> invsPlayers = this.getInvsPlayers();
        if (invsPlayers.contains(player))
        {
            invsPlayers.remove(player);
        }
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
            result += currentPlayer.getName().asString() + "(" + currentDist + "m) ";
            invsPlayers.remove(currentPlayer);
        }
        return result;
    }

    private int distanceToMob(Entity e1, Entity e2)
    {
        return (int) Math.sqrt(Math.pow((e1.getX() - e2.getX()), 2) + Math.pow((e1.getY() - e2.getY()), 2) + Math.pow((e1.getZ() - e2.getZ()), 2));
    }
}