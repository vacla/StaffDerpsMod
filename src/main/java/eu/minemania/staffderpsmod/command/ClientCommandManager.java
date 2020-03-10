package eu.minemania.staffderpsmod.command;

import java.util.HashSet;
import java.util.Set;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandException;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

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

    public static void sendError(ITextComponent error)
    {
        sendFeedback(new TextComponentString("").appendText(error.getFormattedText()).applyTextStyles(TextFormatting.RED));
    }

    public static void sendFeedback(String message)
    {
        sendFeedback(new TextComponentString(message));
    }

    public static void sendFeedback(ITextComponent message)
    {
        Minecraft.getInstance().ingameGUI.getChatGUI().printChatMessage(message);
    }

    public static int executeCommand(StringReader reader, String command)
    {
        Minecraft mc = Minecraft.getInstance();
        EntityPlayerSP player = mc.player;
        try
        {
            return player.connection.func_195515_i().execute(reader, new FakeCommandSource(player));
        }
        catch (CommandException e)
        {
            ClientCommandManager.sendError(e.getComponent());
        }
        catch (CommandSyntaxException e)
        {
            ClientCommandManager.sendError(TextComponentUtils.toTextComponent(e.getRawMessage()));
            if (e.getInput() != null && e.getCursor() >= 0)
            {
                int cursor = Math.min(e.getCursor(), e.getInput().length());
                ITextComponent text = new TextComponentString("").applyTextStyles(TextFormatting.GRAY).applyTextStyle(style -> style.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command)));
                if (cursor > 10)
                {
                    text.appendText("...");
                }
                text.appendText(e.getInput().substring(Math.max(0, cursor - 10), cursor));
                if (cursor < e.getInput().length())
                {
                    text.appendText((new TextComponentString(e.getInput().substring(cursor)).applyTextStyles(TextFormatting.RED, TextFormatting.UNDERLINE)).getFormattedText());
                }

                text.appendText((new TextComponentTranslation("command.context.here").applyTextStyles(TextFormatting.RED, TextFormatting.ITALIC)).getFormattedText());
                ClientCommandManager.sendError(text);
            }
        }
        catch (Exception e)
        {
            ITextComponent error = new TextComponentString(e.getMessage() == null ? e.getClass().getName() : e.getMessage());
            ClientCommandManager.sendError(new TextComponentTranslation("command.failed").applyTextStyle(style -> style.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, error))));
            e.printStackTrace();
        }
        return 1;
    }
}