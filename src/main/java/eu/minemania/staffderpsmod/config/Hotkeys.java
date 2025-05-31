package eu.minemania.staffderpsmod.config;

import fi.dy.masa.malilib.config.options.ConfigHotkey;

import java.util.List;

import com.google.common.collect.ImmutableList;

/**
 * Default hotkeys configuration.
 */
public class Hotkeys
{
    public static final ConfigHotkey COMPASS_LEFT = new ConfigHotkey("compassLeft", "LEFT_BRACKET", "staffderpsmod.hotkey.compass_left.description");
    public static final ConfigHotkey COMPASS_RIGHT = new ConfigHotkey("compassRight", "RIGHT_BRACKET", "staffderpsmod.hotkey.compass_right.description");
    public static final ConfigHotkey OPEN_GUI_SETTINGS = new ConfigHotkey("openGuiSettings", "K,C", "staffderpsmod.hotkey.open_gui_settings.description");
    public static final ConfigHotkey SUMMON = new ConfigHotkey("summon", "", "staffderpsmod.hotkey.summon.description");

    public static final List<ConfigHotkey> HOTKEY_LIST = ImmutableList.of(
            COMPASS_LEFT,
            COMPASS_RIGHT,
            OPEN_GUI_SETTINGS,
            SUMMON
    );
}

