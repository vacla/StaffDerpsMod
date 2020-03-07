package eu.minemania.staffderpsmod.data;

import java.io.File;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import eu.minemania.staffderpsmod.Reference;
import eu.minemania.staffderpsmod.StaffDerpsMod;
import eu.minemania.staffderpsmod.gui.GuiConfigs.ConfigGuiTab;
import eu.minemania.staffderpsmod.subfunctions.CompassMath;
import eu.minemania.staffderpsmod.subfunctions.MobSummoner;
import eu.minemania.staffderpsmod.subfunctions.PetOwner;
import eu.minemania.staffderpsmod.subfunctions.SeeInvisible;
import fi.dy.masa.malilib.util.FileUtils;
import fi.dy.masa.malilib.util.JsonUtils;
import fi.dy.masa.malilib.util.StringUtils;
import fi.dy.masa.malilib.util.WorldUtils;
import net.minecraft.client.Minecraft;

public class DataManager
{
    private static DataManager INSTANCE = new DataManager();
    private static boolean canSave;

    private final CompassMath compassMath = new CompassMath(Minecraft.getInstance());
    private final SeeInvisible invis = new SeeInvisible();
    private final PetOwner owner = new PetOwner();
    private final MobSummoner summoner = new MobSummoner();

    public static DataManager getInstance()
    {
        return INSTANCE;
    }

    public static MobSummoner getSommoner()
    {
        return getInstance().summoner;
    }

    public static PetOwner getOwner()
    {
        return getInstance().owner;
    }

    public static SeeInvisible getInvis()
    {
        return getInstance().invis;
    }

    public static CompassMath getCompassMath()
    {
        return getInstance().compassMath;
    }

    private static ConfigGuiTab configGuiTab = ConfigGuiTab.GENERIC;

    public static ConfigGuiTab getConfigGuiTab()
    {
        return configGuiTab;
    }

    public static void setConfigGuiTab(ConfigGuiTab tab)
    {
        configGuiTab = tab;
    }

    public static void load()
    {
        File file = getCurrentStorageFile(true);

        JsonElement element = JsonUtils.parseJsonFile(file);

        if(element != null && element.isJsonObject())
        {

            JsonObject root = element.getAsJsonObject();

            if (JsonUtils.hasString(root, "config_gui_tab"))
            {
                try
                {
                    configGuiTab = ConfigGuiTab.valueOf(root.get("config_gui_tab").getAsString());
                }
                catch (Exception e) {}

                if (configGuiTab == null)
                {
                    configGuiTab = ConfigGuiTab.GENERIC;
                }
            }
        }

        canSave = true;
    }

    public static void save()
    {
        save(false);
    }

    public static void save(boolean forceSave)
    {
        if(canSave == false && forceSave == false)
        {
            return;
        }

        JsonObject root = new JsonObject();

        root.add("config_gui_tab", new JsonPrimitive(configGuiTab.name()));

        File file = getCurrentStorageFile(true);
        JsonUtils.writeJsonToFile(root, file);

        canSave = false;
    }

    private static File getCurrentStorageFile(boolean globalData)
    {
        File dir = getCurrentConfigDirectory();

        if(dir.exists() == false && dir.mkdirs() == false)
        {
            StaffDerpsMod.logger.warn("Failed to create the config directory '{}'", dir.getAbsolutePath());
        }

        return new File(dir, getStorageFileName(globalData));
    }

    private static String getStorageFileName(boolean globalData)
    {
        Minecraft mc = Minecraft.getInstance();
        String name = StringUtils.getWorldOrServerName();

        if(name != null)
        {
            if(globalData)
            {
                return Reference.MOD_ID + "_" + name + ".json";
            }
            else
            {
                return Reference.MOD_ID + "_" + name + "_dim" + WorldUtils.getDimensionId(mc.world) + ".json";
            }
        }

        return Reference.MOD_ID + "_default.json";
    }

    public static File getCurrentConfigDirectory()
    {
        return new File(FileUtils.getConfigDirectory(), Reference.MOD_ID);
    }
}