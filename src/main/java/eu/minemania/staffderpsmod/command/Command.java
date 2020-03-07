package eu.minemania.staffderpsmod.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandSource;

public class Command
{
    public static CommandDispatcher<CommandSource> commandDispatcher;

    public static void registerCommands(CommandDispatcher<CommandSource> dispatcher)
    {
        ClientCommandManager.clearClientSideCommands();
        StaffDerpsCommand.register(dispatcher);

        if (Minecraft.getInstance().isIntegratedServerRunning())
        {

        }

        commandDispatcher = dispatcher;
    }
}
