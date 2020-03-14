package eu.minemania.staffderpsmod.subfunctions;

import java.util.List;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;

public class PetOwner
{
    private Box bb;
    private String randomOwner;

    public PetOwner(){}

    private List<WolfEntity> getDog()
    {
        PlayerEntity player = MinecraftClient.getInstance().player;
        this.bb = new Box(player.x - 4, player.y - 4, player.z - 4, player.x + 4, player.y + 4, player.z + 4);
        List<WolfEntity> stuff = MinecraftClient.getInstance().world.getEntities(WolfEntity.class, this.bb);
        return stuff;
    }

    public String getDogOwners()
    {
        String result = "";
        List<WolfEntity> dogs = this.getDog();
        if (dogs == null)
        {
            return "";
        }
        for (Object dog : dogs)
        {
            Entity owner = ((WolfEntity)dog).getOwner();
            if (owner == null)
            {
                continue;
            }
            String name = owner.getName().asString();
            if (name != null && name != "")
            {
                this.randomOwner = name;
            }
            result += ((WolfEntity)dog).getName().asString() + " ";
        }
        return result;
    }

    private List<ParrotEntity> getParrot()
    {
        PlayerEntity player = MinecraftClient.getInstance().player;
        this.bb = new Box(player.x - 2, player.y - 2, player.z - 2, player.x + 2, player.y + 2, player.z + 2);
        return MinecraftClient.getInstance().world.getEntities(ParrotEntity.class, this.bb);
    }

    public String getParrotOwners()
    {
        String result = "";
        List<ParrotEntity> parrots = this.getParrot();
        if (parrots == null)
        {
            return "";
        }
        for (Object parrot : parrots)
        {
            Entity owner = ((ParrotEntity)parrot).getOwner();
            if (owner == null)
            {
                continue;
            }
            String name = owner.getName().asString();
            if (name != null && name != "")
            {
                this.randomOwner = name;
            }
            result += ((ParrotEntity)parrot).getName().asString() + " ";          
        }
        return result;
    }

    private List<CatEntity> getCat()
    {
        PlayerEntity player = MinecraftClient.getInstance().player;
        this.bb = new Box(player.x - 2, player.y - 2, player.z - 2, player.x + 2, player.y + 2, player.z + 2);
        return MinecraftClient.getInstance().world.getEntities(CatEntity.class, this.bb);
    }

    public String getCatOwners()
    {
        String result = "";
        List<CatEntity> cats = this.getCat();
        if (cats == null)
        {
            return "";
        }
        for (Object cat : cats)
        {
            Entity owner = ((CatEntity)cat).getOwner();
            if (owner == null)
            {
                continue;
            }
            String name = owner.getName().asString();
            if (name != null && name != "")
            {
                this.randomOwner = name;
            }
            result += ((CatEntity)cat).getName().asString() + " ";			
        }
        return result;
    }

    /**
     * copy UUID of in-range pet to clipboard
     */
    public String getRandomOwner()
    {
        this.randomOwner = "";
        this.getCatOwners();
        this.getDogOwners();
        this.getParrotOwners();
        return this.randomOwner;
    }
}