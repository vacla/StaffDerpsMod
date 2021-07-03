package eu.minemania.staffderpsmod.event;

import eu.minemania.staffderpsmod.config.Configs;
import eu.minemania.staffderpsmod.render.StaffDerpsModRenderer;
import fi.dy.masa.malilib.interfaces.IRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

public class RenderHandler implements IRenderer
{
    private static final RenderHandler INSTANCE = new RenderHandler();

    public static RenderHandler getInstance()
    {
        return INSTANCE;
    }

    @Override
    public void onRenderGameOverlayPost(MatrixStack matrixStack)
    {
        MinecraftClient mc = MinecraftClient.getInstance();

        if (Configs.Generic.ENABLED.getBooleanValue() && mc.options.debugEnabled == false && mc.player != null)
        {
            StaffDerpsModRenderer.renderOverlays(matrixStack);
        }
    }
}