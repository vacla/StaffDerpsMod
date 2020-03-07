package eu.minemania.staffderpsmod.command;

import java.util.Collection;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandSource;

public class FakeCommandSource extends CommandSource
{
    public FakeCommandSource(EntityPlayerSP player)
    {
        super(player, player.getPositionVector(), player.getPitchYaw(), null, 0, player.getScoreboardName(), player.getName(), null, player);
    }

    @Override
    public Collection<String> getPlayerNames()
    {
        return Minecraft.getInstance().getConnection().getPlayerInfoMap().stream().map(e -> e.getGameProfile().getName()).collect(Collectors.toList());
    }
}
