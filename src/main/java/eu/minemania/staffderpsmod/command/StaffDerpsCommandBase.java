package eu.minemania.staffderpsmod.command;

import net.minecraft.command.CommandSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class StaffDerpsCommandBase
{
    public static void localOutput(CommandSource sender, String message)
    {
        sendColoredText(sender, TextFormatting.AQUA, message);
    }

    public static void localOutputT(CommandSource sender, String translationKey, Object... args)
    {
        sendColoredText(sender, TextFormatting.AQUA, new TextComponentTranslation(translationKey, args));
    }

    public static void localError(CommandSource sender, String message)
    {
        sendColoredText(sender, TextFormatting.DARK_RED, message);
    }

    public static void localErrorT(CommandSource sender, String translationKey, Object... args)
    {
        sendColoredText(sender, TextFormatting.DARK_RED, new TextComponentTranslation(translationKey, args));
    }

    public static void sendColoredText(CommandSource sender, TextFormatting color, String message)
    {
        ITextComponent chat = new TextComponentString(message);
        chat.applyTextStyles(color);
        sender.getEntity().sendMessage(chat);
    }

    public static void sendColoredText(CommandSource sender, TextFormatting color, ITextComponent component)
    {
        component.applyTextStyles(color);
        sender.getEntity().sendMessage(component);
    }
}