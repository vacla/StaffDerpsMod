package eu.minemania.staffderpsmod.event;

import eu.minemania.staffderpsmod.config.Configs;
import eu.minemania.staffderpsmod.render.StaffDerpsModRenderer;
import fi.dy.masa.malilib.interfaces.IRenderer;
import net.minecraft.client.Minecraft;

public class RenderHandler implements IRenderer
{
    private static final RenderHandler INSTANCE = new RenderHandler();

    public static RenderHandler getInstance()
    {
        return INSTANCE;
    }

    @Override
    public void onRenderGameOverlayPost(float partialTicks)
    {
        Minecraft mc = Minecraft.getInstance();

        if (Configs.Generic.ENABLED.getBooleanValue() && mc.gameSettings.showDebugInfo == false && mc.player != null)
        {
            StaffDerpsModRenderer.renderOverlays();
        }
    }
}