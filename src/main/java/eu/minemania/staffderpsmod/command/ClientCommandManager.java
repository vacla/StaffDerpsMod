package eu.minemania.staffderpsmod.command;

import java.util.HashSet;
import java.util.Set;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.command.CommandException;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;

/**
 * @author Earthcomputer
 */
public class ClientCommandManager
{
    private static Set<String> clientSideCommands = new HashSet<>();

    public static void clearClientSideCommands()
    {
        clientSideCommands.clear();
    }

    public static Set<String> getClientSideCommands()
    {
        return clientSideCommands;
    }

    public static void addClientSideCommand(String name)
    {
        clientSideCommands.add(name);
    }

    public static boolean isClientSideCommand(String name)
    {
        return clientSideCommands.contains(name);
    }

    public static void sendError(Text error)
    {
        sendFeedback(new LiteralText("").append(error).formatted(Formatting.RED));
    }

    public static void sendFeedback(String message)
    {
        sendFeedback(new LiteralText(message));
    }

    public static void sendFeedback(Text message)
    {
        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(message);
    }

    public static int executeCommand(StringReader reader, String command)
    {
        MinecraftClient mc = MinecraftClient.getInstance();
        ClientPlayerEntity player = mc.player;
        try
        {
            return player.networkHandler.getCommandDispatcher().execute(reader, new FakeCommandSource(player));
        }
        catch (CommandException e)
        {
            ClientCommandManager.sendError(e.getTextMessage());
        }
        catch (CommandSyntaxException e)
        {
            ClientCommandManager.sendError(Texts.toText(e.getRawMessage()));
            if (e.getInput() != null && e.getCursor() >= 0)
            {
                int cursor = Math.min(e.getCursor(), e.getInput().length());
                MutableText text = new LiteralText("").formatted(Formatting.GRAY).styled(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command)));
                if (cursor > 10)
                {
                    text.append("...");
                }
                text.append(e.getInput().substring(Math.max(0, cursor - 10), cursor));
                if (cursor < e.getInput().length())
                {
                    text.append((new LiteralText(e.getInput().substring(cursor)).formatted(Formatting.RED, Formatting.UNDERLINE)));
                }

                text.append((new TranslatableText("command.context.here").formatted(Formatting.RED, Formatting.ITALIC)));
                ClientCommandManager.sendError(text);
            }
        }
        catch (Exception e)
        {
            Text error = new LiteralText(e.getMessage() == null ? e.getClass().getName() : e.getMessage());
            ClientCommandManager.sendError(new TranslatableText("command.failed").styled(style -> style.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, error))));
            e.printStackTrace();
        }
        return 1;
    }
}