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

    public PetOwner()
    {
    }

    private List<WolfEntity> getDog()
    {
        PlayerEntity player = MinecraftClient.getInstance().player;
        this.bb = new Box(player.getX() - 4, player.getY() - 4, player.getZ() - 4, player.getX() + 4, player.getY() + 4, player.getZ() + 4);
        return MinecraftClient.getInstance().world.getNonSpectatingEntities(WolfEntity.class, this.bb);
    }

    public String getDogOwners()
    {
        StringBuilder result = new StringBuilder();
        List<WolfEntity> dogs = this.getDog();
        if (dogs == null)
        {
            return "";
        }
        for (Object dog : dogs)
        {
            Entity owner = ((WolfEntity) dog).getOwner();
            if (owner == null)
            {
                continue;
            }
            String name = owner.getName().asString();
            if (name != null && !name.equals(""))
            {
                this.randomOwner = name;
            }
            result.append(((WolfEntity) dog).getName().asString()).append(" ");
        }
        return result.toString();
    }

    private List<ParrotEntity> getParrot()
    {
        PlayerEntity player = MinecraftClient.getInstance().player;
        this.bb = new Box(player.getX() - 4, player.getY() - 4, player.getZ() - 4, player.getX() + 4, player.getY() + 4, player.getZ() + 4);
        return MinecraftClient.getInstance().world.getNonSpectatingEntities(ParrotEntity.class, this.bb);
    }

    public String getParrotOwners()
    {
        StringBuilder result = new StringBuilder();
        List<ParrotEntity> parrots = this.getParrot();
        if (parrots == null)
        {
            return "";
        }
        for (Object parrot : parrots)
        {
            Entity owner = ((ParrotEntity) parrot).getOwner();
            if (owner == null)
            {
                continue;
            }
            String name = owner.getName().asString();
            if (name != null && !name.equals(""))
            {
                this.randomOwner = name;
            }
            result.append(((ParrotEntity) parrot).getName().asString()).append(" ");
        }
        return result.toString();
    }

    private List<CatEntity> getCat()
    {
        PlayerEntity player = MinecraftClient.getInstance().player;
        this.bb = new Box(player.getX() - 4, player.getY() - 4, player.getZ() - 4, player.getX() + 4, player.getY() + 4, player.getZ() + 4);
        return MinecraftClient.getInstance().world.getNonSpectatingEntities(CatEntity.class, this.bb);
    }

    public String getCatOwners()
    {
        StringBuilder result = new StringBuilder();
        List<CatEntity> cats = this.getCat();
        if (cats == null)
        {
            return "";
        }
        for (Object cat : cats)
        {
            Entity owner = ((CatEntity) cat).getOwner();
            if (owner == null)
            {
                continue;
            }
            String name = owner.getName().asString();
            if (name != null && !name.equals(""))
            {
                this.randomOwner = name;
            }
            result.append(((CatEntity) cat).getName().asString()).append(" ");
        }
        return result.toString();
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