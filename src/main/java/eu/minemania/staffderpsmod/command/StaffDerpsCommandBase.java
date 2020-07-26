package eu.minemania.staffderpsmod.command;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

public class StaffDerpsCommandBase
{
    public static void localOutput(ServerCommandSource sender, String message)
    {
        sendColoredText(sender, Formatting.AQUA, message);
    }

    public static void localOutputT(ServerCommandSource sender, String translationKey, Object... args)
    {
        sendColoredText(sender, Formatting.AQUA, new TranslatableText(translationKey, args));
    }

    public static void localError(ServerCommandSource sender, String message)
    {
        sendColoredText(sender, Formatting.DARK_RED, message);
    }

    public static void localErrorT(ServerCommandSource sender, String translationKey, Object... args)
    {
        sendColoredText(sender, Formatting.DARK_RED, new TranslatableText(translationKey, args));
    }

    public static void sendColoredText(ServerCommandSource sender, Formatting color, String message)
    {
        LiteralText chat = new LiteralText(message);
        chat.formatted(color);
        sender.getEntity().sendSystemMessage(chat, sender.getEntity().getUuid());
    }

    public static void sendColoredText(ServerCommandSource sender, Formatting color, MutableText component)
    {
        component.formatted(color);
        sender.getEntity().sendSystemMessage(component, sender.getEntity().getUuid());
    }
}