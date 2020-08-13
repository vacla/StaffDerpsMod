package eu.minemania.staffderpsmod;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import fi.dy.masa.malilib.MaLiLibReference;
import fi.dy.masa.malilib.event.InitializationHandler;
import fi.dy.masa.malilib.util.StringUtils;
import net.fabricmc.api.ClientModInitializer;

@Environment(EnvType.CLIENT)
public class StaffDerpsMod implements ClientModInitializer
{
    public static final Logger logger = LogManager.getLogger(Reference.MOD_ID);

    @Override
    public void onInitializeClient()
    {
        try
        {
            Class.forName("fi.dy.masa.malilib.event.InitializationHandler");
            new MalilibInit().run();
        }
        catch (ClassNotFoundException e)
        {
            throw new IllegalStateException("Malilib not found. Requires Malilib for StaffDerpsMod", e);
        }
        catch (LinkageError e)
        {
            throw new IllegalStateException("Incompatible Malilib version (" + StringUtils.getModVersionString(MaLiLibReference.MOD_ID) + ")", e);
        }
    }

    // separate class to avoid loading malilib classes outside the try-catch
    private static class MalilibInit implements Runnable
    {
        @Override
        public void run()
        {
            InitializationHandler.getInstance().registerInitializationHandler(new InitHandler());
        }
    }
}
