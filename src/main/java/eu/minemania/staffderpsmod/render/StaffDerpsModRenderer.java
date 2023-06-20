package eu.minemania.staffderpsmod.render;

import eu.minemania.staffderpsmod.config.Configs;
import eu.minemania.staffderpsmod.data.DataManager;
import fi.dy.masa.malilib.render.RenderUtils;
import fi.dy.masa.malilib.util.StringUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.List;

public class StaffDerpsModRenderer
{
    private static final StaffDerpsModRenderer INSTANCE = new StaffDerpsModRenderer();

    public static StaffDerpsModRenderer getInstance()
    {
        return INSTANCE;
    }

    public static void renderOverlays(DrawContext drawContext)
    {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.currentScreen == null && MinecraftClient.isHudEnabled())
        {
            int y = 0;
            y = StaffDerpsModRenderer.getInstance().displayInvisible(drawContext, y);
            StaffDerpsModRenderer.getInstance().displayPetOwner(drawContext, y);
        }
    }

    /**
     * Display invisible players
     */
    public int displayInvisible(DrawContext drawContext, int y)
    {
        if (Configs.Generic.SEE_INVISIBLE.getBooleanValue())
        {
            RenderUtils.renderText(0, y, 0xFFAA00, StringUtils.translate("staffderpsmod.message.display.hidden", DataManager.getInvis().getInvsString()), drawContext);
            y += 10;
        }

        return y;
    }

    /**
     * Display pet owner
     */
    public void displayPetOwner(DrawContext drawContext, int y)
    {
        if (Configs.Generic.SEE_PET_OWNER.getBooleanValue())
        {
            List<String> dogs = DataManager.getOwner().getDogOwners();
            List<String> cats = DataManager.getOwner().getCatOwners();
            List<String> parrots = DataManager.getOwner().getParrotOwners();
            RenderUtils.renderText(0, y, 0xFFAA00, StringUtils.translate("staffderpsmod.message.display.dog"), drawContext);
            y += 10;
            for (String dog : dogs)
            {
                RenderUtils.renderText(0, y, 0xFFAA00, dog, drawContext);
                y += 10;
            }
            RenderUtils.renderText(0, y, 0xFFAA00, StringUtils.translate("staffderpsmod.message.display.cat"), drawContext);
            y += 10;
            for (String cat : cats)
            {
                RenderUtils.renderText(0, y, 0xFFAA00, cat, drawContext);
                y += 10;
            }
            RenderUtils.renderText(0, y, 0xFFAA00, StringUtils.translate("staffderpsmod.message.display.parrot"), drawContext);
            y += 10;
            for (String parrot : parrots)
            {
                RenderUtils.renderText(0, y, 0xFFAA00, parrot, drawContext);
                y += 10;
            }
        }
    }
}