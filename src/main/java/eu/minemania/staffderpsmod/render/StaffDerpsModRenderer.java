package eu.minemania.staffderpsmod.render;

import eu.minemania.staffderpsmod.config.Configs;
import eu.minemania.staffderpsmod.data.DataManager;
import fi.dy.masa.malilib.render.RenderUtils;
import fi.dy.masa.malilib.util.StringUtils;
import net.minecraft.client.MinecraftClient;

public class StaffDerpsModRenderer
{
    private static final StaffDerpsModRenderer INSTANCE = new StaffDerpsModRenderer();

    public static StaffDerpsModRenderer getInstance()
    {
        return INSTANCE;
    }

    public static void renderOverlays()
    {
        MinecraftClient mc = MinecraftClient.getInstance();
        if(mc.currentScreen == null && MinecraftClient.isHudEnabled())
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
        RenderUtils.renderText(0, 0, 0xFFAA00, StringUtils.translate("staffderpsmod.message.display.hidden", DataManager.getInvis().getInvsString()));
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
            RenderUtils.renderText(0, 0, 0xFFAA00, StringUtils.translate("staffderpsmod.message.display.dog", dogs));
            RenderUtils.renderText(0, 10, 0xFFAA00, StringUtils.translate("staffderpsmod.message.display.cat", cats));
            RenderUtils.renderText(0, 20, 0xFFAA00, StringUtils.translate("staffderpsmod.message.display.parrot", parrots));
        }
    }
}