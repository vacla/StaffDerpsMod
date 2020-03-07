package eu.minemania.staffderpsmod.event;

import eu.minemania.staffderpsmod.config.Hotkeys;
import eu.minemania.staffderpsmod.data.DataManager;
import eu.minemania.staffderpsmod.gui.GuiConfigs;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.hotkeys.IHotkeyCallback;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.KeyAction;
import net.minecraft.client.Minecraft;

public class KeyCallbacks
{

    public static void init(Minecraft mc)
    {	
        IHotkeyCallback callbackHotkeys = new KeyCallbackHotkeys(mc);

        Hotkeys.SUMMON.getKeybind().setCallback(callbackHotkeys);
        Hotkeys.COMPASS_LEFT.getKeybind().setCallback(callbackHotkeys);
        Hotkeys.COMPASS_RIGHT.getKeybind().setCallback(callbackHotkeys);
        Hotkeys.OPEN_GUI_SETTINGS.getKeybind().setCallback(callbackHotkeys);
    }

    private static class KeyCallbackHotkeys implements IHotkeyCallback
    {
        private final Minecraft mc;

        public KeyCallbackHotkeys(Minecraft mc)
        {
            this.mc = mc;
        }

        @Override
        public boolean onKeyAction(KeyAction action, IKeybind key)
        {
            if(this.mc.player == null || this.mc.world == null || !Minecraft.isGuiEnabled())
            {
                return false;
            }
            if(key == Hotkeys.SUMMON.getKeybind())
            {
                DataManager.getSommoner().summon();
            }
            if(key == Hotkeys.COMPASS_LEFT.getKeybind()) {
                DataManager.getCompassMath().jumpTo();
            } else if(key == Hotkeys.COMPASS_RIGHT.getKeybind()) {
                DataManager.getCompassMath().passThrough();
            }
            if(key == Hotkeys.OPEN_GUI_SETTINGS.getKeybind())
            {
                GuiBase.openGui(new GuiConfigs());
                return true;
            }
            return false;
        }
    }
}

