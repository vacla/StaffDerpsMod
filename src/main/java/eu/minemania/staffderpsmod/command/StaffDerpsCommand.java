package eu.minemania.staffderpsmod.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;
import eu.minemania.staffderpsmod.Reference;
import eu.minemania.staffderpsmod.config.Configs;
import eu.minemania.staffderpsmod.data.DataManager;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.EntitySummonArgument;
import net.minecraft.command.arguments.SuggestionProviders;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import java.util.Map;
import static com.mojang.brigadier.arguments.DoubleArgumentType.doubleArg;
import static com.mojang.brigadier.arguments.DoubleArgumentType.getDouble;
import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;
import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.BoolArgumentType.bool;
import static com.mojang.brigadier.arguments.BoolArgumentType.getBool;
import static net.minecraft.command.Commands.argument;
import static net.minecraft.command.Commands.literal;
import static net.minecraft.command.arguments.BlockPosArgument.blockPos;
import static net.minecraft.command.arguments.BlockPosArgument.getBlockPos;

public class StaffDerpsCommand extends StaffDerpsCommandBase
{
    public static void register(CommandDispatcher<CommandSource> dispatcher)
    {
        ClientCommandManager.addClientSideCommand("staffderps");
        LiteralArgumentBuilder<CommandSource> staffderps = literal("staffderps").executes(StaffDerpsCommand::info)
                .then(literal("help").executes(StaffDerpsCommand::help))
                .then(literal("invis").executes(StaffDerpsCommand::invis)
                        .then(argument("on", bool()).executes(StaffDerpsCommand::invis)))
                .then(literal("invisible").executes(StaffDerpsCommand::invis)
                        .then(argument("on", bool()).executes(StaffDerpsCommand::invis)))
                .then(literal("pet").executes(StaffDerpsCommand::pet)
                        .then(argument("on", bool()).executes(StaffDerpsCommand::pet))
                        .then(literal("copy").executes(StaffDerpsCommand::petCopy)))
                .then(literal("chunk").executes(StaffDerpsCommand::chunk)
                        .then(argument("<x> <z>", greedyString()).executes(StaffDerpsCommand::chunk)))
                .then(literal("c").executes(StaffDerpsCommand::chunk)
                        .then(argument("<x> <z>", greedyString()).executes(StaffDerpsCommand::chunk)))
                .then(literal("tp").executes(StaffDerpsCommand::tp)
                        .then(argument("pos", blockPos()).executes(StaffDerpsCommand::tp)))
                .then(literal("summon").executes(StaffDerpsCommand::summon)
                        .then(argument("pos", blockPos())
                                .then(argument("entity", EntitySummonArgument.entitySummon()).suggests(SuggestionProviders.SUMMONABLE_ENTITIES).executes(StaffDerpsCommand::summon))))
                .then(literal("scalar").executes(StaffDerpsCommand::scalar)
                        .then(argument("scalar", doubleArg(0,9)).executes(StaffDerpsCommand::scalar)));
        dispatcher.register(staffderps);
    }

    private static int info(CommandContext<CommandSource> context)
    {
        localOutput(context.getSource(), Reference.MOD_NAME + " ["+ Reference.MOD_VERSION+"]");
        localOutput(context.getSource(), "Type /staffderps help for commands.");
        return 1;
    }

    private static int invis(CommandContext<CommandSource> context)
    {
        boolean on;
        try
        {
            on = getBool(context, "on");
            Configs.Generic.SEE_INVISIBLE.setBooleanValue(on);
            localOutput(context.getSource(), "See through invisibility: "+ String.valueOf(on).toUpperCase());
        }
        catch (Exception e)
        {
            localError(context.getSource(), "/staffderps invis <on|off>");
        }
        return 1;
    }

    private static int pet(CommandContext<CommandSource> context)
    {
        boolean on;
        try
        {
            on = getBool(context, "on");
            Configs.Generic.SEE_PET_OWNER.setBooleanValue(on);
            if(on)
            {
                localOutput(context.getSource(), "Displaying pets in 2 block radius");
            }
            else
            {
                localOutput(context.getSource(), "Pet display OFF");
            }
        }
        catch (Exception e)
        {
            localError(context.getSource(), "/staffderps pet <on|off|copy>");
        }
        return 1;
    }

