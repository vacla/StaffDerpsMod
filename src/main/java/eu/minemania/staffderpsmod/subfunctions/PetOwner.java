package eu.minemania.staffderpsmod.subfunctions;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;

public class PetOwner
{
    private AxisAlignedBB bb;
    private String randomOwner;

    public PetOwner(){}

    private List<EntityWolf> getDog()
    {
        EntityPlayer player = Minecraft.getInstance().player;
        this.bb = new AxisAlignedBB(player.posX - 4, player.posY - 4, player.posZ - 4, player.posX + 4, player.posY + 4, player.posZ + 4);
        List<EntityWolf> stuff = Minecraft.getInstance().world.getEntitiesWithinAABB(EntityWolf.class, this.bb);
        return stuff;
    }

    public String getDogOwners()
    {
        String result = "";
        List<EntityWolf> dogs = this.getDog();
        if (dogs == null)
        {
            return "";
        }
        for (Object dog : dogs)
        {
            Entity owner = ((EntityWolf)dog).getOwner();
            if (owner == null)
            {
                continue;
            }
            String name = owner.getName().getUnformattedComponentText();
            if (name != null && name != "")
            {
                this.randomOwner = name;
            }
            result += ((EntityWolf)dog).getName().getUnformattedComponentText() + " ";
        }
        return result;
    }

    private List<EntityParrot> getParrot()
    {
        EntityPlayer player = Minecraft.getInstance().player;
        this.bb = new AxisAlignedBB(player.posX - 2, player.posY - 2, player.posZ - 2, player.posX + 2, player.posY + 2, player.posZ + 2);
        return Minecraft.getInstance().world.getEntitiesWithinAABB(EntityParrot.class, this.bb);
    }

    public String getParrotOwners()
    {
        String result = "";
        List<EntityParrot> parrots = this.getParrot();
        if (parrots == null)
        {
            return "";
        }
        for (Object parrot : parrots)
        {
            Entity owner = ((EntityParrot)parrot).getOwner();
            if (owner == null)
            {
                continue;
            }
            String name = owner.getName().getUnformattedComponentText();
            if (name != null && name != "")
            {
                this.randomOwner = name;
            }
            result += ((EntityParrot)parrot).getName() + " ";          
        }
        return result;
    }

    private List<EntityOcelot> getCat()
    {
        EntityPlayer player = Minecraft.getInstance().player;
        this.bb = new AxisAlignedBB(player.posX - 2, player.posY - 2, player.posZ - 2, player.posX + 2, player.posY + 2, player.posZ + 2);
        return Minecraft.getInstance().world.getEntitiesWithinAABB(EntityOcelot.class, this.bb);
    }

    public String getCatOwners()
    {
        String result = "";
        List<EntityOcelot> cats = this.getCat();
        if (cats == null)
        {
            return "";
        }
        for (Object cat : cats)
        {
            Entity owner = ((EntityOcelot)cat).getOwner();
            if (owner == null)
            {
                continue;
            }
            String name = owner.getName().getUnformattedComponentText();
            if (name != null && name != "")
            {
                this.randomOwner = name;
            }
            result += ((EntityOcelot)cat).getName() + " ";			
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