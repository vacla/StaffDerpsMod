package eu.minemania.staffderpsmod.data;

import java.nio.file.Files;
import java.nio.file.Path;

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
import net.minecraft.client.MinecraftClient;

public class DataManager
{
    private static DataManager INSTANCE = new DataManager();
    private static boolean canSave;

    private final CompassMath compassMath = new CompassMath(MinecraftClient.getInstance());
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
        Path file = getCurrentStorageFile();

        JsonElement element = JsonUtils.parseJsonFileAsPath(file);

        if (element != null && element.isJsonObject())
        {

            JsonObject root = element.getAsJsonObject();

            if (JsonUtils.hasString(root, "config_gui_tab"))
            {
                try
                {
                    configGuiTab = ConfigGuiTab.valueOf(root.get("config_gui_tab").getAsString());
                }
                catch (Exception e)
                {
                }

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
        if (canSave == false && forceSave == false)
        {
            return;
        }

        JsonObject root = new JsonObject();

        root.add("config_gui_tab", new JsonPrimitive(configGuiTab.name()));

        Path file = getCurrentStorageFile();
        JsonUtils.writeJsonToFileAsPath(root, file);

        canSave = false;
    }

    private static Path getCurrentStorageFile()
    {
        Path dir = getCurrentConfigDirectory();

        if (!Files.exists(dir))
        {
            FileUtils.createDirectoriesIfMissing(dir);
        }

        if (!Files.isDirectory(dir))
        {
            StaffDerpsMod.logger.warn("Failed to create the config directory '{}'", dir.toAbsolutePath());
        }

        return dir.resolve(getStorageFileName());
    }

    private static String getStorageFileName()
    {
        String name = StringUtils.getWorldOrServerName();

        if (name == null)
        {
            return Reference.MOD_ID + "_default.json";
        }

        return Reference.MOD_ID + "_" + name + ".json";
    }

    public static Path getCurrentConfigDirectory()
    {
        return FileUtils.getConfigDirectoryAsPath().resolve(Reference.MOD_ID);
    }
}