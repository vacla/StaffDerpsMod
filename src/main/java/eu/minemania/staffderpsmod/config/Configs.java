package eu.minemania.staffderpsmod.config;

import fi.dy.masa.malilib.config.IConfigHandler;

import java.io.File;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import eu.minemania.staffderpsmod.Reference;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigDouble;
import fi.dy.masa.malilib.config.options.ConfigString;
import fi.dy.masa.malilib.util.FileUtils;
import fi.dy.masa.malilib.util.JsonUtils;

public class Configs implements IConfigHandler
{
    /**
     * Config file for mod.
     */
    private static final String CONFIG_FILE_NAME = Reference.MOD_ID + ".json";

    /**
     * Default Generic configuration.
     */
    public static class Generic
    {
        public static final ConfigBoolean ENABLED = new ConfigBoolean("enabled", true, "staffderpsmod.description.config.enabled");
        public static final ConfigBoolean SEE_INVISIBLE = new ConfigBoolean("seeInvisible", false, "staffderpsmod.description.config.see_invisible");
        public static final ConfigBoolean SEE_PET_OWNER = new ConfigBoolean("seePetOwner", false, "staffderpsmod.description.config.see_pet_owner");
        public static final ConfigDouble SCALAR = new ConfigDouble("scalar", 2, 1, 9, "staffderpsmod.description.config.scalar");
        public static final ConfigString SUMMON_COMMAND = new ConfigString("summonCommand", "/summon minecraft:pig ~ ~1 ~ {Attributes:[{Name:generic.maxHealth,Base:1}],Age:-99}", "staffderpsmod.description.config.summon_command");
        public static final ConfigString TP_COMMAND = new ConfigString("tpCommand", "/tp", "staffderpsmod.description.config.tp_command");

        public static final ImmutableList<IConfigBase> OPTIONS = ImmutableList.of(
                ENABLED,
                SEE_INVISIBLE,
                SEE_PET_OWNER,
                SCALAR,
                SUMMON_COMMAND,
                TP_COMMAND
        );
    }

    /**
     * Loads configurations from configuration file.
     */
    public static void loadFromFile()
    {
        File configFile = new File(FileUtils.getConfigDirectory(), CONFIG_FILE_NAME);

        if (configFile.exists() && configFile.isFile() && configFile.canRead())
        {
            JsonElement element = JsonUtils.parseJsonFile(configFile);

            if (element != null && element.isJsonObject())
            {
                JsonObject root = element.getAsJsonObject();

                ConfigUtils.readConfigBase(root, "Generic", Generic.OPTIONS);
                ConfigUtils.readConfigBase(root, "Hotkeys", Hotkeys.HOTKEY_LIST);
            }
        }
    }

    /**
     * Saves configurations to configuration file.
     */
    public static void saveToFile()
    {
        File dir = FileUtils.getConfigDirectory();

        if ((dir.exists() && dir.isDirectory()) || dir.mkdirs())
        {
            JsonObject root = new JsonObject();

            ConfigUtils.writeConfigBase(root, "Generic", Generic.OPTIONS);
            ConfigUtils.writeConfigBase(root, "Hotkeys", Hotkeys.HOTKEY_LIST);

            JsonUtils.writeJsonToFile(root, new File(dir, CONFIG_FILE_NAME));
        }
    }

    @Override
    public void load()
    {
        loadFromFile();
    }

    @Override
    public void save()
    {
        saveToFile();
    }
}