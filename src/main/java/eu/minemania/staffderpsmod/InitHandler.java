package eu.minemania.staffderpsmod;

import eu.minemania.staffderpsmod.config.Configs;
import eu.minemania.staffderpsmod.event.InputHandler;
import eu.minemania.staffderpsmod.event.KeyCallbacks;
import eu.minemania.staffderpsmod.event.RenderHandler;
import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.event.RenderEventHandler;
import fi.dy.masa.malilib.interfaces.IInitializationHandler;
import fi.dy.masa.malilib.interfaces.IRenderer;
import net.minecraft.client.MinecraftClient;

public class InitHandler implements IInitializationHandler
{
    @Override
    public void registerModHandlers()
    {
        ConfigManager.getInstance().registerConfigHandler(Reference.MOD_ID, new Configs());

        InputEventHandler.getKeybindManager().registerKeybindProvider(InputHandler.getInstance());
        InputEventHandler.getInputManager().registerKeyboardInputHandler(InputHandler.getInstance());
        InputEventHandler.getInputManager().registerMouseInputHandler(InputHandler.getInstance());

        IRenderer renderer = new RenderHandler();
        RenderEventHandler.getInstance().registerGameOverlayRenderer(renderer);

        KeyCallbacks.init(MinecraftClient.getInstance());
    }
}
