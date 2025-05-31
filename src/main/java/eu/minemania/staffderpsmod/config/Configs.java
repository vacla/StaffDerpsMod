package eu.minemania.staffderpsmod.config;

import fi.dy.masa.malilib.config.IConfigHandler;

import java.nio.file.Files;
import java.nio.file.Path;

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
        public static final ConfigBoolean ENABLED = new ConfigBoolean("enabled", true, "staffderpsmod.config.enabled.description");
        public static final ConfigBoolean SEE_INVISIBLE = new ConfigBoolean("seeInvisible", false, "staffderpsmod.config.see_invisible.description");
        public static final ConfigBoolean SEE_PET_OWNER = new ConfigBoolean("seePetOwner", false, "staffderpsmod.config.see_pet_owner.description");
        public static final ConfigDouble SCALAR = new ConfigDouble("scalar", 2, 1, 9, "staffderpsmod.config.scalar.description");
        public static final ConfigString SUMMON_COMMAND = new ConfigString("summonCommand", "summon minecraft:pig ~ ~1 ~ {Attributes:[{Name:generic.maxHealth,Base:1}],Age:-99}", "staffderpsmod.config.summon_command.description");
        public static final ConfigString TP_COMMAND = new ConfigString("tpCommand", "tp", "staffderpsmod.config.tp_command.description");

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
        Path configFile = FileUtils.getConfigDirectoryAsPath().resolve(CONFIG_FILE_NAME);

        if (Files.exists(configFile) && Files.isReadable(configFile))
        {
            JsonElement element = JsonUtils.parseJsonFileAsPath(configFile);

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
        Path dir = FileUtils.getConfigDirectoryAsPath();

        if (!Files.exists(dir))
        {
            FileUtils.createDirectoriesIfMissing(dir);
        }

        if (Files.isDirectory(dir))
        {
            JsonObject root = new JsonObject();

            ConfigUtils.writeConfigBase(root, "Generic", Generic.OPTIONS);
            ConfigUtils.writeConfigBase(root, "Hotkeys", Hotkeys.HOTKEY_LIST);

            JsonUtils.writeJsonToFileAsPath(root, dir.resolve(CONFIG_FILE_NAME));
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