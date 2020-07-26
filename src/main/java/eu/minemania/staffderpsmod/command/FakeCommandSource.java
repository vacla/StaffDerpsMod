package eu.minemania.staffderpsmod.command;

import java.util.Collection;
import java.util.stream.Collectors;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;

public class FakeCommandSource extends ServerCommandSource
{
    public FakeCommandSource(PlayerEntity player)
    {
        super(player, player.getPos(), player.getRotationClient(), null, 0, player.getEntityName(), player.getName(), null, player);
    }

    @Override
    public Collection<String> getPlayerNames()
    {
        return MinecraftClient.getInstance().getNetworkHandler().getPlayerList().stream().map(e -> e.getProfile().getName()).collect(Collectors.toList());
    }
}