    private static int petCopy(CommandContext<CommandSource> context)
    {
        String owner = DataManager.getOwner().getRandomOwner();
        if(owner == null || owner == "")
        {
            localError(context.getSource(), "No owners for any pets in range!");
        }
        else
        {
            localOutput(context.getSource(), "Owner name is "+ owner);
        }
        return 1;
    }

    private static int chunk(CommandContext<CommandSource> context)
    {
        int first = 10000000;
        int second = 10000000;
        String chunkmessage;
        try
        {
            chunkmessage = getString(context, "<x> <z>");
            String[] splitmessage = chunkmessage.split(" ");
            for(int i = 0; i < splitmessage.length; i++)
            {
                if(splitmessage[i].matches("-?[0-9]*"))
                {
                    if(first != 10000000)
                    {
                        second = Integer.parseInt(splitmessage[i]) * 16 + 8;
                        first = first * 16 + 8;
                        Minecraft.getInstance().player.sendChatMessage("/tppos " + first + " 100 " + second);
                    }
                    else
                    {
                        first = Integer.parseInt(splitmessage[i]);
                    }
                }
            }
        }
        catch (Exception e)
        {
            localError(context.getSource(), "/staffderps chunk <x> <z>");
        }
        return 1;
    }

    private static int tp(CommandContext<CommandSource> context)
    {
        BlockPos blockPos;
        try
        {
            blockPos = getBlockPos(context, "pos");
            localOutput(context.getSource(), "Running /tppos " + blockPos.getX() + " " + blockPos.getY() + " " + blockPos.getZ());
            Minecraft.getInstance().player.sendChatMessage("/tppos " + blockPos.getX() + " " + blockPos.getY() + " " + blockPos.getZ());
        }
        catch (Exception e)
        {
            localError(context.getSource(), "/staffderps tp <x> <y> <z>");
        }
        return 1;
    }

    private static int summon(CommandContext<CommandSource> context)
    {
        BlockPos blockPos;
        ResourceLocation entity;
        try
        {
            blockPos = getBlockPos(context, "pos");
            entity = EntitySummonArgument.getEntityId(context, "entity");
            if(entity != null)
            {
                String command = "/summon " + entity.toString() + " " + blockPos.getX() + " " + blockPos.getY() + " " + blockPos.getZ() + " {Attributes:[{Name:generic.maxHealth,Base:1}],Age:-99}";
                Configs.Generic.SUMMON_COMMAND.setValueFromString(command);
                localOutput(context.getSource(), "Summon command set: " + command);
            }
        }
        catch (Exception e)
        {
            localError(context.getSource(), "Summon format incorrect! Needs summon x y z entity");
        }
        return 1;
    }

    private static int scalar(CommandContext<CommandSource> context)
    {
        double scalar;
        try
        {
            scalar = getDouble(context, "scalar");
            Configs.Generic.SCALAR.setDoubleValue(scalar);
            localOutput(context.getSource(), "Summon vector scalar set to " + scalar);
        }
        catch (Exception e)
        {
            localOutput(context.getSource(), "Current summon vector scalar is " + Configs.Generic.SCALAR.getDoubleValue());
        }
        return 1;
    }

    private static int help(CommandContext<CommandSource> context)
    {
        localOutput(context.getSource(), Reference.MOD_NAME + " ["+ Reference.MOD_VERSION+"] commands");
        int cmdCount = 0;
        CommandDispatcher<CommandSource> dispatcher = Command.commandDispatcher;
        for(CommandNode<CommandSource> command : dispatcher.getRoot().getChildren())
        {
            String cmdName = command.getName();
            if(ClientCommandManager.isClientSideCommand(cmdName))
            {
                Map<CommandNode<CommandSource>, String> usage = dispatcher.getSmartUsage(command, context.getSource());
                for(String u : usage.values())
                {
                    ClientCommandManager.sendFeedback(new TextComponentString("/" + cmdName + " " + u));
                }
                cmdCount += usage.size();
                if(usage.size() == 0)
                {
                    ClientCommandManager.sendFeedback(new TextComponentString("/" + cmdName));
                    cmdCount++;
                }
            }
        }
        return cmdCount;
    }
}