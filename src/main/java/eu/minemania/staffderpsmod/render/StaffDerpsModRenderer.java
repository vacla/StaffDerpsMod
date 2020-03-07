package eu.minemania.staffderpsmod.render;

import eu.minemania.staffderpsmod.config.Configs;
import eu.minemania.staffderpsmod.data.DataManager;
import fi.dy.masa.malilib.render.RenderUtils;
import net.minecraft.client.Minecraft;

public class StaffDerpsModRenderer
{
    private static final StaffDerpsModRenderer INSTANCE = new StaffDerpsModRenderer();

    public static StaffDerpsModRenderer getInstance()
    {
        return INSTANCE;
    }

    public static void renderOverlays()
    {
        Minecraft mc = Minecraft.getInstance();
        if(mc.currentScreen == null && Minecraft.isGuiEnabled())
        {
            StaffDerpsModRenderer.getInstance().displayInvisible();
            StaffDerpsModRenderer.getInstance().displayPetOwner();
        }
    }

    /**
     * Display invisible players
     */
    public void displayInvisible()
    {
        if(Configs.Generic.SEE_INVISIBLE.getBooleanValue())
        {
        RenderUtils.renderText(0, 0, 0xFFAA00, "Hidden players: " + DataManager.getInvis().getInvsString());
        }
    }

    /**
     * Display pet owner
     */
    public void displayPetOwner()
    {
        if(Configs.Generic.SEE_PET_OWNER.getBooleanValue())
        {
            String dogs = DataManager.getOwner().getDogOwners();
            String cats = DataManager.getOwner().getCatOwners();
            String parrots = DataManager.getOwner().getParrotOwners();
            RenderUtils.renderText(0, 0, 0xFFAA00, "Dogs: " + dogs);
            RenderUtils.renderText(0, 10, 0xFFAA00, "Cats: " + cats);
            RenderUtils.renderText(0, 20, 0xFFAA00, "Parrots: " + parrots);
        }
    }
}