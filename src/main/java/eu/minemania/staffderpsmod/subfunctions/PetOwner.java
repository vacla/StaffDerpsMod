package eu.minemania.staffderpsmod.subfunctions;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.passive.TameableEntity;
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

    public List<String> getDogOwners()
    {
        List<String> owners = new ArrayList<>();
        List<WolfEntity> dogs = this.getDog();
        if (dogs == null)
        {
            return owners;
        }
        for (WolfEntity dog : dogs)
        {
            setOwner(owners, dog);
        }
        return owners;
    }

    private List<ParrotEntity> getParrot()
    {
        PlayerEntity player = MinecraftClient.getInstance().player;
        this.bb = new Box(player.getX() - 4, player.getY() - 4, player.getZ() - 4, player.getX() + 4, player.getY() + 4, player.getZ() + 4);
        return MinecraftClient.getInstance().world.getNonSpectatingEntities(ParrotEntity.class, this.bb);
    }

    public List<String> getParrotOwners()
    {
        List<String> owners = new ArrayList<>();
        List<ParrotEntity> parrots = this.getParrot();
        if (parrots == null)
        {
            return owners;
        }
        for (ParrotEntity parrot : parrots)
        {
            setOwner(owners, parrot);
        }
        return owners;
    }

    private List<CatEntity> getCat()
    {
        PlayerEntity player = MinecraftClient.getInstance().player;
        this.bb = new Box(player.getX() - 4, player.getY() - 4, player.getZ() - 4, player.getX() + 4, player.getY() + 4, player.getZ() + 4);
        return MinecraftClient.getInstance().world.getNonSpectatingEntities(CatEntity.class, this.bb);
    }

    public List<String> getCatOwners()
    {
        List<String> owners = new ArrayList<>();
        List<CatEntity> cats = this.getCat();
        if (cats == null)
        {
            return owners;
        }
        for (CatEntity cat : cats)
        {
            setOwner(owners, cat);
        }
        return owners;
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

    private void setOwner(List<String> owners, TameableEntity entity)
    {
        Entity owner = entity.getOwner();
        String name;
        if (owner == null)
        {
            if (entity.getOwnerUuid() == null)
            {
                return;
            }
            name = entity.getOwnerUuid().toString();
        }
        else
        {
            name = owner.getName().asString();
        }
        if (name != null && !name.equals(""))
        {
            this.randomOwner = name;
            owners.add(name);
        }
    }
}