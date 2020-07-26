package eu.minemania.staffderpsmod.render;

import eu.minemania.staffderpsmod.config.Configs;
import eu.minemania.staffderpsmod.data.DataManager;
import fi.dy.masa.malilib.render.RenderUtils;
import fi.dy.masa.malilib.util.StringUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

public class StaffDerpsModRenderer
{
    private static final StaffDerpsModRenderer INSTANCE = new StaffDerpsModRenderer();

    public static StaffDerpsModRenderer getInstance()
    {
        return INSTANCE;
    }

    public static void renderOverlays(MatrixStack matrixStack)
    {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.currentScreen == null && MinecraftClient.isHudEnabled())
        {
            StaffDerpsModRenderer.getInstance().displayInvisible(matrixStack);
            StaffDerpsModRenderer.getInstance().displayPetOwner(matrixStack);
        }
    }

    /**
     * Display invisible players
     */
    public void displayInvisible(MatrixStack matrixStack)
    {
        if (Configs.Generic.SEE_INVISIBLE.getBooleanValue())
        {
            RenderUtils.renderText(0, 0, 0xFFAA00, StringUtils.translate("staffderpsmod.message.display.hidden", DataManager.getInvis().getInvsString()), matrixStack);
        }
    }

    /**
     * Display pet owner
     */
    public void displayPetOwner(MatrixStack matrixStack)
    {
        if (Configs.Generic.SEE_PET_OWNER.getBooleanValue())
        {
            String dogs = DataManager.getOwner().getDogOwners();
            String cats = DataManager.getOwner().getCatOwners();
            String parrots = DataManager.getOwner().getParrotOwners();
            RenderUtils.renderText(0, 0, 0xFFAA00, StringUtils.translate("staffderpsmod.message.display.dog", dogs), matrixStack);
            RenderUtils.renderText(0, 10, 0xFFAA00, StringUtils.translate("staffderpsmod.message.display.cat", cats), matrixStack);
            RenderUtils.renderText(0, 20, 0xFFAA00, StringUtils.translate("staffderpsmod.message.display.parrot", parrots), matrixStack);
        }
    }
}