package eu.minemania.staffderpsmod.subfunctions;

import java.text.DecimalFormat;

import eu.minemania.staffderpsmod.config.Configs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;

public class MobSummoner
{
    private DecimalFormat df;

    public MobSummoner()
    {
        this.df = new DecimalFormat("0.0");
    }

    public void summon()
    {
        String command = Configs.Generic.SUMMON_COMMAND.getStringValue();
        PlayerEntity player = MinecraftClient.getInstance().player;

        double x = player.getRotationVector().x * Configs.Generic.SCALAR.getDoubleValue();
        double y = player.getRotationVector().y * Configs.Generic.SCALAR.getDoubleValue();
        double z = player.getRotationVector().z * Configs.Generic.SCALAR.getDoubleValue();

        String result = "Motion:[" + df.format(x).replace(',', '.') + "," + df.format(y).replace(',', '.') + "," + df.format(z).replace(',', '.') + "]";
        if (command.contains(":"))
        {
            result = "," + result;
        }
        result = command.substring(0, command.length() - 1) + result + "}";
        MinecraftClient.getInstance().player.sendCommand(result);
    }
}